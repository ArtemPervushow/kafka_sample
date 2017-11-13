package consumer.model;

import spongecell.animal_farm.model.Animal;

/**
 * Created by a.pervushov on 13.11.2017.
 */
public interface AnimalDAO {
    Animal selectById(Integer id);
    Animal insert(Animal animal);
}
