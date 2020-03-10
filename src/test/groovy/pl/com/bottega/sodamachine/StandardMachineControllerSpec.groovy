package pl.com.bottega.sodamachine

import io.vavr.control.Try
import spock.lang.Specification

import static pl.com.bottega.sodamachine.Driver.*

class StandardMachineControllerSpec extends Specification {

    private static final COLA = new Drink((byte) 1, "Coca-Cola", new Money(250));
    private static final FANTA = new Drink((byte) 2, "Fanta", new Money(250));
    private static final SPRITE = new Drink((byte) 3, "Sprite", new Money(250));
    public static final String WELCOME_TEXT = "Good Morning! Always Coca-Cola!"
    public static final String THANK_YOU_TEXT = "Thank you! Please collect your drink."
    public static final String THANK_YOU_TEXT_WITH_CHANGE = "Thank you! Please collect your drink and change."
    public static final Try<Object> SUCCESS = Try.success(null)
    private Dispenser dispenser = Mock(Dispenser)
    private Ledger ledger = Mock(Ledger)
    private Display display = Mock(Display)
    private InMemoryDrinkRepository drinkRepository = new InMemoryDrinkRepository()


    void setup() {
        drinkRepository.givenDrinks(COLA, FANTA, SPRITE)
        display._ >> SUCCESS
    }

    def "displays welcome text after creation"() {
        when:
            new StandardMachineController(display, dispenser, ledger, drinkRepository)

        then:
            1 * display.displayPermanently(WELCOME_TEXT);
            0 * ledger._
            0 * dispenser._
    }

    def "briefly displays drink price when user presses the drink button"() {
        given:
            StandardMachineController controller = aController()

        when:
            controller.drinkButtonPressed(COLA.nr)

        then:
            1 * display.displayBriefly("${COLA.name}: ${COLA.price}") >> SUCCESS
            0 * ledger._
            0 * dispenser._
    }

    def "takes money and dispenses the drink and displays messages"() {
        given:
            ledger.acceptCoins() >> SUCCESS
            dispenser.dispense(_) >> SUCCESS
            StandardMachineController controller = aController()

        when:
            controller.coinInserted(new Money(200))

        then:
            1 * display.displayPermanently("Amount: ${new Money(200)}") >> SUCCESS

        when:
            controller.coinInserted(new Money(50))

        then:
            1 * display.displayPermanently("Amount: ${new Money(250)}") >> SUCCESS

        when:
            controller.drinkButtonPressed(COLA.nr)

        then:
            1 * ledger.acceptCoins()
            0 * ledger._
            1 * dispenser.dispense(COLA)
            0 * dispenser._
            1 * display.displayBrieflyAndThePermanently(THANK_YOU_TEXT, WELCOME_TEXT)
    }

    def "displays drink price when customer inserted not enough coins and pressed the dispense button"() {
        given:
            StandardMachineController controller = aController()

        when:
            controller.coinInserted(new Money(200))
            controller.drinkButtonPressed(COLA.nr)

        then:
            1 * display.displayPermanently("Amount: ${new Money(200)}") >> SUCCESS
            1 * display.displayBriefly("${COLA.name}: ${COLA.price}") >> SUCCESS
    }

    def "dispenses the coins back when user presses the cancel button"() {
        when:
            StandardMachineController controller = aController()

        then:
            1 * display.displayPermanently(WELCOME_TEXT)

        when:
            controller.coinInserted(new Money(200))
            controller.cancelButtonPressed()

        then:
            1 * ledger.rejectCoins() >> SUCCESS
            0 * ledger._
            1 * display.displayPermanently(WELCOME_TEXT)
            0 * dispenser._
    }

    def "dispenses change when user inserts an amount that is bigger than drink price"() {
        given:
            StandardMachineController controller = aController()

        when:
            controller.coinInserted(new Money(500))
            controller.drinkButtonPressed(COLA.nr)

        then:
            1 * ledger.acceptCoins()
            1 * ledger.dispense(new Money(250))
            1 * display.displayPermanently("Amount: ${new Money(500)}") >> SUCCESS
            1 * display.displayBrieflyAndThePermanently(THANK_YOU_TEXT_WITH_CHANGE, WELCOME_TEXT) >> SUCCESS
            1 * dispenser.dispense(COLA)
    }

    def "does nothing when user presses cancel button without inserting any coins"() {
        given:
            StandardMachineController controller = aController()

        when:
            controller.cancelButtonPressed()

        then:
            0 * ledger._
            0 * dispenser._
    }

    def "it is possible to continue purchase if rejecting coins fails"() {
        given:
            StandardMachineController controller = aController()
            ledger.rejectCoins() >> Try.failure(new DriverException(1))

        when:
            controller.coinInserted(new Money(500))
            controller.cancelButtonPressed()
            controller.drinkButtonPressed(FANTA.nr)

        then:
            1 * dispenser.dispense(FANTA)
            1 * ledger.acceptCoins()
    }

    private StandardMachineController aController() {
        return new StandardMachineController(display, dispenser, ledger, drinkRepository)
    }
}