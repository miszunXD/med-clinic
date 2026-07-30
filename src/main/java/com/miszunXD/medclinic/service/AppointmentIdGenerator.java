package com.miszunXD.medclinic.service;

import com.miszunXD.medclinic.model.Appointment;

import java.util.List;

public class AppointmentIdGenerator {
    public String generateId(List<Appointment> appointments) {
        int maxId = appointments.stream()
                .map(Appointment::appointmentId)
                .filter(id -> id.startsWith("A-"))
                .map(id -> id.substring(2))
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(100);

        return "A-" + (maxId + 1);
    }
}
