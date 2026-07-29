package com.miszunXD.medclinic.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miszunXD.medclinic.model.Doctor;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public class DoctorRepository {
    private final List<Doctor> doctors;
    private final ObjectMapper objectMapper;

    public DoctorRepository() {
        this.objectMapper = new ObjectMapper();
        this.doctors = loadDoctors();
    }

    private List<Doctor> loadDoctors() {
        try (InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream("doctors.json")) {

            if (inputStream == null) {
                throw new RuntimeException("Brak pliku doctors.json");
            }
            return objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<Doctor>>() {}
            );
        } catch (IOException e) {
            throw new RuntimeException("Błąd ładowanie pliku doctors", e);
        }
    }

    public List<Doctor> findAll() {
        return doctors;
    }

    public Optional<Doctor> findById(String id) {
        return doctors.stream()
                .filter(d -> d.id().equals(id))
                .findFirst();
    }
}
