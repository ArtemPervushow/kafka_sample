package spongecell.animal_farm.model;

import lombok.Data;

@Data
@org.msgpack.annotation.Message
public class Animal {
    private Integer id;
    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
