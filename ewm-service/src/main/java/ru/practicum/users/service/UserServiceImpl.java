package ru.practicum.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.users.dto.NewUserRequest;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.mapper.UserMapper;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto create(NewUserRequest newUserRequest) {
        if (userRepository.existsByEmail(newUserRequest.getEmail())) {
            throw new ConflictException("email " + newUserRequest.getEmail() + " уже используется");
        }
        User user = UserMapper.convertToUser(newUserRequest);
        user = userRepository.save(user);
        return UserMapper.convertToUserDto(user);
    }

    @Override
    public List<UserDto> findUsers(List<Long> ids, int from, int size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);

        List<User> users = ids == null || ids.isEmpty()
                ? userRepository.findAll(pageRequest).getContent()
                : userRepository.findByIdIn(ids, pageRequest);

        return users.stream()
                .map(UserMapper::convertToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        userRepository.deleteById(userId);
    }
}
