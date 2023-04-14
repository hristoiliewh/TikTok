package com.tiktok.tiktok.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String name;
    @Column
    private String password;
    @Column
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @Column
    private String gender;
    @Column
    private String bio;
    @Column(name = "profile_photo")
    private String profilePhotoURL;
    @Column
    private String username;
    @Column(name = "is_email_confirmed")
    private boolean isEmailConfirmed;
    @OneToMany(mappedBy = "owner")
    private List<Video> videos;
    @OneToMany(mappedBy = "owner")
    private List<Comment> comments;
    @ManyToMany
    @JoinTable(name = "users_follow_users",
            joinColumns = @JoinColumn(name = "following_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id"))
    private Set<User> followers = new HashSet<>();

    @ManyToMany(mappedBy = "followers")
    private Set<User> following = new HashSet<>();


    public enum Gender{
        MALE, FEMALE
    }

}
