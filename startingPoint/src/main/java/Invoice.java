import java.util.List;

public class Invoice {
    private String customer;
    private List<Performance> performances;

    // Constructor
    public Invoice(String customer, List<Performance> performances) {
        this.customer = customer;
        this.performances = performances;
    }

    // Getter methods
    public String getCustomer() {
        return customer;
    }

    public List<Performance> getPerformances() {
        return performances;
    }
}
