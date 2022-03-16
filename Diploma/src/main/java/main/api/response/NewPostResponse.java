package main.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.validation.Errors;

public class NewPostResponse {

    private String title;
    private String text;
    private boolean result = true;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Errors errors;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }
}
