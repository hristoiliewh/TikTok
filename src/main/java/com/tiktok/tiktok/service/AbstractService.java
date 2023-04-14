package com.tiktok.tiktok.service;

import com.tiktok.tiktok.model.entities.User;
import com.tiktok.tiktok.model.exceptions.NotFoundException;
import com.tiktok.tiktok.model.repositories.SoundRepository;
import com.tiktok.tiktok.model.repositories.UserRepository;
import com.tiktok.tiktok.model.repositories.VideoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public abstract class AbstractService {
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected VideoRepository videoRepository;
    @Autowired
    protected SoundRepository soundRepository;
    @Autowired
    protected ModelMapper mapper;

    protected User getUserById(int id){
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }
}
