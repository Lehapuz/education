package main.repositories;

import main.api.response.PostResponse;
import main.model.ModerationStatus;
import main.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post, Integer> {

//    @Query("SELECT p FROM Post p")
//    Page<Post> findRecentPosts(Pageable pageable);

}
