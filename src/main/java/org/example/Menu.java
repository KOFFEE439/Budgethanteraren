package org.example;

public class Menu {

    public void showMenu() {
        boolean menu = true;

        while (menu) {

            IO.println("Budgethanteraren");
            IO.println("================");
            IO.println("1. Lagg till transaktion");
            IO.println("2. Visa alla transaktioner");
            IO.println("3. Visa saldo och sammanstallning per kategori");
            IO.println("4. Filtrera transaktioner");
            IO.println("5. Spara till fil");
            IO.println("6. Avsluta);");

            String choice = IO.readln("Välj ett alternativ : ");
            switch (choice) {
                case "1":
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
}
