package main.controller;

import main.api.request.NewPostRequest;
import main.api.response.*;
import main.model.PostModerationStatus;
import main.service.PostService;
import main.service.TagService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/")
public class ApiPostController {

    private final PostService postService;
    private final TagService tagService;

    public ApiPostController(PostService postService, TagService tagService) {
        this.postService = postService;
        this.tagService = tagService;
    }


    @GetMapping("post")
    public ResponseEntity<AllPostResponse> postResponse(@RequestParam Integer offset,
                                                        @RequestParam Integer limit,
                                                        @RequestParam String mode) {

        return ResponseEntity.ok(postService.getPost(mode, PageRequest.of(offset / limit, limit)));
    }


    @GetMapping("tag")
    public ResponseEntity<TagListResponse> tagResponse(@RequestParam(required = false) String query) {
        return ResponseEntity.ok(tagService.getTags(query));
    }


    @GetMapping("post/search")
    public ResponseEntity<AllPostResponse> postBySearchResponse(@RequestParam Integer offset,
                                                                @RequestParam Integer limit,
                                                                @RequestParam String query) {
        return ResponseEntity.ok(postService.getPostBySearch(query, PageRequest.of(offset / limit, limit)));
    }


    @GetMapping("calendar")
    public ResponseEntity<CalendarResponse> postByYearResponse(@RequestParam(required = false) Integer year) {
        return ResponseEntity.ok(postService.getPostByYear(year));
    }


    @GetMapping("post/byDate")
    public ResponseEntity<AllPostResponse> postByDateResponse(@RequestParam Integer offset,
                                                              @RequestParam Integer limit,
                                                              @RequestParam String date) {
        return ResponseEntity.ok(postService.getPostByDate(date, PageRequest.of(offset / limit, limit)));
    }


    @GetMapping("post/byTag")
    public ResponseEntity<AllPostResponse> postByTagResponse(@RequestParam Integer offset,
                                                             @RequestParam Integer limit,
                                                             @RequestParam String tag) {
        return ResponseEntity.ok(postService.getPostByTag(tag, PageRequest.of(offset / limit, limit)));
    }


    @GetMapping("post/{id}")
    public ResponseEntity<IdPostResponse> postByIdResponse(@PathVariable int id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }


    @GetMapping("post/my")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<AllPostResponse> findMyPostsResponse(@RequestParam Integer offset,
                                                               @RequestParam Integer limit,
                                                               @RequestParam PostModerationStatus status) {
        return ResponseEntity.ok(postService.findMyPosts(status, PageRequest.of((int) offset / limit, limit)));
    }


    @PostMapping("post")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<NewPostResponse> addNewPost(@RequestBody @Valid NewPostRequest request, Errors errors){
        return ResponseEntity.ok(postService.addNewPost(request, errors));
    }


    @PutMapping("post/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<NewPostResponse> updatePost(@PathVariable Integer id,
            @RequestBody @Valid NewPostRequest request, Errors errors){
        return ResponseEntity.ok(postService.updatePost(id, request, errors));
    }
}
