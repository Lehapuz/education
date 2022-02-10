package main.service;

import main.api.response.SettingsResponse;
import main.model.GlobalSetting;
import main.repositories.GlobalSettingRepository;
import org.springframework.stereotype.Service;

@Service
public class SettingService {

    private final GlobalSettingRepository globalSettingRepository;

    public SettingService(GlobalSettingRepository globalSettingRepository) {
        this.globalSettingRepository = globalSettingRepository;
    }

    public SettingsResponse getSettings() {

        Iterable<GlobalSetting> globalSettingIterable = globalSettingRepository.findAll();
        SettingsResponse settingsResponse = new SettingsResponse();

        for (GlobalSetting globalSetting : globalSettingIterable) {
            if (globalSetting.getCode().equals("MULTIUSER_MODE") && globalSetting.getValue().name().equals("YES")) {
                    settingsResponse.setMultiuserMode(true);
            }
            if (globalSetting.getCode().equals("POST_PREMODERATION") && globalSetting.getValue().name().equals("YES")) {
                    settingsResponse.setPostPremoderation(true);
            }
            if (globalSetting.getCode().equals("STATISTICS_IS_PUBLIC") && globalSetting.getValue().name().equals("YES")) {
                    settingsResponse.setStatisticsIsPublic(true);
            }
        }
        return settingsResponse;
    }
}
