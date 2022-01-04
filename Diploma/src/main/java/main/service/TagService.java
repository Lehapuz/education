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
        TagResponse tagResponse3 = new TagResponse();

        TagListResponse tagListResponse = new TagListResponse();

        List<TagResponse> tags = new ArrayList<>();
        tagResponse.setName("Java");
        tagResponse.setWeight(1.0);
        tagResponse1.setName("Spring");
        tagResponse1.setWeight(0.56);
        tagResponse2.setName("Hibernate");
        tagResponse2.setWeight(0.22);
        tagResponse3.setName("Hadoop");
        tagResponse3.setWeight(0.17);
        tags.add(tagResponse);
        tags.add(tagResponse1);
        tags.add(tagResponse2);
        tags.add(tagResponse3);
        tagListResponse.setTags(tags);

        return tagListResponse;
    }
}
