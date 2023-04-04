package com.example.cotransfers.controller;



import com.example.cotransfers.service.TransferSharedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sharedTransfer")
@RequiredArgsConstructor
public class SharedTransferController {
    private final TransferSharedService transferSharedService;

    /**
     * Метод для получения всех зашеренных трансферов
     */
    @GetMapping("/getSharedTranfers")
    private ResponseEntity<?> getSharedTransfers(){
        return  transferSharedService.getSharedTransfers();
    }
    /**
     * Метод для устанвки shared на трансфер
     */
    @PostMapping("/setShared")
    private ResponseEntity<?> setShared(@RequestHeader(name = "id") Long id) {
        return transferSharedService.setShared(id);
    }
    /**
     * Метод для создания зашеренного трансфера
     */
    @PostMapping("create-shared-transfer")
    private ResponseEntity<?> createSharedTransferFromAirport(@RequestBody String transfer, @RequestHeader(name = "id") Long id) {
        return ResponseEntity.ok(transferSharedService.createSharedTransferFromAirport(transfer, id));
    }
}
