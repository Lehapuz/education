package basavets.dto;

public class RegistrationResponse {

    private boolean isResult;
    private RegisterErrorResponse registerErrorResponse;

    public boolean isResult() {
        return isResult;
    }

    public void setResult(boolean result) {
        isResult = result;
    }

    public RegisterErrorResponse getRegisterErrorResponse() {
        return registerErrorResponse;
    }

    public void setRegisterErrorResponse(RegisterErrorResponse registerErrorResponse) {
        this.registerErrorResponse = registerErrorResponse;
    }
}
