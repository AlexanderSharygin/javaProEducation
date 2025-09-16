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

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto getUserById(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Не найден user c id = " + userId));
        return userMapper.toUserDtoFromUser(user);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserDtoFromUser).toList();
    }

    public void createUser(UserDto userDto) {
        userRepository.findUserByName(userDto.name())
                .ifPresent(__ -> {
                    throw new ConflictException("User с именем " + userDto.name() + " ");
                });
        userRepository.save(userMapper.toUserFromUserDto(userDto));
    }

    public void updateUser(UserDto userDto) {
        userRepository.findById(userDto.id())
                .orElseThrow(() -> new NotFoundException("Не найден user c id = " + userDto.id()));
        userRepository.findUserByName(userDto.name())
                .ifPresent(__ -> {
                    throw new ConflictException("User с именем " + userDto.name() + " ");
                });
        userRepository.save(userMapper.toUserFromUserDto(userDto));
    }

    public void deleteUserById(long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Не найден user c id = " + userId));
        userRepository.deleteById(userId);
    }
}
