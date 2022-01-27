package main.controller;

import main.api.response.InitResponse;
import main.api.response.SettingsResponse;
import main.service.SettingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class ApiGeneralController {

    private final InitResponse initResponse;
    private final SettingService settingService;

    public ApiGeneralController(InitResponse initResponse, SettingService settingService) {
        this.initResponse = initResponse;
        this.settingService = settingService;
    }


    @GetMapping("init")
    public ResponseEntity<InitResponse> initResponse() {
        return ResponseEntity.ok(initResponse);
    }

    @GetMapping("settings")
    public ResponseEntity<SettingsResponse> settingResponse() {
        return ResponseEntity.ok(settingService.getSettings());
    }
}