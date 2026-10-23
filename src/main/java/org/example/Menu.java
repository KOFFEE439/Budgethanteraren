package org.example;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Menu {

    private final Repository<Transaction> transactions = new Repository<>();
    private final BudgetCalculator calculator = new BudgetCalculator();
    private final TransactionFileService fileService = new TransactionFileService(Path.of("data", "transactions.txt"));
    private static final Logger LOGGER = Logger.getLogger(Menu.class.getName());

    public void showMenu() {
        loadTransactions();
        boolean menu = true;

        while (menu) {

            IO.println("Budgethanteraren");
            IO.println("================");
            IO.println("1. Lägg till transaktion");
            IO.println("2. Visa alla transaktioner");
            IO.println("3. Visa saldo och sammanställning per kategori");
            IO.println("4. Filtrera transaktioner");
            IO.println("5. Spara till fil");
            IO.println("6. Avsluta ");

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
                case "4":
                    filterTransactionsByCategory();
                    break;
                case "5":
                    saveTransactions();
                    break;
                case "6":
                    saveTransactions();
                    menu = false;
                    IO.println("\nVÄLKOMMEN TILLBAKA!");
                    break;
                default:
                    IO.println("\n" + "Ogiltigt val, vänligen välj en siffra mellan 1 och 6." + "\n");
                    break;
            }
        }
    }

    public void createTransaction() {
        String category = IO.readln("Vad är det för kategori för transaktionen? ");
        String typeOfAmount =
                IO.readln("Vad är det för typ av transaktion (INKOMST/UTGIFT)? ");
        String amountNumber = IO.readln("Vad är beloppet? ");

        try {
            BigDecimal amount = new BigDecimal(amountNumber);
            TransactionType type =
                    TransactionType.valueOf(typeOfAmount.trim().toUpperCase());

            if (amount.signum() <= 0) {
                throw new InvalidTransactionException(
                        "Beloppet måste vara större än noll."
                );
            }

            Transaction transaction =
                    new Transaction(LocalDate.now(), category, amount, type);

            transactions.add(transaction);
            IO.println("Transaktionen har lagts till.");
        } catch (InvalidTransactionException | IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Ogiltig transaktionsindata: {0}", e.getMessage());
            IO.println(e.getMessage());
        }
    }

    private void showTransactions() {
        if (transactions.findAll().isEmpty()) {
            IO.println("Det finns inga transaktioner ännu.");
            return;
        }

        for (Transaction transaction : transactions.findAll()) {
            IO.println(transaction);
        }
    }

    private void showSummary() {
        var allTransactions = transactions.findAll();

        IO.println("Saldo: " + calculator.calculateBalance(allTransactions));
        IO.println("Utgifter per kategori:");

        Map<String, BigDecimal> totals =
                calculator.calculateExpensesByCategory(allTransactions);

        if (totals.isEmpty()) {
            IO.println("Inga utgifter att sammanställa.");
            return;
        }

        totals.forEach((category, total) ->
                IO.println(category + ": " + total)
        );
    }

    private void filterTransactionsByCategory() {
        String categoryToFind =
                IO.readln("Vilken kategori vill du visa? ").trim();

        boolean found = false;

        for (Transaction transaction : transactions.findWhere(
                t -> t.category().equalsIgnoreCase(categoryToFind))) {
            IO.println(transaction);
            found = true;
        }

        if (!found) {
            IO.println("Hittade inga transaktioner i den kategorin.");
        }
    }

    private void loadTransactions() {
        try {
            for (Transaction transaction : fileService.load()) {
                transactions.add(transaction);
            }
        } catch (IOException e) {
            IO.println("Kunde inte läsa transaktionsfilen.");
            LOGGER.log(Level.SEVERE, "Fel vid inläsning av transaktionsfilen.", e);
        }
    }
    private void saveTransactions() {
        try {
            fileService.save(transactions.findAll());
            IO.println("Transaktionerna har sparats.");
        } catch (IOException | FileFormatException e) {
            IO.println("Kunde inte spara transaktionerna.");
            LOGGER.log(Level.SEVERE, "Fel vid sparning av transaktionsfilen.", e);
        }
    }
}



