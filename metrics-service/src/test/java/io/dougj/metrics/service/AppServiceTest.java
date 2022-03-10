package io.dougj.metrics.service;

import io.dougj.metrics.dto.AppDataDto;
import io.dougj.metrics.exception.ApiException;
import io.dougj.metrics.service.impl.AppServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AppServiceTest {

    @Autowired
    private AppServiceImpl appService;

    @Value("${version.txt}")
    private String versionText;

    @Test
    public void testGetAppVersion() {
        String appVersion = appService.getAppVersion();
        Assertions.assertEquals("metrics-service v0.0.1-SNAPSHOT", appVersion);
    }
    @Test
    public void testGetAppVersion_handleException() {
        appService.setVersionText(null);
        ApiException apiException = Assertions.assertThrows(ApiException.class, () -> appService.getAppVersion());
        Assertions.assertNotNull(apiException);
        Assertions.assertEquals("app version missing", apiException.getMessage());
        // set data back since junit does not guarantee test run order
        appService.setVersionText(versionText);
        Assertions.assertNotNull(appService.getAppVersion());
    }

    @Test
    public void testGetAppData() {
        AppDataDto appDataDto = appService.getAppData();
        Assertions.assertNotNull(appDataDto);
        Assertions.assertEquals("metrics-demo-service-test", appDataDto.getApplicationName());
        Assertions.assertEquals("metrics-service v0.0.1-SNAPSHOT", appDataDto.getApplicationVersion());
    }
}
