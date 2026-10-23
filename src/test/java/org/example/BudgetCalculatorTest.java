package org.example;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class BudgetCalculatorTest {

    private final BudgetCalculator calculator = new BudgetCalculator();

    @Test
    void calculatorBalanceReturnsZeroEmptyList() {
        List<Transaction> transactions = List.of();

        BigDecimal result = calculator.calculateBalance(transactions);

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void calculatorBalanceSubtractsExpensesFromIncome() {
        List<Transaction> transactions = List.of(
                transaction("100.00", TransactionType.INKOMST),
                transaction("22.20", TransactionType.UTGIFT));

        BigDecimal result = calculator.calculateBalance(transactions);

        assertEquals(new BigDecimal("77.80"), result);
    }


    @Test
    void calculateExpensesByCategoryAddsExpensesInSameCategory() {
        List<Transaction> transactions = List.of(
                transaction("20.00", "Mat", TransactionType.UTGIFT),
                transaction("15.50", "Mat", TransactionType.UTGIFT),
                transaction("100.00", "Lön", TransactionType.INKOMST)
        );


        Map<String, BigDecimal> result =
                calculator.calculateExpensesByCategory(transactions);

        assertEquals(1, result.size());
        assertEquals(new BigDecimal("35.50"), result.get("Mat"));
        assertTrue(!result.containsKey("Lön"));
    }

    @Test
    void calculateExpensesByCategoryReturnsEmptyMapWhenThereAreNoExpenses() {
        List<Transaction> transactions = List.of(
                transaction("100.00", TransactionType.INKOMST)
        );

        Map<String, BigDecimal> result =
                calculator.calculateExpensesByCategory(transactions);

        assertTrue(result.isEmpty());
    }

    private Transaction transaction(String amount, TransactionType type) {
        return transaction(amount, "Testkategori", type);
    }

    private Transaction transaction(
            String amount,
            String category,
            TransactionType type
    ) {
        return new Transaction(
                LocalDate.of(2026, 1, 1),
                category,
                new BigDecimal(amount),
                type
        );
    }

}