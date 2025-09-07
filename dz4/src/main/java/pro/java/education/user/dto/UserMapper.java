package pro.java.education.user.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pro.java.education.user.model.User;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static UserDto toUserDtoFromUser(User user) {
        return new UserDto(user.getId(), user.getName());
    }

    public static User toUserFromUserDto(UserDto userDto) {
        return new User(userDto.getId(), userDto.getName());
    }
}