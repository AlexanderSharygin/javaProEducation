package pro.java.education.user.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pro.java.education.user.model.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    private final DataSource dataSource;

    public Optional<User> findUserById(long userId) {
        String sql = "SELECT id, username FROM users WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setLong(1, userId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(new User(rs.getLong("id"),
                        rs.getString("username")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении user", e);
        }
        return Optional.empty();
    }

    public List<User> findUsers() {
        String sql = "SELECT * FROM users ORDER BY id";
        List<User> users = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                users.add(new User(rs.getLong("id"),
                        rs.getString("username")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("EОшибка при получении списка users", e);
        }
        return users;
    }

    public void createUser(String userName) {
        String sql = "INSERT INTO users (username) VALUES (?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, userName);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при создании user", e);
        }
    }

    public void updateUser(long userId, String userName) {
        String sql = "UPDATE users SET username = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, userName);
            statement.setLong(2, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении пользователя user", e);
        }
    }

    public void deleteUserById(long userId) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setLong(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении пользователя user", e);
        }
    }
}
