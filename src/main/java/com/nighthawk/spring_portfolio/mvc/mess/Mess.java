package com.nighthawk.spring_portfolio.mvc.mess;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Data  // Annotations to simplify writing code (ie constructors, setters)
@Entity // Annotation to simplify creating an entity, which is a lightweight persistence domain object. Typically, an entity represents a table in a relational database, and each entity instance corresponds to a row in that table.
public class Mess {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String mess;

    public static String[] init(){
        final String[] messesArray = new String[2];
        messesArray[0] = "hi";
        messesArray[1] = "hello";
        return messesArray;
    }

    public Mess(Long id, String mess){
        this.mess = mess;
    }

    public Mess(){
        this.mess = null;
    }
}
