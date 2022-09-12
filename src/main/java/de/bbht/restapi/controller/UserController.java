package de.bbht.restapi.controller;

import de.bbht.restapi.dto.UserDto;
import de.bbht.restapi.dto.UserRegistrationDto;
import de.bbht.restapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @Operation(summary = "Returns a list of all available registered users of the system.")
    @ApiResponse(responseCode = "200", description = "User list was loaded and will be returned.")
    @ApiResponse(responseCode = "204", description = "User list could not be loaded for unknown reasons.")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDto>> listUsers() {
        List<UserDto> users = userService.listUsers();
        if (users != null) {
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Registers a new users in the system.")
    @ApiResponse(responseCode = "201", description = "User was successfully created.")
    @ApiResponse(responseCode = "409", description = "User already exists.")
    @ApiResponse(responseCode = "422", description = "An unknown error occured.")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> registerUser(@RequestBody UserRegistrationDto userRegistrationDto) {
        if (userRegistrationDto != null && userRegistrationDto.getEmail() != null && userRegistrationDto.getFirstName() != null && userRegistrationDto.getLastName() != null) {
            boolean successful = userService.createIfNotExists(userRegistrationDto);
            if (!successful) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Operation(summary = "Loads a user by their email address.")
    @ApiResponse(responseCode = "209", description = "User was successfully loaded and returned.")
    @ApiResponse(responseCode = "404", description = "No user with the given email address was found.")
    @ApiResponse(responseCode = "400", description = "The request was invalid.")
    @GetMapping(path = "/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> loadUser(@PathVariable("email") String email) {
        if (email != null) {
            Optional<UserDto> user = userService.loadUser(email);
            if (user.isPresent()) {
                return new ResponseEntity<>(user.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
