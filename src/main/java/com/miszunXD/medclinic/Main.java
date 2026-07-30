package com.miszunXD.medclinic;

import com.miszunXD.medclinic.exception.AppointmentNotFoundException;
import com.miszunXD.medclinic.exception.DoubleBookingException;
import com.miszunXD.medclinic.exception.InvalidDateException;
import com.miszunXD.medclinic.exception.PatientAlreadyExistsException;
import com.miszunXD.medclinic.model.Appointment;
import com.miszunXD.medclinic.model.Doctor;
import com.miszunXD.medclinic.model.Patient;
import com.miszunXD.medclinic.repository.AppointmentRepository;
import com.miszunXD.medclinic.repository.DoctorRepository;
import com.miszunXD.medclinic.repository.PatientRepository;
import com.miszunXD.medclinic.service.AppointmentIdGenerator;
import com.miszunXD.medclinic.service.AuditService;
import com.miszunXD.medclinic.service.ClinicService;
import com.miszunXD.medclinic.service.DiscountService;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AppointmentRepository appointmentRepository = new AppointmentRepository();
        DoctorRepository doctorRepository = new DoctorRepository();
        PatientRepository patientRepository = new PatientRepository();
        AuditService auditService = new AuditService();
        DiscountService discountService = new DiscountService();
        AppointmentIdGenerator idGenerator = new AppointmentIdGenerator();

        ClinicService clinicService = new ClinicService(
                appointmentRepository,
                doctorRepository,
                patientRepository,
                auditService,
                discountService,
                idGenerator
                );

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("=== MED CLINIC ===");
            System.out.println("Wybierz wskazaną opcję, używająć klawiatury numerycznej");
            System.out.println("1. Wyświetl wszystkich lekarzy");
            System.out.println("2. Wyświetl wszystkich pacjentów");
            System.out.println("3. Wyświetl wszystkie wizyty");
            System.out.println("4. Dodaj pacjenta");
            System.out.println("5. Zarezerwuj wizytę");
            System.out.println("6. Anuluj wizytę");
            System.out.println("7. Historia wizyt pacjenta");
            System.out.println("8. Wyświetl trzech najdroższych lekarzy");
            System.out.println("9. Lista lekarzy wg specjalizacji");
            System.out.println("10. Łączny przychód wybranego lekarza");
            System.out.println("0. Wyjście z programu i zapis plików");

            int choice = readInt(scanner);

            switch (choice) {
                case 1 -> printDoctors(clinicService);
                case 2 -> printPatients(clinicService);
                case 3 -> printAppointments(clinicService);
                case 4 -> registerPatient(scanner, clinicService);
                case 5 -> registerAppointment(scanner, clinicService);
                case 6 -> cancelAppointment(scanner, clinicService);
                case 7 -> printPatientHistory(scanner, clinicService);
                case 8 -> printThreeMostExpensiveDoctors(clinicService);
                case 9 -> printDoctorsCatalogue(clinicService);
                case 10 -> printDoctorEarnings(scanner, clinicService);
                case 0 -> {
                    saveAndExit(clinicService);
                    running = false;
                }
                default -> System.out.println("Nieznane działanie!");
            }
        }
    }

    private static void printAppointments(ClinicService clinicService) {
        System.out.println("=== LISTA WIZYT===");

        clinicService.getAllAppointments().forEach(a -> System.out.println(
                a.appointmentId() + " | " +
                        a.doctorId() + " | " +
                        a.patientPesel() + " | " +
                        a.dateTime() + " | Czy wizyta jest anulowana: " +
                        a.isCancelled() + " | " +
                        a.price() + " PLN"
        ));
    }

    private static void printPatients(ClinicService clinicService) {
        System.out.println("===LISTA PACJENTÓW===");

        clinicService.getAllPatients().forEach(p -> System.out.println(
                p.pesel() + " | " +
                        p.fullName() + " | " +
                        p.phoneNumber()
        ));
    }

    private static void printDoctors(ClinicService clinicService) {
        System.out.println("=== LISTA LEKARZY ===");
        clinicService.getAllDoctors().forEach(d -> System.out.println(
                        d.id() + " | " +
                                d.fullName() + " | " +
                                d.specialty() + " | " +
                                d.visitPrice() + " PLN "
                ));
    }

    private static void registerPatient(Scanner scanner, ClinicService clinicService) {
        System.out.println("Podaj PESEL pacjenta: ");
        String pesel = scanner.nextLine().trim();
        System.out.println("Podaj imię i nazwisko pacjenta: ");
        String fullName = scanner.nextLine().trim();
        System.out.println("Podaj numer telefonu pacjenta: ");
        String phoneNumber = scanner.nextLine().trim();

        try {
            clinicService.registerPatient(new Patient(pesel, fullName, phoneNumber));
            System.out.println("Pacjent dodany!");
        } catch (PatientAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void registerAppointment(Scanner scanner, ClinicService clinicService) {

        System.out.println("Podaj ID lekarza: ");
        String doctorId = scanner.nextLine();

        System.out.println("Podaj PESEL pacjenta: ");
        String patientPesel = scanner.nextLine();

        LocalDateTime dateTime;
        try {
            System.out.println("Podaj datę wizyty (format: RRRR-MM-DD): ");
            String date = scanner.nextLine();

            System.out.println("Podaj godzinę wizyty (format HH:MM): ");
            String time = scanner.nextLine();
            dateTime = LocalDateTime.parse(date + "T" + time);
        } catch (DateTimeException e) {
            System.out.println("Niepoprawny format daty lub godziny!");
            return;
        }

        Appointment appointment = new Appointment(
                null,
                doctorId,
                patientPesel,
                dateTime,
                false,
                0
        );

        try {
            clinicService.registerAppointment(appointment);
            System.out.println("Wizyta poprawnie umówiona!");
        } catch (DoubleBookingException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void cancelAppointment(Scanner scanner, ClinicService clinicService) {
        System.out.println("Podaj ID wizyty, którą chcesz anulować: ");
        String appointmentId = scanner.nextLine().trim();

        try {
            clinicService.cancelAppointment(appointmentId);
            System.out.println("Wizyty anulowana!");
        } catch (AppointmentNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void printPatientHistory(Scanner scanner, ClinicService clinicService) {
        System.out.println("Podaj PESEL pacjenta, którego historię wizyt chcesz sprawdzić: ");
        String pesel = scanner.nextLine();

        List<Appointment> history = clinicService.patientAppointmentHistory(pesel);

        if (history.isEmpty()) {
            System.out.println("Brak historii dla tego numeru PESEL");
            return;
        }

        System.out.println("===LISTA WIZYT PACJENTA===");
        history.forEach(a -> System.out.println(
                a.appointmentId() + " | " +
                        a.doctorId() + " | " +
                        a.patientPesel() + " | " +
                        a.dateTime() + " | "
        ));
    }

    private static void printThreeMostExpensiveDoctors(ClinicService clinicService) {
        System.out.println("=== TRZECH NAJDROŻSZYCH LEKARZY===");
        System.out.println(clinicService.mostExpensiveDoctors());
    }

    private static void printDoctorsCatalogue(ClinicService clinicService) {
        System.out.println("===LISTA LEKARZY WG SPECJALIZACJI===");
        clinicService.doctorsCatalogue().forEach((specialty, doctor) -> {
            System.out.println("===" + specialty + "===");

            doctor.forEach(d -> System.out.println(
                    d.id() + " | " +
                            d.fullName() + " | " +
                            d.visitPrice() + " PLN"
            ));
        });
    }

    private static void printDoctorEarnings(Scanner scanner, ClinicService clinicService) {
        System.out.println("Podaj ID lekarza dla którego chcesz sprawdzić łączny przychód: ");
        String doctorId = scanner.nextLine();

        Doctor doctor = clinicService.getDoctorById(doctorId);
        double earnings = clinicService.doctorEarnings(doctorId);

        System.out.println(
                "Lekarz: " + doctor.fullName() + " | " +
                        "Specjalizacja: " + doctor.specialty() + " | " +
                        "Łączny przychód: " + earnings + " PLN"
        );
    }

    private static void saveAndExit(ClinicService clinicService) {
        clinicService.saveAll();
        System.out.println("Pliki zapisane. Do widzenia!");
    }

    private static int readInt(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Podaj liczbę!");
            scanner.nextLine();
        }

        int number = scanner.nextInt();
        scanner.nextLine();

        return number;
    }

}
