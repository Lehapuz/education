package main.api.response;

public class SettingsResponse {

    private String multiuserMode;
    private String postPremoderation;
    private String statisticsIsPublic;


    public String getMultiuserMode() {
        return multiuserMode;
    }

    public void setMultiuserMode(String multiuserMode) {
        this.multiuserMode = multiuserMode;
    }

    public String getPostPremoderation() {
        return postPremoderation;
    }

    public void setPostPremoderation(String postPremoderation) {
        this.postPremoderation = postPremoderation;
    }

    public String getStatisticsIsPublic() {
        return statisticsIsPublic;
    }

    public void setStatisticsIsPublic(String statisticsIsPublic) {
        this.statisticsIsPublic = statisticsIsPublic;
    }
}
