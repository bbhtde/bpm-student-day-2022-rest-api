package de.bbht.restapi.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "courseSeqGen")
    @SequenceGenerator(name = "courseSeqGen", sequenceName = "course_seq", initialValue = 1, allocationSize = 1)
    @Column(name = "course_id")
    private Long id;

    @Column(name = "course_number")
    private String courseNumber;

    @Column(name = "title")
    private String courseTitle;

    @Column(name = "description")
    private String description;

    @Column(name = "start")
    private LocalDateTime start;

    @Column(name = "max_seats")
    private Integer maxSeats;

    @Column(name = "pricing")
    @Enumerated(EnumType.STRING)
    private PricingType pricing;


    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "courses", cascade = {
            CascadeType.PERSIST, CascadeType.MERGE
    })
    private Set<User> users;
}
