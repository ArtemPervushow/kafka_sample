package producer.service.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import producer.model.AnimalCreateDAO;
import producer.service.AnimalCreateService;
import spongecell.animal_farm.model.Animal;

/**
 * Created by a.pervushov on 13.11.2017.
 */
@Service
public class AnimalCreateServiceQueue implements AnimalCreateService {

    private AnimalCreateDAO animalCreateDAO;

    @Autowired
    public void setAnimalCreateDAO(AnimalCreateDAO animalCreateDAO) {
        this.animalCreateDAO = animalCreateDAO;
    }

    @Override
    public Animal createAnimal(Animal animal) {
        return animalCreateDAO.persistAnimal(animal);
    }

}
