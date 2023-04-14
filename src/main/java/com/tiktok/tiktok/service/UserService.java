package com.tiktok.tiktok.service;

import com.tiktok.tiktok.model.DTOs.*;
import com.tiktok.tiktok.model.entities.User;
import com.tiktok.tiktok.model.exceptions.BadRequestException;
import com.tiktok.tiktok.model.exceptions.NotFoundException;
import com.tiktok.tiktok.model.exceptions.UnauthorizedException;
import com.tiktok.tiktok.model.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService extends AbstractService{
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;
    public UserSimpleDTO register(RegisterDTO dto) {
        if(!dto.getPassword().equals(dto.getConfirmPassword())){
            throw new BadRequestException("Passwords missmatch!");
        }
        if(!isStrongPassword(dto.getPassword())){
            throw new BadRequestException("Weak password!");
        }
        if(!isValidEmail(dto.getEmail())){
            throw new BadRequestException("Invalid email!");
        }
        if(userRepository.existsByEmail(dto.getEmail())){
            throw new BadRequestException("Email already exists!");
        }
        if(userRepository.existsByUsername(dto.getUsername())){
            throw new BadRequestException("Username already exists!");
        }
        User u = mapper.map(dto, User.class);
        u.setPassword(encoder.encode(u.getPassword()));
        userRepository.save(u);
//        new Thread(() -> {
//            //MailSender.sendEmail(u.getEmail(), "Bravo, eto ti link", "Reg successful");
//        }).start();
        return mapper.map(u, UserSimpleDTO.class);
    }
    public UserFullInfoDTO login(LoginDTO dto) {
        Optional<User> u = userRepository.getByUsername(dto.getUsername());
        if(!u.isPresent()){
            throw new UnauthorizedException("Wrong credentials");
        }
        if(!encoder.matches(dto.getPassword(), u.get().getPassword())){
            throw new UnauthorizedException("Wrong credentials");
        }
        return mapper.map(u, UserFullInfoDTO.class);
    }
    private boolean isStrongPassword(String password) {
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        return password.matches(pattern);
    }

    private boolean isValidEmail(String email) {
        String pattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(pattern);
    }

    public int follow(int followerId, int followToId) {
        User follower = getUserById(followerId);
        User following = getUserById(followToId);

        if (following.getFollowers().contains(follower)) {
            following.getFollowers().remove(follower);
        } else {
            following.getFollowers().add(follower);
        }
        userRepository.save(following);
        return following.getFollowers().size();
    }

    public UserFullInfoDTO searchByUsername(String username) {
        Optional<User> user = userRepository.getByUsername(username);
        if (!user.isPresent()){
            throw new NotFoundException("User not found");
        }
        return mapper.map(user,UserFullInfoDTO.class);
    }

    public UserFullInfoDTO getById(int id) {
        Optional<User> u = userRepository.findById(id);
        System.out.println("found");
        if(u.isPresent()){
            System.out.println("found1");
            return mapper.map(u.get(), UserFullInfoDTO.class);
        }
        throw new NotFoundException("User not found");
    }

    public List<UserWithPicNameIdDTO> getAllFollowers(int id) {
        Set<User> users = userRepository.findById(id).get().getFollowers();
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()){
            throw new NotFoundException("No followers found");
        }
        Set<User> followers = user.get().getFollowers();
        return followers.stream()
                .map(f -> mapper.map(f, UserWithPicNameIdDTO.class))
                .collect(Collectors.toList());
    }
}
