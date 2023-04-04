package com.example.cotransfers.controller;

import com.example.cotransfers.model.Transfer;
import com.example.cotransfers.model.User;
import com.example.cotransfers.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/allUsers")
    private ResponseEntity<?> getAllUsers(){
        List<User> allUsers = userService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    @GetMapping("/all-transfers")
    private Set<Transfer> getAllUserTransfers(@RequestHeader(name = "id") Long id){
        return userService.getAllUserTransfers(id);
    }

//    @GetMapping("/allTransfers")
//    private Page<Transfer> getAllTransfers(@PageableDefault(size = 125) Pageable pageable, @RequestHeader(name = "id") Long id ) {
//        Page<Transfer> allUserTransfers = userService.getAllUserTransfers(id, pageable);
//        return allUserTransfers;
//    }

    @PostMapping("/create-user/{id}")
    private ResponseEntity<?> createUser(@PathVariable("id") Long id,
                                         @RequestBody String user){
        userService.createUser(id, user);
        return ResponseEntity.ok("Пользователь создан");
    }
}
