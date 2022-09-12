package de.bbht.restapi.service;

import de.bbht.restapi.dto.CourseInformationDto;
import de.bbht.restapi.model.Course;
import de.bbht.restapi.model.User;
import de.bbht.restapi.repository.CourseRepository;
import de.bbht.restapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public List<CourseInformationDto> listCourses() {
        List<Course> courses = courseRepository.findAll();
        if (!courses.isEmpty()) {
            return courses.stream().filter(Objects::nonNull).map(CourseService::map).collect(Collectors.toList());
        }
        return null;
    }

    public CourseInformationDto loadCourse(String courseNumber) {
        Optional<Course> course = courseRepository.findByCourseNumber(courseNumber);
        if (course.isPresent()) {
            return map(course.get());
        }
        return null;
    }

    public boolean makeReservation(String courseNumber, String emailAddress) {
        Optional<Course> course = courseRepository.findByCourseNumber(courseNumber);
        Optional<User> user = userRepository.findByEmail(emailAddress);

        if (course.isPresent() && user.isPresent()) {
            Course bookCourse = course.get();
            User bookUser = user.get();
            final boolean duplicateUser = bookCourse.getUsers().stream().anyMatch(testUser -> testUser.getId().equals(bookUser.getId()));
            if (!duplicateUser && bookCourse.getUsers().size() < bookCourse.getMaxSeats()) {
                bookCourse.getUsers().add(bookUser);
                bookUser.getCourses().add(bookCourse);
                courseRepository.save(bookCourse);
                return true;
            }
        }
        return false;
    }

    public boolean cancelReservation(String courseNumber, String emailAddress) {
        Optional<Course> course = courseRepository.findByCourseNumber(courseNumber);
        Optional<User> user = userRepository.findByEmail(emailAddress);

        if (course.isPresent() && user.isPresent()) {
            Course bookCourse = course.get();
            User bookUser = user.get();
            final boolean hasUser = bookCourse.getUsers().stream().anyMatch(testUser -> testUser.getId().equals(bookUser.getId()));
            if (hasUser) {
                bookCourse.getUsers().remove(bookUser);
                bookUser.getCourses().remove(bookCourse);
                courseRepository.save(bookCourse);
                return true;
            }
        }
        return false;
    }

    private static CourseInformationDto map(Course course) {
        CourseInformationDto result = new CourseInformationDto();

        result.setId(course.getId());
        result.setCourseNumber(course.getCourseNumber());
        result.setCourseTitle(course.getCourseTitle());
        result.setDescription(course.getDescription());
        result.setStart(course.getStart());
        result.setMaxSeats(course.getMaxSeats());
        result.setPricing(course.getPricing().name());
        if (course.getUsers() != null) {
            result.setParticipants(course.getUsers().stream().map(User::getEmail).collect(Collectors.toList()));
            result.setCurrentSeats(course.getUsers().size());
        } else {
            result.setParticipants(new ArrayList<>());
            result.setCurrentSeats(0);
        }

        return result;
    }
}
