package com.tiktok.tiktok.service;

import com.tiktok.tiktok.model.DTOs.*;
import com.tiktok.tiktok.model.entities.User;
import com.tiktok.tiktok.model.entities.Video;
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
        if (userRepository.existsByPhoneNumber(dto.getPhoneNumber())){
            throw new BadRequestException("Phone number already exists");
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
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()){
            throw new NotFoundException("User not found");
        }
        Set<User> followers = userRepository.findById(id).get().getFollowers();
        if (followers.size() == 0){
            throw new NotFoundException("No followers found");
        }
        return followers.stream()
                .map(f -> mapper.map(f, UserWithPicNameIdDTO.class))
                .collect(Collectors.toList());
    }

    public List<UserWithPicNameIdDTO> getAllFollowing(int id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()){
            throw new NotFoundException("User not found");
        }
        Set<User> following = userRepository.findById(id).get().getFollowing();
        if (following.size() == 0){
            throw new NotFoundException("No following users found");
        }
        return following.stream()
                .map(f -> mapper.map(f, UserWithPicNameIdDTO.class))
                .collect(Collectors.toList());
    }

    public List<VideoWithoutOwnerDTO> getAllVideos(int id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()){
            throw new NotFoundException("User not found");
        }
        List<Video> videos = userRepository.findById(id).get().getVideos();
        if (videos.size() == 0){
            throw new NotFoundException("No videos found");
        }
        return videos.stream()
                .map(v -> mapper.map(v, VideoWithoutOwnerDTO.class))
                .collect(Collectors.toList());
    }

    public UserDeletedDTO deleteAccount(int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()){
            throw new NotFoundException("User not found");
        }
        userRepository.deleteById(id);
        return mapper.map(user, UserDeletedDTO.class);
    }

    public UserSimpleDTO editAccount(UserEditDTO corrections, int id) {
        User u = getUserById(id);
        if (!corrections.getPassword().equals(corrections.getConfirmPassword())){
            throw new BadRequestException("Password missmatch");
        }
        if (corrections.getEmail().equals(null)){
            if (!isValidEmail(corrections.getEmail())){
                throw new BadRequestException("Invalid Email");
            }
            if (userRepository.existsByEmail(corrections.getEmail())){
                throw new BadRequestException("Email already exists");
            }
            u.setEmail(corrections.getEmail());
        }
        if (corrections.getUsername().equals(null)){
            if (userRepository.existsByUsername(corrections.getUsername())){
                throw new BadRequestException("Username already exists");
            }
            u.setUsername(corrections.getUsername());
        }
        if (corrections.getPhoneNumber().equals(null)){
            if (userRepository.existsByPhoneNumber(corrections.getPhoneNumber())){
                throw new BadRequestException("Phone number already exists");
            }
            u.setPhoneNumber(corrections.getPhoneNumber());
        }
        if (corrections.getBio().equals(null)){
            if (corrections.getBio().length() >= 200){
                throw new BadRequestException("Too long bio. It must be max 200 symbols.");
            }
            u.setBio(corrections.getBio());
        }
        userRepository.save(u);
//        new Thread(() -> {
//            //MailSender.sendEmail(u.getEmail(), "Bravo, eto ti link", "Reg successful");
//        }).start();
        return mapper.map(u, UserSimpleDTO.class);
    }
}
