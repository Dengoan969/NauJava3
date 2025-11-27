package ru.Golov_Denis.NauJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.Golov_Denis.NauJava.entity.ReportStatus;
import ru.Golov_Denis.NauJava.repository.ReportRepository;
import ru.Golov_Denis.NauJava.service.ReportService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;
    private final ReportRepository reportRepository;

    @Autowired
    public ReportController(ReportService reportService, ReportRepository reportRepository) {
        this.reportService = reportService;
        this.reportRepository = reportRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createAndStartReport() {
        var id = reportService.createReport();
        var future = reportService.generateReportAsync(id);

        var resp = new HashMap<String, Object>();
        resp.put("reportId", id);
        resp.put("status", "created");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(resp);
    }

    @GetMapping("/content/{id}")
    public ResponseEntity<?> getReport(@PathVariable Long id) {
        var optionalReport = reportRepository.findById(id);
        if (optionalReport.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Report not found"));
        }

        var report = optionalReport.get();
        var status = report.getStatus();

        if (status == ReportStatus.CREATED) {
            return ResponseEntity.ok(Map.of(
                    "reportId", id,
                    "status", "created",
                    "message", "Отчёт ещё формируется"
            ));
        } else if (status == ReportStatus.ERROR) {
            var content = report.getContent();
            return ResponseEntity.ok(Map.of(
                    "reportId", id,
                    "status", "error",
                    "content", content != null ? content : "Ошибка при формировании отчёта"
            ));
        } else {
            var html = report.getContent();
            return ResponseEntity.ok()
                    .header("Content-Type", "text/html; charset=utf-8")
                    .body(html != null ? html : "");
        }
    }
}
