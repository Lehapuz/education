package basavets.dto;

public class LoginResponse {

    private boolean isResult;
    private UserLoginResponse userLoginResponse;

    public boolean isResult() {
        return isResult;
    }

    public void setResult(boolean result) {
        isResult = result;
    }

    public UserLoginResponse getUserLoginResponse() {
        return userLoginResponse;
    }

    public void setUserLoginResponse(UserLoginResponse userLoginResponse) {
        this.userLoginResponse = userLoginResponse;
    }
}
