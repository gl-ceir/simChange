package com.gl.eirs.simchange.service.impl;

import com.gl.eirs.simchange.alert.AlertService;
import com.gl.eirs.simchange.builder.ModulesAuditTrailBuilder;
import com.gl.eirs.simchange.config.AppConfig;
import com.gl.eirs.simchange.dto.FileDto;
import com.gl.eirs.simchange.entity.aud.ModulesAuditTrail;
import com.gl.eirs.simchange.messages.FailureMsg;
import com.gl.eirs.simchange.repository.app.SysParamRepository;
import com.gl.eirs.simchange.repository.aud.ModulesAuditTrailRepository;
import com.gl.eirs.simchange.validation.FileValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static com.gl.eirs.simchange.constants.Constants.featureName;
import static com.gl.eirs.simchange.constants.Constants.moduleName;

@Service
public class MainService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    FileService fileService;

    @Autowired
    AppConfig appConfig;

    @Autowired
    ModulesAuditTrailBuilder modulesAuditTrailBuilder;

    @Autowired
    ModulesAuditTrailRepository modulesAuditTrailRepository;

    @Autowired
    FileValidation fileValidation;

    @Autowired
    SysParamRepository sysParamRepository;

    @Autowired
    AlertService alertService;


    public void simChangeProcess() {
        logger.info("Starting the process of Sim Change process");
        String imsiPrefixValue = sysParamRepository.getValueFromTag("imsiPrefix");
        String msisdnPrefixValue = sysParamRepository.getValueFromTag("msisdnPrefix");
        if(imsiPrefixValue.isBlank() || imsiPrefixValue.isEmpty() || msisdnPrefixValue.isEmpty() || msisdnPrefixValue.isBlank()) {
            // alert and exit the process
            logger.error("The configuration value of imsiPrefix or msisdnPrefix is missing in DB.");
            alertService.raiseAnAlert("alert5001", "", "", 0);
            return ;
        }

        logger.info("Getting list of files present in the directory {}", appConfig.getFilePath());
        ArrayList<FileDto> fileDtos = fileService.getFiles(appConfig.getFilePath());

        /*
            Alert raising is removed
         */
        if(fileDtos.isEmpty()) {
            logger.error("No files found. Exiting the process");
            return ;
        }

        // no error then pick each file and process that file. File should be picked from oldest to latest.
        try {
            for (FileDto fileDto : fileDtos) {
                logger.info("File picked up for processing is {}", fileDto.getFileName());
                fileService.checkFileUploaded(fileDto);
                long startTime = System.currentTimeMillis();
                fileDto.setStartTime(LocalDateTime.now());
                // create modules_audit_trail entry for this file.
                ModulesAuditTrail modulesAuditTrail = modulesAuditTrailBuilder.forInsert(201, "INITIAL", "NA", moduleName + appConfig.getOperatorName(), featureName, "", fileDto.getFileName(), LocalDateTime.now());
                ModulesAuditTrail entity = modulesAuditTrailRepository.save(modulesAuditTrail);
                int moduleAuditId = entity.getId();

            /*
                now validate the file
                1. check the headers  (imsi, msisdn, deactivation_date)
                2. check if msisdn starts with 855.
                3. check if imsi starts with 456.
                4. check if imsi and msisdn pair unique or not.
             */
                logger.info("Checking validation for the file {}", fileDto.getFileName());
                boolean checkHeaders = fileValidation.validateHeaders(fileDto, moduleAuditId, startTime);
                if (!checkHeaders) {
                    // stop the process;
                    logger.error("The file {} failed validation for headers check. Skipping this file from further processing", fileDto.getFileName());
//                logger.info("Skipping this file from further processing");
                    fileService.moveFile(fileDto, appConfig.getMoveFilePath());
                    logger.info("The details of file {} processed. " +
                                    "For grey_list: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}], " +
                                    "For black_list: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}], " +
                                    "For exception_list: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}], " +
                                    "For duplicate_device_detail: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}]," +
                                    "For imei_pair_detail: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}]",
                            fileDto.getName(), fileDto.getGreyListFound(), fileDto.getGreyListSuccess(), fileDto.getGreyListFailure(),
                            fileDto.getBlacklistFound(), fileDto.getBlackListSuccess(), fileDto.getBlackListFailure(),
                            fileDto.getExceptionListFound(), fileDto.getExceptionListSuccess(), fileDto.getExceptionListFailure(),
                            fileDto.getDuplicateDeviceDetailFound(), fileDto.getDuplicateDeviceDetailSuccess(), fileDto.getDuplicateDeviceDetailFailure(),
                            fileDto.getImeiListFound(), fileDto.getImeiListSuccess(), fileDto.getImeiListFailure());
                    continue;
                }


                boolean validateIMSIAndMSISDN = fileValidation.checkPrefixForIMSIAndMSISDN(fileDto, imsiPrefixValue, msisdnPrefixValue, moduleAuditId, startTime);
                if (!validateIMSIAndMSISDN) {
                    logger.error("The file {} failed validation. Skipping this file from further processing", fileDto.getFileName());
//                logger.info("Skipping this file from further processing");
                    logger.info("The details of file {} processed. " +
                                    "For grey_list: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}], " +
                                    "For black_list: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}], " +
                                    "For exception_list: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}], " +
                                    "For duplicate_device_detail: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}]," +
                                    "For imei_pair_detail: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}]",
                            fileDto.getName(), fileDto.getGreyListFound(), fileDto.getGreyListSuccess(), fileDto.getGreyListFailure(),
                            fileDto.getBlacklistFound(), fileDto.getBlackListSuccess(), fileDto.getBlackListFailure(),
                            fileDto.getExceptionListFound(), fileDto.getExceptionListSuccess(), fileDto.getExceptionListFailure(),
                            fileDto.getDuplicateDeviceDetailFound(), fileDto.getDuplicateDeviceDetailSuccess(), fileDto.getDuplicateDeviceDetailFailure(),
                            fileDto.getImeiListFound(), fileDto.getImeiListSuccess(), fileDto.getImeiListFailure());
                    fileService.moveFile(fileDto, appConfig.getMoveFilePath());
                    continue;
                }

                boolean validateNewImsiUnique = fileValidation.checkNewImsiUnique(fileDto, moduleAuditId, startTime);
                if (!validateNewImsiUnique) {
                    logger.error("The file {} failed validation for unique of new IMSI values check. Skipping this file from further processing", fileDto.getFileName());
//                logger.info("Skipping this file from further processing");
                    logger.info("The details of file {} processed. " +
                                    "For grey_list: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}], " +
                                    "For black_list: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}], " +
                                    "For exception_list: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}], " +
                                    "For duplicate_device_detail: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}]," +
                                    "For imei_pair_detail: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}]",
                            fileDto.getName(), fileDto.getGreyListFound(), fileDto.getGreyListSuccess(), fileDto.getGreyListFailure(),
                            fileDto.getBlacklistFound(), fileDto.getBlackListSuccess(), fileDto.getBlackListFailure(),
                            fileDto.getExceptionListFound(), fileDto.getExceptionListSuccess(), fileDto.getExceptionListFailure(),
                            fileDto.getDuplicateDeviceDetailFound(), fileDto.getDuplicateDeviceDetailSuccess(), fileDto.getDuplicateDeviceDetailFailure(),
                            fileDto.getImeiListFound(), fileDto.getImeiListSuccess(), fileDto.getImeiListFailure());
                    fileService.moveFile(fileDto, appConfig.getMoveFilePath());
                    continue;
                }
                boolean validateOldImsiUnique = fileValidation.checkOldImsiUnique(fileDto, moduleAuditId, startTime);
                if (!validateOldImsiUnique) {
                    logger.error("The file {} failed validation for unique old imsi values check. Skipping this file from further processing", fileDto.getFileName());
//                logger.info("Skipping this file from further processing");
                    logger.info("The details of file {} processed. " +
                                    "For grey_list: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}], " +
                                    "For black_list: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}], " +
                                    "For exception_list: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}], " +
                                    "For duplicate_device_detail: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}]," +
                                    "For imei_pair_detail: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}]",
                            fileDto.getName(), fileDto.getGreyListFound(), fileDto.getGreyListSuccess(), fileDto.getGreyListFailure(),
                            fileDto.getBlacklistFound(), fileDto.getBlackListSuccess(), fileDto.getBlackListFailure(),
                            fileDto.getExceptionListFound(), fileDto.getExceptionListSuccess(), fileDto.getExceptionListFailure(),
                            fileDto.getDuplicateDeviceDetailFound(), fileDto.getDuplicateDeviceDetailSuccess(), fileDto.getDuplicateDeviceDetailFailure(),
                            fileDto.getImeiListFound(), fileDto.getImeiListSuccess(), fileDto.getImeiListFailure());
                    fileService.moveFile(fileDto, appConfig.getMoveFilePath());

                    continue;
                }

                boolean validateUniqueMsisdn = fileValidation.checkMsisdnUnique(fileDto, moduleAuditId, startTime);
                if (!validateUniqueMsisdn) {
                    logger.error("The file {} failed validation for unique msisdn values check. Skipping this file from further processing", fileDto.getFileName());
//                logger.info("Skipping this file from further processing");
                    logger.info("The details of file {} processed. " +
                                    "For grey_list: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}], " +
                                    "For black_list: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}], " +
                                    "For exception_list: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}], " +
                                    "For duplicate_device_detail: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}]," +
                                    "For imei_pair_detail: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}]",
                            fileDto.getName(), fileDto.getGreyListFound(), fileDto.getGreyListSuccess(), fileDto.getGreyListFailure(),
                            fileDto.getBlacklistFound(), fileDto.getBlackListSuccess(), fileDto.getBlackListFailure(),
                            fileDto.getExceptionListFound(), fileDto.getExceptionListSuccess(), fileDto.getExceptionListFailure(),
                            fileDto.getDuplicateDeviceDetailFound(), fileDto.getDuplicateDeviceDetailSuccess(), fileDto.getDuplicateDeviceDetailFailure(),
                            fileDto.getImeiListFound(), fileDto.getImeiListSuccess(), fileDto.getImeiListFailure());
                    fileService.moveFile(fileDto, appConfig.getMoveFilePath());

                    continue;
                }

                boolean validateUniquePairs = fileValidation.checkUniquePairNewImsiOldImsiMsisdn(fileDto, moduleAuditId, startTime);
                if (!validateUniquePairs) {
                    logger.error("The file {} failed validation for uniqueness of records values check. Skipping this file from further processing", fileDto.getFileName());
//                logger.info("Skipping this file from further processing");
                    logger.info("The details of file {} processed. " +
                                    "For grey_list: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}], " +
                                    "For black_list: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}], " +
                                    "For exception_list: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}], " +
                                    "For duplicate_device_detail: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}]," +
                                    "For imei_pair_detail: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}]",
                            fileDto.getName(), fileDto.getGreyListFound(), fileDto.getGreyListSuccess(), fileDto.getGreyListFailure(),
                            fileDto.getBlacklistFound(), fileDto.getBlackListSuccess(), fileDto.getBlackListFailure(),
                            fileDto.getExceptionListFound(), fileDto.getExceptionListSuccess(), fileDto.getExceptionListFailure(),
                            fileDto.getDuplicateDeviceDetailFound(), fileDto.getDuplicateDeviceDetailSuccess(), fileDto.getDuplicateDeviceDetailFailure(),
                            fileDto.getImeiListFound(), fileDto.getImeiListSuccess(), fileDto.getImeiListFailure());
                    fileService.moveFile(fileDto, appConfig.getMoveFilePath());

                    continue;
                }
//
                logger.info("All validation passed for the file {}, will read the file and process each entry", fileDto.getFileName());

            /*
            1. read contents and check the file.
            2. check values in table and make entries accordingly.

             */

                FailureMsg failureMsg = fileService.readFile(fileDto);

                // no need because already raising exception from read file function.
                if (!failureMsg.getErrMsg().isBlank() || !failureMsg.getErrMsg().isEmpty()) {
                    // some error
                    logger.error("The processing failed for file {}", fileDto.getFileName());
                     modulesAuditTrailRepository.updateModulesAudit(501, "FAIL", "The file " + fileDto.getName() + " failed due to exception " + failureMsg.getErrMsg() + " for operator " + appConfig.getOperatorName(), (int)fileDto.getExceptionListSuccess() + (int)fileDto.getBlackListSuccess() + (int)fileDto.getGreyListSuccess(), (int) fileDto.getBlackListFailure() +  (int) fileDto.getGreyListFailure() + (int) fileDto.getExceptionListFailure(), (int) (System.currentTimeMillis() - startTime), LocalDateTime.now(), moduleAuditId);
                    // update the audit entry for this file.
                    alertService.raiseAnAlert("alert5411", fileDto.getFileName(), appConfig.getOperatorName(), 0);
                    fileService.moveFile(fileDto, appConfig.getMoveFilePath());
                    logger.info("The details of file {} processed. " +
                                    "For grey_list: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}], " +
                                    "For black_list: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}], " +
                                    "For exception_list: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}], " +
                                    "For duplicate_device_detail: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}]," +
                                    "For imei_pair_detail: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}]",
                            fileDto.getName(), fileDto.getGreyListFound(), fileDto.getGreyListSuccess(), fileDto.getGreyListFailure(),
                            fileDto.getBlacklistFound(), fileDto.getBlackListSuccess(), fileDto.getBlackListFailure(),
                            fileDto.getExceptionListFound(), fileDto.getExceptionListSuccess(), fileDto.getExceptionListFailure(),
                            fileDto.getDuplicateDeviceDetailFound(), fileDto.getDuplicateDeviceDetailSuccess(), fileDto.getDuplicateDeviceDetailFailure(),
                            fileDto.getImeiListFound(), fileDto.getImeiListSuccess(), fileDto.getImeiListFailure());
                    continue;
                }
//            modulesAuditTrail = ModulesAuditTrailBuilder.forUpdate(moduleAuditId, 200, "SUCCESS", "NA", moduleName, featureName, "", fileDto.getFileName(), (int) fileDto.getNumberOfRecords(), 0, startTime);

                modulesAuditTrailRepository.updateModulesAudit(200, "SUCCESS", "NA", (int) fileDto.getNumberOfRecords(), 0, (int) (System.currentTimeMillis() - startTime), LocalDateTime.now(), moduleAuditId);
                // make this in else for an every false case.
                logger.info("The details of file {} processed. " +
                                "For grey_list: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}], " +
                                "For black_list: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}], " +
                                "For exception_list: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}], " +
                                "For duplicate_device_detail: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}]," +
                                "For imei_pair_detail: [recordsFound: {}, recordsProcessedSuccessfully: {}, recordsFailed: {}]",
                        fileDto.getName(), fileDto.getGreyListFound(), fileDto.getGreyListSuccess(), fileDto.getGreyListFailure(),
                        fileDto.getBlacklistFound(), fileDto.getBlackListSuccess(), fileDto.getBlackListFailure(),
                        fileDto.getExceptionListFound(), fileDto.getExceptionListSuccess(), fileDto.getExceptionListFailure(),
                        fileDto.getDuplicateDeviceDetailFound(), fileDto.getDuplicateDeviceDetailSuccess(), fileDto.getDuplicateDeviceDetailFailure(),
                        fileDto.getImeiListFound(), fileDto.getImeiListSuccess(), fileDto.getImeiListFailure());
                fileService.moveFile(fileDto, appConfig.getMoveFilePath());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
