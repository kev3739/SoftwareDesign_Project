import model.Equipment;
import model.EquipmentSensorSystem;
import model.HeadLabCoordinator;
import model.LabManager;
import model.Reservation;
import model.UsageRecord;
import model.User;
import service.BillingService;
import service.LateArrivalObserver;
import service.RegistrationService;
import service.SensorUpdateObserver;
import strategy.*;
import util.SystemClock;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        RegistrationService registrationService = new RegistrationService();

        User student = registrationService.registerUser(
                "student",
                "Ali",
                "ali@example.com",
                "Strong@123",
                "123456789",
                true
        );

        System.out.println("Created user type: " + student.getUserType());
        System.out.println("Hourly rate: " + student.getHourlyRate());

        SystemClock c1 = SystemClock.getInstance();
        SystemClock c2 = SystemClock.getInstance();
        System.out.println("Same SystemClock instance? " + (c1 == c2));
        System.out.println("Current time: " + c1.now());

        HeadLabCoordinator h1 = HeadLabCoordinator.getInstance();
        HeadLabCoordinator h2 = HeadLabCoordinator.getInstance();
        System.out.println("Same HeadLabCoordinator instance? " + (h1 == h2));

        LabManager manager = h1.createLabManagerAccount(
                "Manager1",
                "manager@example.com",
                "Admin@123",
                "LM001"
        );

        System.out.println("Created manager type: " + manager.getUserType());
        System.out.println("Manager ID: " + manager.getManagerId());

        // === Observer Pattern Demo ===
        System.out.println("\n=== Observer Pattern: Late Arrival & Sensor ===");
        Equipment microscope = new Equipment("EQ001", "Microscope", "Lab A");
        Reservation reservation = new Reservation(
                "R001",
                student,
                microscope,
                LocalDateTime.now().minusMinutes(30),
                LocalDateTime.now().plusHours(1),
                student.getHourlyRate()
        );

        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);

        LateArrivalObserver lateArrivalObserver = new LateArrivalObserver(reservations);
        c1.attach(lateArrivalObserver);
        c1.tick();

        EquipmentSensorSystem sensorSystem = new EquipmentSensorSystem();
        SensorUpdateObserver sensorUpdateObserver = new SensorUpdateObserver();
        sensorSystem.attach(sensorUpdateObserver);

        sensorSystem.sendStatusUpdate(microscope, "IN_USE");

        UsageRecord usageRecord = new UsageRecord(
                "UR001",
                LocalDateTime.now().minusMinutes(40),
                LocalDateTime.now(),
                "Completed Successfully"
        );

        sensorSystem.sendUsageUpdate(microscope, usageRecord);

        // === State Pattern Demo ===
        System.out.println("\n=== State Pattern: Equipment Lifecycle ===");
        Equipment microscope2 = manager.addEquipment(
                "EQ002",
                "Digital Microscope",
                "Lab A"
        );

        System.out.println("Equipment ID: " + microscope2.getEquipmentId());
        System.out.println("Description: " + microscope2.getDescription());
        System.out.println("Location: " + microscope2.getLabLocation());
        System.out.println("Initial state: " + microscope2.getCurrentStateName());
        System.out.println("Available? " + microscope2.isAvailable());

        manager.disableEquipment(microscope2);
        System.out.println("After disable: " + microscope2.getCurrentStateName());
        System.out.println("Available? " + microscope2.isAvailable());

        manager.enableEquipment(microscope2);
        System.out.println("After enable: " + microscope2.getCurrentStateName());
        System.out.println("Available? " + microscope2.isAvailable());

        manager.markEquipmentUnderMaintenance(microscope2);
        System.out.println("After maintenance: " + microscope2.getCurrentStateName());
        System.out.println("Available? " + microscope2.isAvailable());

        manager.enableEquipment(microscope2);
        System.out.println("After maintenance finished: " + microscope2.getCurrentStateName());
        System.out.println("Available? " + microscope2.isAvailable());

        // === Strategy Pattern Demo ===
        System.out.println("\n=== Strategy Pattern: Pricing ===");
        BillingService billing = new BillingService();

        billing.setPricingStrategy(new StudentPricingStrategy());
        System.out.println("Student - 3 hours: $" + billing.calculateFee(3));
        System.out.println("Student - Deposit: $" + billing.getDeposit());
        System.out.println("Student - Total with deposit applied: $" + billing.calculateFeeWithDeposit(3, false));
        System.out.println("Student - Total with deposit forfeited: $" + billing.calculateFeeWithDeposit(3, true));

        billing.setPricingStrategy(new FacultyPricingStrategy());
        System.out.println("\nFaculty - 3 hours: $" + billing.calculateFee(3));
        System.out.println("Faculty - Deposit: $" + billing.getDeposit());
        System.out.println("Faculty - Total with deposit applied: $" + billing.calculateFeeWithDeposit(3, false));

        billing.setPricingStrategy(new ResearcherPricingStrategy());
        System.out.println("\nResearcher - 3 hours: $" + billing.calculateFee(3));
        System.out.println("Researcher - Deposit: $" + billing.getDeposit());

        billing.setPricingStrategy(new GuestPricingStrategy());
        System.out.println("\nGuest - 3 hours: $" + billing.calculateFee(3));
        System.out.println("Guest - Deposit: $" + billing.getDeposit());

        // === Strategy Pattern: Payment Methods ===
        System.out.println("\n=== Strategy Pattern: Payment Methods ===");

        billing.setPricingStrategy(new StudentPricingStrategy());
        billing.setPaymentStrategy(new InstitutionalAccountPaymentStrategy());
        double studentFee = billing.calculateFeeWithDeposit(2, false);
        System.out.println("Student owes: $" + studentFee + " via " + billing.getPaymentMethodName());
        billing.processPayment(studentFee);

        billing.setPricingStrategy(new FacultyPricingStrategy());
        billing.setPaymentStrategy(new CreditCardPaymentStrategy());
        double facultyFee = billing.calculateFeeWithDeposit(2, false);
        System.out.println("\nFaculty owes: $" + facultyFee + " via " + billing.getPaymentMethodName());
        billing.processPayment(facultyFee);

        billing.setPricingStrategy(new ResearcherPricingStrategy());
        billing.setPaymentStrategy(new ResearchGrantPaymentStrategy());
        double researcherFee = billing.calculateFeeWithDeposit(2, false);
        System.out.println("\nResearcher owes: $" + researcherFee + " via " + billing.getPaymentMethodName());
        billing.processPayment(researcherFee);

        billing.setPricingStrategy(new GuestPricingStrategy());
        billing.setPaymentStrategy(new DebitPaymentStrategy());
        double guestFee = billing.calculateFeeWithDeposit(2, true);
        System.out.println("\nGuest (deposit forfeited) owes: $" + guestFee + " via " + billing.getPaymentMethodName());
        billing.processPayment(guestFee);
    }
}