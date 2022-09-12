package de.bbht.restapi.service;

import de.bbht.restapi.dto.UserDto;
import de.bbht.restapi.dto.UserRegistrationDto;
import de.bbht.restapi.model.User;
import de.bbht.restapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserDto> listUsers() {
        List<User> users = userRepository.findAll();
        if (!users.isEmpty()) {
            return users.stream().filter(Objects::nonNull).map(UserService::map).collect(Collectors.toList());
        }
        return null;
    }
    public boolean createIfNotExists(UserRegistrationDto userRegistrationDto) {
        if (!userRepository.existsByEmail(userRegistrationDto.getEmail())) {
            User newUser = mapRegistration(userRegistrationDto);
            userRepository.saveAndFlush(newUser);
            return true;
        }
        return false;
    }

    public Optional<UserDto> loadUser(String emailAddress) {
        Optional<User> user = userRepository.findByEmail(emailAddress);
        return user.map(UserService::map);
    }
    private static User mapRegistration(UserRegistrationDto userRegistration) {
        User user = new User();
        user.setEmail(userRegistration.getEmail());
        user.setFirstName(userRegistration.getFirstName());
        user.setLastName(userRegistration.getLastName());
        user.setIban(userRegistration.getIban());
        return user;
    }

    private static UserDto map(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName((user.getFirstName()));
        userDto.setLastName(user.getLastName());
        userDto.setIban(user.getIban());

        return userDto;
    }
}
