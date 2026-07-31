package com.miszunXD.medclinic;

import com.miszunXD.medclinic.exception.*;
import com.miszunXD.medclinic.repository.AppointmentRepository;
import com.miszunXD.medclinic.repository.DoctorRepository;
import com.miszunXD.medclinic.repository.PatientRepository;
import com.miszunXD.medclinic.service.AppointmentIdGenerator;
import com.miszunXD.medclinic.service.AuditService;
import com.miszunXD.medclinic.service.ClinicService;
import com.miszunXD.medclinic.service.DiscountService;
import com.miszunXD.medclinic.ui.Menu;

public class Main {
    public static void main(String[] args) {
        ClinicService clinicService = createClinicService();

        Menu menu = new Menu(clinicService);
        menu.start();
    }

    private static ClinicService createClinicService() {
        AppointmentRepository appointmentRepository = new AppointmentRepository();
        DoctorRepository doctorRepository = new DoctorRepository();
        PatientRepository patientRepository = new PatientRepository();
        AuditService auditService = new AuditService();
        DiscountService discountService = new DiscountService();
        AppointmentIdGenerator idGenerator = new AppointmentIdGenerator();

        return new ClinicService(
                appointmentRepository,
                doctorRepository,
                patientRepository,
                auditService,
                discountService,
                idGenerator
        );
    }
}