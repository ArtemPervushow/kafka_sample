package spongecell.animal_farm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import spongecell.animal_farm.model.Animal;
import spongecell.animal_farm.model.AnimalRepository;

@RestController
@Deprecated
public class AnimalController {
    @Autowired
    AnimalRepository animalDAO;

    @RequestMapping(value = "/animals", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Animal createAnimal(@RequestBody Animal animal) {
        return animalDAO.insert(animal);
    }

    @RequestMapping(value = "/animals/{id}")
    public Animal selectAnimal(@PathVariable Integer id) {
        return animalDAO.selectById(id);
    }
}
