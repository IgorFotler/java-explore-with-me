package ru.practicum.users.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.users.dto.NewUserRequest;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.dto.UserShortDto;
import ru.practicum.users.model.User;

@Component
public class UserMapper {

    public static User convertToUser(NewUserRequest newUserRequest) {
        User user = new User();
        user.setName(newUserRequest.getName());
        user.setEmail(newUserRequest.getEmail());
        return user;
    }

    public static UserDto convertToUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public static UserShortDto convertToUserShortDto(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }
}
