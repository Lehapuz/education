package main.api.response;

import java.util.List;

public class TagListResponse {

    private List<TagResponse> tags;

    public List<TagResponse> getTags() {
        return tags;
    }

    public void setTags(List<TagResponse> tags) {
        this.tags = tags;
    }
}
