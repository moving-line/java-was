package service;

import db.DataBase;
import model.User;

public class UserService {
    public static User add(User user) {
        DataBase.addUser(user);
        return user;
    }

    public static User findUserById(String userId) {
        return DataBase.findUserById(userId);
    }
}
