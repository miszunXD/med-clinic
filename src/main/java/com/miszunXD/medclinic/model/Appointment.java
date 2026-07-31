package com.miszunXD.medclinic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.miszunXD.medclinic.repository.Identifiable;

import java.time.LocalDateTime;
import java.util.Objects;

public record Appointment(String appointmentId, String doctorId, String patientPesel,
                          LocalDateTime dateTime, boolean isCancelled, double price)
        implements Identifiable<String>, Comparable<Appointment>{
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return Objects.equals(appointmentId, that.appointmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(appointmentId);
    }

    @Override
    @JsonIgnore
    public String getId() {
        return appointmentId;
    }

    @Override
    public int compareTo(Appointment o) {
        int dateComparison =  this.dateTime.compareTo(o.dateTime);

        if (dateComparison != 0) {
            return dateComparison;
        }

        return this.appointmentId.compareTo(o.appointmentId);
    }
}
