package pl.com.bottega.sodamachine

import io.vavr.control.Try
import spock.lang.Specification

class StandardDisplayTest extends Specification {

    private static final long TEST_BRIEF_TIME = 1000
    private DriverFacade driverFacade = Mock(DriverFacade)
    private StandardDisplay standardDisplay = new StandardDisplay(driverFacade, TEST_BRIEF_TIME)
    private static final TEXT1 = "test1"
    private static final TEXT2 = "test2"
    private static final TEXT3 = "test3"

    def "displays text permanently"() {
        when:
            standardDisplay.displayPermanently(TEXT1)

        then:
            1 * driverFacade.execute(new Command.DisplayCommand(TEXT1)) >> Try.success(null)
            0 * driverFacade._
    }

    def "displays text briefly"() {
        when:
            standardDisplay.displayPermanently(TEXT1)
            standardDisplay.displayBriefly(TEXT2)
            Thread.sleep(TEST_BRIEF_TIME + 5)

        then:
            2 * driverFacade.execute(new Command.DisplayCommand(TEXT1)) >> Try.success(null)
            1 * driverFacade.execute(new Command.DisplayCommand(TEXT2)) >> Try.success(null)
            0 * driverFacade._
    }

    def "cancels brief text when second brief comes"() {
        when:
            standardDisplay.displayPermanently(TEXT1)
            standardDisplay.displayBriefly(TEXT2)
            Thread.sleep(TEST_BRIEF_TIME.intdiv(2))
            standardDisplay.displayBriefly(TEXT3)
            Thread.sleep(TEST_BRIEF_TIME + 5)

        then:
            2 * driverFacade.execute(new Command.DisplayCommand(TEXT1)) >> Try.success(null)
            1 * driverFacade.execute(new Command.DisplayCommand(TEXT2)) >> Try.success(null)
            1 * driverFacade.execute(new Command.DisplayCommand(TEXT3)) >> Try.success(null)
            0 * driverFacade._
    }

}