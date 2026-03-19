package strategy;

public class ResearcherPricingStrategy implements PricingStrategy {
    @Override
    public double getRate() {
        return 20.0;
    }
}
