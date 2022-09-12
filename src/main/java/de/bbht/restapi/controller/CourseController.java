package de.bbht.restapi.controller;

import de.bbht.restapi.dto.CourseInformationDto;
import de.bbht.restapi.dto.ReservationDto;
import de.bbht.restapi.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/course")
@RequiredArgsConstructor
@Slf4j
public class CourseController {

    private final CourseService courseService;

    @Operation(summary = "Lists all existing courses and their participants.")
    @ApiResponse(responseCode = "200", description = "Course list could be loaded and will be returned.")
    @ApiResponse(responseCode = "204", description = "The course list could not be loaded for unknown reasons. No list is returned.")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CourseInformationDto>> listCourses() {
        List<CourseInformationDto> courses = courseService.listCourses();
        if (courses != null) {
            return new ResponseEntity<>(courses, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Loads a course and its participants by its course number.")
    @ApiResponse(responseCode = "200", description = "Course could be loaded and will be returned.")
    @ApiResponse(responseCode = "204", description = "The course could not be loaded for unknown reasons. No list is returned.")
    @GetMapping(path = "/{courseNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CourseInformationDto> loadCourse(@PathVariable("courseNumber") String courseNumber) {
        CourseInformationDto course = courseService.loadCourse(courseNumber);
        if (course != null) {
            return new ResponseEntity<>(course, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Reserves on of the existing seats of the course for a new participant.")
    @ApiResponse(responseCode = "201", description = "Course reservation could be created.")
    @ApiResponse(responseCode = "422", description = "The course reservation could not be placed.")
    @PutMapping(path = "/{courseNumber}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> registerCourse(@PathVariable("courseNumber") String courseNumber, @RequestBody ReservationDto reservation) {
        if (courseNumber != null && reservation != null && reservation.getEmailAddress() != null) {
            boolean reservationSuccessful = courseService.makeReservation(courseNumber, reservation.getEmailAddress());
            if (reservationSuccessful) {
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Operation(summary = "Cancels a reservation for a given course number and participants' email address.")
    @ApiResponse(responseCode = "201", description = "Course reservation was cancelled.")
    @ApiResponse(responseCode = "304", description = "Cancellation was not possible.")
    @DeleteMapping(path = "/{courseNumber}/{emailAddress}", consumes = {MediaType.ALL_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> cancelReservation(@PathVariable("courseNumber") String courseNumber, @PathVariable("emailAddress") String emailAddress) {
        if (courseNumber != null && emailAddress != null) {
            boolean cancellationSuccessful = courseService.cancelReservation(courseNumber, emailAddress);
            if (cancellationSuccessful) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
