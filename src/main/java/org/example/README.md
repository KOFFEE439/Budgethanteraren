# Budgethanteraren

En konsolapplikation som registrerar inkomster och utgifter,
visa ekonomiskt saldo och sammanställa utgifter per kategori. Transaktioner
kan sparas till och läsas från data/transactions.txt.

## Funktioner

1. Lägg till transaktion: lägger till transaktion genom att skriva en kategori, sen
skriva i om det är inkomst eller utgift, sedan vilken summa det handlar om. 
2. Visa alla transaktioner: visar alla transaktioner. 
3. Visa saldo och sammanställning per kategori: visar en sammanställning i kategori. 
4. Filtrera transaktioner: skriv i kategori som man söker transaktioner inom.
5. Spara till fil: sparar till en fil data/transactions.txt. 
6. Avsluta: avsluta och sparar till fil.

### Bugg 
När en ogiltig transaktion skapade InvalidTransactionException fångades 
inte undantaget av mitt catch-block.
Provade att ange ett ogiltigt belopp och såg att felhanteringen inte 
fungerade som förväntat och applikationen stoppade.
Följde undantaget från valideringen till catch-blocket och upptäckte att 
min egen undantagsklass inte hanterades där.
Sen lade jag till InvalidTransactionException i felhanteringen och provade 
ogiltig indata igen.

