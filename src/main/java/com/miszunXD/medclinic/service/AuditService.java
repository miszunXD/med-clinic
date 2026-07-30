package com.miszunXD.medclinic.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

public class AuditService {
    private static final Path FILE_PATH = Path.of("audit-log.txt");

    public void log(String message) {
        try {
            String logEntry = LocalDateTime.now() + " - " + message + System.lineSeparator();
            Files.writeString(FILE_PATH, logEntry, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("Nie udało się zapisać wpisu do dziennika", e);
        }
    }
}
