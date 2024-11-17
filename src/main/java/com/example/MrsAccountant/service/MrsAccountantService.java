package com.example.mrsaccountant.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.mrsaccountant.entity.Record;
import com.example.mrsaccountant.entity.User;
import com.example.mrsaccountant.repository.RecordRepository;
import com.example.mrsaccountant.repository.UserRepository;
import java.util.Optional;

@Service
public class MrsAccountantService {

    private final RecordRepository recordRepository;
    private final UserRepository userRepository;

    public MrsAccountantService(RecordRepository recordRepository, UserRepository userRepository) {
        this.recordRepository = recordRepository;
        this.userRepository = userRepository;
    }

    public List<Record> getAllRecords() {
        return recordRepository.findAll();
    }

    public Optional<User> getUserById(long id){
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();


    }

    public void saveUser(User user){
        userRepository.save(user);
    }

    public void deleteById(Long id){
        userRepository.deleteById(id);
    }


}
