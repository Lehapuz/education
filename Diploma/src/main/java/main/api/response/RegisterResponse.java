package main.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;

public class RegisterResponse {

    private boolean result = true;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private RegisterErrorResponse errors;


    public RegisterErrorResponse getErrors() {
        return errors;
    }

    public void setErrors(RegisterErrorResponse errors) {
        this.errors = errors;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
