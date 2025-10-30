package ru.Golov_Denis.NauJava.console;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.Golov_Denis.NauJava.service.NotesService;

import java.util.Arrays;

@Component
public class CommandProcessor {

    private final NotesService notesService;

    @Autowired
    public CommandProcessor(NotesService notesService) {
        this.notesService = notesService;
    }

    public void processCommand(String input) {
        var arguments = input.split(" ");

        if (arguments.length == 0) {
            System.out.println("Введите команду");
            return;
        }

        switch (arguments[0].toLowerCase()) {
            case "create" -> {
                if (arguments.length < 5) {
                    System.out.println("Использование команды: create <id> <title> <content> <tag1,tag2,...>");
                    return;
                }

                var id = Long.valueOf(arguments[1]);
                var title = arguments[2];
                var content = arguments[3];
                var tags = Arrays.asList(arguments[4].split(","));

                notesService.createNote(id, title, content, tags);

                System.out.println("Заметка с id: " + id + " успешно создана.");
            }

            case "read" -> {
                if (arguments.length < 2) {
                    System.out.println("Использование команды: read <id>");
                    return;
                }

                var id = Long.valueOf(arguments[1]);

                var note = notesService.findById(id);

                if (note != null) {
                    System.out.println("Найдена заметка:\n" + note);
                } else {
                    System.out.println("Заметка с id: " + id + " не найдена.");
                }
            }

            case "update" -> {
                if (arguments.length < 3) {
                    System.out.println("Использование команды: update <id> <newContent>");
                    return;
                }

                var id = Long.valueOf(arguments[1]);
                var newContent = arguments[2];

                notesService.updateNoteContent(id, newContent);

                System.out.println("Заметка успешно обновлена.");
            }

            case "delete" -> {
                if (arguments.length < 2) {
                    System.out.println("Использование команды: delete <id>");
                    return;
                }

                var id = Long.valueOf(arguments[1]);

                notesService.deleteById(id);

                System.out.println("Заметка успешно удалена.");
            }

            default -> System.out.println("Неизвестная команда: " + arguments[0]);
        }
    }
}
