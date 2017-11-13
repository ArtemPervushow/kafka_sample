package spongecell.animal_farm.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import spongecell.animal_farm.model.Animal;

import java.sql.PreparedStatement;

@Repository
public class AnimalRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Animal insert(Animal animal) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO animals (type) VALUES (?)");
            statement.setString(1, animal.getType());
            return statement;
        }, keyHolder);
        animal.setId((Integer)keyHolder.getKey());
        return animal;
    }

    public Animal selectById(Integer id) {
        return jdbcTemplate.queryForObject("SELECT id, type FROM animals WHERE id = ?", new BeanPropertyRowMapper<Animal>(Animal.class), id);
    }
}
