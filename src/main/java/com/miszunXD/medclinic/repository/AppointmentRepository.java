package com.miszunXD.medclinic.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.miszunXD.medclinic.model.Appointment;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AppointmentRepository {
    private final static String FILE_NAME = "appointments.json";
    private final List<Appointment> appointments;
    private final ObjectMapper objectMapper;

    public AppointmentRepository() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.appointments = loadAppointments();
    }

    private List<Appointment> loadAppointments() {
        try {
            File file = new File(FILE_NAME);

            if (!file.exists()) {
                throw new RuntimeException("Brak pliku " + FILE_NAME);
            }

            return objectMapper.readValue(
                    file,
                    new TypeReference<List<Appointment>>() {}
            );
        } catch (IOException e) {
            throw new RuntimeException("Błąd ładowania pliku appointments", e);
        }
    }

    public void saveAppointments() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File("appointments.json"), appointments);
        } catch (IOException e) {
            throw new RuntimeException("Nie udało się zapisach pliku", e);
        }
    }

    public List<Appointment> findAll() {
        return appointments;
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    public Optional<Appointment> findById(String id) {
        return appointments.stream()
                .filter(a -> a.appointmentId().equals(id))
                .findFirst();
    }

    public void updateAppointment(Appointment updatedAppointment) {
        appointments.removeIf(a ->
                a.appointmentId().equals(updatedAppointment.appointmentId()));
        appointments.add(updatedAppointment);
    }
}
