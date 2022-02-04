package main.api.response;

public class CommentResponse {

    private Integer id;
    private Long timestamp;
    private String text;
    private UserPostCommentsResponse user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserPostCommentsResponse getUser() {
        return user;
    }

    public void setUser(UserPostCommentsResponse user) {
        this.user = user;
    }
}
