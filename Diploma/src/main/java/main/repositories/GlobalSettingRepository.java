package main.repositories;

import main.model.GlobalSetting;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;

@Repository
public interface GlobalSettingRepository extends CrudRepository<GlobalSetting, Integer> {

    @Query("SELECT gs FROM GlobalSetting gs")
    HashSet<GlobalSetting> selectGlobalSettings();

}
