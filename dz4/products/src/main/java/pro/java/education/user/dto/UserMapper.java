package pro.java.education.user.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import pro.java.education.user.model.User;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class UserMapper {
    public UserDto toUserDtoFromUser(User user) {
        return new UserDto(user.getId(), user.getName());
    }

    public User toUserFromUserDto(UserDto userDto) {
        return new User(userDto.id(), userDto.name());
    }
}