
import java.util.List;
public class Invoices {

    private List<Invoice> invoices;

    // Constructor
    public Invoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }

    // Getter method
    public List<Invoice> getInvoices() {
        return invoices;
    }
}
