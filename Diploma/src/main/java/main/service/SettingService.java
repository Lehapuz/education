package main.service;

import main.api.response.SettingsResponse;
import main.model.GlobalSetting;
import main.repositories.GlobalSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingService {

    @Autowired
    private final GlobalSettingRepository globalSettingRepository;

    public SettingService(GlobalSettingRepository globalSettingRepository) {
        this.globalSettingRepository = globalSettingRepository;
    }

    public SettingsResponse getSettings() {

        Iterable<GlobalSetting> globalSettingIterable = globalSettingRepository.findAll();
        SettingsResponse settingsResponse = new SettingsResponse();

        for (GlobalSetting globalSetting : globalSettingIterable) {
            if (globalSetting.getCode().equals("MULTIUSER_MODE")) {
                settingsResponse.setMultiuserMode(globalSetting.getValue().name());
            }

            if (globalSetting.getCode().equals("POST_PREMODERATION")) {
                settingsResponse.setPostPremoderation(globalSetting.getValue().name());
            }

            if (globalSetting.getCode().equals("STATISTICS_IS_PUBLIC")) {
                settingsResponse.setStatisticsIsPublic(globalSetting.getValue().name());
            }
        }
        return settingsResponse;
    }
}
