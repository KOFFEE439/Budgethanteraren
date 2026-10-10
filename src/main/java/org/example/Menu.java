package org.example;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Menu {

    private final List<Transaction> transactions = new ArrayList<>();

    public void showMenu() {
        boolean menu = true;

        while (menu) {

            IO.println("Budgethanteraren");
            IO.println("================");
            IO.println("1. Lägg till transaktion");
            IO.println("2. Visa alla transaktioner");
            IO.println("3. Visa saldo och sammanställning per kategori");
            IO.println("4. Filtrera transaktioner");
            IO.println("5. Spara till fil");
            IO.println("6. Avsluta);");

            String choice = IO.readln("Välj ett alternativ : ");
            switch (choice) {
                case "1":
                    createTransaction();
                    break;
                case "2":
                    showTransactions();
                    break;
                case "3":
                    showSummary();
                    break;
                case "6":
                    menu = false;
                    IO.println("\n" + "VÄLKOMMEN TILLBAKA! ");
                    break;
                default:
                    IO.println("\n" + "Ogiltigt val, vänligen välj en siffra mellan 1 och 6." + "\n");
                    break;
            }
        }
    }

    public void createTransaction() {
        String category = IO.readln("Vad är det för kategori för transaktionen? ");
        String typeOfAmount = IO.readln("Vad är det för kategori av belopp (INKOMST/UTGIFT)");
        String amountNumber = IO.readln("Vad är det för belopp ");

        try {
            BigDecimal amount = new BigDecimal(amountNumber);
            TransactionType type = TransactionType.valueOf(typeOfAmount.trim().toUpperCase());

            if (amount.signum() <= 0) {
                IO.println("Beloppet måste vara större än noll.");
                return;
            }

            Transaction transaction = new Transaction(LocalDate.now(), category, amount, type);

            transactions.add(transaction);
            IO.println("Transaktionen har lagts till.");
        } catch (IllegalArgumentException e) {
            IO.println("Ogiltigt belopp eller typ. Försök igen. ");
        }
    }

    private void showTransactions() {
        if (transactions.isEmpty()) {
            IO.println("Det finns inga transaktioner ännu. ");
            return;
        }

        for (Transaction transaction : transactions) {
            IO.println(transaction);
        }

    }

        private BigDecimal calculateBalance() {
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

    private Map<String, BigDecimal> calculateExpensesByCategory() {
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
    private void showSummary() {
        IO.println("Saldo: " + calculateBalance());
        IO.println("Utgifter per kategori:");

        Map<String, BigDecimal> totals = calculateExpensesByCategory();

        if (totals.isEmpty()) {
            IO.println("Inga utgifter att sammanställa.");
            return;
        }

        totals.forEach((category, total) ->
                IO.println(category + ": " + total)
        );
    }
    }

