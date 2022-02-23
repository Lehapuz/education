package main.api.response;

public class LoginResponse {
    private boolean result;
    private UserLoginResponse user;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public UserLoginResponse getUser() {
        return user;
    }

    public void setUser(UserLoginResponse user) {
        this.user = user;
    }
}
