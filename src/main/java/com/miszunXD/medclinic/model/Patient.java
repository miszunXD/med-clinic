package com.miszunXD.medclinic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.miszunXD.medclinic.repository.Identifiable;

import java.util.Objects;

public record Patient(String pesel, String fullName, String phoneNumber)
        implements Identifiable<String> {
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return Objects.equals(pesel, patient.pesel);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(pesel);
    }

    @Override
    @JsonIgnore
    public String getId() {
        return pesel;
    }
}
