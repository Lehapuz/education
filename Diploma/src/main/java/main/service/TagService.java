package main.service;

import main.api.response.TagListResponse;
import main.api.response.TagResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {

    public TagListResponse getTags(){
        TagResponse tagResponse = new TagResponse();
        TagResponse tagResponse1 = new TagResponse();
        TagResponse tagResponse2 = new TagResponse();

        TagListResponse tagListResponse = new TagListResponse();

        List<TagResponse> tags = new ArrayList<>();
        tagResponse.setName("Привет");
        tagResponse.setWeight(1.0);
        tagResponse1.setName("Видимся");
        tagResponse1.setWeight(0.56);
        tagResponse2.setName("Пока");
        tagResponse2.setWeight(0.55);
        tags.add(tagResponse);
        tags.add(tagResponse1);
        tags.add(tagResponse2);
        tagListResponse.setTags(tags);


        return tagListResponse;
    }
}
