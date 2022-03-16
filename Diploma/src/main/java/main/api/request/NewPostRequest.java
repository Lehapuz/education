package main.api.request;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewPostRequest {

        private long timestamp;
        private int active;
        @NotBlank(message = "Заголовок поста не установлен")
        @Size(min = 5, message = "Заголовок поста слишком короткий")
        private String title;
        private List<String> tags;
        @NotBlank(message = "Текст поста не установлен")
        @Size(min = 50, message = "Текст поста менее 50 символов")
        private String text;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
