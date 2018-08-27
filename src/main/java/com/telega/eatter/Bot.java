package com.telega.eatter;

import com.telega.eatter.Utils.BotUtils;
import com.telega.eatter.configuration.GameStatusInfo;
import com.telega.processors.MessageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Objects;


@Component
public class Bot extends TelegramLongPollingBot {


    private final GameStatusInfo gameStatus;

    private final ApplicationContext context;

    private MessageProcessor messageProcessor;

    @Value("${bot.token}")
    private String botToken;

    @Value("${bot.name}")
    private String botName;

    @Autowired
    public Bot(ApplicationContext context, GameStatusInfo gameStatus) {
        this.context = context;
        this.gameStatus = gameStatus;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Integer userId = update.getMessage().getFrom().getId();
        Long chatId = update.getMessage().getChatId();
        SendMessage sendMessage = new SendMessage(chatId, "Да проще тебе ебало набить");



        String message = update.getMessage().getText();

        System.out.println(message);

        if (gameStatus.isActiveGame() && gameStatus.getGameOwnerId().equals(userId) && !gameStatus.isGameIsReady()) {
            messageProcessor = getProcessorByName(MessageProcessor.ACTIVE_GAME_BY_OWNER_PROCESSOR_BEAN);
        } else if (message != null && message.equals(BotUtils.START_GAME_MESSAGE)) {
            messageProcessor = getProcessorByName(MessageProcessor.START_GAME_PROCESSOR_BEAN);
        } else if (message != null && message.startsWith(BotUtils.ANSWER_MESSAGE)) {
            messageProcessor = getProcessorByName(MessageProcessor.ANSWER_PROCESSOR_BEAN);
        } else if (message != null && message.startsWith(BotUtils.FINISH_GAME_MESSAGE)) {
            messageProcessor = getProcessorByName(MessageProcessor.FINISH_PROCESSOR_BEAN);
        } else {
            messageProcessor = getProcessorByName(MessageProcessor.RANDOM_MESSAGES_PROCESSOR_BEAN);
        }

        sendMessage.setText(messageProcessor.process(update));
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

    private MessageProcessor getProcessorByName(String name) {
        return BeanFactoryAnnotationUtils.qualifiedBeanOfType(Objects.requireNonNull(context.getAutowireCapableBeanFactory()), MessageProcessor.class, name);
    }
}
