package main.service;

import main.api.response.AllPostResponse;
import main.api.response.PostResponse;
import main.api.response.UserPostResponse;
import main.model.ModerationStatus;
import main.model.Post;
import main.model.PostComment;
import main.model.PostVotes;
import main.repositories.PostCommentRepository;
import main.repositories.PostRepository;
import main.repositories.PostVotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostCommentRepository postCommentRepository;
    @Autowired
    private PostVotesRepository postVotesRepository;

    private int count = 0;

    public AllPostResponse getPost(String mode, Pageable pageable) {
        AllPostResponse allPostResponse = new AllPostResponse();
        List<PostResponse> postResponses = new ArrayList<>();

        Iterable<Post> posts = postRepository.findAll();
        Iterable<PostComment> postComments = postCommentRepository.findAll();
        Iterable<PostVotes> postVotes = postVotesRepository.findAll();

        for (Post post : posts) {
            int comment = 0;
            int like = 0;
            int dislike = 0;
            if (post.getIsActive() == 1 && ModerationStatus.ACCEPTED.equals(post.getModerationStatus())) {

                PostResponse postResponse = new PostResponse();

                UserPostResponse user = new UserPostResponse();
                user.setId(post.getUser().getId());
                user.setName(post.getUser().getName());

                postResponse.setId(post.getId());

                String announce = post.getText().replaceAll("<.*?>", "");
                announce = announce.length() > 150 ? announce.substring(0, 150) + "..." : announce;
                postResponse.setAnnounce(announce);

                for (PostComment postComment : postComments) {
                    if (postComment.getPost().getId().equals(post.getId())) {
                        comment = comment + 1;
                    }
                }
                postResponse.setCommentCount(comment);

                postResponse.setTimestamp(post.getTime().toEpochSecond(ZoneOffset.UTC));

                for (PostVotes postVote : postVotes) {
                    if (postVote.getPostId().getId().equals(post.getId())) {
                        if (postVote.getValue() > 0) {
                            like = like + 1;
                        }
                        if (postVote.getValue() < 0) {
                            dislike = dislike + 1;
                        }
                    }
                    postResponse.setLikeCount(like);
                    postResponse.setDislikeCount(dislike);
                }

                postResponse.setTitle(post.getTitle());
                postResponse.setViewCount(post.getViewCount());
                postResponse.setUser(user);
                postResponses.add(postResponse);
                count = count++;
            }
        }

        if (mode.contains("recent")) {
            postResponses.sort(Comparator.comparing(PostResponse::getTimestamp).reversed());
            allPostResponse.setCount(postResponses.size());
            allPostResponse.setPosts(postResponses);
        }

        if (mode.contains("popular")) {
            postResponses.sort(Comparator.comparing(PostResponse::getCommentCount).reversed());
            allPostResponse.setCount(postResponses.size());
            allPostResponse.setPosts(postResponses);
        }

        if (mode.contains("best")) {
            postResponses.sort(Comparator.comparing(PostResponse::getLikeCount).reversed());
            allPostResponse.setCount(postResponses.size());
            allPostResponse.setPosts(postResponses);
        }

        if (mode.contains("early")) {
            postResponses.sort(Comparator.comparing(PostResponse::getTimestamp));
            allPostResponse.setCount(postResponses.size());
            allPostResponse.setPosts(postResponses);
        }
        return allPostResponse;
    }
}
