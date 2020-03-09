package pl.com.bottega.sodamachine

import io.vavr.control.Try

class InMemoryDrinkRepository implements DrinkRepository {

    private List<Drink> drinks = new LinkedList<>();

    void givenDrinks(Drink... drinks) {
        this.drinks.addAll(drinks)
    }

    @Override
    Try<Drink> getDrink(byte nr) {
        return Try.of {
            drinks.stream().filter { it.nr == nr }.findFirst().orElseThrow { new IllegalArgumentException("No such drink") }
        }
    }
}
