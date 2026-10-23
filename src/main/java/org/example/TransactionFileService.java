package org.example;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

    public class TransactionFileService {
        private static final Logger LOGGER =
                Logger.getLogger(TransactionFileService.class.getName());

        private final Path filePath;

        public TransactionFileService(Path filePath) {
            this.filePath = filePath;
        }

        public List<Transaction> load() throws IOException {
            createFileIfMissing();

            List<Transaction> transactions = new ArrayList<>();
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);

                if (line.isBlank()) {
                    continue;
                }

                try {
                    transactions.add(parseLine(line));
                } catch (FileFormatException e) {
                    LOGGER.log(
                            Level.WARNING,
                            "Kunde inte läsa rad {0} i filen {1}: {2}",
                            new Object[]{i + 1, filePath, e.getMessage()}
                    );
                }
            }

            LOGGER.log(
                    Level.INFO,
                    "Läste in {0} transaktioner från {1}",
                    new Object[]{transactions.size(), filePath}
            );

            return transactions;
        }

        public void save(List<Transaction> transactions)
                throws IOException, FileFormatException {
            createFileIfMissing();

            List<String> lines = new ArrayList<>();

            for (Transaction transaction : transactions) {
                validateCategory(transaction.category());

                lines.add(String.join(";",
                        transaction.date().toString(),
                        transaction.category(),
                        transaction.totalSum().toPlainString(),
                        transaction.transactionType().name()
                ));
            }

            Files.write(filePath, lines, StandardCharsets.UTF_8);

            LOGGER.log(
                    Level.INFO,
                    "Sparade {0} transaktioner till {1}",
                    new Object[]{transactions.size(), filePath}
            );
        }

        private void createFileIfMissing() throws IOException {
            Path parent = filePath.getParent();

            if (parent != null) {
                Files.createDirectories(parent);
            }

            if (Files.notExists(filePath)) {
                Files.createFile(filePath);
                LOGGER.log(Level.INFO, "Skapade ny datafil: {0}", filePath);
            }
        }

        private Transaction parseLine(String line) throws FileFormatException {
            String[] parts = line.split(";", -1);

            if (parts.length != 4) {
                throw new FileFormatException(
                        "Förväntade fyra fält, men hittade " + parts.length
                );
            }

            try {
                LocalDate date = LocalDate.parse(parts[0]);
                String category = parts[1];
                BigDecimal amount = new BigDecimal(parts[2]);
                TransactionType type = TransactionType.valueOf(parts[3]);

                validateCategory(category);

                if (amount.signum() <= 0) {
                    throw new FileFormatException("Beloppet måste vara större än noll.");
                }

                return new Transaction(date, category, amount, type);
            } catch (DateTimeParseException | IllegalArgumentException e) {
                throw new FileFormatException("Ogiltigt datum, belopp eller typ.", e);
            }
        }

        private void validateCategory(String category) throws FileFormatException {
            if (category == null || category.isBlank()) {
                throw new FileFormatException("Kategorin får inte vara tom.");
            }

            if (category.contains(";") || category.contains("\n")
                    || category.contains("\r")) {
                throw new FileFormatException(
                        "Kategorin får inte innehålla semikolon eller radbrytningar."
                );
            }
        }
    }

