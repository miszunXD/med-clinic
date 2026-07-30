package com.miszunXD.medclinic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.miszunXD.medclinic.repository.Identifiable;

import java.util.Objects;

public record Doctor (String id, String fullName, String specialty, double visitPrice)
implements Identifiable<String> {
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) return false;
        Doctor doctor = (Doctor) o;
        return Objects.equals(id, doctor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String getId() {
        return id;
    }
}
