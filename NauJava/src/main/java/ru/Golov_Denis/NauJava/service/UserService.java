package ru.Golov_Denis.NauJava.service;

import ru.Golov_Denis.NauJava.entity.UserEntity;

public interface UserService {

    UserEntity getUserByUsername(String username);

    UserEntity addUser(String username, String email, String password);
}
