package main.model;

import org.springframework.core.convert.converter.Converter;

public enum PostModerationStatus {
    INACTIVE(0, ModerationStatus.NEW),
    PENDING(1, ModerationStatus.NEW),
    DECLINED(1, ModerationStatus.DECLINED),
    PUBLISHED(1, ModerationStatus.ACCEPTED);

    private final int active;
    private final ModerationStatus moderationStatus;

    PostModerationStatus(int active, ModerationStatus moderationStatus) {
        this.active = active;
        this.moderationStatus = moderationStatus;
    }

    public int isActive() {
        return active;
    }

    public ModerationStatus getModerationStatus() {
        return moderationStatus;
    }

    public static class StringToEnumConverter implements Converter<String, PostModerationStatus> {
        @Override
        public PostModerationStatus convert(String s) {
            return PostModerationStatus.valueOf(s.toUpperCase());
        }
    }
}
