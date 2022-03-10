package io.dougj.metrics.service.impl;

import io.dougj.metrics.dto.AppDataDto;
import io.dougj.metrics.exception.ApiException;
import io.dougj.metrics.service.AppService;
import io.dougj.metrics.util.HttpUtil;
import io.dougj.metrics.util.errorCodes.AppErrorCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppServiceImpl implements AppService {

    private final Logger LOG = LoggerFactory.getLogger(AppServiceImpl.class);

    @Value("${version.txt}")
    private String versionText;

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public String getAppVersion() throws ApiException {
        LOG.info("getting appVersion data: {}", versionText);

        return Optional.ofNullable(versionText)
                .orElseThrow(() -> new ApiException("app version missing",
                        HttpUtil.getErrorResponse(AppErrorCodes.APP_ERROR_NO_DATA)));
    }

    @Override
    public AppDataDto getAppData() throws ApiException {
        LOG.info("getting app data for applicationName: {}", applicationName);
        AppDataDto appDataDto = new AppDataDto();
        appDataDto.setApplicationName(applicationName);
        appDataDto.setApplicationVersion(versionText);

        return appDataDto;
    }
}
