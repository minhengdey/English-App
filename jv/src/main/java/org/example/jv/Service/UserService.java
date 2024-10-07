package org.example.jv.Service;

import org.example.jv.Entity.UserEntity;
import org.example.jv.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserEntity findById (Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Not Found"));
    }

    public UserEntity save (UserEntity user) {
        if (user.getId() != null) {
            UserEntity userEntity = findById(user.getId());
            userEntity.setUsername(user.getUsername());
            userEntity.setPassword(user.getPassword());
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    public void deleteById (Long id) {
        userRepository.deleteById(id);
    }
}
