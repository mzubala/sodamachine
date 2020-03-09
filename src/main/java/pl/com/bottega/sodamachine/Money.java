package pl.com.bottega.sodamachine;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
class Money {

    private long cents;

    Money(long cents) {
        this.cents = cents;
    }

    Money add(Money add) {
        return new Money(cents + add.cents);
    }

    Money subtract(Money subtrahend) {
        return new Money(cents - subtrahend.cents);
    }

    Money multiply(long factor) {
        return new Money(factor * cents);
    }

    Money opposite() {
        return new Money(-cents);
    }

    int compareTo(Money money) {
        if (cents == money.cents) {
            return 0;
        }
        else if (cents < money.cents) {
            return -1;
        }
        else {
            return 1;
        }
    }

    long toCents() {
        return cents;
    }
}
