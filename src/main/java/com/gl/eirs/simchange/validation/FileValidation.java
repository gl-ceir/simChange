package com.gl.eirs.simchange.validation;

import com.gl.eirs.simchange.alert.AlertService;
import com.gl.eirs.simchange.config.AppConfig;
import com.gl.eirs.simchange.dto.FileDto;
import com.gl.eirs.simchange.entity.aud.ModulesAuditTrail;
import com.gl.eirs.simchange.repository.app.SysParamRepository;
import com.gl.eirs.simchange.repository.aud.ModulesAuditTrailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;


@Component
public class FileValidation {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    AppConfig appConfig;
    @Autowired
    SysParamRepository sysParamRepository;

    @Autowired
    ModulesAuditTrailRepository modulesAuditTrailRepository;
    @Autowired
    AlertService alertService;

    Pattern onlyNumberPattern = Pattern.compile("^[0-9]*$");

    public boolean validateHeaders(FileDto file, int modulesAuditId, long startTime) {
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(file.getFileName()));
            String line = reader.readLine();

            if (line != null) {

                String[] lines = line.split(appConfig.getFileSeparator(), -1);
                logger.info("Checking new_imsi header present in file {} ", file.getFileName());
                int newImsiColumnNumber = isContains(lines, appConfig.getNewImsiHeaderValue());

                if (newImsiColumnNumber != -1) {
                    file.setNewImsiColumnNumber(newImsiColumnNumber);
                } else {
                    logger.info("The file {} does not contains new_imsi header", file.getFileName());
                    modulesAuditTrailRepository.updateModulesAudit(501, "FAIL",
                            "The Sim Change file "+ file.getFileName() + " does not contains new IMSI " +
                                    "for operator " + appConfig.getOperatorName(), 0,
                            (int) file.getNumberOfRecords(), (int) ( System.currentTimeMillis()  -  startTime ),
                            LocalDateTime.now(), modulesAuditId);
                    alertService.raiseAnAlert("alert5401", file.getFileName(), appConfig.getOperatorName(), 0);
                    return false;
                }

                logger.info("Checking old_imsi header present in file {} ", file.getFileName());

                int oldImsiColumnNumber = isContains(lines, appConfig.getOldImsiHeaderValue());

                if (oldImsiColumnNumber != -1) {
                    file.setOldImsiColumnNumber(oldImsiColumnNumber);
                } else {
                    logger.info("The file {} does not contains old_imsi header.", file.getFileName());

                    modulesAuditTrailRepository.updateModulesAudit(501, "FAIL",
                            "The Sim Change file "+ file.getFileName() + " does not contains old_imsi " +
                                    "for operator " + appConfig.getOperatorName(), 0,
                            (int) file.getNumberOfRecords(), (int) ( System.currentTimeMillis()  -  startTime ),
                            LocalDateTime.now(), modulesAuditId);

                    // update the audit entry for this file.
                    alertService.raiseAnAlert("alert5402", file.getFileName(), appConfig.getOperatorName(), 0);
                    return false;
                }

                logger.info("Checking msisdn header present in file {} ", file.getFileName());
                int msisdnColumnNumber = isContains(lines, appConfig.getMsisdnHeaderValue());
                if (msisdnColumnNumber != -1) {
                    file.setMsisdnColumnNumber(msisdnColumnNumber);
                } else {
                    logger.info("The file {} does not contains msisdn header", file.getFileName());

                    modulesAuditTrailRepository.updateModulesAudit(501, "FAIL",
                            "The Sim Change file "+ file.getFileName() + " does not contains msisdn for " +
                                    "operator " + appConfig.getOperatorName(), 0, (int) file.getNumberOfRecords(),
                            (int) ( System.currentTimeMillis()  -  startTime ), LocalDateTime.now(), modulesAuditId);

                    // update the audit entry for this file.
                    alertService.raiseAnAlert("alert5403", file.getFileName(), appConfig.getOperatorName(), 0);
                    return false;
                }
                logger.info("Checking sim_change_date header present in file {} ", file.getFileName());
                int simChangeDateColumnNumber = isContains(lines, appConfig.getSimChangeDateHeaderValue());
                if ( simChangeDateColumnNumber != -1) {
                    logger.info("sim_change_date present in the file {}", file.getFileName());
                    file.setSimChangeDateColumnNumber(simChangeDateColumnNumber);
                } else {
                    logger.info("The file {} does not contains sim_change_date header", file.getFileName());

                    modulesAuditTrailRepository.updateModulesAudit(501, "FAIL",
                            "The Sim Change file "+ file.getFileName() + " does not contains " +
                                    "sim_change_date for operator " + appConfig.getOperatorName(), 0,
                            (int) file.getNumberOfRecords(), (int) ( System.currentTimeMillis()  -  startTime ),
                            LocalDateTime.now(), modulesAuditId);

                    // update the audit entry for this file.
                    alertService.raiseAnAlert("alert5404", file.getFileName(), appConfig.getOperatorName(), 0);
                    return false;
                }
            }
            else {
                logger.info("The file is empty, skipping the file {}", file.getFileName());
                return false;
            }
            reader.close();
            return true;
        } catch (IOException e) {
            logger.error("Exception in the checking validation for headers in file {}" , file.getFileName());
            modulesAuditTrailRepository.updateModulesAudit(501, "FAIL",
                    "The Sim Change file "+ file.getFileName() + " failed due to " +
                            e.getLocalizedMessage(), 0, (int) file.getNumberOfRecords(),
                    (int) ( System.currentTimeMillis()  -  startTime ), LocalDateTime.now(), modulesAuditId);

            // update the audit entry for this file.
            alertService.raiseAnAlert("alert5411", file.getFileName(), appConfig.getOperatorName(), 0);
            return false;
        }
    }

    public int isContains(String[] line, String value) {
        for (int i = 0; i < line.length; i++) {
            if (line[i].equalsIgnoreCase(value)) {
                return i;
            }
        }
        return -1;

    }

    public boolean checkPrefix(String[] prefix, String value) {
        boolean flagPrefix = false;

        for(int i=0;i<prefix.length;i++) {

            if(!value.startsWith(prefix[i])) {
                logger.error("The value {} does not starts with prefix {}", value, prefix[i]);
                flagPrefix = true;
                break;
            }
        }
        return flagPrefix;
    }

    public boolean checkNull(String value) {
        if(value == null || value.isEmpty() || value.isEmpty())
            return true;
        else return false;

    }
    public boolean checkNumeric(String value) {
        if(!onlyNumberPattern.matcher(value.trim()).matches())
            return true;
        else return false;

    }

    // check if imsi and msisdn starts with prefix or not

    public boolean checkPrefixForIMSIAndMSISDN(FileDto file, String imsiPrefixValue, String msisdnPrefixValue, int modulesAuditId,  long startTime) {

        String[] imsiPrefix = imsiPrefixValue.split(",", -1);
        String[] msisdnPrefix = msisdnPrefixValue.split(",", -1);
        logger.info("The imsiprefix and msisdnPrefix is respectively {} {}", imsiPrefix, msisdnPrefix);
        int newImsiColumn = file.getNewImsiColumnNumber();
        int oldImsiColumn = file.getOldImsiColumnNumber();
        int msisdnColumn = file.getMsisdnColumnNumber();
        try (BufferedReader reader = new BufferedReader(new FileReader(file.getFileName()));) {
            String nextLine;
            // skipping first line as headers/
            nextLine = reader.readLine();
            while ((nextLine = reader.readLine()) != null) {
                if (nextLine.isEmpty()) {
                    continue;
                }
                String[] record = nextLine.split(appConfig.getFileSeparator(), -1);
                String newImsi = record[newImsiColumn].trim();
                String oldImsi = record[oldImsiColumn].trim();
                String msisdn = record[msisdnColumn].trim();

                logger.info("The record in file is {}", (Object) record);

                boolean flagNewImsiNull = checkNull(newImsi);

                if(flagNewImsiNull) {
                    logger.error("The new_imsi value failed null validation check for entry {}", nextLine);
                    modulesAuditTrailRepository.updateModulesAudit(501, "FAIL", "Null entry detected in new_imsi for Sim Change file " + file.getFileName() + "in new_imsi for operator " + appConfig.getOperatorName(), 0, (int) file.getNumberOfRecords(), (int) ( System.currentTimeMillis()  -  startTime ), LocalDateTime.now(), modulesAuditId);
                    alertService.raiseAnAlert("alert5408", file.getFileName() + "for new_imsi", appConfig.getOperatorName(), 0);
                    return false;
                }
                boolean flagNewImsiNonNumeric=checkNumeric(newImsi);
                if(flagNewImsiNonNumeric) {
                    logger.error("The new_imsi value failed non-numeric validation check for entry {}", nextLine);
                    modulesAuditTrailRepository.updateModulesAudit(501, "FAIL", "Non-Numeric entries detected in IMSI for Sim Change file " + file.getFileName() + "in new_imsi for operator " + appConfig.getOperatorName(), 0, (int) file.getNumberOfRecords(), (int) ( System.currentTimeMillis()  -  startTime ), LocalDateTime.now(), modulesAuditId);
                    alertService.raiseAnAlert("alert5413", file.getFileName() + "for new_imsi", appConfig.getOperatorName(), 0);
                    return false;
                }
                boolean flagNewImsiPrefix = checkPrefix(imsiPrefix, newImsi);
                if(flagNewImsiPrefix) {
                    // alert
                    logger.error("The new_imsi value failed prefix validation check for entry {}", nextLine);
                    modulesAuditTrailRepository.updateModulesAudit(501, "FAIL", "Some entries does not matches the new_imsi prefix for Sim Change file " + file.getFileName() + "in new_imsi for operator " + appConfig.getOperatorName(), 0, (int) file.getNumberOfRecords(), (int) ( System.currentTimeMillis()  -  startTime ), LocalDateTime.now(), modulesAuditId);
                    alertService.raiseAnAlert("alert5405", file.getFileName(), appConfig.getOperatorName(), 0);
                    return false;
                }

                boolean flagOldImsiNull = checkNull(oldImsi);
                if(!flagOldImsiNull) {
                    boolean flagOldImsiNonNumeric=checkNumeric(oldImsi);
                    if(!flagOldImsiNonNumeric) {
                        boolean flagOldImsiPrefix = checkPrefix(imsiPrefix, oldImsi);
                        if (flagOldImsiPrefix) {
                            // alert
                            logger.error("old imsi is not starting with prefix for the record {}", nextLine);
//                    modulesAuditTrail = ModulesAuditTrailBuilder.forUpdate(modulesAuditId, 501, "FAIL", "Some entries does not matches the IMSI prefix for HLR deactivation file " + file.getFileName() + "for operator " + appConfig.getOperatorName(), moduleName, featureName, "", file.getFileName(), 0, (int)file.getNumberOfRecords(), startTime);
//                    modulesAuditTrailRepository.save(modulesAuditTrail);
                            modulesAuditTrailRepository.updateModulesAudit(501, "FAIL", "Some entries does not matches the IMSI prefix for Sim change file " + file.getFileName() + "for operator " + appConfig.getOperatorName(), 0, (int) file.getNumberOfRecords(), (int) (System.currentTimeMillis() - startTime), LocalDateTime.now(), modulesAuditId);

                            // update the audit entry for this file.
                            alertService.raiseAnAlert("alert5406", file.getFileName(), appConfig.getOperatorName(), 0);
                            return false;
                        }
                    } else {
                        logger.error("The old_imsi value failed non-numeric validation check for entry {}", nextLine);
                        modulesAuditTrailRepository.updateModulesAudit(501, "FAIL", "Non-Numeric entries detected in old_imsi for Sim Change file " + file.getFileName() + "in new_imsi for operator " + appConfig.getOperatorName(), 0, (int) file.getNumberOfRecords(), (int) ( System.currentTimeMillis()  -  startTime ), LocalDateTime.now(), modulesAuditId);
                        alertService.raiseAnAlert("alert5413", file.getFileName() + "for old_imsi", appConfig.getOperatorName(), 0);
                        return false;
                    }
                }

                boolean flagMsisdnNull = checkNull(msisdn);
                if(flagMsisdnNull) {
                    logger.error("The msisdn value failed null validation check for entry {}", nextLine);
                    modulesAuditTrailRepository.updateModulesAudit(501, "FAIL", "Null/Non-Numeric entries detected in MSISDN for Sim Change file " + file.getFileName() + " in msisdn for operator " + appConfig.getOperatorName(), 0, (int) file.getNumberOfRecords(), (int) ( System.currentTimeMillis()  -  startTime ), LocalDateTime.now(), modulesAuditId);

                    // update the audit entry for this file.
                    alertService.raiseAnAlert("alert5408", file.getFileName() +  "for msisdn", appConfig.getOperatorName(), 0);
                    return false;

                }
                boolean flagMsisdnNonNumeric=checkNumeric(msisdn);
                if(flagMsisdnNonNumeric) {
                    logger.error("The msisdn value failed non-numeric validation check for entry {}", nextLine);
                    modulesAuditTrailRepository.updateModulesAudit(501, "FAIL", "Non-Numeric entries detected in msidsn for Sim Change file " + file.getFileName() + "in new_imsi for operator " + appConfig.getOperatorName(), 0, (int) file.getNumberOfRecords(), (int) ( System.currentTimeMillis()  -  startTime ), LocalDateTime.now(), modulesAuditId);
                    alertService.raiseAnAlert("alert5413", file.getFileName() + "for msisdn", appConfig.getOperatorName(), 0);
                    return false;
                }
                boolean flagMsisdnPrefix = checkPrefix(msisdnPrefix, msisdn);
                if(flagMsisdnPrefix) {
                    logger.error("The msisdn value failed prefix validation check for entry {}", nextLine);
                    modulesAuditTrailRepository.updateModulesAudit(501, "FAIL", "Some entries does not matches the MSISDN prefix for Sim Change file " + file.getFileName() + "in msisdn for operator " + appConfig.getOperatorName(), 0, (int) file.getNumberOfRecords(), (int) ( System.currentTimeMillis()  -  startTime ), LocalDateTime.now(), modulesAuditId);

                    // update the audit entry for this file.
                    alertService.raiseAnAlert("alert5407", file.getFileName(), appConfig.getOperatorName(), 0);
                    return false;
                }
            }
            reader.close();
        } catch (IOException e) {
            logger.error("Error while validating the file : {} with exception {}", file.getFileName(), e.getLocalizedMessage());
            modulesAuditTrailRepository.updateModulesAudit(501, "FAIL", "The Sim Change file "+ file.getFileName() + " failed due to " + e.getLocalizedMessage(), 0, (int) file.getNumberOfRecords(), (int) ( System.currentTimeMillis()  -  startTime ), LocalDateTime.now(), modulesAuditId);

            alertService.raiseAnAlert("alert5411", file.getFileName(), appConfig.getOperatorName(), 0);
            return false;
        }

        return true;
    }

    public boolean checkNewImsiUnique(FileDto file, int modulesAuditId, long startTime) {
        String newImsiNumber= String.valueOf(file.getNewImsiColumnNumber()+1);
        try {

//            List<ProcessBuilder> processBuilders = Arrays.asList(
//                    new ProcessBuilder("cut ", "-d "+appConfig.getFileSeparator(),
//                            "-f "+newImsiNumber.trim(),
//                            file.getFileName()),
//                    new ProcessBuilder("sort"),
//                    new ProcessBuilder("uniq"),
//                    new ProcessBuilder("wc", "-l")
//            );
            String command= "cut -d " + appConfig.getFileSeparator() + " -f " + newImsiNumber.trim()  + " " + file.getFileName() + " | sort | uniq | wc -l";
            logger.info("Command for new imsi uniqueness check {}", command);
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c",  command);
//            processBuilder.redirectOutput(ProcessBuilder.Redirect.to(sortFile));
            Process process = processBuilder.start();
            int exitStatus = process.waitFor();

            InputStream errorStream = process.getErrorStream();
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
            String line;
            while ((line = errorReader.readLine()) != null) {
                logger.error(line);
            }

//            List<Process> processes = ProcessBuilder.startPipeline(processBuilders);
//            logger.info("Process array is {}" , processes);
//            Process last = processes.get(processes.size() - 1);


            // Read the output of the process
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//                String line;

                while ((line = reader.readLine()) != null) {
                    logger.info("Output of command is : {}", line);
                    if( !line.trim().equalsIgnoreCase(String.valueOf(file.getNumberOfRecords()))) {
                        modulesAuditTrailRepository.updateModulesAudit(501, "FAIL", "The Sim Change file " +file.getFileName()+" does not contain unique values for new_imsi for operator " +appConfig.getOperatorName(), 0, (int) file.getNumberOfRecords(), (int) ( System.currentTimeMillis()  -  startTime ), LocalDateTime.now(), modulesAuditId);

                        alertService.raiseAnAlert("alert5409", file.getFileName(), appConfig.getOperatorName(), 0);
                        return false;
                    }
                }
            }

            catch (IOException e) {

                modulesAuditTrailRepository.updateModulesAudit(501, "FAIL", "The Sim Change file "+ file.getFileName() + " failed due to " + e.getLocalizedMessage(), 0, (int) file.getNumberOfRecords(), (int) ( System.currentTimeMillis()  -  startTime ), LocalDateTime.now(), modulesAuditId);

                alertService.raiseAnAlert("alert5411", file.getFileName(), appConfig.getOperatorName(), 0);

                return false;
            }
        } catch (Exception e) {

            modulesAuditTrailRepository.updateModulesAudit(501, "FAIL", "The Sim Change file "+ file.getFileName() + " failed due to " + e.getLocalizedMessage(), 0, (int) file.getNumberOfRecords(), (int) ( System.currentTimeMillis()  -  startTime ), LocalDateTime.now(), modulesAuditId);

            // update the audit entry for this file.
            alertService.raiseAnAlert("alert5411", file.getFileName(), appConfig.getOperatorName(), 0);
            return false;
        }
        return true;
    }

    public boolean checkUniquePairNewImsiOldImsiMsisdn(FileDto file, int modulesAuditId, long startTime) {
        String newImsiNumber= String.valueOf(file.getNewImsiColumnNumber()+1);
        String oldImsiNumber= String.valueOf(file.getOldImsiColumnNumber()+1);
        String msisdnNumber= String.valueOf(file.getMsisdnColumnNumber()+1);
        try {

            String command= "cut -d " + appConfig.getFileSeparator() + " -f " + newImsiNumber.trim() +"," +oldImsiNumber.trim() + "," + msisdnNumber.trim() + " " + file.getFileName() + " | sort | uniq | wc -l";
            logger.info("Command for new imsi, old imsi and msisdn uniqueness check {}", command);
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c",  command);
//            processBuilder.redirectOutput(ProcessBuilder.Redirect.to(sortFile));
            Process process = processBuilder.start();
            int exitStatus = process.waitFor();

            InputStream errorStream = process.getErrorStream();
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
            String line;
            while ((line = errorReader.readLine()) != null) {
                logger.error(line);
            }

//            List<Process> processes = ProcessBuilder.startPipeline(processBuilders);
//            logger.info("Process array is {}" , processes);
//            Process last = processes.get(processes.size() - 1);


            // Read the output of the process
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//                String line;

                while ((line = reader.readLine()) != null) {
                    logger.info("Output of command is : {}", line);
                    if( !line.trim().equalsIgnoreCase(String.valueOf(file.getNumberOfRecords()))) {
                        modulesAuditTrailRepository.updateModulesAudit(501, "FAIL", "The Sim Change file " +file.getFileName()+" does not contain unique pair values for new_imsi, old_imsi and msisdn for operator " +appConfig.getOperatorName(), 0, (int) file.getNumberOfRecords(), (int) ( System.currentTimeMillis()  -  startTime ), LocalDateTime.now(), modulesAuditId);
                        alertService.raiseAnAlert("alert5414", file.getFileName(), appConfig.getOperatorName(), 0);
                        return false;
                    }
                }
            }

            catch (IOException e) {

                modulesAuditTrailRepository.updateModulesAudit(501, "FAIL", "The Sim Change file "+ file.getFileName() + " failed due to " + e.getLocalizedMessage(), 0, (int) file.getNumberOfRecords(), (int) ( System.currentTimeMillis()  -  startTime ), LocalDateTime.now(), modulesAuditId);

                alertService.raiseAnAlert("alert5411", file.getFileName(), appConfig.getOperatorName(), 0);

                return false;
            }
        } catch (Exception e) {

            modulesAuditTrailRepository.updateModulesAudit(501, "FAIL", "The Sim Change file "+ file.getFileName() + " failed due to " + e.getLocalizedMessage(), 0, (int) file.getNumberOfRecords(), (int) ( System.currentTimeMillis()  -  startTime ), LocalDateTime.now(), modulesAuditId);

            // update the audit entry for this file.
            alertService.raiseAnAlert("alert5411", file.getFileName(), appConfig.getOperatorName(), 0);
            return false;
        }
        return true;
    }


    public boolean checkOldImsiUnique(FileDto file, int modulesAuditId, long startTime) {
        String oldImsiNumber= String.valueOf(file.getOldImsiColumnNumber()+1);
        try {

//            String command= "cut -d " + appConfig.getFileSeparator() + " -f " + msisdnNumber.trim()  + " " + file.getFileName() + " | sort | uniq | wc -l";
            String command = "cut -d " + appConfig.getFileSeparator() + " -f " + oldImsiNumber.trim() + " " +  file.getFileName() + " | tail -n +2 | grep -v '^$' | sort | uniq -c | awk '$1 > 1'";
            logger.info("Command for old imsi uniqueness check {}", command);
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c",  command);
//            processBuilder.redirectOutput(ProcessBuilder.Redirect.to(sortFile));
            Process process = processBuilder.start();
            int exitStatus = process.waitFor();

            InputStream errorStream = process.getErrorStream();
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = errorReader.readLine()) != null) {

                logger.error(line);

            }

