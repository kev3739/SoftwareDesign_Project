package strategy;

public class FacultyPricingStrategy implements PricingStrategy {
    @Override
    public double getRate() {
        return 15.0;
    }
}
