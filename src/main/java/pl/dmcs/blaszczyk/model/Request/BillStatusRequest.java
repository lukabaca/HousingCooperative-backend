package pl.dmcs.blaszczyk.model.Request;

public class BillStatusRequest {
    private boolean isAccepted;
    public boolean isAccepted() {
        return isAccepted;
    }
    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }
}
