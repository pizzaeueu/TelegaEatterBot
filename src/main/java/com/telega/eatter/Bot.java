package com.telega.eatter;

import com.telega.eatter.Utils.BotUtils;
import com.telega.eatter.configuration.GameStatusInfo;
import com.telega.eatter.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class Bot extends TelegramLongPollingBot {


    @Autowired
    GameService gameService;

    @Autowired
    BotUtils botUtils;

    @Autowired
    GameStatusInfo gameStatus;

    @Value("${bot.token}")
    private String botToken;

    @Value("${bot.name}")
    private String botName;

    @Override
    public void onUpdateReceived(Update update) {
        Integer userId = update.getMessage().getFrom().getId();
        Long chatId = update.getMessage().getChatId();
        SendMessage sendMessage = new SendMessage(chatId, "Да проще тебе ебало набить");

        if (gameStatus.isActiveGame() && gameStatus.getGameOwnerId().equals(userId) && !gameStatus.isGameIsReady()) {
            sendMessage.setText(botUtils.processActiveGameByOwner(update));
            sendMsg(sendMessage);
            return;
        }

        String message = update.getMessage().getText();
        if (message == null) {
            return;
        }


        System.out.println(message);

        if (message.equals(BotUtils.START_GAME_MESSAGE)) {
            sendMessage.setText(botUtils.processStartGame(update));
            sendMsg(sendMessage);
            return;
        }

        if (message.startsWith(BotUtils.ANSWER_MESSAGE)) {
            sendMessage.setText(botUtils.processAnswers(update));
            sendMsg(sendMessage);
            return;
        }

        if (message.startsWith(BotUtils.FINISH_GAME_MESSAGE)) {
            sendMessage.setText(botUtils.processFinish(update));
            sendMsg(sendMessage);
            return;

        }

        sendMessage.setText(botUtils.processRandomMessages(update));
        sendMsg(sendMessage);
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
}
