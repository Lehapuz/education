package main.service;

import main.api.request.NewPostRequest;
import main.api.response.*;
import main.model.*;
import main.repositories.PostRepository;
import main.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final SettingService settingService;
    private final TagService tagService;

    public PostService(PostRepository postRepository, UserRepository userRepository, SettingService settingService, TagService tagService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.settingService = settingService;
        this.tagService = tagService;
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

            for (PostComment postComment : post.get().getPostComments()) {
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

            for (Tag tag : post.get().getTags()) {
                tagList.add(tag.getName());
            }
            idPostResponse.setTags(tagList);

            int viewCount = post.get().getViewCount() + 1;
            post.get().setViewCount(viewCount);
            postRepository.save(post.get());
        }

        return idPostResponse;
    }

    public AllPostResponse findMyPosts(PostModerationStatus status, Pageable pageable) {

        AllPostResponse allPostResponse = new AllPostResponse();
        List<PostResponse> postResponses = new ArrayList<>();

        org.springframework.security.core.userdetails.User user
                = (org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User currentUser = userRepository.findByEmail(user.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(user.getUsername()));

        Page<Post> postsPage = postRepository.findMyPosts(
                currentUser.getId(),
                status.getModerationStatus(),
                status.isActive(),
                pageable);

        for (Post post : postsPage) {
            int like = 0;
            int dislike = 0;
            PostResponse postResponse = new PostResponse();
            UserPostResponse user1 = new UserPostResponse();

            user1.setId(post.getUser().getId());
            user1.setName(post.getUser().getName());

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
            postResponse.setUser(user1);
            postResponses.add(postResponse);
        }

        allPostResponse.setCount((int) postsPage.getTotalElements());
        allPostResponse.setPosts(postResponses);

        return allPostResponse;
    }


    public NewPostResponse addNewPost(NewPostRequest request, Errors errors) {
        NewPostResponse newPostResponse = new NewPostResponse();
        List<Tag> tags = newPostTags(request);

        org.springframework.security.core.userdetails.User user
                = (org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userRepository.findByEmail(user.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(user.getUsername()));


        if (errors.hasFieldErrors("title")) {
            newPostResponse.setResult(false);
            newPostResponse.setTitle(errors.getFieldError("title").getDefaultMessage());
        }
        if (errors.hasFieldErrors("text")) {
            newPostResponse.setResult(false);
            newPostResponse.setText(errors.getFieldError("text").getDefaultMessage());
        }

        HashSet<GlobalSetting> siteSettings = settingService.getSiteSettings();
        ModerationStatus moderationStatus = ModerationStatus.NEW;
        for (GlobalSetting setting : siteSettings) {
            if (setting.getCode().equals("POST_PREMODERATION") && !setting.getValue().name().equals("YES")) {
                moderationStatus = ModerationStatus.ACCEPTED;
                break;
            }
        }

        Post post = new Post();
        post.setIsActive(1);
        post.setModerationStatus(moderationStatus);
        post.setUser(currentUser);
        post.setTags(tags);
        post.setTime(LocalDateTime.now());
        post.setTitle(request.getTitle());
        post.setText(request.getText());
        post.setViewCount(0);
// мне нужно избавиться от от этих значений
        post.setModeratorId(currentUser);
//
        postRepository.save(post);

        return newPostResponse;
    }


    public NewPostResponse updatePost(Integer id, NewPostRequest request, Errors errors) {
        NewPostResponse newPostResponse = new NewPostResponse();
        Optional<Post> currentPost = postRepository.findById(id);


        org.springframework.security.core.userdetails.User user
                = (org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userRepository.findByEmail(user.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(user.getUsername()));


        if (errors.hasFieldErrors("title")) {
            newPostResponse.setResult(false);
            newPostResponse.setTitle(errors.getFieldError("title").getDefaultMessage());
        }
        if (errors.hasFieldErrors("text")) {
            newPostResponse.setResult(false);
            newPostResponse.setText(errors.getFieldError("text").getDefaultMessage());
        }

        HashSet<GlobalSetting> siteSettings = settingService.getSiteSettings();
        ModerationStatus moderationStatus = ModerationStatus.NEW;
        for (GlobalSetting setting : siteSettings) {
            if (setting.getCode().equals("POST_PREMODERATION") && !setting.getValue().name().equals("YES")) {
                moderationStatus = ModerationStatus.ACCEPTED;
                break;
            }
        }
        List<Tag> tags = newPostTags(request);

        if (currentPost.isPresent()) {
            Post post = currentPost.get();

            post.setIsActive(1);
            post.setModerationStatus(moderationStatus);
            post.setUser(currentUser);
            post.setTags(tags);
            post.setTime(LocalDateTime.now());
            post.setTitle(request.getTitle());
            post.setText(request.getText());
            post.setViewCount(0);
            // мне нужно избавиться от от этих значений
            post.setModeratorId(currentUser);

            postRepository.save(post);
        }


        return newPostResponse;
    }


    private List<Tag> newPostTags(NewPostRequest request) {
        List<Tag> tags = new ArrayList<>();
        if (request.getTags() != null) {
            request.getTags().forEach(tag -> tags.add(takeTag(tag)));
        }
        return tags;
    }


    private Tag takeTag(String name) {
        Tag tag = tagService.findTagByName(name);
        return (tag != null) ? tag : tagService.saveNewTag(name);
    }
}



