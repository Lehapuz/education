package main.service;

import main.api.response.AllPostResponse;
import main.api.response.CheckResponse;
import main.api.response.PostResponse;
import main.api.response.UserPostResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {


    public AllPostResponse getPost(){
        PostResponse postResponse = new PostResponse();
        AllPostResponse allPostResponse = new AllPostResponse();
        UserPostResponse userPostResponse = new UserPostResponse();
        List<PostResponse> posts = new ArrayList<>();

        userPostResponse.setId(1);
        userPostResponse.setName("Ваня");
        postResponse.setId(1);
        postResponse.setAnnounce("Охуенно");
        postResponse.setCommentCount(5);
        postResponse.setTimestamp(125557441L);
        postResponse.setLikeCount(5);
        postResponse.setDislikeCount(1);
        postResponse.setTitle("Говно");
        postResponse.setViewCount(20);
        postResponse.setUserPostResponse(postResponse.getUserPostResponse());
        allPostResponse.setCount(1);
        posts.add(postResponse);
        allPostResponse.setPosts(posts);

        return allPostResponse;
    }
}
