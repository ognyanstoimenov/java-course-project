package bg.sofia.uni.fmi.mjt.splitwise.server;

public class Debt {
    private String inDebtUser;
    private String owesToUser;

    private float owedAmount;

    public Debt(String inDebtUser, String owesToUser, float owedAmount) {
        this.inDebtUser = inDebtUser;
        this.owesToUser = owesToUser;
        this.owedAmount = owedAmount;
    }

    public String getInDebtUser() {
        return inDebtUser;
    }

    public String getOwesToUser() {
        return owesToUser;
    }

    public float getOwedAmount() {
        return owedAmount;
    }

    public void setOwedAmount(float owedAmount) {
        this.owedAmount = owedAmount;
        if (owedAmount < 0) {
            swapUsers();
        }
    }

    public void reimburse(String payer, float amount) {
        if (owedAmount < amount) {
            throw new IllegalArgumentException("Invalid amount.");
        }
    }

    private void swapUsers() {
        String temp = inDebtUser;
        inDebtUser = owesToUser;
        owesToUser = temp;
        owedAmount *= -1;
    }

}