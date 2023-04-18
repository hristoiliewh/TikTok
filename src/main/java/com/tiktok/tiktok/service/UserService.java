package com.tiktok.tiktok.service;

import com.tiktok.tiktok.model.DTOs.*;
import org.mindrot.jbcrypt.BCrypt;
import com.tiktok.tiktok.model.entities.User;
import com.tiktok.tiktok.model.entities.Video;
import com.tiktok.tiktok.model.exceptions.BadRequestException;
import com.tiktok.tiktok.model.exceptions.NotFoundException;
import com.tiktok.tiktok.model.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService extends AbstractService {
    @Autowired
    private MailSenderService senderService;
    @Autowired
    private BCryptPasswordEncoder encoder;

    private static class PasswordGenerator {
        private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";
        private static final Random random = new SecureRandom();

        public static String generatePassword() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 12; i++) {
                int index = random.nextInt(CHARACTERS.length());
                sb.append(CHARACTERS.charAt(index));
            }
            return sb.toString();
        }
    }

    public UserSimpleDTO register(RegisterDTO dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new BadRequestException("Passwords missmatch!");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Email already exists!");
        }
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new BadRequestException("Username already exists!");
        }
        if (userRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new BadRequestException("Phone number already exists!");
        }
        if (!isValidAge(dto.getDateOfBirth())){
            throw new BadRequestException("You must be older than 16 years.");
        }
        User u = mapper.map(dto, User.class);
        u.setPassword(encoder.encode(u.getPassword()));
        String confirmationCode = UUID.randomUUID().toString();
        u.setConfirmationCode(confirmationCode);
        userRepository.save(u);

        String confirmationLink = "https://localuser:6666/confirm/" + confirmationCode;
        new Thread(() -> {
            senderService.sendEmail(dto.getEmail(),
                    "Confirm your registration",
                    "Dear " + dto.getName() + ",\n\n" +
                            "Thank you for registering with our service. To complete your registration," +
                            " please click on the following link:\n\n" +
                            confirmationLink + "\n\n" +
                            "Best regards,\n" +
                            "TikTok Team.");
        }).start();
        return mapper.map(u, UserSimpleDTO.class);
    }

    public UserFullInfoDTO login(LoginDTO dto) {

        Optional<User> u = userRepository.getByUsername(dto.getUsername());
        if (!u.isPresent()) {
            throw new UnauthorizedException("Wrong credentials");
        }
        if (!encoder.matches(dto.getPassword(), u.get().getPassword())) {
            throw new UnauthorizedException("Wrong credentials");
        }
        if (!u.get().isEmailConfirmed()) {
            throw new UnauthorizedException("Your email is not confirmed. Please confirm you registration before login.");
        }
        return mapper.map(u.get(), UserFullInfoDTO.class);
    }


    private boolean isValidAge(LocalDate dateOfBirth){
        LocalDate today = LocalDate.now();
        Period ageLimit = Period.ofYears(16);
        LocalDate minimumDateOfBirth = today.minus(ageLimit);
        if (dateOfBirth.isBefore(minimumDateOfBirth)){
            return true;
        }
        return false;
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
        if (!user.isPresent()) {
            throw new NotFoundException("User not found");
        }
        return mapper.map(user, UserFullInfoDTO.class);
    }

    public UserFullInfoDTO getById(int userId) {
        User user = getUserById(userId);
        return mapper.map(user, UserFullInfoDTO.class);
    }

    public List<UserWithPicNameIdDTO> getAllFollowers(int userId) {
        Set<User> followers = getUserById(userId).getFollowers();
        if (followers.size() == 0) {
            throw new NotFoundException("No followers found");
        }
        return followers.stream()
                .map(f -> mapper.map(f, UserWithPicNameIdDTO.class))
                .collect(Collectors.toList());
    }

    public List<UserWithPicNameIdDTO> getAllFollowing(int userId) {
        Set<User> following = getUserById(userId).getFollowing();
        if (following.size() == 0) {
            throw new NotFoundException("No following users found");
        }
        return following.stream()
                .map(f -> mapper.map(f, UserWithPicNameIdDTO.class))
                .collect(Collectors.toList());
    }

    public UserDeletedDTO deleteAccount(int userId) {
        User user = getUserById(userId);
        userRepository.deleteById(userId);
        return mapper.map(user, UserDeletedDTO.class);
    }

    public UserSimpleDTO editAccount(UserEditDTO corrections, int userId) {
        User u = getUserById(userId);
        if (!corrections.getPassword().equals(corrections.getConfirmPassword())) {
            throw new BadRequestException("Password missmatch");
        }
        if (!corrections.getPassword().equals("")) {
            if (!corrections.getPassword().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+])(?=\\S+$).{8,}$")) {
                throw new BadRequestException("Weak password");
            }
            u.setPassword(corrections.getPassword());
        }
        if (!corrections.getName().equals("")) {
            if (!corrections.getName().matches("^[A-Za-z -]{2,50}$")) {
                throw new BadRequestException("Invalid name format");
            }
            u.setName(corrections.getName());
        }
        if (!corrections.getEmail().equals("")) {
            if (!corrections.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                throw new BadRequestException("Invalid Email");
            }
            if (userRepository.existsByEmail(corrections.getEmail())) {
                throw new BadRequestException("Email already exists");
            }
            u.setEmail(corrections.getEmail());
        }
        if (!corrections.getUsername().equals("")) {
            if (!corrections.getPhoneNumber().matches("^[a-zA-Z0-9]*$")) {
                throw new BadRequestException("Username must contain only alphanumeric characters");
            }
            if (userRepository.existsByUsername(corrections.getUsername())) {
                throw new BadRequestException("Username already exists");
            }
            u.setUsername(corrections.getUsername());
        }
        if (!corrections.getPhoneNumber().equals("")) {
            if (!corrections.getPhoneNumber().matches("^\\d{10}$")) {
                throw new BadRequestException("Invalid phone number format");
            }
            if (userRepository.existsByPhoneNumber(corrections.getPhoneNumber())) {
                throw new BadRequestException("Phone number already exists");
            }
            u.setPhoneNumber(corrections.getPhoneNumber());
        }
        if (!corrections.getBio().equals("")) {
            if (corrections.getBio().length() >= 200) {
                throw new BadRequestException("The bio should not be larger than 200 symbols.");
            }
            u.setBio(corrections.getBio());
        }
        userRepository.save(u);
        return mapper.map(u, UserSimpleDTO.class);
    }

    public UserConfirmedDTO confirmRegistration(String confirmationCode) {
        Optional<User> user = userRepository.getByConfirmationCode(confirmationCode);
        if (!user.isPresent()) {
            throw new NotFoundException("Not a valid confirmation code.");
        }
        if (user.get().isEmailConfirmed()) {
            throw new BadRequestException("Registration already confirmed.");
        }
        user.get().setEmailConfirmed(true);
        userRepository.save(user.get());
        return mapper.map(user, UserConfirmedDTO.class);
    }

    public String forgottenPassword(ForgottenPasswordDTO dto) {
        Optional<User> user = userRepository.getByEmail(dto.getEmail());
        if (!user.isPresent()) {
            throw new NotFoundException("The given email is not registered in our system.");
        }
        String pass = PasswordGenerator.generatePassword();
        user.get().setPassword(encoder.encode(pass));
        userRepository.save(user.get());
        new Thread(() -> {
            senderService.sendEmail(dto.getEmail(),
                    "Reset Password",
                    "Dear " + user.get().getName() + ",\n\n" +
                            "We are sending you a new password, which was autogenerated. For security reasons, " +
                            "please change your password as soon as possible. \n\n" +
                            "To do this, after logging into your account, select \"edit profile\".\n\n" +
                            "Your new password is: " + pass + "\n\n" +
                            "Best regards,\n" +
                            "TikTok Team.");
        }).start();
        return "Password changed successfully.";
    }
}
