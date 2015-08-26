package nl.mad.bacchus.repository;

import nl.mad.bacchus.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    Photo findByWineId(Long wineId);
}