package com.example.library.Service;

import com.example.library.Entity.User;
import com.example.library.Exception.InvalidCredentialsException;
import com.example.library.Exception.UserAlreadyExistsException;
import com.example.library.Exception.UserNotFoundException;
import com.example.library.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    //Register New User
    public User RegisterUser(User user) {
        // Checks if email already exists
        userRepo.findByEmail(user.getEmail())
                .ifPresent(exists -> {
            throw new UserAlreadyExistsException("Email already registered: "+ user.getEmail());
        });

        // Save user
        return userRepo.save(user);
    }

    //Login User
    public User loginUser(String email, String password) {
        return userRepo.findByEmail(email)
                .filter(user -> user.getPassword().equals(password))
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    // Get user by ID
    public User getUserById(Long id) throws UserNotFoundException {
        return userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

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
