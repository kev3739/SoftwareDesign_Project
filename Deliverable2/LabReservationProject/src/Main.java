import model.HeadLabCoordinator;
import model.LabManager;
import model.User;
import service.BillingService;
import service.RegistrationService;
import strategy.*;
import util.SystemClock;

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

        // === Strategy Pattern Demo ===
        System.out.println("\n=== Strategy Pattern: Pricing ===");
        BillingService billing = new BillingService();

        // Student: $10/hr
        billing.setPricingStrategy(new StudentPricingStrategy());
        System.out.println("Student - 3 hours: $" + billing.calculateFee(3));
        System.out.println("Student - Deposit: $" + billing.getDeposit());
        System.out.println("Student - Total with deposit applied: $" + billing.calculateFeeWithDeposit(3, false));
        System.out.println("Student - Total with deposit forfeited: $" + billing.calculateFeeWithDeposit(3, true));

        // Faculty: $15/hr
        billing.setPricingStrategy(new FacultyPricingStrategy());
        System.out.println("\nFaculty - 3 hours: $" + billing.calculateFee(3));
        System.out.println("Faculty - Deposit: $" + billing.getDeposit());
        System.out.println("Faculty - Total with deposit applied: $" + billing.calculateFeeWithDeposit(3, false));

        // Researcher: $20/hr
        billing.setPricingStrategy(new ResearcherPricingStrategy());
        System.out.println("\nResearcher - 3 hours: $" + billing.calculateFee(3));
        System.out.println("Researcher - Deposit: $" + billing.getDeposit());

        // Guest: $30/hr
        billing.setPricingStrategy(new GuestPricingStrategy());
        System.out.println("\nGuest - 3 hours: $" + billing.calculateFee(3));
        System.out.println("Guest - Deposit: $" + billing.getDeposit());

        // === Strategy Pattern: Payment Methods ===
        System.out.println("\n=== Strategy Pattern: Payment Methods ===");

        // Student pays with Institutional Account
        billing.setPricingStrategy(new StudentPricingStrategy());
        billing.setPaymentStrategy(new InstitutionalAccountPaymentStrategy());
        double studentFee = billing.calculateFeeWithDeposit(2, false);
        System.out.println("Student owes: $" + studentFee + " via " + billing.getPaymentMethodName());
        billing.processPayment(studentFee);

        // Faculty pays with Credit Card
        billing.setPricingStrategy(new FacultyPricingStrategy());
        billing.setPaymentStrategy(new CreditCardPaymentStrategy());
        double facultyFee = billing.calculateFeeWithDeposit(2, false);
        System.out.println("\nFaculty owes: $" + facultyFee + " via " + billing.getPaymentMethodName());
        billing.processPayment(facultyFee);

        // Researcher pays with Research Grant
        billing.setPricingStrategy(new ResearcherPricingStrategy());
        billing.setPaymentStrategy(new ResearchGrantPaymentStrategy());
        double researcherFee = billing.calculateFeeWithDeposit(2, false);
        System.out.println("\nResearcher owes: $" + researcherFee + " via " + billing.getPaymentMethodName());
        billing.processPayment(researcherFee);

        // Guest pays with Debit
        billing.setPricingStrategy(new GuestPricingStrategy());
        billing.setPaymentStrategy(new DebitPaymentStrategy());
        double guestFee = billing.calculateFeeWithDeposit(2, true); // deposit forfeited
        System.out.println("\nGuest (deposit forfeited) owes: $" + guestFee + " via " + billing.getPaymentMethodName());
        billing.processPayment(guestFee);
    }
}
