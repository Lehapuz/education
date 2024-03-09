package basavets.test.repository;

import basavets.test.entity.Data;
import java.util.Optional;

public interface CustomSumRepository {

    void save(Data data);
    void delete(Data data);
    Optional<Data> findByName(String name);
}
