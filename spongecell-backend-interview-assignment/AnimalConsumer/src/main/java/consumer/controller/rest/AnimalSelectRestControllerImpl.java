package consumer.controller.rest;

import consumer.service.AnimalSelectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spongecell.animal_farm.model.Animal;

/**
 * Created by a.pervushov on 13.11.2017.
 */

@RestController
public class AnimalSelectRestControllerImpl {
    private AnimalSelectService animalSelectService;

    @Autowired
    public void setAnimalSelectService(AnimalSelectService animalSelectService) {
        this.animalSelectService = animalSelectService;
    }

    @RequestMapping(value = "/animals/{id}")
    public Animal selectAnimal(@PathVariable Integer id) {

        return animalSelectService.selectAnimal(id);
    }

    @RequestMapping(value = "/test/{id}")
    public Animal selectAnimall(@PathVariable Integer id) {
        Animal animal = new Animal();
        animal.setId(1);
        animal.setType("type");
        return animal;
    }
}
