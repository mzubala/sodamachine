package pl.com.bottega.sodamachine;

import lombok.AllArgsConstructor;

interface MachineController {

    void coinInserted(Money money);

    void cancelButtonPressed();

    void drinkButtonPressed(byte nr);
}

@AllArgsConstructor
class StandardMachineController implements MachineController {

    private final Display display;
    private final Dispenser dispenser;
    private final Ledger ledger;
    private final DrinkRepository drinkRepository;

    @Override
    public void coinInserted(Money money) {

    }

    @Override
    public void cancelButtonPressed() {

    }

    @Override
    public void drinkButtonPressed(byte nr) {

    }
}