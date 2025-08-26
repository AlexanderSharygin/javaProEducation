package pro.java.education.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.java.education.user.dao.UserDao;
import pro.java.education.user.model.User;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    public User getUserById(int userId) {
        Optional<User> user = userDao.findUserById(userId);
        return user.orElseThrow(() -> new NoSuchElementException("Не найден user c id = " + userId));
    }

    public List<User> getAllUsers() {
        return userDao.findUsers();
    }

    public void createUser(String userName) {
        userDao.createUser(userName);
    }

    public void updateUser(User user) {
        Optional<User> existedUser = userDao.findUserById(user.getId());
        if (existedUser.isPresent()) {
            userDao.updateUser(user.getId(), user.getName());
        } else {
            throw new NoSuchElementException("Не найден user c id = " + user.getId());
        }
    }

    public void deleteUser(User user) {
        Optional<User> existedUser = userDao.findUserById(user.getId());
        if (existedUser.isPresent()) {
            userDao.deleteUserById(user.getId());
        } else {
            throw new NoSuchElementException("Не найден user c id = " + user.getId());
        }
    }
}
