package pl.dmcs.blaszczyk.model.Request;

public class BillPaymentStatusRequest {
    private boolean isPaid;

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }
}
