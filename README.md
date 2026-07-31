# MED-CLINIC 🏥

Aplikacja konsolowa do zarządzania kliniką medyczną napisana w języku Java.

Projekt symuluje podstawowy system obsługi kliniki: zarządzanie lekarzami, pacjentami, wizytami oraz generowanie prostych raportów biznesowych.

## Technologie

- Java 17
- Maven
- Jackson (JSON serialization/deserialization)
- Stream API
- LocalDateTime
- Git / GitHub

## Architektura projektu

Aplikacja została podzielona na warstwy zgodnie z zasadami separacji odpowiedzialności:

```
src/main/java/com/miszunXD/medclinic

├── model
│   ├── Doctor
│   ├── Patient
│   └── Appointment
│
├── repository
│   ├── DoctorRepository
│   ├── PatientRepository
│   └── AppointmentRepository
│
├── service
│   ├── ClinicService
│   ├── DiscountService
│   ├── AuditService
│   └── AppointmentIdGenerator
│
├── exception
│   └── Custom Exceptions
│
└── ui
    └── Menu
```

## Funkcjonalności

### 👨‍⚕️ Zarządzanie lekarzami

- wyświetlanie listy lekarzy
- wyszukiwanie lekarza po ID
- katalog lekarzy według specjalizacji
- raport trzech najdroższych lekarzy

### 👤 Zarządzanie pacjentami

- rejestracja nowych pacjentów
- sprawdzanie unikalności numeru PESEL
- walidacja poprawności PESEL
- wyświetlanie listy pacjentów

### 📅 Obsługa wizyt

- rezerwacja wizyt
- automatyczne generowanie ID wizyty
- sprawdzanie dostępności lekarza
- blokowanie podwójnych rezerwacji
- anulowanie wizyt
- historia wizyt pacjenta

### 💰 System cen i rabatów

Aplikacja posiada system wyliczania końcowej ceny wizyty.

Uwzględniane są między innymi:
- rabaty pacjentów
- specjalne zasady cenowe
- końcowa cena zapisywana przy wizycie

### 📊 Raporty biznesowe

Wykorzystując Stream API aplikacja generuje:

- historię wizyt pacjenta
- katalog lekarzy według specjalizacji
- ranking najdroższych lekarzy
- sumę przychodów wybranego lekarza

## Przechowywanie danych

Dane aplikacji przechowywane są w plikach JSON:

```
resources/
├── doctors.json
├── patients.json
└── appointments.json
```

Przy uruchomieniu aplikacja ładuje dane do pamięci, a przy zamknięciu zapisuje aktualny stan.

## System harmonogramów lekarzy

Dla zarządzania terminami wykorzystano strukturę:

```
Map<String, TreeSet<Appointment>>
```

Każdy lekarz posiada własny uporządkowany harmonogram wizyt.

`TreeSet` automatycznie sortuje wizyty według daty oraz godziny dzięki implementacji `Comparable` w klasie `Appointment`.

## Audyt zmian

Każda ważna operacja systemowa jest zapisywana w dzienniku audytu:

- dodanie pacjenta
- rezerwacja wizyty
- anulowanie wizyty

Przykład:

```
Dodano pacjenta: Jan Kowalski
Umówiono wizytę: A-108
Anulowano wizytę: A-102
```

## Uruchomienie projektu

### Wymagania

- Java 17+
- Maven

### Uruchomienie

Sklonuj repozytorium:

```bash
git clone <repository-url>
```

Przejdź do katalogu projektu:

```bash
cd med-clinic
```

Uruchom aplikację:

```bash
mvn clean compile
```

Następnie uruchom klasę:

```
Main.java
```

## Przykładowe menu

```
=== MED CLINIC ===

1. Wyświetl wszystkich lekarzy
2. Wyświetl wszystkich pacjentów
3. Wyświetl wszystkie wizyty
4. Dodaj pacjenta
5. Zarezerwuj wizytę
6. Anuluj wizytę
7. Historia wizyt pacjenta
8. Wyświetl trzech najdroższych lekarzy
9. Lista lekarzy wg specjalizacji
10. Łączny przychód wybranego lekarza
0. Wyjście i zapis danych
```
