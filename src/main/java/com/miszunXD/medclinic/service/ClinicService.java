package com.miszunXD.medclinic.service;

import com.miszunXD.medclinic.exception.*;
import com.miszunXD.medclinic.model.Appointment;
import com.miszunXD.medclinic.model.Doctor;
import com.miszunXD.medclinic.model.Patient;
import com.miszunXD.medclinic.repository.AppointmentRepository;
import com.miszunXD.medclinic.repository.DoctorRepository;
import com.miszunXD.medclinic.repository.PatientRepository;

import java.util.*;
import java.util.stream.Collectors;

public class ClinicService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AuditService auditService;
    private final DiscountService discountService;
    private final AppointmentIdGenerator idGenerator;
    private final Map<String, TreeSet<Appointment>> doctorSchedule = new HashMap<>();

    public ClinicService(AppointmentRepository appointmentRepository,
                         DoctorRepository doctorRepository,
                         PatientRepository patientRepository,
                         AuditService auditService,
                         DiscountService discountService,
                         AppointmentIdGenerator idGenerator) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.auditService = auditService;
        this.discountService = discountService;
        this.idGenerator = idGenerator;

        appointmentRepository.findAll().stream()
                .filter(a -> !a.isCancelled())
                .forEach(a -> doctorSchedule
                        .computeIfAbsent(a.doctorId(), id -> new TreeSet<>())
                        .add(a));
    }

    public void registerPatient(Patient patient) {
        validatePesel(patient.pesel());
        Optional<Patient> existingPatient = patientRepository.findByPesel(patient.pesel());

        if (existingPatient.isPresent()) {
            throw new PatientAlreadyExistsException("Pacjent: " + patient.fullName()
                    + " o numerze PESEL: " + patient.pesel() + " już istnieje!");
        }

        patientRepository.save(patient);
        auditService.log("Dodano pacjenta: "
        + patient.fullName()
        + ", PESEL: " + patient.pesel());
    }

    public void registerAppointment(Appointment appointment) {
        Optional<Doctor> doctor = doctorRepository.findById(appointment.doctorId());
        if (doctor.isEmpty()) {
            throw new DoctorNotFoundException("Lekarz o numerze ID: "
                    + appointment.doctorId() + " nie istnieje!");
        }

        Optional<Patient> patient = patientRepository.findByPesel(appointment.patientPesel());
        if (patient.isEmpty()) {
            throw new PatientNotFoundException("Pacjent o numerze PESEL: "
                    + appointment.patientPesel() + " nie istnieje!");
        }

        TreeSet<Appointment> appointments =
                doctorSchedule.getOrDefault(
                        appointment.doctorId(),
                        new TreeSet<>());

        boolean appointmentsCollide = appointments.stream()
                .anyMatch(a -> a.dateTime().equals(appointment.dateTime()));

        if (appointmentsCollide) {
            throw new DoubleBookingException("Ten termin jest już zarezerwowany. Wybierz inny!");
        }

        double finalPrice = discountService.calculateFinalPrice(
                doctor.get(),
                patient.get(),
                appointment.dateTime()
        );

        String appointmentId = idGenerator.generateId(appointmentRepository.findAll());

        Appointment appointmentWithPrice = new Appointment(
                appointmentId,
                appointment.doctorId(),
                appointment.patientPesel(),
                appointment.dateTime(),
                appointment.isCancelled(),
                finalPrice
        );

        appointmentRepository.save(appointmentWithPrice);

        doctorSchedule
                .computeIfAbsent(appointment.doctorId(), id -> new TreeSet<>())
                        .add(appointmentWithPrice);
        auditService.log("Umówiono wizytę: " + appointmentId
        + ", lekarz: " + appointment.doctorId()
        + ", pacjent: " + appointment.patientPesel()
        + ", cena: " + finalPrice + " PLN");
    }

    public void cancelAppointment(String appointmentId) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);

        if (appointment.isEmpty()) {
            throw new AppointmentNotFoundException("Wizyta o ID: " + appointmentId + " nie istnieje!");
        }

        Appointment oldAppointment = appointment.get();

        Appointment cancelledAppointment = new Appointment(
                oldAppointment.appointmentId(),
                oldAppointment.doctorId(),
                oldAppointment.patientPesel(),
                oldAppointment.dateTime(),
                true,
                oldAppointment.price()
        );

        appointmentRepository.save(cancelledAppointment);
        auditService.log(" anulowano wizytę " + appointmentId);
    }

    public List<Appointment> patientAppointmentHistory(String pesel) {
        return appointmentRepository.findAll().stream()
                .filter(p -> p.patientPesel().equals(pesel))
                .filter(a -> !a.isCancelled())
                .sorted(Comparator.comparing(Appointment::dateTime).reversed())
                .toList();
    }

    public String mostExpensiveDoctors() {
        return doctorRepository.findAll().stream()
                .sorted(Comparator.comparing(Doctor::visitPrice).reversed())
                .limit(3)
                .map(Doctor::fullName)
                .collect(Collectors.joining(", "));
    }

    public Map<String, List<Doctor>> doctorsCatalogue() {
        return doctorRepository.findAll().stream()
                .collect(Collectors.groupingBy(Doctor::specialty));
    }

    public double doctorEarnings(String doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();

        return appointmentRepository.findAll().stream()
                .filter(a -> a.doctorId().equals(doctorId))
                .filter(a -> !a.isCancelled())
                .mapToDouble(Appointment::price)
                .sum();
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Doctor getDoctorById(String id) {
        return doctorRepository.findById(id).orElseThrow();
    }

    public void saveAll() {
        doctorRepository.saveAll();
        patientRepository.saveAll();
        appointmentRepository.saveAll();
    }

    private void validatePesel(String pesel) {
        if (pesel == null || !pesel.trim().matches("\\d{11}")) {
            throw new InvalidPeselException("PESEL nie może być pusty i musi zawierać 11 cyfr!");
        }
    }
}
