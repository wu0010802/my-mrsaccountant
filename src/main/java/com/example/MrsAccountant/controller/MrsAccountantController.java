package com.example.mrsaccountant.controller;

import com.example.mrsaccountant.entity.Record;
import com.example.mrsaccountant.entity.User;
import com.example.mrsaccountant.service.MrsAccountantService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/mrsaccountant")
public class MrsAccountantController {

    private final MrsAccountantService mrsAccountantService;

    public MrsAccountantController(MrsAccountantService mrsAccountantService) {
        this.mrsAccountantService = mrsAccountantService;
    }

    @GetMapping("/records")
    public List<Record> getAllRecords() {
        return mrsAccountantService.getAllRecords();
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return mrsAccountantService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> optionalUser = mrsAccountantService.getUserById(id);
        
        return optionalUser.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }


    @PostMapping("users")
	@ResponseStatus(HttpStatus.CREATED)
	public void add(@RequestBody User user) {
		mrsAccountantService.saveUser(user);
	}

    @DeleteMapping("users/{id}")
    public void deleteById(@PathVariable long id){
        mrsAccountantService.deleteById(id);
    }

}
