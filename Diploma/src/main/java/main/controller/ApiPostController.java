package main.controller;

import main.api.response.AllPostResponse;
import main.api.response.TagListResponse;
import main.repositories.PostRepository;
import main.service.PostService;
import main.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class ApiPostController {

    private final PostService postService;
    private final TagService tagService;

    @Autowired
    private PostRepository postRepository;

    public ApiPostController(PostService postService, TagService tagService) {
        this.postService = postService;
        this.tagService = tagService;
    }

    @GetMapping("post")
    public ResponseEntity<AllPostResponse> postResponse(@RequestParam Integer offset,
                                                     @RequestParam Integer limit,
                                                     @RequestParam String mode){
        return ResponseEntity.ok(postService.getPost(offset, limit, mode));
    }


    @GetMapping("tag")
    public ResponseEntity<TagListResponse> tagResponse(){
        return ResponseEntity.ok(tagService.getTags());
        }
}
