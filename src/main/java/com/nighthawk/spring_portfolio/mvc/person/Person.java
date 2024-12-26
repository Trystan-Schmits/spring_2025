package com.nighthawk.spring_portfolio.mvc.person;


import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Convert;
import static jakarta.persistence.FetchType.EAGER;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import jakarta.persistence.CascadeType;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.format.annotation.DateTimeFormat;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nighthawk.spring_portfolio.mvc.userStocks.userStocksTable;
import com.vladmihalcea.hibernate.type.json.JsonType;
import com.nighthawk.spring_portfolio.mvc.bathroom.Tinkle;
import com.nighthawk.spring_portfolio.mvc.student.StudentInfo;

import jakarta.persistence.FetchType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;


/**
 * Person is a POJO, Plain Old Java Object.
 * --- @Data is Lombox annotation
 * for @Getter @Setter @ToString @EqualsAndHashCode @RequiredArgsConstructor
 * --- @AllArgsConstructor is Lombox annotation for a constructor with all
 * arguments
 * --- @NoArgsConstructor is Lombox annotation for a constructor with no
 * arguments
 * --- @Entity annotation is used to mark the class as a persistent Java class.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Convert(attributeName = "person", converter = JsonType.class)
public class Person implements Comparable<Person> {

    private static Person createPerson(String name, String email, String uid, String password, Boolean kasmServerNeeded, String balance, String dob, List<String> asList) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /** Automatic unique identifier for Person record 
     * --- Id annotation is used to specify the identifier property of the entity.
     * ----GeneratedValue annotation is used to specify the primary key generation
     * strategy to use.
     * ----- The strategy is to have the persistence provider pick an appropriate
     * strategy for the particular database.
     * ----- GenerationType.AUTO is the default generation type and it will pick the
     * strategy based on the used database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // @OneToMany(mappedBy="student")
    // private List<SynergyGrade> grades;
    
    @ManyToMany(fetch = EAGER)
    @JoinTable(
        name = "person_person_sections",  // unique name to avoid conflicts
        joinColumns = @JoinColumn(name = "person_id"),
        inverseJoinColumns = @JoinColumn(name = "section_id")
    )
    private Collection<PersonSections> sections = new ArrayList<>();

    /**
     * Many to Many relationship with PersonRole
     * --- @ManyToMany annotation is used to specify a many-to-many relationship
     * between the entities.
     * --- FetchType.EAGER is used to specify that data must be eagerly fetched,
     * meaning that it must be loaded immediately.
     * --- Collection is a root interface in the Java Collection Framework, in this
     * case it is used to store PersonRole objects.
     * --- ArrayList is a resizable array implementation of the List interface,
     * allowing all elements to be accessed using an integer index.
     * --- PersonRole is a POJO, Plain Old Java Object.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<PersonRole> roles = new ArrayList<>();

    @OneToOne(mappedBy = "person", cascade=CascadeType.ALL)
    private Tinkle timeEntries;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "person")
    @JsonIgnore
    private StudentInfo studentInfo;
    /**
     * email, password, roles are key attributes to login and authentication
     * --- @NotEmpty annotation is used to validate that the annotated field is not
     * null or empty, meaning it has to have a value.
     * --- @Size annotation is used to validate that the annotated field is between
     * the specified boundaries, in this case greater than 5.
     * --- @Email annotation is used to validate that the annotated field is a valid
     * email address.
     * --- @Column annotation is used to specify the mapped column for a persistent
     * property or field, in this case unique and email.
     */
    @NotEmpty
    @Size(min = 5)
    @Column(unique = true, nullable = false)
    @Email
    private String email;

    @Column(unique = true, nullable = false)
    private String uid; // New `uid` column added


    @NotEmpty
    private String password;

    /**
     * name, dob are attributes to describe the person
     * --- @NonNull annotation is used to generate a constructor witha
     * AllArgsConstructor Lombox annotation.
     * --- @Size annotation is used to validate that the annotated field is between
     * the specified boundaries, in this case between 2 and 30 characters.
     * --- @DateTimeFormat annotation is used to declare a field as a date, in this
     * case the pattern is specified as yyyy-MM-dd.
     */
    @NonNull
    @Size(min = 2, max = 30, message = "Name (2 to 30 chars)")
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;

    /** Profile picture (pfp) in base64 */
    @Column(length = 255, nullable = true)
    private String pfp;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean kasmServerNeeded = false;

    /**
     * user_stocks and balance describe properties used by the gamify application
     */
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "person")
    @JsonIgnore
    private userStocksTable user_stocks;

    @Column
    private String balance;
    public double getBalanceDouble() {
        var balance_tmp = getBalance();
        return Double.parseDouble(balance_tmp);
    }
    public String setBalanceString(double updatedBalance) {
        this.balance = String.valueOf(updatedBalance); // Update the balance as a String
        return this.balance; // Return the updated balance as a String
    }
    /**
     * stats is used to store JSON for daily stats
     * --- @JdbcTypeCode annotation is used to specify the JDBC type code for a
     * column, in this case json.
     * --- @Column annotation is used to specify the mapped column for a persistent
     * property or field, in this case columnDefinition is specified as jsonb.
     * * * Example of JSON data:
     * "stats": {
     * "2022-11-13": {
     * "calories": 2200,
     * "steps": 8000
     * }
     * }
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Map<String, Object>> stats = new HashMap<>();

    /** Custom constructor for Person when building a new Person object from an API call
     * @param email, a String
     * @param password, a String
     * @param name, a String
     * @param balance,
     * @param dob, a Date
     */
    public Person(String email, String uid, String password, String name, Date dob, String pfp, String balance,  Boolean kasmServerNeeded, PersonRole role) {
        this.email = email;
        this.uid = uid;
        this.password = password;
        this.name = name;
        this.dob = dob;
        this.kasmServerNeeded = kasmServerNeeded;
        this.pfp = pfp;
        this.balance = balance;
        this.roles.add(role);
    }

    public boolean hasRoleWithName(String roleName) {
        for (PersonRole role : roles) {
            if (role.getName().equals(roleName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Custom getter to return age from dob attribute
     */
    /** Custom getter to return age from dob attribute
     * @return int, the age of the person
    */
    public int getAge() {
        if (this.dob != null) {
            LocalDate birthDay = this.dob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return Period.between(birthDay, LocalDate.now()).getYears();
        }
        return -1;
    }

    /** Custom compareTo method to compare Person objects by name
     * @param other, a Person object
     * @return int, the result of the comparison
     */
    @Override
    public int compareTo(Person other) {
        return this.name.compareTo(other.name);
    }

    /** 1st telescoping method to create a Person object with USER role
     * @param name
     * @param email
     * @param password
     * @param balance
     * @param dob
     * @return Person
     */
    public static Person createPerson(String name, String email, String uid, String password, Boolean kasmServerNeeded, Boolean kasmServerNeeded1, String balance, String dob, List<String> asList) {
        // By default, Spring Security expects roles to have a "ROLE_" prefix.
        return createPerson(name, email, uid, password, kasmServerNeeded, balance, dob, Arrays.asList("ROLE_USER", "ROLE_STUDENT"));
    }
    
    /**
     * 2nd telescoping method to create a Person object with parameterized roles
     * 
     * @param roles
     */
    public static Person createPerson(String name, String uid, String email, String password, String pfp, Boolean kasmServerNeeded, String balance, String dob, List<String> roleNames) {
        Person person = new Person();
        person.setName(name);
        person.setUid(uid);
        person.setEmail(email);
        person.setPassword(password);
        person.setKasmServerNeeded(kasmServerNeeded);
        person.setBalance(balance);
        person.setPfp(pfp);
        try {
            Date date = new SimpleDateFormat("MM-dd-yyyy").parse(dob);
            person.setDob(date);
        } catch (Exception e) {
            // handle exception
        }

        List<PersonRole> roles = new ArrayList<>();
        for (String roleName : roleNames) {
            PersonRole role = new PersonRole(roleName);
            roles.add(role);
        }
        person.setRoles(roles);
        
        return person;
    }
    
    /**
     * Static method to initialize an array list of Person objects
     * Uses createPerson method to create Person objects
     * Sorts the list of Person objects using Collections.sort which uses the compareTo method 
     * @return Person[], an array of Person objects
     */
    public static String startingBalance = "100000";
    public static Person[] init() {
        ArrayList<Person> people = new ArrayList<>();
        people.add(createPerson("Thomas Edison", "toby", "toby@gmail.com", "123toby", "/images/toby.png", true, startingBalance, "01-01-1840", Arrays.asList("ROLE_ADMIN", "ROLE_USER", "ROLE_TESTER", "ROLE_TEACHER")));
        people.add(createPerson("Alexander Graham Bell", "lex", "lexb@gmail.com", "123lex", "/images/lex.png", true, startingBalance, "01-01-1847", Arrays.asList("ROLE_USER", "ROLE_STUDENT")));
        people.add(createPerson("Nikola Tesla", "niko",  "niko@gmail.com",  "123niko", "/images/niko.png", true, startingBalance, "01-01-1850", Arrays.asList("ROLE_USER", "ROLE_STUDENT")));
        people.add(createPerson("Madam Curie", "madam", "madam@gmail.com", "123madam", "/images/madam.png", true, startingBalance, "01-01-1860", Arrays.asList("ROLE_USER", "ROLE_STUDENT")));
        people.add(createPerson("Grace Hopper", "hop",  "hop@gmail.com", "123hop", "/images/hop.png", true, startingBalance, "12-09-1906", Arrays.asList("ROLE_USER", "ROLE_STUDENT")));
        people.add(createPerson("John Mortensen","jm1021",  "jm1021@gmail.com", "123Qwerty!", "/images/jm1021.png", true, startingBalance, "10-21-1959", Arrays.asList("ROLE_ADMIN", "ROLE_TEACHER")));
        
        Collections.sort(people);
        for (Person person : people) {
            userStocksTable stock = new userStocksTable("AAPL,TSLA,AMZN", "BTC,ETH", startingBalance, person.getEmail(), person);
            person.setUser_stocks(stock);
        }

        return people.toArray(new Person[0]);
    }

    /**
     * Static method to print Person objects from an array
     * 
     * @param args, not used
     */
    public static void main(String[] args) {
        // obtain Person from initializer
        Person[] persons = init();

        // iterate using "enhanced for loop"
        for (Person person : persons) {
            System.out.println(person);  // print object
            System.out.println();
        }
    }
}
