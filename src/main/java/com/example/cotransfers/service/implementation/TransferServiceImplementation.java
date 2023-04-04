package com.example.cotransfers.service.implementation;

import com.example.cotransfers.model.Transfer;
import com.example.cotransfers.model.TransferUser;
import com.example.cotransfers.model.User;
import com.example.cotransfers.repository.TransferRepository;
import com.example.cotransfers.repository.TransferUserRepository;
import com.example.cotransfers.repository.UserRepository;
import com.example.cotransfers.service.TransferService;
import com.example.cotransfers.telegram.CoTransferBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferServiceImplementation implements TransferService {

    private final TransferRepository transferRepository;
    private final UserRepository userRepository;
    private final TransferUserRepository transferUserRepository;

    @Override
    public List<Transfer> getAllTransfers() {
        log.info("Получение всех трансферов");
        List<Transfer> allTransfers = transferRepository.findAllByIsShared(false);
        log.info("Все трансферы получены");
        return allTransfers;
    }

    @Override
    public Transfer getTransfer(Long id) {
        log.info("Получение трансфера с id:{}", id);
        Optional<Transfer> optional = transferRepository.findByIdAndIsShared(id,false);
        if (optional.isPresent()) {
            Transfer transfer = optional.get();
            log.info("Трансфер получен с id:{}", id);
            return transfer;
        } else
            throw new EntityNotFoundException("Трансфер не найден");
    }

    @Override
    public void save(Transfer transfer) {
        log.info("Создание трансфера");
        transferRepository.save(transfer);
        log.info("Трансфер создан и записан в базу");
    }


    @Override
    public void deleteTransfer(Transfer transfer){
        List<TransferUser> transferUser = transferUserRepository.findAllByTransferId(transfer);
        transferUserRepository.deleteAll(transferUser);
        transferRepository.delete(transfer);
    }

    @Override
    public ResponseEntity<?> deleteTransfer(String transfer) {


        JSONObject jsonObjectRequest = new JSONObject(transfer);
        JSONObject jsonObjectUpdate = jsonObjectRequest.getJSONObject(("order"));
        Long id = jsonObjectUpdate.getLong("id");

        log.info("Удаление трансфера с id: {}", id);

        Optional<Transfer> transferOptional = transferRepository.findById(id);
        Transfer newTransfer = transferOptional.get();
        List<TransferUser> transferUser = transferUserRepository.findAllByTransferId(newTransfer);
        transferUserRepository.deleteAll(transferUser);

        JSONArray jsonArray = jsonObjectUpdate.getJSONArray("users");
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject arrayJson = jsonArray.getJSONObject(i);
            Long userId = arrayJson.getLong("id");

            Optional<User> userOptional = userRepository.findById(userId);
            User updateUser = userOptional.get();

            userRepository.delete(updateUser);
        }

        transferRepository.delete(newTransfer);
        log.info("Трансфер удалён с id: {}", id);
        return ResponseEntity.ok(id);
    }

    @Override
    public void update(Transfer transfer) {
        log.info("Обновление данных о трансфере с id: {} ", transfer.getId());
        transferRepository.save(transfer);
        log.info("Данные обновлены");
    }

    public ResponseEntity<?> createTransferFromAirport(String transfer, Long id) {
        log.info("Создание трансфера");


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
        newTransfer.setIsShared(jsonObject.getBoolean("isShared"));
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

        int allUsers = jsonObject.getInt("adultsAmount")
                + jsonObject.getInt("childrenUnder5")
                + jsonObject.getInt("childrenAbove5");

        SendMessage sendMessage = SendMessage.builder()
                .chatId(356840503L)
                .text(String.format("Был создан трансфер.%nИнформация о трансфере:%n" +
                        "Время заказа: " + newTransfer.getTransferDate() + " "
                                + newTransfer.getTransferTime() +
                        "%nФИО заказчика: " + newTransfer.getUsers().get(0).getName() +
                        "%nТелеграм для связи: " + newTransfer.getUsers().get(0).getTelegramLogin() +
                        "%nМесто отправления: " + newTransfer.getStartLocation() +
                        "%nМесто прибытия: " + newTransfer.getEndLocation() +
                        "%nКоличество пассажиров: " + allUsers))
                .build();

        SendMessage sendMessage2 = SendMessage.builder()
                .chatId(641113889L)
                .text(String.format("Был создан трансфер.%nИнформация о трансфере:%n" +
                        "Время заказа: " + newTransfer.getTransferDate() + " "
                        + newTransfer.getTransferTime() +
                        "%nФИО заказчика: " + newTransfer.getUsers().get(0).getName() +
                        "%nТелеграм для связи: " + newTransfer.getUsers().get(0).getTelegramLogin() +
                        "%nМесто отправления: " + newTransfer.getStartLocation() +
                        "%nМесто прибытия: " + newTransfer.getEndLocation() +
                        "%nКоличество пассажиров: " + allUsers))
                .build();

        CoTransferBot coTransferBot = new CoTransferBot();

        try {
            coTransferBot.execute(sendMessage);
            coTransferBot.execute(sendMessage2);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

        log.info("Трансфер создан");
        return ResponseEntity.ok(newTransfer);
    }


    @Override
    public ResponseEntity<?> updateTransfer(String transfer) {

        JSONObject jsonObjectRequest = new JSONObject(transfer);
        JSONObject jsonObjectUpdate = jsonObjectRequest.getJSONObject(("order"));
        List<User> userList = new ArrayList<>();
        Long id = jsonObjectUpdate.getLong("id");
        log.info("Обновление трансфера с id:{}", id);
        System.out.println(id);

        Optional<Transfer> transferOptional = transferRepository.findById(id);
        Transfer newTransfer = transferOptional.get();


        newTransfer.setTransferDate(jsonObjectUpdate.getString("transferDate"));
        newTransfer.setTransferTime(jsonObjectUpdate.getString("transferTime"));
        newTransfer.setStartLocation(jsonObjectUpdate.getString("startLocation"));
        newTransfer.setIsPickUpFromAirport(jsonObjectUpdate.getBoolean("isPickUpFromAirport"));
        newTransfer.setAdultsAmount(jsonObjectUpdate.getInt("adultsAmount"));
        newTransfer.setEndLocation(jsonObjectUpdate.getString("endLocation"));
        newTransfer.setCarType(jsonObjectUpdate.getString("carType"));
        newTransfer.setAdultsAmount(jsonObjectUpdate.getInt("adultsAmount"));
        newTransfer.setChildrenUnder5(jsonObjectUpdate.getInt("childrenUnder5"));
        newTransfer.setChildrenAbove5(jsonObjectUpdate.getInt("childrenAbove5"));
        newTransfer.setIsShared(jsonObjectUpdate.getBoolean("isShared"));
        Long identificationNumber = 0L;

        JSONArray jsonArray = jsonObjectUpdate.getJSONArray("users");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject arrayJson = jsonArray.getJSONObject(i);
            Long userIdentif = arrayJson.getLong("identificationNumber");

            Long userId = arrayJson.getLong("id");

            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                User updateUser = userOptional.get();

                updateUser.setArrivalDate(arrayJson.getString("arrivalDate"));
                updateUser.setArrivalTime(arrayJson.getString("arrivalTime"));
                updateUser.setEmail(arrayJson.getString("email"));
                updateUser.setFlightNumber(arrayJson.getString("flightNumber"));
                String name = arrayJson.getString("name");
                identificationNumber = arrayJson.getLong("identificationNumber");
                updateUser.setName(name);
                updateUser.setPassport(arrayJson.getString("passport"));
                updateUser.setPhoneNumber(String.valueOf(arrayJson.getString("phoneNumber")));
                updateUser.setTelegramLogin(arrayJson.getString("telegramLogin"));
                updateUser.setTripComment(arrayJson.getString("tripComment"));
                userList.add(updateUser);
                userRepository.save(updateUser);
            } else {
                User updateUser = new User();
                TransferUser transferUser = new TransferUser();

                updateUser.setArrivalDate(arrayJson.getString("arrivalDate"));
                updateUser.setArrivalTime(arrayJson.getString("arrivalTime"));
                updateUser.setEmail(arrayJson.getString("email"));
                updateUser.setFlightNumber(arrayJson.getString("flightNumber"));
                String name = arrayJson.getString("name");
                updateUser.setName(name);
                updateUser.setIdentificationNumber(identificationNumber);
                updateUser.setPassport(arrayJson.getString("passport"));
                updateUser.setPhoneNumber(String.valueOf(arrayJson.getString("phoneNumber")));
                updateUser.setTelegramLogin(arrayJson.getString("telegramLogin"));
                updateUser.setTripComment(arrayJson.getString("tripComment"));

                transferUser.setTransferId(newTransfer);
                transferUser.setUserId(updateUser);
                transferUser.setUserIdentificationNumber(identificationNumber);

                userList.add(updateUser);
                userRepository.save(updateUser);
                transferUserRepository.save(transferUser);
            }
        }

        newTransfer.setUsers(userList);
        transferRepository.save(newTransfer);
        log.info("Данные обновлены");
        return ResponseEntity.ok(newTransfer);
    }
}
