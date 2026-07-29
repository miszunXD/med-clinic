package com.miszunXD.medclinic.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miszunXD.medclinic.model.Patient;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public class PatientRepository {
    private final List<Patient> patients;
    private final ObjectMapper objectMapper;

    public PatientRepository() {
        this.objectMapper = new ObjectMapper();
        this.patients = loadPatients();
    }

    private List<Patient> loadPatients() {
        try (InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream("patients.json")) {
            if (inputStream == null) {
                throw new RuntimeException("Brak pliku patients.json");
            }
            return objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<Patient>>() {}
            );

        } catch (IOException e) {
            throw new RuntimeException("Błąd ładowania pliku patients", e);
        }
    }

    public List<Patient> findAll() {
        return patients;
    }

    public Optional<Patient> findByPesel(String pesel) {
        return patients.stream()
                .filter(p -> p.pesel().equals(pesel))
                .findFirst();
    }

    public void addPatient(Patient patient) {
        patients.add(patient);
    }
}
