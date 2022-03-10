package io.dougj.metrics.service;

import io.dougj.metrics.dto.AppDataDto;
import io.dougj.metrics.exception.ApiException;

public interface AppService {
    String getAppVersion() throws ApiException;
    AppDataDto getAppData() throws ApiException;
}
