package nl.mad.bacchus.repository;

import nl.mad.bacchus.model.Cheese;
import org.springframework.data.jpa.repository.JpaRepository;
import nl.mad.bacchus.model.Match;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by al on 24-10-16.
 */
public interface MatchRepository  extends JpaRepository<Match, Long> {

    List<Match> findAllByCheeseId(Long cheeseId);

    List<Match> findAllByWineId(Long wineId);

    Match findByWineIdAndCheeseId(Long wineId, Long cheeseId);

//    @Query("select match FROM Match match " +
//            "where (:cheeseId = : cheeseId) " +
//            "and (:wineId = : wineId) " )
//    List<Match> searchMatch(@Param("cheeseId") String cheeseId,
//                      @Param("wineID") String wineId);


}
