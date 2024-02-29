package basavets.test.repository;

import basavets.test.entity.Data;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SumRepository extends JpaRepository<Data, Long> {
    Data findByName(String name);
}
