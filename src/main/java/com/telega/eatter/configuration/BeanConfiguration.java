package com.telega.eatter.configuration;

import com.telega.eatter.service.GameService;
import com.telega.processors.MessageProcessor;
import com.telega.processors.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean(name = MessageProcessor.RANDOME_MESSAGES_PROCESSOR_BEAN)
    public MessageProcessor getRandomMessageProcessor() {
        return new RandomMessagesProcessor();
    }

    @Bean(name = MessageProcessor.START_GAME_PROCESSOR_BEAN)
    public MessageProcessor getStartGameProcessor(@Autowired GameStatusInfo gameStatus) {
        return new StartGameProcess(gameStatus);
    }

    @Bean(name = MessageProcessor.ACTIVE_GAME_BY_OWNER_PROCESSOR_BEAN)
    public MessageProcessor getActiveGameByOwnerProcessor(@Autowired GameStatusInfo gameStatus) {
        return new ActiveGameByOwnerProcessor(gameStatus);
    }

    @Bean(name = MessageProcessor.ANSWER_PROCESSOR_BEAN)
    public MessageProcessor getAnswersProcessor(@Autowired GameStatusInfo gameStatus, @Autowired GameService gameService) {
        return new AnswersProcessor(gameStatus, gameService);
    }

    @Bean(name = MessageProcessor.FINISH_PROCESSOR_BEAN)
    public MessageProcessor getFinishProcessor(@Autowired GameStatusInfo gameStatus, @Autowired GameService gameService) {
        return new FinishProcessor(gameStatus, gameService);
    }

}
