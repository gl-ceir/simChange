package com.gl.eirs.simchange.service.impl;

import com.gl.eirs.simchange.alert.AlertService;
import com.gl.eirs.simchange.builder.*;
import com.gl.eirs.simchange.config.AppConfig;
import com.gl.eirs.simchange.config.AppDbConfig;
import com.gl.eirs.simchange.dto.FileDto;
import com.gl.eirs.simchange.entity.app.*;

import com.gl.eirs.simchange.messages.FailureMsg;
import com.gl.eirs.simchange.repository.app.*;
import com.gl.eirs.simchange.repository.aud.ModulesAuditTrailRepository;
import com.gl.eirs.simchange.service.intf.IFileService;
import com.imsi_retriever.IMSI_RETRIEVER;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
public class FileService implements IFileService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AppConfig appConfig;
    @Autowired
    GreyListRepository greyListRepository;
    @Autowired
    GreyListHisRepository greyListHisRepository;
    @Autowired
    BlackListRepository blackListRepository;
    @Autowired
    BlackListHisRepository blackListHisRepository;
    @Autowired
    ExceptionListRepository exceptionListRepository;
    @Autowired
    ExceptionListHisRepository exceptionListHisRepository;
    @Autowired
    GreyListHisBuilder greyListHisBuilder;
    @Autowired
    BlackListHisBuilder blackListHisBuilder;
    @Autowired
    ExceptionListHisBuilder exceptionListHisBuilder;
    @Autowired
    DbTransactionsService dbTransactionsService;
    @Autowired
    AlertService alertService;
    @Autowired
    ModulesAuditTrailRepository modulesAuditTrailRepository;
    @Autowired
    ModulesAuditTrailBuilder modulesAuditTrailBuilder;
    @Autowired
    ImeiListRepository imeiListRepository;
    @Autowired
    ImeiListHisBuilder imeiListHisBuilder;
    @Autowired
    AppDbConfig appDbConfig;
    @Autowired
    ActiveMsisdnListRepository activeMsisdnListRepository;
    @Autowired
    ActiveMsisdnListHisRepository activeMsisdnListHisRepository;
    @Autowired
    private DuplicateDeviceDetailRepository duplicateDeviceDetailRepository;
    @Autowired
    private DuplicateDeviceDetailHisRepository duplicateDeviceDetailHisRepository;

    //    @Override
    public ArrayList<FileDto> getFiles(String folderPath) {

        File dir = new File(folderPath);
        FileFilter fileFilter = new WildcardFileFilter("*"+appConfig.getFileSuffix()+"*");
        File[] files = dir.listFiles(fileFilter);
        logger.info("The count of files is {}", files.length);
        Arrays.sort(files, Comparator.comparingLong(File::lastModified));
        logger.info("The list of files picked are {}", (Object) files);
        ArrayList<FileDto> fileDtos = new ArrayList<>();
        for (File file : files) {
            fileDtos.add(FileDto.FileDtoBuilder(file, folderPath, getFileRecordCount(file)));
        }
        return fileDtos;
    }

    public void checkFileUploaded(FileDto fileDto) throws Exception{
        Path filePath = Paths.get(fileDto.getFileName());

        // Get the initial size of the file
        long initialSize = Files.size(filePath);
        // make configurable
        Thread.sleep(appConfig.getInitialTimer());
        long currentSize = Files.size(filePath);
        // Wait for a specific duration (e.g., 5 seconds)
        while(initialSize != currentSize) {
            logger.info("The file {} is still uploading waiting for {} secs.", fileDto.getName(), appConfig.getFinalTimer());
            // make configurable

            Thread.sleep(appConfig.getFinalTimer());
            initialSize = currentSize;
            currentSize = Files.size(filePath);
        }
        logger.info("File {} uploaded completely.", fileDto.getFileName());
        return;
    }
    public long getFileRecordCount(File file) {
        try {
            logger.info("Getting the file size for file {}", file.toURI());
            Path pathFile = Paths.get(file.toURI());
            return (long) Files.lines(pathFile).count();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    //    @Override
    public void moveFile(FileDto file, String moveFilePath) {
        try {
            logger.info("Moving File:{} to {}", file.getFileName(), moveFilePath);
            Files.move(Paths.get(file.getFileName()), Paths.get(moveFilePath + "/" + file.getName()));
            logger.info("Moved File:{} to {}", file.getFileName(), moveFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FailureMsg readFile(FileDto file) throws SQLException {
        Connection conn = appDbConfig.appDataSource().getConnection();
        FailureMsg failureMsg;
        int greyListSuccessCount = 0;
        int blackListSuccessCount = 0;
        int exceptionListSuccessCount = 0;
        int imeiListSuccessCount = 0;
        int duplicateDeviceDetailSucessCount =0;
        String filePath = appConfig.getProcessedFile();
        String processedFileName = "b_processed.txt";
        filePath = filePath+"/"+processedFileName;
        IMSI_RETRIEVER imsiRetriever = new IMSI_RETRIEVER();
        try (BufferedReader reader = new BufferedReader(new FileReader(file.getFileName()));
             BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            String record;
            boolean isFirstLine = true;
            while ((record = reader.readLine()) != null) {
                if (record.isEmpty()) {
                    continue;
                }
                try {
                    String[] splitRecord = record.split(appConfig.getFileSeparator(), -1);
                    if (isFirstLine) {
                        writer.write(record + appConfig.getFileSeparator() + "status");
                        writer.newLine();
                        isFirstLine = false;
                        continue;
                    }
                    String newImsi = splitRecord[file.getNewImsiColumnNumber()].trim();
                    String oldImsi = splitRecord[file.getOldImsiColumnNumber()].trim();
                    String msisdn = splitRecord[file.getMsisdnColumnNumber()].trim();

                    if (oldImsi == null || oldImsi.isEmpty()) {
                        oldImsi = imsiRetriever.getOldImsi(msisdn, newImsi, conn);
                        if (oldImsi != null) {
                            splitRecord[file.getOldImsiColumnNumber()] = oldImsi;
                        } else {
                            logger.error("No old IMSI found or IMSI matches the new one, skipping this record");
                            writer.write(record + "," + "not ok\n");
                            continue;
                        }
                    }

                    String status = oldImsi.isEmpty() ? "not ok" : "ok";

                    List<GreyList> greyList = greyListRepository.findAllByImsi(oldImsi);
                    file.setGreyListFound(file.getGreyListFound() + greyList.size());
                    if (!greyList.isEmpty()) {
                        for (GreyList list : greyList) {
                            logger.info("The IMSI matched in grey list is : {}", list.toString());
                            boolean output1 = dbTransactionsService.dbTransaction(list, newImsi);
                            if (!output1) {
                                logger.error("The entry {} failed for grey list.", greyList);
                                file.setGreyListFailure(greyList.size() - greyListSuccessCount);
                                file.setGreyListSuccess(greyListSuccessCount);
                                failureMsg = FailureMsg.FailureMsgBuilder("");
                                return failureMsg;
                            }
                            greyListSuccessCount++;
                        }
                    }
                    file.setGreyListSuccess(greyListSuccessCount);

                    List<BlackList> blackList = blackListRepository.findAllByImsi(oldImsi);
                    file.setBlacklistFound(file.getBlacklistFound() + blackList.size());
                    if (!blackList.isEmpty()) {
                        for (BlackList list : blackList) {
                            logger.info("The IMSI matched in black list : {}", list.toString());
                            boolean output2 = dbTransactionsService.dbTransaction(list, newImsi);
                            if (!output2) {
                                logger.error("The entry {} failed for black list.", list);
                                file.setBlackListFailure(blackList.size() - blackListSuccessCount);
                                file.setBlackListSuccess(blackListSuccessCount);
                                failureMsg = FailureMsg.FailureMsgBuilder("");
                                return failureMsg;
                            }
                            blackListSuccessCount++;
                        }
                    }
                    file.setBlackListSuccess(blackListSuccessCount);

                    List<ExceptionList> exceptionList = exceptionListRepository.findAllByImsi(oldImsi);
                    file.setExceptionListFound(file.getExceptionListFound() + exceptionList.size());
                    if (!exceptionList.isEmpty()) {
                        for (ExceptionList list : exceptionList) {
                            logger.info("The IMSI matched in exception list : {}", exceptionList.toString());
                            boolean output3 = dbTransactionsService.dbTransaction(list, newImsi);
                            if (!output3) {
                                logger.error("The entry {} failed for exception list.", exceptionList);
                                file.setExceptionListFailure(exceptionList.size() - exceptionListSuccessCount);
                                file.setExceptionListSuccess(exceptionListSuccessCount);
                                failureMsg = FailureMsg.FailureMsgBuilder("");
                                return failureMsg;
                            }
                            exceptionListSuccessCount++;
                        }
                    }
                    file.setExceptionListSuccess(exceptionListSuccessCount);

                    List<ImeiList> imeiList = imeiListRepository.findAllByImsi(oldImsi);
                    logger.info("Found {} entries with IMSI: {}", imeiList.size(), oldImsi);
                    file.setImeiListFound(file.getImeiListFound() + imeiList.size());
                    if (!imeiList.isEmpty()) {
                        for (ImeiList list : imeiList) {
                            logger.info("The IMSI matched in Imei list : {}", list.toString());
                            boolean output4 = dbTransactionsService.dbTransaction(list, newImsi);
                            if (!output4) {
                                logger.error("The entry {} failed for imei list.", imeiList);
                                file.setImeiListFailure(imeiList.size() - imeiListSuccessCount);
                                file.setImeiListSuccess(imeiListSuccessCount);
                                failureMsg = FailureMsg.FailureMsgBuilder("");
                                return failureMsg;
                            }
                            imeiListSuccessCount++;
                        }
                    }
                    file.setImeiListSuccess(imeiListSuccessCount);

                    List<DuplicateDeviceDetail> duplicateDeviceDetail = duplicateDeviceDetailRepository.findAllByImsi(oldImsi);
                    file.setDuplicateDeviceDetailFound(file.getDuplicateDeviceDetailFound() + duplicateDeviceDetail.size());
                    if (!duplicateDeviceDetail.isEmpty()) {
                        for (DuplicateDeviceDetail list : duplicateDeviceDetail) {
                            logger.info("The IMSI matched in Duplicate Device Details list is : {}", list.toString());
                            boolean output5 = dbTransactionsService.dbTransaction(list, newImsi);
                            if (!output5) {
                                logger.error("The entry {} failed for Duplicate Device Detail list.", duplicateDeviceDetail);
                                file.setDuplicateDeviceDetailFailure(duplicateDeviceDetail.size() - duplicateDeviceDetailSucessCount);
                                file.setDuplicateDeviceDetailSuccess(duplicateDeviceDetailSucessCount);
                                failureMsg = FailureMsg.FailureMsgBuilder("");
                                return failureMsg;
                            }
                            duplicateDeviceDetailSucessCount++;
                        }
                    }
                    file.setDuplicateDeviceDetailSuccess(duplicateDeviceDetailSucessCount);

                    // Process Active Msisdn List
                    ActiveMsisdnList activeMsisdnEntryToDelete = null;
                    List<ActiveMsisdnList> activeMsisdnList = activeMsisdnListRepository.findAllByImsi(oldImsi);

                    if (!activeMsisdnList.isEmpty()) {
                        for (ActiveMsisdnList list : activeMsisdnList) {
                            if (list.getImsi().equals(oldImsi) && list.getMsisdn().equals(msisdn)) {
                                activeMsisdnEntryToDelete = list;
                                break;
                            }
                        }
                        if (activeMsisdnEntryToDelete != null) {
                            logger.info("The IMSI matched in active msisdn list : {}", activeMsisdnEntryToDelete.toString());

                            // Check if the same event already exists in history
                            ActiveMsisdnListHis existingEntry = null;
                            int count = activeMsisdnListHisRepository.countByImsiAndMsisdnAndRemarks(oldImsi, msisdn, "Sim Change");
                            if (count == 0) {
                                ActiveMsisdnListHis activeMsisdnListHis = new ActiveMsisdnListHis(activeMsisdnEntryToDelete);
                                activeMsisdnListHis.setRemarks("Sim Change");
                                activeMsisdnListHisRepository.save(activeMsisdnListHis);
                                logger.info("Entry added to active msisdn list history: {}", activeMsisdnListHis.toString());
                            }

                            activeMsisdnListRepository.delete(activeMsisdnEntryToDelete);
                            logger.info("Deleted entry from active msisdn list: {}", activeMsisdnEntryToDelete.toString());
                        } else {
                            logger.info("No entry found with IMSI: {} and MSISDN: {} in active msisdn list.", oldImsi, msisdn);
                            // Log the absence of old IMSI here
                            ActiveMsisdnListHis noOldImsiEntry = new ActiveMsisdnListHis();
                            noOldImsiEntry.setImsi(oldImsi);
                            noOldImsiEntry.setMsisdn(msisdn);
                            noOldImsiEntry.setRemarks("No Old IMSI Found");
                            activeMsisdnListHisRepository.save(noOldImsiEntry);
                        }
                    }

                    if (activeMsisdnEntryToDelete != null) {
                        ActiveMsisdnList newEntry = new ActiveMsisdnList(newImsi, msisdn);
                        activeMsisdnListRepository.save(newEntry);
                        logger.info("New entry added to active msisdn list: {}", newEntry.toString());
                    }



                    writer.write(String.join(appConfig.getFileSeparator(), splitRecord) + appConfig.getFileSeparator() + status);
                    writer.newLine();
                } catch (Exception e) {
                    logger.error(e.toString());
                    failureMsg = FailureMsg.FailureMsgBuilder(e.getLocalizedMessage());
                    return failureMsg;
                }
            }
        } catch (Exception e) {
            logger.error(e.toString());
            failureMsg = FailureMsg.FailureMsgBuilder(e.getMessage());
            return failureMsg;
        }
        failureMsg = FailureMsg.FailureMsgBuilder("");
        return failureMsg;
    }

}
