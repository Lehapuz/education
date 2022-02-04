package main.service;

import main.api.response.*;
import main.model.Post;
import main.model.PostComment;
import main.model.PostVotes;
import main.model.Tag;
import main.repositories.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PostService {

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


    public AllPostResponse getPostBySearch(String query, Pageable pageable) {
        AllPostResponse allPostResponse = new AllPostResponse();
        List<PostResponse> postResponses = new ArrayList<>();
        Page<Post> posts = postRepository.findPostsBySearch(query, pageable);

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

        allPostResponse.setCount((int) posts.getTotalElements());
        allPostResponse.setPosts(postResponses);

        return allPostResponse;
    }


    public CalendarResponse getPostByYear(Integer year) {
        CalendarResponse calendarResponse = new CalendarResponse();
        Set<Integer> years = new TreeSet<>();
        Map<String, Integer> posts = new TreeMap<>();
        DateTimeFormatter dt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        int count = 1;

        List<Post> postList = postRepository.findPostsByYear();

        for (Post post : postList) {
            if (!posts.containsKey(post.getTime().format(dt))) {
                count = 1;
            }
            if (posts.containsKey(post.getTime().format(dt))) {
                count = count + 1;
            }

            years.add(post.getTime().getYear());
            posts.put(post.getTime().format(dt), count);

            calendarResponse.setYears(years);
            calendarResponse.setPosts(posts);
        }
        return calendarResponse;
    }


    public AllPostResponse getPostByDate(String date, Pageable pageable) {
        AllPostResponse allPostResponse = new AllPostResponse();
        List<PostResponse> postResponses = new ArrayList<>();
        Page<Post> posts = postRepository.findPostByDate(date, pageable);

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

        allPostResponse.setCount((int) posts.getTotalElements());
        allPostResponse.setPosts(postResponses);

        return allPostResponse;
    }


    public AllPostResponse getPostByTag(String tag, Pageable pageable) {
        AllPostResponse allPostResponse = new AllPostResponse();
        List<PostResponse> postResponses = new ArrayList<>();
        Page<Post> posts = postRepository.findPostsByTag(tag, pageable);

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

        allPostResponse.setCount((int) posts.getTotalElements());
        allPostResponse.setPosts(postResponses);

        return allPostResponse;
    }

    public IdPostResponse getPostById(Integer id) {
        Optional<Post> post = postRepository.findById(id);
        UserPostResponse userPostResponse = new UserPostResponse();

        IdPostResponse idPostResponse = new IdPostResponse();
        List<CommentResponse> commentResponseList = new ArrayList<>();
        List<String> tagList = new ArrayList<>();
        if (post.isPresent()) {
            idPostResponse.setId(id);
            idPostResponse.setTimestamp(post.get().getTime().toEpochSecond(ZoneOffset.UTC));
            idPostResponse.setActive(true);

            userPostResponse.setId(post.get().getUser().getId());
            userPostResponse.setName(post.get().getUser().getName());
            idPostResponse.setUser(userPostResponse);

            idPostResponse.setTitle(post.get().getTitle());
            idPostResponse.setText(post.get().getText());

            int like = 0;
            int dislike = 0;
            for (PostVotes postVote : post.get().getPostVotesList()) {
                if (postVote.getValue() > 0) {
                    like = like + 1;
                }
                if (postVote.getValue() < 0) {
                    dislike = dislike + 1;
                }
            }
            idPostResponse.setLikeCount(like);
            idPostResponse.setDislikeCount(dislike);

            idPostResponse.setViewCount(post.get().getViewCount());

            for (PostComment postComment : post.get().getPostComments()){
                CommentResponse commentResponse = new CommentResponse();
                UserPostCommentsResponse userPostCommentsResponse = new UserPostCommentsResponse();
                commentResponse.setId(postComment.getId());
                commentResponse.setTimestamp(postComment.getRegTime().toEpochSecond(ZoneOffset.UTC));
                commentResponse.setText(postComment.getText());

                userPostCommentsResponse.setId(postComment.getUser().getId());
                userPostCommentsResponse.setName(postComment.getUser().getName());
                userPostCommentsResponse.setPhoto(postComment.getUser().getPhoto());
                commentResponse.setUser(userPostCommentsResponse);
                commentResponseList.add(commentResponse);
            }
            idPostResponse.setComments(commentResponseList);

            for (Tag tag : post.get().getTags()){
                tagList.add(tag.getName());
            }
            idPostResponse.setTags(tagList);

            int viewCount = post.get().getViewCount() + 1;
            post.get().setViewCount(viewCount);
            postRepository.save(post.get());
        }

        return idPostResponse;
    }
}



