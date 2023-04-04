package com.example.cotransfers.service.implementation;

import com.example.cotransfers.model.Transfer;
import com.example.cotransfers.model.TransferUser;
import com.example.cotransfers.model.User;
import com.example.cotransfers.repository.TransferRepository;
import com.example.cotransfers.repository.TransferUserRepository;
import com.example.cotransfers.repository.UserRepository;
import com.example.cotransfers.service.TransferSharedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferSharedServiceImplementation implements TransferSharedService {

    private final TransferRepository transferRepository;
    private final UserRepository userRepository;
    private final TransferUserRepository transferUserRepository;

    @Override
    public ResponseEntity<?> getSharedTransfers() {
        log.info("Получение всех зашеренных трансферов");
        return ResponseEntity.ok(transferRepository.findAllByIsShared(true));
    }

    @Override
    public ResponseEntity<?> setShared(Long id) {
        log.info("Получение трансфера с id = {}", id);
        Optional<Transfer> transfer = transferRepository.findById(id);
        if (transfer.isPresent()){
            Transfer newTransfer = transfer.get();
            newTransfer.setIsShared(true);
            log.info("Установление трансферу с id = {} shared true", id);
            transferRepository.save(newTransfer);
        } else throw new EntityNotFoundException("Трансфер не найден");

        return ResponseEntity.ok(transfer);
    }


    @Override
    public ResponseEntity<?> createSharedTransferFromAirport(String transfer, Long id) {
        log.info("Создание пошеренного трансфера");

        Transfer newTransfer = new Transfer();

        JSONObject jsonObjectTr = new JSONObject(transfer);
        JSONObject jsonObject = jsonObjectTr.getJSONObject(("order"));

        List<User> userList = new ArrayList<>();


        newTransfer.setTransferDate(jsonObject.getString("transferDate"));
        newTransfer.setTransferTime(jsonObject.getString("transferTime"));
        newTransfer.setStartLocation(jsonObject.getString("startLocation"));
        newTransfer.setIsPickUpFromAirport(jsonObject.getBoolean("isPickUpFromAirport"));
        newTransfer.setAdultsAmount(jsonObject.getInt("adultsAmount"));
        newTransfer.setEndLocation(jsonObject.getString("endLocation"));
        newTransfer.setCarType(jsonObject.getString("carType"));
        newTransfer.setAdultsAmount(jsonObject.getInt("adultsAmount"));
        newTransfer.setChildrenUnder5(jsonObject.getInt("childrenUnder5"));
        newTransfer.setChildrenAbove5(jsonObject.getInt("childrenAbove5"));
        newTransfer.setIsEnded(false);
        newTransfer.setIsShared(true);
        JSONArray jsonArray = jsonObject.getJSONArray("users");

        for (int i = 0; i < jsonArray.length(); i++) {
            User newUser = new User();
            TransferUser transferUser = new TransferUser();

            JSONObject arrayJson = jsonArray.getJSONObject(i);

            newUser.setArrivalDate(arrayJson.getString("arrivalDate"));
            newUser.setArrivalTime(arrayJson.getString("arrivalTime"));
            newUser.setEmail(arrayJson.getString("email"));
            newUser.setFlightNumber(arrayJson.getString("flightNumber"));
            String name = arrayJson.getString("name");
            newUser.setIdentificationNumber(id);
            newUser.setName(name);
            newUser.setPassport(arrayJson.getString("passport"));
            newUser.setPhoneNumber(String.valueOf(arrayJson.getString("phoneNumber")));
            newUser.setTelegramLogin(arrayJson.getString("telegramLogin"));
            newUser.setTripComment(arrayJson.getString("tripComment"));
            userList.add(newUser);
            transferUser.setUserIdentificationNumber(id);
            transferUser.setTransferId(newTransfer);
            transferUser.setUserId(newUser);
            userRepository.save(newUser);
            newTransfer.setUsers(userList);
            transferRepository.save(newTransfer);
            transferUserRepository.save(transferUser);

            log.info("Пользователь создан");
        }
        return ResponseEntity.ok(newTransfer);
    }
}
