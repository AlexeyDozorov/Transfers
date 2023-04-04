package com.example.cotransfers.service;

import com.example.cotransfers.model.Transfer;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TransferService {

    List<Transfer> getAllTransfers();

    Transfer getTransfer(Long id);

    void save(Transfer transfer);

    ResponseEntity<?> deleteTransfer(String transfer);

    void update(Transfer transfer);

    ResponseEntity<?> createTransferFromAirport(String transfer, Long id);

    ResponseEntity<?> updateTransfer(String transfer);

    void deleteTransfer(Transfer transfer);

}
