package pl.com.bottega.sodamachine;

import io.vavr.control.Option;
import io.vavr.control.Try;

import java.util.List;

interface DriverFacade {

    Try<Option<Event>> nextEvent();

    Try<Void> execute(Command command);

    Try<Money> awaitingCoins();

    Try<List<Money>> coinsLedger();
}
