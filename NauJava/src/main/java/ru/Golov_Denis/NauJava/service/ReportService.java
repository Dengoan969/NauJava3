package ru.Golov_Denis.NauJava.service;

import java.util.concurrent.CompletableFuture;

public interface ReportService {

    Long createReport();

    String getReportContent(Long id);

    CompletableFuture<Void> generateReportAsync(Long reportId);
}
