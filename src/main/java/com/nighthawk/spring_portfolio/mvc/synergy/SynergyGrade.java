package com.nighthawk.spring_portfolio.mvc.synergy;

import com.nighthawk.spring_portfolio.mvc.assignments.Assignment;
import com.nighthawk.spring_portfolio.mvc.assignments.AssignmentJpaRepository;
import com.nighthawk.spring_portfolio.mvc.person.Person;
import com.nighthawk.spring_portfolio.mvc.person.PersonJpaRepository;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SynergyGrade {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Double grade;

    @ManyToOne
    @JoinColumn(name="assignment_id", nullable=false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Assignment assignment;

    @ManyToOne
    @JoinColumn(name="student_id", nullable=false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Person student;

    public SynergyGrade(Double grade, Assignment assignment, Person student) {
        this.grade = grade;
        this.assignment = assignment;
        this.student = student;
    }

    public static SynergyGrade createFromRequest(SynergyGradeRequest request) {
        return new SynergyGrade(request.getGradeSuggestion(), request.getAssignment(), request.getStudent());
    }

    public static String[][] init() {
        // Create SynergyGrade objects
        return new String[][] {
            {"0.98", "Sprint 1 Live Review", "toby"},
            {"0.92", "Sprint 1 Live Review", "madam"},
            {"0.89", "Sprint 1 Live Review", "lex"},
            {"0.79", "Sprint 1 Live Review", "hop"},
            {"0.55", "Sprint 1 Live Review", "niko"},
            {"1.00", "Sprint 1 Live Review", "mirage"},

            {"0.98", "Seed", "toby"},
            {"0.92", "Seed", "madam"},
            {"0.85", "Seed", "lex"},
            {"0.79", "Seed", "hop"},
            {"0.55", "Seed", "niko"},
            {"1.00", "Seed", "mirage"},

            {"0.98", "Assignment 1", "toby"},
            {"0.92", "Assignment 1", "madam"},
            {"0.89", "Assignment 1", "lex"},
            {"0.79", "Assignment 1", "hop"},
            {"0.55", "Assignment 1", "niko"},
            {"1.00", "Assignment 1", "mirage"},
            
            
        };
    }
}
