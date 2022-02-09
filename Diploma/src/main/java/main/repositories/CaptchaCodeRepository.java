package main.repositories;

import main.model.CaptchaCode;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@Transactional
public interface CaptchaCodeRepository extends CrudRepository<CaptchaCode, Integer> {

    @Modifying
    @Query("DELETE FROM CaptchaCode c WHERE time < :time")
    void deleteLessThanTime(LocalDateTime time);

    Optional<CaptchaCode> findBySecretCode(String secretCode);
}
