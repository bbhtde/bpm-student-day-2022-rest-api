package de.bbht.restapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CourseInformationDto {

    private Long id;
    private String courseNumber;
    private String courseTitle;
    private String description;
    private LocalDateTime start;
    private Integer maxSeats;
    private Integer currentSeats;
    private String pricing;
    private List<String> participants;
}
