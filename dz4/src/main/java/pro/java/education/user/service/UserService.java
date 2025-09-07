package pro.java.education.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.java.education.exception.model.ConflictException;
import pro.java.education.exception.model.NotFoundException;
import pro.java.education.user.dto.UserDto;
import pro.java.education.user.dto.UserMapper;
import pro.java.education.user.model.User;
import pro.java.education.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto getUserById(long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Не найден user c id = " + userId);
        }
        return UserMapper.toUserDtoFromUser(user.get());
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDtoFromUser).toList();
    }

    public void createUser(UserDto userDto) {
        Optional<User> user = userRepository.findUserByName(userDto.getName());
        if (user.isPresent()) {
            throw new ConflictException("User с именем " + userDto.getName() + " уже существует!");
        }
        userRepository.save(UserMapper.toUserFromUserDto(userDto));
    }

    public void updateUser(UserDto userDto) {
        Optional<User> existedUser = userRepository.findById(userDto.getId());
        if (existedUser.isEmpty()) {
            throw new NotFoundException("Не найден user c id = " + userDto.getId());
        }
        Optional<User> user = userRepository.findUserByName(userDto.getName());
        if (user.isPresent()) {
            throw new ConflictException("User с именем " + userDto.getName() + " ");
        }
        userRepository.save(UserMapper.toUserFromUserDto(userDto));
    }

    public void deleteUserById(long userId) {
        Optional<User> existedUser = userRepository.findById(userId);
        if (existedUser.isEmpty()) {
            throw new NotFoundException("Не найден user c id = " + userId);
        }
        userRepository.deleteById(userId);
    }
}
