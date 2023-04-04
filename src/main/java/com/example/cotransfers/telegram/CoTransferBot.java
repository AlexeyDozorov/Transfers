package com.example.cotransfers.telegram;

import com.example.cotransfers.model.Transfer;
import com.example.cotransfers.model.TransferComparator;
import com.example.cotransfers.repository.TransferRepository;
import com.example.cotransfers.repository.TransferUserRepository;
import com.example.cotransfers.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Optional;

@Component
public class CoTransferBot extends TelegramLongPollingBot {

    private TransferService transferService;

    private  TransferUserRepository transferUserRepository;

    private  TransferRepository transferRepository;
    @Autowired
    public CoTransferBot(TransferService transferService, TransferUserRepository transferUserRepository, TransferRepository transferRepository) {
        this.transferService = transferService;
        this.transferUserRepository = transferUserRepository;
        this.transferRepository = transferRepository;
    }

    public CoTransferBot(){
    }

    @Override
    public String getBotUsername() {
        return "test_transfer_transfer_bot";
    }

    @Override
    public String getBotToken() {
        return "6012133392:AAF5GhVftU82Fevftn1cQb3Z5by_fygcljQ";
    }

    @Override
    public void onUpdateReceived(Update update) {

        if(update.hasMessage() && update.getMessage().getText().equals("/info")){
            System.out.println(update.getMessage().getChatId());
            //356840503
        }
        else if(update.hasMessage() && update.getMessage().getText().equals("/allTransfers")){
            List<Transfer> allTransfers = transferService.getAllTransfers();
            allTransfers.sort(new TransferComparator());
            for (int i = 5; i >= 0 ; i--) {
                Transfer transfer = allTransfers.get(i);
                int allPassengers = transfer.getAdultsAmount() + transfer.getChildrenAbove5() + transfer.getChildrenUnder5();
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(update.getMessage().getChatId())
                        .text(String.format("Информация о трансфере:" +
                                        "%nId трансфера: " + transfer.getId() +
                                        "%nКоличествво пассажиров: " + allPassengers +
                                        "%nДата трансфера: " + transfer.getTransferDate() +
                                        "%nВремя транфсера: " + transfer.getTransferTime() +
                                        "%nОткуда ехать: " + transfer.getStartLocation() +
                                        "%nКуда ехать: " + transfer.getEndLocation()))
                        .build();

                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        else if(update.hasMessage() && update.getMessage().getText().equals("/delete")){

            List<Transfer> allTransfers = transferRepository.findAll();

            for (Transfer transfer : allTransfers) {
                transfer.setIsDeleted(true);
                transferRepository.save(transfer);
            }

            SendMessage sendMessage = SendMessage.builder()
                    .chatId(update.getMessage().getChatId())
                    .text("Введите id трансфера, который необходимо удалить")
                    .build();
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        else if((update.hasMessage() && transferRepository.findAll().get(0).getIsDeleted())){
            Optional<Transfer> deletedTransfer = transferRepository.findById(Long.valueOf(update.getMessage().getText()));

            transferService.deleteTransfer(deletedTransfer.get());

            List<Transfer> allTransfers = transferRepository.findAll();

            for (Transfer transfer : allTransfers) {
                transfer.setIsDeleted(false);
                transferRepository.save(transfer);
            }

            SendMessage sendMessage = SendMessage.builder()
                    .chatId(update.getMessage().getChatId())
                    .text("Трансфер успешно удалён")
                    .build();

            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        else if(update.hasMessage() && update.getMessage().getText().equals("/update")){

            List<Transfer> allTransfers = transferRepository.findAll();

            for (Transfer transfer : allTransfers) {
                transfer.setIsUpdated(true);
                transferRepository.save(transfer);
            }


            SendMessage sendMessage = SendMessage.builder()
                    .chatId(update.getMessage().getChatId())
                    .text("Введите id трансфера, который был завершён")
                    .build();
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        else if(update.hasMessage() && transferRepository.findAll().get(0).getIsUpdated()){
            Optional<Transfer> transferOptional = transferRepository.findById(Long.valueOf(update.getMessage().getText()));
            Transfer transfer = transferOptional.get();

            transfer.setIsEnded(true);
            transferRepository.save(transfer);

            SendMessage sendMessage = SendMessage.builder()
                    .chatId(update.getMessage().getChatId())
                    .text("Трансфер успешно обновлён")
                    .build();

            List<Transfer> allTransfers = transferRepository.findAll();

            for (Transfer tmpTransfer : allTransfers) {
                tmpTransfer.setIsUpdated(false);
                transferRepository.save(tmpTransfer);
            }
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
