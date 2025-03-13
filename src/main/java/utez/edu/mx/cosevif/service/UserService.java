package utez.edu.mx.cosevif.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utez.edu.mx.cosevif.model.User;
import utez.edu.mx.cosevif.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public List<User> registerUser() {
        return userRepository.findAll();
    }
}