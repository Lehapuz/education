package main.api.response;

public class AuthCheckResponse {

    private Integer id;

    private String name;

    private String photo;

    private String email;

    private Boolean moderation;

    private Integer moderationCount;

    private Boolean settings;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getModerationCount() {
        return moderationCount;
    }

    public void setModerationCount(Integer moderationCount) {
        this.moderationCount = moderationCount;
    }

    public Boolean getSettings() {
        return settings;
    }

    public void setSettings(Boolean settings) {
        this.settings = settings;
    }

    public Boolean getModeration() {
        return moderation;
    }

    public void setModeration(Boolean moderation) {
        this.moderation = moderation;
    }
}
