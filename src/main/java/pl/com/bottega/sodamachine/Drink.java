package pl.com.bottega.sodamachine;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
class Drink {

    private final byte nr;
    private final String name;
    private final Money price;

    Drink(byte nr, String name, Money price) {
        this.nr = nr;
        this.name = name;
        this.price = price;
    }

    byte getNr() {
        return nr;
    }

    String getName() {
        return name;
    }

    Money getPrice() {
        return price;
    }
}