//            List<Process> processes = ProcessBuilder.startPipeline(processBuilders);
//            logger.info("Process array is {}" , processes);
//            Process last = processes.get(processes.size() - 1);


            // Read the output of the process
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {

                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
                int exitCode = process.waitFor();
                if(exitCode == 0 ) {
                    if (output.toString().isEmpty()) {
                        logger.info("All non-empty values for old imsi are unique.");
                    }
                    else {
//                        logger.error(output.toString());
                        logger.error(Arrays.toString(new String[]{output.toString().split("\n")[0]}));
                        modulesAuditTrailRepository.updateModulesAudit(501, "FAIL", "The Sim Change file " +file.getFileName()+" does not contain unique values for old imsi for operator " +appConfig.getOperatorName(), 0, (int) file.getNumberOfRecords(), (int) ( System.currentTimeMillis()  -  startTime ), LocalDateTime.now(), modulesAuditId);

                        alertService.raiseAnAlert("alert5410", file.getFileName(), appConfig.getOperatorName(), 0);
                        return false;
                    }
                }
            } catch (IOException e) {

                modulesAuditTrailRepository.updateModulesAudit(501, "FAIL", "The Sim Change file "+ file.getFileName() + " failed due to " + e.getLocalizedMessage(), 0, (int) file.getNumberOfRecords(), (int) ( System.currentTimeMillis()  -  startTime ), LocalDateTime.now(), modulesAuditId);

                alertService.raiseAnAlert("alert5411", file.getFileName(), appConfig.getOperatorName(), 0);

                return false;
            }
        } catch (Exception e) {

            modulesAuditTrailRepository.updateModulesAudit(501, "FAIL", "The Sim Change file "+ file.getFileName() + " failed due to " + e.getLocalizedMessage(), 0, (int) file.getNumberOfRecords(), (int) ( System.currentTimeMillis()  -  startTime ), LocalDateTime.now(), modulesAuditId);

            // update the audit entry for this file.
            alertService.raiseAnAlert("alert5411", file.getFileName(), appConfig.getOperatorName(), 0);
            return false;
        }
        return true;
    }

    public boolean checkMsisdnUnique(FileDto file, int modulesAuditId, long startTime) {
        String msisdnNumber= String.valueOf(file.getMsisdnColumnNumber()+1);
        try {

            String command= "cut -d " + appConfig.getFileSeparator() + " -f " + msisdnNumber.trim()  + " " + file.getFileName() + " | sort | uniq | wc -l";
            logger.info("Command for msisdn uniqueness check {}", command);
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c",  command);
//            processBuilder.redirectOutput(ProcessBuilder.Redirect.to(sortFile));
            Process process = processBuilder.start();
            int exitStatus = process.waitFor();

            InputStream errorStream = process.getErrorStream();
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
            String line;
            while ((line = errorReader.readLine()) != null) {
                logger.error(line);
            }

//            List<Process> processes = ProcessBuilder.startPipeline(processBuilders);
//            logger.info("Process array is {}" , processes);
//            Process last = processes.get(processes.size() - 1);


            // Read the output of the process
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//                String line;

                while ((line = reader.readLine()) != null) {
                    logger.info("Output of command is : {}", line );
                    if( !line.trim().equalsIgnoreCase(String.valueOf(file.getNumberOfRecords()))) {
                        modulesAuditTrailRepository.updateModulesAudit(501, "FAIL", "The Sim Change file " +file.getFileName()+" does not contain unique values for msisdn for operator " +appConfig.getOperatorName(), 0, (int) file.getNumberOfRecords(), (int) ( System.currentTimeMillis()  -  startTime ), LocalDateTime.now(), modulesAuditId);

                        alertService.raiseAnAlert("alert5412", file.getFileName(), appConfig.getOperatorName(), 0);
                        return false;
                    }
                }
            } catch (IOException e) {

                modulesAuditTrailRepository.updateModulesAudit(501, "FAIL", "The Sim Change file "+ file.getFileName() + " failed due to " + e.getLocalizedMessage(), 0, (int) file.getNumberOfRecords(), (int) ( System.currentTimeMillis()  -  startTime ), LocalDateTime.now(), modulesAuditId);

                alertService.raiseAnAlert("alert5411", file.getFileName(), appConfig.getOperatorName(), 0);

                return false;
            }
        } catch (Exception e) {

            modulesAuditTrailRepository.updateModulesAudit(501, "FAIL", "The Sim Change file "+ file.getFileName() + " failed due to " + e.getLocalizedMessage(), 0, (int) file.getNumberOfRecords(), (int) ( System.currentTimeMillis()  -  startTime ), LocalDateTime.now(), modulesAuditId);

            // update the audit entry for this file.
            alertService.raiseAnAlert("alert5411", file.getFileName(), appConfig.getOperatorName(), 0);
            return false;
        }
        return true;
    }


}
