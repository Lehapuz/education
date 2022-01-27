package main.service;

import main.api.response.AllPostResponse;
import main.api.response.PostResponse;
import main.api.response.UserPostResponse;
import main.model.Post;
import main.model.PostVotes;
import main.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    public AllPostResponse getPost(String mode, Pageable pageable) {
        AllPostResponse allPostResponse = new AllPostResponse();
        List<PostResponse> postResponses = new ArrayList<>();
        Page<Post> posts = null;

        if (mode.contains("recent")) {
            posts = postRepository.findRecentPosts(pageable);
        }
        if (mode.contains("early")) {
            posts = postRepository.findEarlyPosts(pageable);
        }
        if (mode.contains("best")) {
            posts = postRepository.findBestPosts(pageable);
        }
        if (mode.contains("popular")) {
            posts = postRepository.findPopularPosts(pageable);
        }

        if (posts != null) {
            for (Post post : posts) {
                int like = 0;
                int dislike = 0;

                PostResponse postResponse = new PostResponse();

                UserPostResponse user = new UserPostResponse();
                user.setId(post.getUser().getId());
                user.setName(post.getUser().getName());

                postResponse.setId(post.getId());

                String announce = post.getText().replaceAll("<.*?>", "");
                announce = announce.length() > 150 ? announce.substring(0, 150) + "..." : announce;
                postResponse.setAnnounce(announce);

                postResponse.setCommentCount(post.getPostComments().size());

                postResponse.setTimestamp(post.getTime().toEpochSecond(ZoneOffset.UTC));

                for (PostVotes postVote : post.getPostVotesList()) {
                    if (postVote.getValue() > 0) {
                        like = like + 1;
                    }
                    if (postVote.getValue() < 0) {
                        dislike = dislike + 1;
                    }
                }

                postResponse.setLikeCount(like);
                postResponse.setDislikeCount(dislike);

                postResponse.setTitle(post.getTitle());
                postResponse.setViewCount(post.getViewCount());
                postResponse.setUser(user);
                postResponses.add(postResponse);
            }
        }

        if (posts != null) {
            allPostResponse.setCount((int) posts.getTotalElements());
        }
        allPostResponse.setPosts(postResponses);

        return allPostResponse;
    }
}



