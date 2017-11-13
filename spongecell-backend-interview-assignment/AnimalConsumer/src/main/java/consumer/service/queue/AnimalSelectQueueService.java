package consumer.service.queue;

import consumer.model.AnimalDAO;
import consumer.service.AnimalSelectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spongecell.animal_farm.model.Animal;

/**
 * Created by a.pervushov on 13.11.2017.
 */
@Service
public class AnimalSelectQueueService implements AnimalSelectService {
    private AnimalDAO animalDAO;

    @Autowired
    public void setAnimalDAO(AnimalDAO animalDAO) {
        this.animalDAO = animalDAO;
    }

    @Override
    public Animal selectAnimal(Integer id) {
        return animalDAO.selectById(id);
    }
}
