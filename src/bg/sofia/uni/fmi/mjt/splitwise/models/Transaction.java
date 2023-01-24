package bg.sofia.uni.fmi.mjt.splitwise.models;

import bg.sofia.uni.fmi.mjt.splitwise.utils.DateTimeUtils;

import java.time.LocalDateTime;

public class Transaction {
    private final String payer;

    private final float amount;

    private final String comment;

    private final String dateTime;

    private final String reimburseTo;

    public Transaction(String payer, float amount, String comment) {
        this(payer, amount, comment, null);
    }

    public Transaction(String payer, float amount, String comment, String reimburseTo) {
        this.payer = payer;
        this.amount = amount;
        this.comment = comment;
        this.reimburseTo = reimburseTo;
        dateTime = DateTimeUtils.dateToString(LocalDateTime.now());
    }

    public LocalDateTime getDateTime() {
        return DateTimeUtils.stringToDate(dateTime);
    }

    public String getReimburseTo() {
        return reimburseTo;
    }

    public String getPayer() {
        return payer;
    }

    public float getAmount() {
        return amount;
    }

    public String getComment() {
        return comment;
    }
}
