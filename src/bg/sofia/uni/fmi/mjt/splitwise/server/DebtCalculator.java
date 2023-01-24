package bg.sofia.uni.fmi.mjt.splitwise.server;

import bg.sofia.uni.fmi.mjt.splitwise.models.Group;
import bg.sofia.uni.fmi.mjt.splitwise.models.Transaction;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class DebtCalculator {
    public static Set<Debt> calculateDebts(Group group) {
        Set<Debt> debts = new HashSet<>();
        for (Transaction transaction : group.getTransactions()) {
            if (transaction.getReimburseTo() != null) {
                Debt debt = findDebt(transaction.getPayer(), transaction.getReimburseTo(), debts);
                if (debt.getInDebtUser().equals(transaction.getPayer())) {
                    debt.setOwedAmount(debt.getOwedAmount() - transaction.getAmount());
                }
                continue;
            }
            String payer = transaction.getPayer();
            float amount = transaction.getAmount();
            int userCount = group.getUsers().size();
            for (String username : group.getUsers()) {
                if (username.equals(payer)) {
                    continue;
                }

                Debt debt = findDebt(username, payer, debts);
                float amountForUser = amount / userCount;
                if (debt == null)
                {
                    debts.add(new Debt(username, payer, amountForUser));
                }
                else {
                    if (debt.getOwesToUser().equals(username)) {
                        amountForUser *= -1;
                    }
                    debt.setOwedAmount(debt.getOwedAmount() + amountForUser);
                }
            }
        }
        return debts;
    }

    public static Set<Debt> calculateDebtOfUser(String username, Group group) {
        Set<Debt> debts = calculateDebts(group);
        return debts.stream()
                .filter(x -> x.getInDebtUser().equals(username))
                .collect(Collectors.toSet());
    }

    public static Set<Debt> calculateOwesToUser(String username, Group group) {
        Set<Debt> debts = calculateDebts(group);
        return debts.stream()
                .filter(x -> x.getOwesToUser().equals(username))
                .collect(Collectors.toSet());
    }

    public static Debt calculateDebtOfUser(String username, Group group, String toUser) {
        Set<Debt> debts = calculateDebts(group);
        return debts.stream()
                .filter(x -> x.getInDebtUser().equals(username) && x.getOwesToUser().equals(toUser))
                .findFirst()
                .orElse(null);
    }

    public static Debt calculateOwesToUser(String username, Group group, String byUser) {
        Set<Debt> debts = calculateDebts(group);
        return debts.stream()
                .filter(x -> x.getOwesToUser().equals(username) && x.getInDebtUser().equals(byUser))
                .findFirst()
                .orElse(null);
    }

    private static Debt findDebt(String user1, String user2, Set<Debt> debts) {
        return debts.stream()
                .filter(x -> x.getInDebtUser().equals(user1) && x.getOwesToUser().equals(user2)
                || x.getInDebtUser().equals(user2) && x.getOwesToUser().equals(user1))
                .findFirst().orElse(null);
    }
}
