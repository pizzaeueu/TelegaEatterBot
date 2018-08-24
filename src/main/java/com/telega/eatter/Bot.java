package com.telega.eatter;

import com.telega.eatter.Utils.BotUtils;
import com.telega.eatter.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class Bot extends TelegramLongPollingBot {

    private volatile boolean isActiveGame = false;
    private Long gameOwnerId = null;
    private Document document = null;
    private Double price = -2.0;
    private boolean gameIsReady = false;

    @Autowired
    GameService gameService;

    @Autowired
    BotUtils botUtils;

    @Value("${bot.token}")
    private String botToken;

    @Value("${bot.name}")
    private String botName;

    @Override
    public void onUpdateReceived(Update update) {
        String message = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        System.out.println(message);
        SendMessage sendMessage = botUtils.getSubsribttionMessage(chatId);
        botUtils.setDefaultButtons(sendMessage);

        if (isActiveGame && gameOwnerId.equals(chatId)) {
            processActiveGameByOwner(update);
        }

        if (message.equals(BotUtils.SUBSCRIBE_MESSAGE)) {
            sendMsg(sendMessage);
        } else if (message.equals(BotUtils.START_GAME_MESSAGE)) {
            if (isActiveGame) {
                sendMessage.setText("Game has already started");
                sendMsg(sendMessage);
                return;
            }
            isActiveGame = true;
            gameOwnerId = chatId;
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    private void sendMsg(SendMessage sendMessage) {
        try {
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void processActiveGameByOwner(Update update) {
        SendMessage sendMessage = new SendMessage(update.getMessage().getChatId(), "ERROR");
        while (!gameIsReady) {
            botUtils.setGameOwnerButtons(sendMessage);
            if (document == null && update.getMessage().getDocument() == null) {
                sendMessage.setText("Send me picture as a document");
                sendMsg(sendMessage);
                return;
            } else if (document == null && update.getMessage().getDocument() != null) {
                document = update.getMessage().getDocument();
            } else if (document != null && price < 0) {
                try {
                    price = Double.parseDouble(update.getMessage().getText());
                } catch (Exception e) {
                    sendMessage.setText("Send me price");
                    sendMsg(sendMessage);
                    return;
                }

            } else if (document != null && price > 0) {
                sendMessage.setText("Game has started, Пидор");
                sendMsg(sendMessage);
                gameIsReady = true;
                return;
            }
        }
    }
}
