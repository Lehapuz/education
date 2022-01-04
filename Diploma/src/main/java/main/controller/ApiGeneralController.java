package main.controller;

import main.api.response.InitResponse;
import main.api.response.SettingsResponse;
import main.model.GlobalSetting;
import main.repositories.GlobalSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class ApiGeneralController {

    private final InitResponse initResponse;
    @Autowired
    private GlobalSettingRepository globalSettingRepository;


    public ApiGeneralController(InitResponse initResponse) {
        this.initResponse = initResponse;
    }


    @GetMapping("init")
    public ResponseEntity<InitResponse> initResponse() {
        return ResponseEntity.ok(initResponse);
    }

    @GetMapping("settings")
    public ResponseEntity<SettingsResponse> settingResponse() {
        Iterable<GlobalSetting> globalSettingIterable = globalSettingRepository.findAll();
        SettingsResponse settingsResponse = new SettingsResponse();
        for (GlobalSetting globalSetting : globalSettingIterable) {
            if (globalSetting.getId() == 1) {
                settingsResponse.setMultiuserMode(globalSetting.getValue().name().equals("YES"));
            }
            if (globalSetting.getId() == 2) {
                settingsResponse.setPostPremoderation(globalSetting.getValue().name().equals("YES"));
            }
            if (globalSetting.getId() == 3) {
                settingsResponse.setStatisticsIsPublic(globalSetting.getValue().name().equals("YES"));
            }
        }
        return ResponseEntity.ok(settingsResponse);
    }
}
