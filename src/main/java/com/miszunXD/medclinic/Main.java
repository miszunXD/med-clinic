package com.miszunXD.medclinic;

import com.miszunXD.medclinic.repository.AppointmentRepository;
import com.miszunXD.medclinic.repository.DoctorRepository;
import com.miszunXD.medclinic.repository.PatientRepository;
import com.miszunXD.medclinic.service.ClinicService;

public class Main {
    public static void main(String[] args) {
        DoctorRepository doctorRepository = new DoctorRepository();
        PatientRepository patientRepository = new PatientRepository();
        AppointmentRepository appointmentRepository = new AppointmentRepository();
        ClinicService clinicService = new ClinicService(appointmentRepository, doctorRepository, patientRepository);

        doctorRepository.findAll().forEach(System.out::println);
        patientRepository.findAll().forEach(System.out::println);
        appointmentRepository.findAll().forEach(System.out::println);

        System.out.println(clinicService.mostExpensiveDoctors());




    }
}
