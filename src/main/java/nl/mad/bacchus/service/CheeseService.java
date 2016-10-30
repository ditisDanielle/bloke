package nl.mad.bacchus.service;

import nl.mad.bacchus.model.Employee;
import nl.mad.bacchus.model.Photo;
import nl.mad.bacchus.model.Cheese;
import nl.mad.bacchus.model.meta.CheeseType;
import nl.mad.bacchus.model.meta.MilkType;
import nl.mad.bacchus.repository.CheeseRepository;
import nl.mad.bacchus.service.dto.PhotoDTO;
import nl.mad.bacchus.service.dto.CheeseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by fd on 1-10-16.
 */
@Service
public class CheeseService extends ReadService<Cheese>{


        private final CheeseRepository cheeseRepository;
        private final PhotoService photoService;

        @Autowired
        public CheeseService(CheeseRepository cheeseRepository, PhotoService photoservice) {
            super(cheeseRepository);
            this.cheeseRepository =  cheeseRepository;
            this.photoService = photoservice;
        }


        public List<Cheese> search(String name, CheeseType cheeseType, MilkType milkType) {
            String cheeseName = (name != null) ? name : "";
            String cheeseTypeName = (cheeseType != null) ? cheeseType.name() : "";
            String milkTypeName = (milkType != null) ? milkType.name() : "";
            return cheeseRepository.search(cheeseName, cheeseTypeName, milkTypeName);
        }


        @Secured(Employee.ADMIN)
        public Cheese create(CheeseDTO cheeseDTO) {
            return save(cheeseDTO.createCheese());
        }

        @Secured(Employee.ADMIN)
        public Cheese update(Long cheeseId, CheeseDTO cheeseDTO) {
            Cheese cheese = cheeseRepository.findOne(cheeseId);
            return save(cheeseDTO.update(cheese));
        }

        private Cheese save(Cheese cheese) {
            return cheeseRepository.save(cheese);
        }

        @Secured(Employee.ADMIN)
        public void delete(Long cheeseId) {
            cheeseRepository.delete(cheeseId);
        }


        public void savePhotoFor(Cheese cheese, PhotoDTO photoDTO) {
            if (!photoDTO.isEmpty()) {
                photoService.save(photoDTO.createPhotoFor(cheese));
            }
        }


        public Photo findPhotoFor(Long cheeseId) {
            return photoService.findPhotoFor(cheeseId);
        }
}


