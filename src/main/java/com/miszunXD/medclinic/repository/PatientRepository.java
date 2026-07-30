package com.miszunXD.medclinic.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miszunXD.medclinic.model.Patient;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class PatientRepository {
    private final static String FILE_NAME = "patients.json";
    private final List<Patient> patients;
    private final ObjectMapper objectMapper;

    public PatientRepository() {
        this.objectMapper = new ObjectMapper();
        this.patients = loadPatients();
    }

    private List<Patient> loadPatients() {
        try {
            File file = new File(FILE_NAME);

            if (!file.exists()) {
                throw new RuntimeException("Brak pliku " + FILE_NAME);
            }
            return objectMapper.readValue(
                    file,
                    new TypeReference<List<Patient>>() {}
            );

        } catch (IOException e) {
            throw new RuntimeException("Błąd ładowania pliku patients", e);
        }
    }

    public void savePatients() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File("patients.json"), patients);
        } catch (IOException e) {
            throw new RuntimeException("Nie udało się zapisać pliku", e);
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
