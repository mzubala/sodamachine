package pl.com.bottega.sodamachine;

import java.time.Instant;

class Event {

    private final Instant timestamp;

    private Event(Instant timestamp) {
        this.timestamp = timestamp;
    }

    static class CoinInserted extends Event {

        CoinInserted(Instant timestamp) {
            super(timestamp);
        }
    }

    static class CancelButtonPressed extends Event {
        CancelButtonPressed(Instant timestamp) {
            super(timestamp);
        }
    }

    static class DrinkButtonPressed extends Event {
        private final Drink drink;

       DrinkButtonPressed(Instant timestamp, Drink drink) {
            super(timestamp);
            this.drink = drink;
        }
    }
}
