package producer.model;

import spongecell.animal_farm.model.Animal;

/**
 * Created by a.pervushov on 13.11.2017.
 */
public interface AnimalCreateDAO {
    Animal persistAnimal(Animal animal);
}
