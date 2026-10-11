package org.example;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BudgetCalculator {

    public BigDecimal calculateBalance(List<Transaction> transactions) {
        BigDecimal balance = BigDecimal.ZERO;

        for (Transaction transaction : transactions) {
            if (transaction.transactionType() == TransactionType.INKOMST) {
                balance = balance.add(transaction.totalSum());
            } else {
                balance = balance.subtract(transaction.totalSum());
            }
        }

        return balance;
    }

    public Map<String, BigDecimal> calculateExpensesByCategory(
            List<Transaction> transactions) {

        Map<String, BigDecimal> totals = new HashMap<>();

        for (Transaction transaction : transactions) {
            if (transaction.transactionType() == TransactionType.UTGIFT) {
                totals.merge(
                        transaction.category(),
                        transaction.totalSum(),
                        BigDecimal::add
                );
            }
        }

        return totals;
    }
}

