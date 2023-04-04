package com.example.cotransfers.controller;

import com.example.cotransfers.model.Transfer;
import com.example.cotransfers.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

//    @GetMapping("/allTransfers")
//    private Page<Transfer> getAllTransfers(@PageableDefault(size = 125) Pageable pageable) {
//        return transferService.getAllTransfers(pageable);
//    }

    @GetMapping("/oneTransfers/{id}")
    private ResponseEntity<?> getTransfer(@PathVariable("id") Long id) {
        Transfer transfer = transferService.getTransfer(id);
        return ResponseEntity.ok(transfer);
    }

    @PostMapping("/create-transfer")
    private ResponseEntity<?> createTransfer(@RequestBody String transfer, @RequestHeader(name = "id") Long id) {
        return ResponseEntity.ok(transferService.createTransferFromAirport(transfer, id));
    }

    @PutMapping("/updateTransfer")
    private ResponseEntity<?> updateTransfer(@RequestBody String transfer){
        return  ResponseEntity.ok(transferService.updateTransfer(transfer));
    }

    @DeleteMapping("/deleteTransfer")
    private  ResponseEntity<?> deleteTransfer(@RequestBody String transfer){
        return transferService.deleteTransfer(transfer);
    }
}
