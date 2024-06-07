package com.gl.eirs.simchange.config;

import jdk.jfr.Category;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
public class AppConfig {

    @Value("${operator.name}")
    String operatorName;

    @Value("${file.suffix}")
    String fileSuffix;

    @Value("${file.separator.parameter}")
    String fileSeparator;

    @Value("${file.path}")
    String filePath;

    @Value("${processed.file}")
    String processedFile;

    @Value("${new.imsi.header.value}")
    String newImsiHeaderValue;

    @Value("${old.imsi.header.value}")
    String oldImsiHeaderValue;

    @Value("${msisdn.header.value}")
    String msisdnHeaderValue;

    @Value("${simChangeDate.header.value}")
    String simChangeDateHeaderValue;

    @Value("${alert.url}")
    String alertUrl;

    @Value("${move.file.path}")
    String moveFilePath;

    @Value("${initial.timer}")
    int initialTimer;

    @Value("${final.timer}")
    int finalTimer;
}
