package io.dougj.metrics.controller;

import io.dougj.metrics.dto.AppDataDto;
import io.dougj.metrics.exception.ApiException;
import io.dougj.metrics.service.AppService;
import io.dougj.metrics.util.HttpUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppController.class)
public class AppControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppService appService;

    private static final String VERSION = "metrics-service v0.0.1-SNAPSHOT";

    @Test
    public void testGetVersion() throws Exception {
        when(appService.getAppVersion()).thenReturn(VERSION);
        String jsonString = mockMvc.perform(get(HttpUtil.APP_API + HttpUtil.APP_VERSION_ENDPOINT))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        Assertions.assertNotNull(jsonString);
    }

    @Test
    public void testGetVersion_error() throws Exception {
        when(appService.getAppVersion()).thenThrow(new ApiException("app version missing",
                new ResponseEntity<>(HttpStatus.NOT_FOUND)));
        mockMvc.perform(get(HttpUtil.APP_API + HttpUtil.APP_VERSION_ENDPOINT))
                .andExpect(status().isNotFound()).andReturn();
    }

    @Test
    public void testGetAppData() throws Exception {
        AppDataDto appDataDto = new AppDataDto();
        appDataDto.setApplicationVersion(VERSION);
        appDataDto.setApplicationName("metrics-demo-service-test");
        when(appService.getAppData()).thenReturn(appDataDto);
        MvcResult result = mockMvc.perform(post(HttpUtil.APP_API + HttpUtil.APP_DATA_ENDPOINT))
                .andExpect(status().isOk()).andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void testGetAppData_error() throws Exception {
        when(appService.getAppData()).thenThrow(new ApiException("internal error",
                new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR)));
        mockMvc.perform(post(HttpUtil.APP_API + HttpUtil.APP_DATA_ENDPOINT))
                .andExpect(status().isInternalServerError()).andReturn();
    }
}
