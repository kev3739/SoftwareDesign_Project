# Lab Reservation System
EECS 3311 – Software Design

---

## Deliverable 2

### ⚠️ Important — Read Before Running

- Open the project at the **`LabReservationProject`** folder level, NOT at `SoftwareDesign_Project` or `Deliverable2`
- When entering login credentials, **type them manually** — do not copy/paste as extra spaces will cause login to fail
- Make sure you have **Java 17 or higher** installed

---

### How to Run

1. Clone or download the repository
2. Open your IDE (VS Code, IntelliJ, etc.)
3. **Open the folder at this level:**
```
SoftwareDesign_Project/Deliverable2/LabReservationProject
```
4. Navigate to:
```
src/Main.java
```
5. Right-click `Main.java` → click **Run Java** (VS Code) or **Run 'Main'** (IntelliJ)
6. The login window will open

---

### Login Credentials

> ⚠️ **Type these manually — do not copy/paste**

| Name | Email | Password | Role |
|---|---|---|---|
| James Carter | jcarter@yorku.ca | Strong@123 | Student |
| Priya Sharma | psharma@yorku.ca | Faculty@123 | Faculty |
| Darius Tehrani | dtehrani@yorku.ca | Research@123 | Researcher |
| Emily Brown | ebrown@yorku.ca | Guest@123 | Guest |
| Arjun Patel | apatel@yorku.ca | Manager@123 | Lab Manager |

> Lab Manager login unlocks the **Admin** tab for managing equipment and viewing all reservations.

---

### Project Structure

```
LabReservationProject/          ← Open your IDE at this level
├── data/                       ← CSV files (do not delete)
│   ├── users.csv
│   ├── equipment.csv
│   ├── reservations.csv
│   └── payments.csv
└── src/
    ├── Main.java               ← Run this
    ├── command/                ← Command pattern
    ├── data/                   ← CSV persistence layer
    ├── factory/                ← Factory pattern
    ├── gui/                    ← Swing GUI
    ├── model/                  ← Domain objects
    ├── service/                ← Service layer
    ├── strategy/               ← Strategy pattern
    └── util/                   ← Observer interfaces + SystemClock
```

---

### Design Patterns

| Pattern | Location |
|---|---|
| Singleton | `util/SystemClock.java`, `model/HeadLabCoordinator.java` |
| Factory | `factory/UserFactory.java` |
| Observer | `util/`, `service/LateArrivalObserver.java`, `service/SensorUpdateObserver.java` |
| State | `model/AvailableState.java`, `model/DisabledState.java`, `model/MaintenanceState.java` |
| Strategy | `strategy/` |
| Command | `command/` |

---

## Deliverable 3

### ⚠️ Important — Read Before Running

- Open the project at the **`New_LabReservationProject`** folder level
- Make sure you have **Java 21** and **Maven 3.6+** installed

---

### How to Run

1. Open your IDE at:
```
SoftwareDesign_Project/Deliverable3/New_LabReservationProject
```
2. Run the application:
```bash
mvn exec:java -Dexec.mainClass="Main"
```
3. The login window will open — use the same credentials as Deliverable 2

---

### Running Tests

All commands should be run from `Deliverable3/New_LabReservationProject/`.

**Run all tests (with JaCoCo coverage report):**
```bash
mvn clean test
```
Coverage report: `target/jacoco-report/index.html`

**Run only manual tests (Phase 1A/1B — 39 classes):**
```bash
mvn clean test -Pmanual-suite
```
Coverage report: `target/jacoco-manual/index.html`

**Run only Randoop-generated tests (Phase 2 — 3 files):**
```bash
mvn clean test -Prandoop-suite
```
Coverage report: `target/jacoco-randoop/index.html`

**Run only AI-assisted tests (Phase 3 — 37 classes):**
```bash
mvn clean test -Pai-suite
```
Coverage report: `target/jacoco-ai/index.html`

**Run PIT mutation testing:**
```bash
mvn clean test org.pitest:pitest-maven:mutationCoverage
```
Report: `target/pit-reports/index.html`

> Note: The build enforces a minimum of **50% line coverage**. It will fail if coverage drops below this threshold.

---

### Test Suite Summary

| Suite | Command Flag | Classes | Coverage Report |
|---|---|---|---|
| All tests | *(none)* | 79 | `target/jacoco-report/` |
| Manual (Phase 1A/1B) | `-Pmanual-suite` | 39 | `target/jacoco-manual/` |
| Randoop (Phase 2) | `-Prandoop-suite` | 3 | `target/jacoco-randoop/` |
| AI-assisted (Phase 3) | `-Pai-suite` | 37 | `target/jacoco-ai/` |
| Mutation testing | *(see command above)* | — | `target/pit-reports/` |
