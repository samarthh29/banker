package tellerapp.accountservice.dto;

public class BalanceUpdateRequest {
    private double newBalance;

    // Default constructor
    public BalanceUpdateRequest() {
    }

    // Getter and setter
    public double getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(double newBalance) {
        this.newBalance = newBalance;
    }
}