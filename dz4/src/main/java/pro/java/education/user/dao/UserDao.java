package pro.java.education.user.dao;

import pro.java.education.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    Optional<User> findUserById(long userId);

    List<User> findUsers();

    void createUser(String userName);

    void updateUser(long userId, String userName);

    void deleteUserById(long userId);
}
