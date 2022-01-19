package main.service;

import main.api.response.TagListResponse;
import main.api.response.TagResponse;
import main.model.ModerationStatus;
import main.model.Post;
import main.model.Tag;
import main.repositories.PostRepository;
import main.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private PostRepository postRepository;

    TagListResponse tagListResponse = new TagListResponse();

    public TagListResponse getTags(String query) {

        Iterable<Tag> tags = tagRepository.findAll();
        Iterable<Post> posts = postRepository.findAll();
        List<TagResponse> tagList = new ArrayList<>();
        List<Post> postList = new ArrayList<>();

        for (Post post : posts) {
            if (post.getIsActive() == 1 && ModerationStatus.ACCEPTED.equals(post.getModerationStatus())) {
                postList.add(post);
            }
        }

        for (Tag tag : tags) {
            double frequency = 0.0;
            for (Post post : posts) {
                if (post.getText().contains(tag.getName())) {
                    frequency++;
                }
            }
            double tagWeight = frequency / (double) postList.size();
            TagResponse tagResponse = new TagResponse();
            tagResponse.setName(tag.getName());
            tagResponse.setWeight(tagWeight * normalizeCoefficient(postList));
            tagList.add(tagResponse);
            tagListResponse.setTags(tagList);
        }
        return tagListResponse;
    }


    private double normalizeCoefficient(List<Post> posts) {
        Iterable<Post> iterablePosts = postRepository.findAll();
        Iterable<Tag> iterableTags = tagRepository.findAll();
        List<Double> weights = new ArrayList<>();
        double k;
        for (Tag tag : iterableTags) {
            double frequency = 0.0;
            posts.clear();
            for (Post post : iterablePosts) {
                if (post.getIsActive() == 1 && ModerationStatus.ACCEPTED.equals(post.getModerationStatus())) {
                    posts.add(post);
                }
                if (post.getText().contains(tag.getName())) {
                    frequency++;
                }
            }
            Double tagWeight = frequency / (double) posts.size();
            weights.add(tagWeight);
        }
        weights.sort(Comparator.comparingDouble(Double::doubleValue));
        k = 1 / weights.get(weights.size() - 1);

        return k;
    }
}
