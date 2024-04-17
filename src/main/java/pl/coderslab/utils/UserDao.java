package pl.coderslab.utils;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserDao {
    private static final String INSERT_USER = "INSERT INTO users(userName, email, password) VALUES (?,?,?);";

    private static final String READ_USER = "SELECT * FROM users WHERE id = (?)";
    private static final String READ_ALL_USERS = "SELECT * FROM users";
    private static final String UPDATE_USER = "UPDATE users SET userName = (?), email = (?), password = (?) where id = (?);";
    private static final String DELETE_USER = "DELETE FROM users WHERE id = (?);";


    private String hashPassword(String password){
        return BCrypt.hashpw(password, BCrypt.hashpw(password, BCrypt.gensalt()));
    }
    public User create(User user){
        try(Connection connection = DbUtil.getConnection()){
            PreparedStatement statement = connection.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if(resultSet.next()){
                int id = resultSet.getInt(1);
                System.out.println("User added. ID = " + id);
                user.setId(id);
            }
            return user;

        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public User read(int userId){
        User user = new User();
        try(Connection connection = DbUtil.getConnection()){
            PreparedStatement statement = connection.prepareStatement(READ_USER);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                user.setId(resultSet.getInt("id"));
                user.setUserName(resultSet.getString("userName"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
            }
            return user;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public void update(User user){
        try(Connection connection = DbUtil.getConnection()){
            PreparedStatement statement = connection.prepareStatement(UPDATE_USER);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.setInt(4, user.getId());
            statement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection connection = DbUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(READ_ALL_USERS);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                User user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("email"),
                        resultSet.getString("password")
                );
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private User[] addToArray(User u, User[] users) {
        User[] tmpUsers = Arrays.copyOf(users, users.length + 1); // Tworzymy kopię tablicy powiększoną o 1.
        tmpUsers[users.length] = u; // Dodajemy obiekt na ostatniej pozycji.
        return tmpUsers; // Zwracamy nową tablicę.
    }

    public void delete(int userId){
        try(Connection connection = DbUtil.getConnection()){
            PreparedStatement statement = connection.prepareStatement(DELETE_USER);
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

}
