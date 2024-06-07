package com.gl.eirs.simchange.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileDto {

    String fileName;
    boolean isValid;
    long numberOfRecords;
    boolean isProcessed;
    int newImsiColumnNumber;
    int oldImsiColumnNumber;
    int msisdnColumnNumber;
    int simChangeDateColumnNumber;
    LocalDateTime startTime;
    LocalDateTime endTime;
    String name;
    long greyListFound;
    long greyListNotFound;
    long greyListSuccess;
    long greyListFailure;
    long blacklistFound;
    long blackListNotFound;
    long blackListSuccess;
    long blackListFailure;
    long exceptionListFound;
    long exceptionListNotFound;
    long exceptionListSuccess;
    long exceptionListFailure;
    long imeiListFound;
    long imeiListNotFound;
    long imeiListSuccess;
    long imeiListFailure;
    long duplicateDeviceDetailFound;
    long duplicateDeviceDetailNotFound;
    long duplicateDeviceDetailSuccess;
    long duplicateDeviceDetailFailure;
    private List<String> status;

    public static FileDto FileDtoBuilder(File file, String folderPath, long recordCount) {
        FileDto fileDto = new FileDto();
        fileDto.setName(file.getName());
        fileDto.setFileName(folderPath + "/" + file.getName());
        fileDto.setProcessed(false);
        fileDto.setNumberOfRecords(recordCount);
        fileDto.setValid(true);
        fileDto.setEndTime(LocalDateTime.now());
        fileDto.setStartTime(LocalDateTime.now());
        fileDto.setGreyListFound(0);
        fileDto.setGreyListNotFound(0);
        fileDto.setGreyListSuccess(0);

        fileDto.setBlacklistFound(0);
        fileDto.setBlackListNotFound(0);
        fileDto.setBlackListSuccess(0);

        fileDto.setExceptionListFound(0);
        fileDto.setExceptionListNotFound(0);
        fileDto.setExceptionListSuccess(0);

        fileDto.setImeiListFound(0);
        fileDto.setImeiListNotFound(0);
        fileDto.setImeiListSuccess(0);

        fileDto.setDuplicateDeviceDetailFound(0);
        fileDto.setDuplicateDeviceDetailNotFound(0);
        fileDto.setDuplicateDeviceDetailSuccess(0);
        List<String> status;

        return fileDto;
    }

}
