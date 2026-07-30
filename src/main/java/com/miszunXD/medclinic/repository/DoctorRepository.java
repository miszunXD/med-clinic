package com.miszunXD.medclinic.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.miszunXD.medclinic.model.Doctor;
import java.util.List;

public class DoctorRepository extends AbstractJsonRepository<Doctor, String> {
    private final static String FILE_NAME = "doctors.json";


    public DoctorRepository() {
        super(
                FILE_NAME,
                new TypeReference<List<Doctor>>() {}
        );
    }
}
