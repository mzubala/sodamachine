package pl.com.bottega.sodamachine;

import io.vavr.control.Try;

interface Ledger {
    Try<Void> acceptCoins();

    Try<Void> rejectCoins();

    Try<Void> dispense(Money amount);

    Try<Money> awaitingAmount();
}
