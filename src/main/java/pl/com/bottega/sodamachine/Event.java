package pl.com.bottega.sodamachine;

import java.time.Instant;
import java.util.Objects;

abstract class Event {

    private final Instant timestamp;

    private Event(Instant timestamp) {
        this.timestamp = timestamp;
    }

    abstract void dispatch(MachineController machineController);

    static class CoinInserted extends Event {

        CoinInserted(Instant timestamp) {
            super(timestamp);
        }

        @Override
        void dispatch(MachineController machineController) {
            machineController.coinInserted();
        }
    }

    static class CancelButtonPressed extends Event {

        CancelButtonPressed(Instant timestamp) {
            super(timestamp);
        }

        @Override
        void dispatch(MachineController machineController) {
            machineController.cancelButtonPressed();
        }
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return timestamp.equals(event.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp);
    }
}
