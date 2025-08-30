package pro.java.education;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import pro.java.education.user.model.User;
import pro.java.education.user.service.UserService;

import java.util.List;

@ComponentScan
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);

        UserService userService = context.getBean(UserService.class);
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