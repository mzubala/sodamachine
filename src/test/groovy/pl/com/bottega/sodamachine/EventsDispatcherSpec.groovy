package pl.com.bottega.sodamachine

import io.vavr.control.Option
import io.vavr.control.Try
import java.time.Instant
import java.util.function.Function
import java.util.function.Supplier
import spock.lang.Specification

import static java.time.Instant.now

class EventsDispatcherSpec extends Specification {

    private MachineController controller = Mock(MachineController)
    private DriverFacade driverFacade = Mock(DriverFacade)
    private EventsDispatcher eventsDispatcher = new EventsDispatcher(driverFacade, controller)

    def "informs controller that a coin has been inserted"() {
        given:
            nextEventIs(new Event.CoinInserted(now(), new Money(100)))

        when:
            eventsDispatcher.dispatch()

        then:
            1 * controller.coinInserted(new Money(100))
            0 * controller._
    }

    def "does not inform controller about same event 2 times"() {
        given:
            Instant now = now()
            nextEventIs((eventSupplier as Function<Instant, Event>).apply(now))

        when:
            eventsDispatcher.dispatch()

        then:
            1 * controller._

        when:
            nextEventIs((eventSupplier as Function<Instant, Event>).apply(now))
            eventsDispatcher.dispatch()

        then:
            0 * controller._

        where:
            eventSupplier << [
                { t -> new Event.CoinInserted(t, new Money(100)) },
                { t -> new Event.DrinkButtonPressed(t, new Drink((byte) 1, "Coca-Cola", new Money(250))) },
                { t -> new Event.CancelButtonPressed(t) }
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