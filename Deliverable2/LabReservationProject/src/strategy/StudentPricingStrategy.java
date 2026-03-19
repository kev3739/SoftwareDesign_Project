package strategy;

public class StudentPricingStrategy implements PricingStrategy {
    @Override
    public double getRate() {
        return 10.0;
    }
}
