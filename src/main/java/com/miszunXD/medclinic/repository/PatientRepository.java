package com.miszunXD.medclinic.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.miszunXD.medclinic.model.Patient;
import java.util.List;
import java.util.Optional;

public class PatientRepository extends AbstractJsonRepository<Patient, String>{
    private final static String FILE_NAME = "patients.json";

    public PatientRepository() {
        super(
                FILE_NAME,
                new TypeReference<List<Patient>>() {}
        );
    }

    public Optional<Patient> findByPesel(String pesel){
        return findById(pesel);
    }
}
