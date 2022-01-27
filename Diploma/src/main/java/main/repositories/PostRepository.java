package main.repositories;

import main.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends CrudRepository<Post, Integer> {

    @Query("SELECT p FROM Post p WHERE p.time < NOW() AND p.isActive = 1 " +
            "AND p.moderationStatus = 'ACCEPTED' ORDER BY time DESC")
    Page<Post> findRecentPosts(Pageable pageable);


    @Query("SELECT p FROM Post p WHERE p.time < NOW() AND p.isActive = 1 " +
            "AND p.moderationStatus = 'ACCEPTED' ORDER BY time")
    Page<Post> findEarlyPosts(Pageable pageable);


    @Query("SELECT p FROM Post p " +
            "LEFT JOIN PostVotes pv ON pv.post = p.id " +
            "WHERE p.time < NOW() AND p.isActive = 1" +
            "AND p.moderationStatus = 'ACCEPTED' AND pv.value = 1" +
            "GROUP BY p.id ORDER BY COUNT(pv.value) DESC"
    )
    Page<Post> findBestPosts(Pageable pageable);


    @Query("SELECT p FROM Post p " +
            "LEFT JOIN PostComment pc ON pc.post = p.id " +
            "WHERE p.time < NOW() AND p.isActive = 1 " +
            "AND p.moderationStatus = 'ACCEPTED' " +
            "GROUP BY p.id ORDER BY COUNT(pc.id) DESC")
    Page<Post> findPopularPosts(Pageable pageable);
}
