package com.example.cotransfers.repository;



import com.example.cotransfers.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByIdentificationNumber(Long id);
    User findUsersByIdentificationNumber(Long identificationNumber);

}
