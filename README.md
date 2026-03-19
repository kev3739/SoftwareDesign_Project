# Lab Reservation System
EECS 3311 – Software Design | Latest Update: Deliverable 2

---

## How to Run

1. Clone or download the repository
2. Open the project in VS Code or IntelliJ
3. Navigate to:
```
Deliverable2/LabReservationProject/src/Main.java
```
4. Right-click `Main.java` and click **Run Java** (VS Code) or **Run 'Main'** (IntelliJ)
5. The login window will open

> Make sure you have **Java 17 or higher** installed before running.

---

## Login Credentials

| Name | Email | Password | Role |
|---|---|---|---|
| James Carter | jcarter@yorku.ca | Strong@123 | Student |
| Priya Sharma | psharma@yorku.ca | Faculty@123 | Faculty |
| Darius Tehrani | dtehrani@yorku.ca | Research@123 | Researcher |
| Emily Brown | ebrown@yorku.ca | Guest@123 | Guest |
| Arjun Patel | apatel@yorku.ca | Manager@123 | Lab Manager |

---

## Project Structure

```
SoftwareDesign_Project/
└── Deliverable2/
    └── LabReservationProject/
        ├── data/                   ← CSV files (users, equipment, reservations, payments)
        └── src/
            ├── Main.java           ← Entry point, run this
            ├── command/            ← Command pattern
            │   ├── ReservationCommand.java
            │   ├── CommandManager.java
            │   ├── MakeReservationCommand.java
            │   ├── CancelReservationCommand.java
            │   ├── ModifyReservationCommand.java
            │   └── ExtendReservationCommand.java
            ├── data/               ← CSV persistence
            │   ├── MaintainUser.java
            │   ├── MaintainEquipment.java
            │   ├── MaintainReservation.java
            │   ├── MaintainPayment.java
            │   └── StubUser.java
            ├── factory/            ← Factory pattern
            │   └── UserFactory.java
            ├── gui/                ← Swing GUI
            │   ├── LoginUI.java
            │   ├── MainDashboardUI.java
            │   ├── ReservationListPanel.java
            │   ├── MakeReservationPanel.java
            │   ├── EquipmentPanel.java
            │   └── AdminPanel.java
            ├── model/              ← Domain objects
            │   ├── User.java
            │   ├── Student.java
            │   ├── Faculty.java
            │   ├── Researcher.java
            │   ├── Guest.java
            │   ├── LabManager.java
            │   ├── Equipment.java
            │   ├── EquipmentState.java
            │   ├── AvailableState.java
            │   ├── DisabledState.java
            │   ├── MaintenanceState.java
            │   ├── Reservation.java
            │   ├── Payment.java
            │   └── UsageRecord.java
            ├── service/            ← Service layer
            │   ├── ReservationService.java
            │   ├── BillingService.java
            │   ├── RegistrationService.java
            │   ├── LateArrivalObserver.java
            │   └── SensorUpdateObserver.java
            ├── strategy/           ← Strategy pattern
            │   ├── PricingStrategy.java
            │   ├── PaymentStrategy.java
            │   ├── StudentPricingStrategy.java
            │   ├── FacultyPricingStrategy.java
            │   ├── ResearcherPricingStrategy.java
            │   ├── GuestPricingStrategy.java
            │   ├── CreditCardPaymentStrategy.java
            │   ├── DebitPaymentStrategy.java
            │   ├── InstitutionalAccountPaymentStrategy.java
            │   └── ResearchGrantPaymentStrategy.java
            └── util/               ← Observer interfaces + Singleton clock
                ├── Observer.java
                ├── Subject.java
                └── SystemClock.java
```

---

## Design Patterns

| Pattern | Location |
|---|---|
| Singleton | `util/SystemClock.java`, `model/HeadLabCoordinator.java` |
| Factory | `factory/UserFactory.java` |
| Observer | `util/`, `service/LateArrivalObserver.java`, `service/SensorUpdateObserver.java` |
| State | `model/AvailableState.java`, `model/DisabledState.java`, `model/MaintenanceState.java` |
| Strategy | `strategy/` |
| Command | `command/` |
