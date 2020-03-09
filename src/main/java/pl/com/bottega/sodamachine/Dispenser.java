package pl.com.bottega.sodamachine;

import io.vavr.control.Try;

interface Dispenser {
    Try<Void> dispense(Drink drink);
}
