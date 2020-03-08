package pl.com.bottega.sodamachine;

import io.vavr.control.Try;

interface DrinkRepository {

    Try<Drink> getDrink(byte nr);
}
