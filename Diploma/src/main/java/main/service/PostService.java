package main.service;

import main.api.response.AllPostResponse;
import main.api.response.CheckResponse;
import main.api.response.PostResponse;
import main.api.response.UserPostResponse;
import main.model.Post;
import main.model.PostComment;
import main.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    private int count = 0;

    public AllPostResponse getPost(Integer offset,
                                   Integer limit,
                                   String mode) {

        AllPostResponse allPostResponse = new AllPostResponse();
        List<PostResponse> postResponses = new ArrayList<>();
        Iterable<Post> posts = postRepository.findAll();

        for (Post post : posts) {
            PostResponse postResponse = new PostResponse();
            UserPostResponse userPostResponse = new UserPostResponse();

            userPostResponse.setId(post.getUser().getId());
            userPostResponse.setName(post.getUser().getName());
            postResponse.setId(post.getId());
            postResponse.setAnnounce(post.getText());

            postResponse.setCommentCount(5);
            postResponse.setTimestamp(1565652L);

            postResponse.setLikeCount(5);
            postResponse.setDislikeCount(1);

            postResponse.setTitle(post.getTitle());
            postResponse.setViewCount(post.getViewCount());

            postResponse.setUserPostResponse(userPostResponse);
            postResponses.add(postResponse);
            count = count++;
        }

            allPostResponse.setCount(postResponses.size());
            allPostResponse.setPosts(postResponses);

            return allPostResponse;
        }

}
