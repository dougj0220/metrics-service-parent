package io.dougj.metrics.controller;

import com.google.gson.JsonObject;
import io.dougj.metrics.dto.AppDataDto;
import io.dougj.metrics.exception.ApiException;
import io.dougj.metrics.service.AppService;
import io.dougj.metrics.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(HttpUtil.APP_API)
public class AppController {

    private final Logger LOG = LoggerFactory.getLogger(AppController.class);

    private final AppService appService;

    @Autowired
    public AppController(AppService appService) {
        this.appService = appService;
    }

    @GetMapping(value=HttpUtil.APP_VERSION_ENDPOINT, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getVersion() {
        try {
            long startTime = System.currentTimeMillis();
            LOG.info("app version API endpoint called... at startTime: {} millis", startTime);
            JsonObject payload = new JsonObject();
            payload.addProperty(HttpUtil.APP_VERSION_KEY, appService.getAppVersion());
            long endTime = System.currentTimeMillis();
            LOG.info("returning app version API endpoint called... at endTime: {} with elapsedTime: {} millis",
                    endTime, endTime - startTime);
            return ResponseEntity.ok(payload);
        } catch (ApiException e) {
            LOG.error(e.getMessage(), e);
            return e.getResponseEntity();
        }
    }

    @PostMapping(value=HttpUtil.APP_DATA_ENDPOINT, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAppData() {
        try {
            LOG.info("start app data API endpoint...");
            AppDataDto appDataDto = appService.getAppData();
            LOG.info("end app data API endpoint...");
            return ResponseEntity.ok(appDataDto);
        } catch (ApiException e) {
            LOG.error(e.getMessage(), e);
            return e.getResponseEntity();
        }
    }
}
