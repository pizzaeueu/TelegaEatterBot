package com.telega.eatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;

@SpringBootApplication
public class EatterApplication implements CommandLineRunner {

    @Autowired
    private Bot bot;

    static {
        ApiContextInitializer.init();
    }
    @Override
    public void run(String... args) throws Exception {
        TelegramBotsApi botsApi = new TelegramBotsApi();
        botsApi.registerBot(bot);

    }


    public static void main(String[] args) {
        SpringApplication.run(EatterApplication.class, args);
    }
}
