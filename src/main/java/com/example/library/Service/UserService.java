package com.example.library.Service;

import com.example.library.Entity.User;
import com.example.library.Exception.InvalidCredentialsException;
import com.example.library.Exception.UserAlreadyExistsException;
import com.example.library.Exception.UserNotFoundException;
import com.example.library.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    //Register New User
    public User RegisterUser(User user) {
        userRepo.findByEmail(user.getEmail())
                .ifPresent(exists -> {
            throw new UserAlreadyExistsException("Email already registered: "+ user.getEmail());
        });
        return userRepo.save(user);
    }
    /*public User RegisterUser(User user) {
        Optional<User> existingUser = userRepo.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("Email already registered: " + user.getEmail());
        }
        return userRepo.save(user);
    }*/


    //Login User
    public User loginUser(String email, String password) {
        return userRepo.findByEmail(email)
                .filter(user -> user.getPassword().equals(password))
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));
    }

    /*public User loginUser(String email, String password) {
        Optional<User> optionalUser = userRepo.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getPassword().equals(password)) {
                return user;
            } else {
                throw new InvalidCredentialsException("Invalid email or password");
            }
        } else {
            throw new InvalidCredentialsException("Invalid email or password");
        }
    }*/


    // Get all users
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    // Get user by ID
    public User getUserById(Long id) throws UserNotFoundException {
        return userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }
    /*public User getUserById(Long id) {
        Optional<User> optionalUser = userRepo.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
    }*/


    // Delete user by ID
    public void deleteUserById(Long id) throws UserNotFoundException {
        if (!userRepo.existsById(id)) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        userRepo.deleteById(id);
    }

    // Delete all users
    public void deleteAllUsers() {
        userRepo.deleteAll();
    }

}
