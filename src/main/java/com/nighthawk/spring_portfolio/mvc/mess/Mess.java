package com.nighthawk.spring_portfolio.mvc.mess;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Data  // Annotations to simplify writing code (ie constructors, setters)
@NoArgsConstructor
@AllArgsConstructor
@Entity // Annotation to simplify creating an entity, which is a lightweight persistence domain object. Typically, an entity represents a table in a relational database, and each entity instance corresponds to a row in that table.
public class Mess {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique=true)
    private String mess;

    // starting jokes
    public static Integer[] init() {
        final Integer[] messsArray = {
            Integer.valueOf(2),
            Integer.valueOf(3)
        };
        return messsArray;
    }
}
