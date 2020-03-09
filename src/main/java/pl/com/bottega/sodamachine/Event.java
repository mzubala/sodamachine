package pl.com.bottega.sodamachine;

import lombok.EqualsAndHashCode;

import java.time.Instant;

@EqualsAndHashCode
abstract class Event {

    private final Instant timestamp;

    private Event(Instant timestamp) {
        this.timestamp = timestamp;
    }

    abstract void dispatch(MachineController machineController);

    @EqualsAndHashCode(callSuper = true)
    static class CoinInserted extends Event {

        CoinInserted(Instant timestamp) {
            super(timestamp);
        }

        @Override
        void dispatch(MachineController machineController) {
            machineController.coinInserted();
        }
    }

    @EqualsAndHashCode(callSuper = true)
    static class CancelButtonPressed extends Event {

        CancelButtonPressed(Instant timestamp) {
            super(timestamp);
        }

        @Override
        void dispatch(MachineController machineController) {
            machineController.cancelButtonPressed();
        }
    }

    @EqualsAndHashCode(callSuper = true)
    static class DrinkButtonPressed extends Event {
        private final Drink drink;

        DrinkButtonPressed(Instant timestamp, Drink drink) {
            super(timestamp);
            this.drink = drink;
        }

        @Override
        void dispatch(MachineController machineController) {
            machineController.drinkButtonPressed(drink.getNr());
        }
    }
}
