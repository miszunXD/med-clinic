# 🏥 MED-CLINIC

Aplikacja konsolowa do zarządzania kliniką medyczną napisana w języku Java.

Projekt symuluje podstawowy system obsługi kliniki: zarządzanie lekarzami, pacjentami oraz wizytami. Aplikacja wykorzystuje architekturę warstwową, przechowuje dane w plikach JSON oraz posiada podstawowe mechanizmy walidacji, raportowania i kontroli terminów.

---

## 🚀 Funkcjonalności

### 👨‍⚕️ Lekarze

- Wyświetlanie listy lekarzy
- Wyszukiwanie lekarza po ID
- Katalog lekarzy według specjalizacji
- Raport trzech najdroższych lekarzy
- Obliczanie przychodu wybranego lekarza

### 👤 Pacjenci

- Dodawanie nowych pacjentów
- Sprawdzanie unikalności numeru PESEL
- Walidacja poprawności PESEL
- Wyświetlanie listy pacjentów

### 📅 Wizyty

- Rezerwacja wizyt
- Automatyczne generowanie ID wizyty
- Sprawdzanie dostępności lekarza
- Blokowanie podwójnej rezerwacji terminu
- Anulowanie wizyt
- Historia wizyt pacjenta
- Sortowanie harmonogramów lekarzy według daty

### 💰 System zniżek

Aplikacja posiada system wyliczania końcowej ceny wizyty.

Obsługiwane są:
- rabaty dla pacjentów
- specjalne zasady cenowe
- wyliczanie końcowej ceny przed zapisaniem wizyty

### 📝 Audyt zmian

Każda ważna operacja jest zapisywana w dzienniku audytu:

- dodanie pacjenta
- rezerwacja wizyty
- anulowanie wizyty

---

# 🏗️ Architektura projektu

Projekt został podzielony na warstwy zgodnie z zasadami separacji odpowiedzialności:

```
src/main/java/com/miszunXD/medclinic

├── model
│   ├── Appointment
│   ├── Doctor
│   └── Patient
│
├── repository
│   ├── CrudRepository
│   ├── AbstractJsonRepository
│   ├── AppointmentRepository
│   ├── DoctorRepository
│   └── PatientRepository
│
├── service
│   ├── ClinicService
│   ├── DiscountService
│   ├── AuditService
│   └── AppointmentIdGenerator
│
├── exception
│   ├── AppointmentNotFoundException
│   ├── DoctorNotFoundException
│   ├── DoubleBookingException
│   ├── InvalidDateException
│   ├── InvalidPeselException
│   ├── PatientAlreadyExistsException
│   └── PatientNotFoundException
│
├── ui
│   └── Menu
│
└── Main
```

---

# 📦 Technologie

- Java 17
- Maven
- Jackson Databind
- Jackson JavaTimeModule
- JSON jako baza danych
- Stream API
- Git / GitHub

---

# 🧩 Opis warstw

## Model

Warstwa zawierająca obiekty domenowe aplikacji:

- `Doctor`
- `Patient`
- `Appointment`

Modele zostały przygotowane jako rekordy Java oraz posiadają poprawną obsługę `equals()` i `hashCode()`.

---

## Repository

Warstwa odpowiedzialna za komunikację z plikami JSON.

Zastosowano generyczne repozytorium:

- `CrudRepository<T, ID>`
- `AbstractJsonRepository<T>`

Dzięki temu logika odczytu i zapisu danych jest współdzielona pomiędzy różnymi modelami.

Obsługiwane dane:

```
resources/

├── doctors.json
├── patients.json
└── appointments.json
```

---

## Service

Warstwa zawierająca logikę biznesową aplikacji.

Najważniejsza klasa:

`ClinicService`

Odpowiada za:

- rejestrację pacjentów
- obsługę wizyt
- walidację danych
- kontrolę konfliktów terminów
- generowanie raportów
- współpracę z systemem rabatowym i audytem

---

## UI

Warstwa odpowiedzialna za komunikację z użytkownikiem.

Klasa:

`Menu`

Obsługuje:

- menu konsolowe
- pobieranie danych od użytkownika
- wyświetlanie informacji
- wywoływanie operacji systemowych

---

# 🌳 Harmonogram wizyt lekarzy

Do zarządzania terminami wykorzystano strukturę:

```java
Map<String, TreeSet<Appointment>>
```

Każdy lekarz posiada własny uporządkowany zbiór wizyt.

`TreeSet` automatycznie sortuje wizyty według daty i godziny dzięki implementacji `Comparable` w klasie `Appointment`.

Pozwala to na:

- szybkie sprawdzanie zajętych terminów
- utrzymanie kolejności wizyt
- uniknięcie duplikatów

---

# 📊 Stream API

Raporty biznesowe zostały wykonane przy użyciu Stream API.

Aplikacja generuje:

- historię wizyt pacjenta
- ranking najdroższych lekarzy
- katalog lekarzy według specjalizacji
- sumę przychodów lekarza

Przykładowo:

```java
appointmentRepository.findAll()
        .stream()
        .filter(...)
        .mapToDouble(...)
        .sum();
```

---

# ▶️ Uruchomienie projektu

## Wymagania

- Java 17+
- Maven

## Instalacja

Sklonuj repozytorium:

```bash
git clone <repository-url>
```

Przejdź do katalogu projektu:

```bash
cd med-clinic
```

Zbuduj projekt:

```bash
mvn clean compile
```

Następnie uruchom klasę:

```
Main.java
```

---

# 🖥️ Przykładowe menu

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

---
