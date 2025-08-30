package pro.java.education.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.java.education.user.model.User;
import pro.java.education.user.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public User getUserById(long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.orElseThrow(() -> new NoSuchElementException("Не найден user c id = " + userId));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void createUser(String userName) {
        userRepository.save(new User(userName));
    }

    public void updateUser(User user) {
        Optional<User> existedUser = userRepository.findById(user.getId());
        if (existedUser.isPresent()) {
            userRepository.save(new User(user.getId(), user.getName()));
        } else {
            throw new NoSuchElementException("Не найден user c id = " + user.getId());
        }
    }

    public void deleteUser(User user) {
        Optional<User> existedUser = userRepository.findById(user.getId());
        if (existedUser.isPresent()) {
            userRepository.deleteById(user.getId());
        } else {
            throw new NoSuchElementException("Не найден user c id = " + user.getId());
        }
    }
}
