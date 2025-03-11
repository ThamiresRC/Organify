package br.com.fiap.calendario.model;

import java.util.Random;

public class Calendar {
    
    private Long id;
    private String name;
    private String type;

    public Calendar(Long id, String name, String type) {
        this.id = Math.abs(new Random().nextLong());
        this.name = name;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
    public void setId(long id){
        this.id = id;
    }

    @Override
    public String toString() {
        return "Calendar [id=" + id + ", name=" + name + ", type=" + type + "]";
    }
    
}
