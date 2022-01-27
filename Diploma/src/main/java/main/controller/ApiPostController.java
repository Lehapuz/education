package main.controller;

import main.api.response.AllPostResponse;
import main.api.response.TagListResponse;
import main.service.PostService;
import main.service.TagService;
import org.springframework.data.domain.PageRequest;
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

    public ApiPostController(PostService postService, TagService tagService) {
        this.postService = postService;
        this.tagService = tagService;
    }

    @GetMapping("post")
    public ResponseEntity<AllPostResponse> postResponse(@RequestParam Integer offset,
                                                        @RequestParam Integer limit,
                                                        @RequestParam String mode) {

        return ResponseEntity.ok(postService.getPost(mode, PageRequest.of(offset / limit, limit)));
//        return new ResponseEntity<>(postService.getPost(offset,limit,mode,PageRequest.of((int) offset / limit, limit)), HttpStatus.OK);
    }


    @GetMapping("tag")
    public ResponseEntity<TagListResponse> tagResponse(@RequestParam(required = false) String query) {
        return ResponseEntity.ok(tagService.getTags(query));
    }
}
