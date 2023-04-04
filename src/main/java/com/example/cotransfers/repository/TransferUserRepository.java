package com.example.cotransfers.repository;


import com.example.cotransfers.model.Transfer;
import com.example.cotransfers.model.TransferUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TransferUserRepository extends JpaRepository<TransferUser, Long> {

    TransferUser findByTransferId(Transfer transferId);
    List<TransferUser> findAllByTransferId(Transfer transferId);
    List<TransferUser> findAllByUserIdentificationNumber(Long userIdentificationNumber);
}
