package ru.Golov_Denis.NauJava.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ru.Golov_Denis.NauJava.model.Note;
import ru.Golov_Denis.NauJava.console.CommandProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Configuration
public class Config {

    @Value("${app.name}")
    private String appName;

    @Value("${app.version}")
    private String appVersion;

    @Bean
    @Scope(BeanDefinition.SCOPE_SINGLETON)
    public List<Note> notesContainer() {
        return new ArrayList<>();
    }

    @Bean
    public CommandLineRunner commandScanner(CommandProcessor commandProcessor) {
        return args -> {
            try (Scanner scanner = new Scanner(System.in)) {
                System.out.println("Введите команду. 'exit' для выхода.");
                while (true) {
                    System.out.print("> ");

                    var input = scanner.nextLine();

                    if ("exit".equalsIgnoreCase(input.trim())) {
                        System.out.println("Выход из программы...");
                        break;
                    }

                    commandProcessor.processCommand(input);
                }
            }
        };
    }

    @PostConstruct
    public void printAppInfo() {
        System.out.println("app.name: " + appName + " app.version " + appVersion);
    }
}
