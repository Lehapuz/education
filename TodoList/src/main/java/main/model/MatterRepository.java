package main.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatterRepository extends CrudRepository<Matter, Integer>{
}