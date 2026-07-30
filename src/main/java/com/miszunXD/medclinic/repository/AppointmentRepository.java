package com.miszunXD.medclinic.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.miszunXD.medclinic.model.Appointment;
import java.util.List;

public class AppointmentRepository extends AbstractJsonRepository<Appointment, String> {
    private final static String FILE_NAME = "appointments.json";

    public AppointmentRepository() {
        super(
                FILE_NAME,
                new TypeReference<List<Appointment>>() {
                }
        );
    }
}