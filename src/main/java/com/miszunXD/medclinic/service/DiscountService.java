package com.miszunXD.medclinic.service;

import com.miszunXD.medclinic.model.Doctor;
import com.miszunXD.medclinic.model.Patient;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DiscountService {
    private final Map<String, Function<Double, Double>> discounts;
    private static final int SENIOR_YEAR_LIMIT = 1966;

    public DiscountService() {
        discounts = new HashMap<>();

        discounts.put("SENIOR", price -> price * 0.8);
        discounts.put("CARDIO_WEDNESDAY", price -> price - 50.0);
    }

    public double applyDiscount(String discountName, double price) {
        Function<Double, Double> discount = discounts.get(discountName);

        if (discount == null) {
            return price;
        }

        return discount.apply(price);
    }

    public double calculateFinalPrice(Doctor doctor, Patient patient, LocalDateTime dateTime) {
        double price = doctor.visitPrice();
        int birthYear = getBirthYear(patient.pesel());
        boolean isSenior = birthYear < SENIOR_YEAR_LIMIT;

        if (isSenior) {
            price = applyDiscount("SENIOR", price);
        }

        if (doctor.specialty().equals("Kardiolog") && dateTime.getDayOfWeek() == DayOfWeek.WEDNESDAY) {
            price = applyDiscount("CARDIO_WEDNESDAY", price);
        }

        return price;
    }

    public int getBirthYear(String pesel) {
        int year = Integer.parseInt(pesel.substring(0, 2));

        if (year <= 26) {
            return 2000 + year;
        }

        return 1900 + year;
    }
}
