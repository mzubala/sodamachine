package pl.com.bottega.sodamachine;

class Command {

    private Command() {}

    static class DispenseDrink extends Command {
        private final Drink drink;

        DispenseDrink(Drink drink) {
            this.drink = drink;
        }
    }

    static class RejectCoinsCommand extends Command {}

    static class AcceptCoinsCommand extends Command {}

    static class DispenseCoinsCommand extends Command {
        private final Money amount;

        DispenseCoinsCommand(Money amount) {
            this.amount = amount;
        }
    }

    static class DisplayCommand extends Command {
        private final String text;

        public DisplayCommand(String text) {
            this.text = text;
        }
    }
}
