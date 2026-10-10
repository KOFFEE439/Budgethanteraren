package org.example;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Transaction(LocalDate date, String category,
                          BigDecimal amount, TransactionType transactionType) { }

