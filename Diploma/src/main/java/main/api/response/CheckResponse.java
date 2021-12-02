package main.api.response;

public class CheckResponse {

    private Boolean result;

    private AuthCheckResponse authCheckResponse;


    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public AuthCheckResponse getAuthCheckResponse() {
        return authCheckResponse;
    }

    public void setAuthCheckResponse(AuthCheckResponse authCheckResponse) {
        this.authCheckResponse = authCheckResponse;
    }
}
