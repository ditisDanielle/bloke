package nl.mad.bacchus.repository;

import java.util.List;

import nl.mad.bacchus.model.Cheese;
import nl.mad.bacchus.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by fd on 1-10-16.
 */
public interface CheeseRepository extends JpaRepository<Cheese, Long> {


    Cheese findByName(String name);


    @Query("select cheese FROM Cheese cheese " +
            "where (:name = '' or lower(cheese.name) like lower(concat('%', :name, '%'))) " +
            "and (:cheeseType = '' or cheese.cheeseType = :cheeseType) " +
            "and (:milkType = '' or cheese.milkType = :milkType) ")
    List<Cheese> search(@Param("name") String name,
                      @Param("cheeseType") String cheeseType,
                      @Param("milkType") String milkType);

}