package com.miszunXD.medclinic.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.miszunXD.medclinic.model.Appointment;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public class AppointmentRepository {
    private final List<Appointment> appointments;
    private final ObjectMapper objectMapper;

    public AppointmentRepository() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.appointments = loadAppointments();
    }

    private List<Appointment> loadAppointments() {
        try (InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream("appointments.json")) {

            if (inputStream == null) {
                throw new RuntimeException("Brak pliku appointments.json");
            }

            return objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<Appointment>>() {}
            );
        } catch (IOException e) {
            throw new RuntimeException("Błąd ładowania pliku appointments", e);
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
