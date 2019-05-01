package pl.dmcs.blaszczyk.model.Utils;

public class ApiError {
    private String errorMessage;

    public ApiError(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
