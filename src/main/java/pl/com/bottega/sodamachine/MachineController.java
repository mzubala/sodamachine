package pl.com.bottega.sodamachine;

import lombok.AllArgsConstructor;

interface MachineController {

    void coinInserted(Money money);

    void cancelButtonPressed();

    void drinkButtonPressed(byte nr);
}

class StandardMachineController implements MachineController {

    private static final String WELCOME_TEXT = "Good Morning! Always Coca-Cola!";
    private static final String THANK_YOU_TEXT = "Thank you! Please collect your drink.";
    private static final String THANK_YOU_TEXT_WITH_CHANGE = "Thank you! Please collect your drink and change.";

    private final Display display;
    private final Dispenser dispenser;
    private final Ledger ledger;
    private final DrinkRepository drinkRepository;

    private Money insertedAmount = new Money(0);

    public StandardMachineController(Display display, Dispenser dispenser, Ledger ledger, DrinkRepository drinkRepository) {
        this.display = display;
        this.dispenser = dispenser;
        this.ledger = ledger;
        this.drinkRepository = drinkRepository;
        display.displayPermanently(WELCOME_TEXT);
    }

    @Override
    public void coinInserted(Money money) {
        insertedAmount = insertedAmount.add(money);
        display.displayPermanently(String.format("Amount: %s", insertedAmount));
    }

    @Override
    public void cancelButtonPressed() {
        if(insertedAmount.compareTo(new Money(0)) > 0) {
            ledger.rejectCoins();
            display.displayPermanently(WELCOME_TEXT);
            insertedAmount = new Money(0);
        }
    }

    @Override
    public void drinkButtonPressed(byte nr) {
        drinkRepository.getDrink(nr).andThen(drink -> {
            if(enoughCoinsInsertedToBuy(drink)) {
                dispenseDrink(drink);
            } else {
                displayDrinkPrice(drink);
            }
        });
    }

    private boolean enoughCoinsInsertedToBuy(Drink drink) {
        return insertedAmount.compareTo(drink.getPrice()) >= 0;
    }

    private void dispenseDrink(Drink drink) {
        ledger.acceptCoins();
        dispenser.dispense(drink);
        Money change = insertedAmount.subtract(drink.getPrice());
        if(change.compareTo(new Money(0)) > 0) {
            ledger.dispense(change);
            display.displayBrieflyAndThePermanently(THANK_YOU_TEXT_WITH_CHANGE, WELCOME_TEXT);
        } else {
            display.displayBrieflyAndThePermanently(THANK_YOU_TEXT, WELCOME_TEXT);
        }
        insertedAmount = new Money(0);
    }

    private void displayDrinkPrice(Drink drink) {
        display.displayBriefly(String.format("%s: %s", drink.getName(), drink.getPrice()));
    }
}