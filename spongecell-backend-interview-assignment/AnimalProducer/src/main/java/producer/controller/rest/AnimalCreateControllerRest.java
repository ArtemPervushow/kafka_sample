package producer.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import producer.service.AnimalCreateService;
import spongecell.animal_farm.model.Animal;

/**
 * Created by a.pervushov on 13.11.2017.
 */
@RestController
public class AnimalCreateControllerRest {
    private AnimalCreateService animalService;

    @Autowired
    public void setAnimalService(AnimalCreateService animalService) {
        this.animalService = animalService;
    }

    @RequestMapping(value = "/animals", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Animal createAnimal(@RequestBody Animal animal) {
        return animalService.createAnimal(animal);
    }

}
