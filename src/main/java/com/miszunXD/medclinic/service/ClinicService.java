package com.miszunXD.medclinic.service;

import com.miszunXD.medclinic.exception.DoubleBookingException;
import com.miszunXD.medclinic.exception.PatientAlreadyExistsException;
import com.miszunXD.medclinic.model.Appointment;
import com.miszunXD.medclinic.model.Doctor;
import com.miszunXD.medclinic.model.Patient;
import com.miszunXD.medclinic.repository.AppointmentRepository;
import com.miszunXD.medclinic.repository.DoctorRepository;
import com.miszunXD.medclinic.repository.PatientRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public class ClinicService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public ClinicService(AppointmentRepository appointmentRepository,
                         DoctorRepository doctorRepository,
                         PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    public void registerPatient(Patient patient) {
        Optional<Patient> existingPatient = patientRepository.findByPesel(patient.pesel());

        if (existingPatient.isPresent()) {
            throw new PatientAlreadyExistsException("Pacjent: " + patient.fullName()
                    + " o numerze PESEL: " + patient.pesel() + " już istnieje!");
        }

        patientRepository.addPatient(patient);
    }

    public void registerAppointment(Appointment appointment) {
        Optional<Doctor> doctor = doctorRepository.findById(appointment.doctorId());
        if (doctor.isEmpty()) {
            throw new RuntimeException("Taki lekarz nie istnieje!");
        }

        Optional<Patient> patient = patientRepository.findByPesel(appointment.patientPesel());
        if (patient.isEmpty()) {
            throw new RuntimeException("Taki pacjent nie istnieje!");
        }

        boolean appointmentsCollide = appointmentRepository.findAll().stream()
                .anyMatch(a -> a.dateTime().equals(appointment.dateTime())
                        && a.doctorId().equals(appointment.doctorId()));

        if (appointmentsCollide) {
            throw new DoubleBookingException("Ten termin jest już zarezerwowany. Wybierz inny!");
        }

        appointmentRepository.addAppointment(appointment);
    }

    public void cancelAppointment(String appointmentId) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);

        if (appointment.isEmpty()) {
            throw new RuntimeException("Nie ma takiej wizyty!");
        }

        Appointment oldAppointment = appointment.get();

        Appointment cancelledAppointment = new Appointment(
                oldAppointment.appointmentId(),
                oldAppointment.doctorId(),
                oldAppointment.patientPesel(),
                oldAppointment.dateTime(),
                true
        );

        appointmentRepository.updateAppointment(cancelledAppointment);

    }


}
