package ru.Golov_Denis.NauJava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.Golov_Denis.NauJava.entity.NoteEntity;
import ru.Golov_Denis.NauJava.entity.ReportEntity;
import ru.Golov_Denis.NauJava.entity.ReportStatus;
import ru.Golov_Denis.NauJava.repository.NotesRepository;
import ru.Golov_Denis.NauJava.repository.ReportRepository;
import ru.Golov_Denis.NauJava.repository.UserRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final NotesRepository notesRepository;

    @Autowired
    public ReportServiceImpl(ReportRepository reportRepository,
                             UserRepository userRepository,
                             NotesRepository notesRepository) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.notesRepository = notesRepository;
    }

    @Override
    public Long createReport() {
        var entity = new ReportEntity();
        entity.setStatus(ReportStatus.CREATED);
        entity.setContent(null);
        var saved = reportRepository.save(entity);
        return saved.getId();
    }

    @Override
    public String getReportContent(Long id) {
        return reportRepository.findById(id)
                .map(ReportEntity::getContent)
                .orElse(null);
    }

    @Override
    public CompletableFuture<Void> generateReportAsync(Long reportId) {
        return CompletableFuture.runAsync(() -> {
            var overallStart = System.currentTimeMillis();

            var report = reportRepository.findById(reportId).orElse(null);
            if (report == null) {
                return;
            }

            report.setStatus(ReportStatus.CREATED);
            reportRepository.save(report);

            try {
                var userCount = new AtomicLong(0);
                var notesRef = new AtomicReference<List<NoteEntity>>();

                var userStartTimestamp = System.currentTimeMillis();
                var t1 = new Thread(() -> {
                    var c = userRepository.count();
                    userCount.set(c);
                }, "report-user-count-thread");
                t1.start();

                t1.join();
                var userElapsed = System.currentTimeMillis() - userStartTimestamp;

                var notesStartTimestamp = System.currentTimeMillis();
                var t2 = new Thread(() -> {
                    var notes = StreamSupport.stream(notesRepository.findAll().spliterator(), false)
                            .collect(Collectors.toList());
                    notesRef.set(notes);
                }, "report-notes-list-thread");
                t2.start();
                t2.join();
                var notesElapsed = System.currentTimeMillis() - notesStartTimestamp;

                var overallElapsed = System.currentTimeMillis() - overallStart;

                var html = buildHtmlReport(userCount.get(), notesRef.get(), userElapsed, notesElapsed, overallElapsed);

                report.setContent(html);
                report.setStatus(ReportStatus.COMPLETED);
                reportRepository.save(report);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                report.setStatus(ReportStatus.ERROR);
                report.setContent("<h1>Ошибка формирования отчёта</h1><p>Процесс был прерван: " + ie.getMessage() + "</p>");
                reportRepository.save(report);
            } catch (Exception ex) {
                report.setStatus(ReportStatus.ERROR);
                report.setContent("<h1>Ошибка формирования отчёта</h1><p>" + ex.getMessage() + "</p>");
                reportRepository.save(report);
            }
        });
    }

    private String escapeHtml(String s) {
        if (s == null)
            return "";

        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }

    private String buildHtmlReport(long userCount,
                                   List<NoteEntity> notes,
                                   long userElapsedMs,
                                   long notesElapsedMs,
                                   long totalElapsedMs) {

        var sb = new StringBuilder();
        sb.append("<!doctype html>\n")
                .append("<html lang=\"ru\">\n")
                .append("<head>\n")
                .append("  <meta charset=\"utf-8\">\n")
                .append("  <title>Отчёт приложения</title>\n")
                .append("  <style>\n")
                .append("    body{font-family: Arial, sans-serif; padding:16px}\n")
                .append("    table{border-collapse:collapse; width:100%}\n")
                .append("    th,td{border:1px solid #ddd; padding:8px}\n")
                .append("    th{background:#f4f4f4}\n")
                .append("  </style>\n")
                .append("</head>\n")
                .append("<body>\n")
                .append("  <h1>Статистика приложения</h1>\n")
                .append("  <table>\n")
                .append("    <tr><th>Показатель</th><th>Значение</th></tr>\n")
                .append("    <tr><td>Количество зарегистрированных пользователей</td><td>")
                .append(userCount)
                .append("</td></tr>\n")
                .append("    <tr><td>Время подсчёта пользователей (мс)</td><td>")
                .append(userElapsedMs)
                .append("</td></tr>\n")
                .append("    <tr><td>Количество заметок</td><td>")
                .append(notes != null ? notes.size() : 0)
                .append("</td></tr>\n")
                .append("    <tr><td>Время получения списка заметок (мс)</td><td>")
                .append(notesElapsedMs)
                .append("</td></tr>\n")
                .append("    <tr><td>Общее время формирования отчёта (мс)</td><td>")
                .append(totalElapsedMs)
                .append("</td></tr>\n")
                .append("  </table>\n")
                .append("\n")
                .append("  <h2>Список заметок</h2>\n");

        if (notes == null || notes.isEmpty()) {
            sb.append("<p>Заметок не найдено.</p>\n");
        } else {
            sb.append("<table>\n")
                    .append("<thead><tr><th>ID</th><th>Заголовок</th><th>Автор</th><th>Создано</th></tr></thead>\n")
                    .append("<tbody>\n");
            for (var note : notes) {
                var title = escapeHtml(note.getTitle());
                var author = note.getUser() != null ? escapeHtml(note.getUser().getUsername()) : "";
                var created = note.getCreatedAt() != null ? note.getCreatedAt().toString() : "";
                sb.append("<tr>")
                        .append("<td>").append(note.getId()).append("</td>")
                        .append("<td>").append(title).append("</td>")
                        .append("<td>").append(author).append("</td>")
                        .append("<td>").append(created).append("</td>")
                        .append("</tr>\n");
            }
            sb.append("</tbody>\n</table>\n");
        }

        sb.append("</body>\n</html>");
        return sb.toString();
    }
}
