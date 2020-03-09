package pl.com.bottega.sodamachine

import io.vavr.control.Option
import io.vavr.control.Try
import java.util.function.Supplier
import spock.lang.Specification

import static java.time.Instant.now

class EventsDispatcherSpec extends Specification {

    private MachineController controller = Mock(MachineController)
    private DriverFacade driverFacade = Mock(DriverFacade)
    private EventsDispatcher eventsDispatcher = new EventsDispatcher(driverFacade, controller)

    def "informs controller that a coin has been inserted"() {
        given:
            nextEventIs(new Event.CoinInserted(now()))

        when:
            eventsDispatcher.dispatch()

        then:
            1 * controller.coinInserted()
            0 * controller._
    }

    def "does not inform controller about same event 2 times"() {
        given:
            nextEventIs((eventSupplier as Supplier<Event>).get())

        when:
            eventsDispatcher.dispatch()
            eventsDispatcher.dispatch()

        then:
            1 * controller._

        where:
            eventSupplier << [
                { new Event.CoinInserted(now()) },
                { new Event.DrinkButtonPressed(now(), new Drink((byte) 1, "Coca-Cola", new Money(250))) },
                { new Event.CancelButtonPressed(now()) }
            ]
    }

    def "does not inform controller when there is no next event"() {
        given:
            thereIsNoNextEvent()

        when:
            eventsDispatcher.dispatch()

        then:
            0 * controller._
    }

    def "does not inform controller when next event fails"() {
        given:
            nextEventFails()

        when:
            eventsDispatcher.dispatch()

        then:
            0 * controller._
    }

    def "informs controller about cancel button pressed"() {
        given:
            nextEventIs(new Event.CancelButtonPressed(now()))

        when:
            eventsDispatcher.dispatch()

        then:
            1 * controller.cancelButtonPressed()
            0 * controller._
    }

    def "informs controller about drink button pressed"() {
        given:
            nextEventIs(new Event.DrinkButtonPressed(now(), new Drink((byte) 1, "Coca-Cola", new Money(250))))

        when:
            eventsDispatcher.dispatch()

        then:
            1 * controller.drinkButtonPressed(1)
            0 * controller._
    }

    private void nextEventIs(Event event) {
        driverFacade.nextEvent() >> Try.of {
            Option.of(event)
        }
    }

    private void thereIsNoNextEvent() {
        driverFacade.nextEvent() >> Try.of {
            Option.none()
        }
    }

    private void nextEventFails() {
        driverFacade.nextEvent() >> Try.failure(new Driver.DriverException(1))
    }
}