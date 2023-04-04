package com.example.cotransfers.service;


import com.example.cotransfers.model.Transfer;
import com.example.cotransfers.model.User;

import java.util.List;
import java.util.Set;

public interface UserService {

    List<User> getAllUsers();

    User getUser(Long id);

    void save(User user);

    void deleteUser(Long id);

    void update(User user);

    void createUser(Long transferId, String user);

    Set<Transfer> getAllUserTransfers(Long id);
}
