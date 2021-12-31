package main.controller;

import main.api.response.AllPostResponse;
import main.api.response.TagListResponse;
import main.api.response.TagResponse;
import main.service.PostService;
import main.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class ApiPostController {

//    private final PostResponse postResponse;
//    private final AllPostResponse allPostResponse;
//    private final UserPostResponse userPostResponse;
    private final PostService postService;
    private final TagService tagService;

    public ApiPostController(PostService postService, TagService tagService) {
        this.postService = postService;
        this.tagService = tagService;
    }

    @GetMapping("post")
    public ResponseEntity<AllPostResponse> postResponse(){
        return ResponseEntity.ok(postService.getPost());
    }
//    public ResponseEntity<PostResponse> postResponse(@RequestParam Integer offset,
//                                                     @RequestParam Integer limit,
//                                                     @RequestParam String mode) {
//
//
//
//        return ResponseEntity.ok(postResponse);
//    }
    @GetMapping("tag")
    public ResponseEntity<TagListResponse> tagResponse(){
        return ResponseEntity.ok(tagService.getTags());
        }
}
