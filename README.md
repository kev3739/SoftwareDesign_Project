# EECS3311 Lab Equipment Reservation System

A Java Swing application for managing lab equipment reservations.

---

## Project Structure

```
SoftwareDesign_Project/
├── Deliverable2/
│   └── LabReservationProject/     — Original project (no build tool)
│       ├── src/                   — All Java source files (flat structure)
│       │   ├── Main.java
│       │   ├── command/
│       │   ├── data/
│       │   ├── factory/
│       │   ├── gui/
│       │   ├── model/
│       │   ├── service/
│       │   ├── strategy/
│       │   └── util/
│       └── data/                  — CSV data files (users, equipment, reservations, payments)
│
└── Deliverable3/
    └── New_LabReservationProject/ — Updated Maven project with tests
        ├── pom.xml
        ├── src/
        │   ├── main/java/         — Production source code
        │   └── test/java/
        │       ├── manual/        — Phase 1A/1B hand-written tests (39 classes)
        │       ├── ai_assisted/   — Phase 3 AI-assisted tests (37 classes)
        │       └── randoop/       — Phase 2 Randoop-generated tests (3 files)
        └── data/                  — Runtime CSV data files
```

---

## Deliverable 2 — Running the Project

### Prerequisites
- Java 8+
- No build tool required

### Compile

```bash
cd Deliverable2/LabReservationProject
mkdir -p bin
javac -d bin $(find src -name "*.java")
```

### Run

```bash
java -cp bin Main
```

This launches the **Login UI** (Swing GUI). Use the credentials from `data/users.csv` to log in.

---

## Deliverable 3 — Running the Project

### Prerequisites
- Java 21+
- Maven 3.6+

### Compile

```bash
cd Deliverable3/New_LabReservationProject
mvn clean compile
```

### Run the Application

```bash
mvn exec:java -Dexec.mainClass="Main"
```

Or build a JAR first and run it:

```bash
mvn clean package -DskipTests
java -cp target/lab-reservation-system-1.0-SNAPSHOT.jar Main
```

---

## Deliverable 3 — Running Tests

All commands should be run from `Deliverable3/New_LabReservationProject/`.

### Run all tests (with JaCoCo coverage)

```bash
mvn clean test
```

Coverage report: `target/jacoco-report/index.html`

> Note: The build enforces a minimum of **50% line coverage**. The build will fail if coverage drops below this threshold.

### Run only manual tests (Phase 1A/1B — 39 classes)

```bash
mvn clean test -Pmanual-suite
```

Coverage report: `target/jacoco-manual/index.html`

### Run only Randoop-generated tests (Phase 2 — 3 files)

```bash
mvn clean test -Prandoop-suite
```

Coverage report: `target/jacoco-randoop/index.html`

> Note: `ErrorTest0` contains intentional failure cases (Randoop bug-finding output) and is excluded from PIT mutation testing.

### Run only AI-assisted tests (Phase 3 — 37 classes)

```bash
mvn clean test -Pai-suite
```

Coverage report: `target/jacoco-ai/index.html`

### Run PIT mutation testing

```bash
mvn clean test org.pitest:pitest-maven:mutationCoverage
```

Report: `target/pit-reports/index.html`

---

## Test Suite Summary

| Suite | Command Flag | Classes | Coverage Report |
|-------|-------------|---------|-----------------|
| All tests | *(none)* | 79 | `target/jacoco-report/` |
| Manual (Phase 1A/1B) | `-Pmanual-suite` | 39 | `target/jacoco-manual/` |
| Randoop (Phase 2) | `-Prandoop-suite` | 3 | `target/jacoco-randoop/` |
| AI-assisted (Phase 3) | `-Pai-suite` | 37 | `target/jacoco-ai/` |
| Mutation testing | *(see command above)* | — | `target/pit-reports/` |

---

## Design Patterns Implemented

| Pattern | Location |
|---------|----------|
| Command | `command/` |
| Factory | `factory/` |
| Observer | `service/`, `util/` |
| State | `model/` (EquipmentState, AvailableState, MaintenanceState, DisabledState) |
| Strategy | `strategy/` (pricing and payment strategies) |
