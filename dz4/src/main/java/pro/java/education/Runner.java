package pro.java.education;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pro.java.education.user.model.User;
import pro.java.education.user.service.UserService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {

    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {
        userService.createUser("John");
        userService.createUser("Jane");
        userService.createUser("Emma");
        System.out.println("===Add and Get user===");
        User user = userService.getUserById(2);
        System.out.println(user);
        System.out.println("===Update user===");
        user.setName("Jane2.0");
        userService.updateUser(user);
        user = userService.getUserById(2);
        System.out.println(user);
        System.out.println("===Delete and Get users list===");
        List<User> users = userService.getAllUsers();
        System.out.println(users);
        userService.deleteUser(user);
        users = userService.getAllUsers();
        System.out.println(users);
    }
}
