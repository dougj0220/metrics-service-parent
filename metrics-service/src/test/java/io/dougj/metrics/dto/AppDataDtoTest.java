package io.dougj.metrics.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AppDataDtoTest {

    private static AppDataDto appDataDto;

    @BeforeAll
    public static void setup() {
        appDataDto = new AppDataDto();
        appDataDto.setApplicationVersion("0.0.1");
        appDataDto.setApplicationName("metrics-service");
    }

    @Test
    public void testGetApplicationName() {
        Assertions.assertEquals("metrics-service", appDataDto.getApplicationName());
    }

    @Test
    public void testGetApplicationVersion() {
        Assertions.assertEquals("0.0.1", appDataDto.getApplicationVersion());
    }

    @Test
    public void testSetApplicationName() {
        AppDataDto appDataDto = new AppDataDto();
        appDataDto.setApplicationName("new-app-name");
        Assertions.assertEquals("new-app-name", appDataDto.getApplicationName());
    }

    @Test
    public void testSetApplicationVersion() {
        AppDataDto appDataDto = new AppDataDto();
        appDataDto.setApplicationVersion("0.0.2");
        Assertions.assertEquals("0.0.2", appDataDto.getApplicationVersion());
    }
}
