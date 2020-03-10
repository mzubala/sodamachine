package pl.com.bottega.sodamachine;

import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

interface Display {

    Try<Void> displayPermanently(String text);

    Try<Void> displayBriefly(String text);

    Try<Void> displayBrieflyAndThePermanently(String briefText, String permanentText);
}

@Slf4j
class DriverFacadeDisplay implements Display {

    private final DriverFacade driverFacade;

    public DriverFacadeDisplay(DriverFacade driverFacade) {
        this.driverFacade = driverFacade;
    }

    @Override
    public Try<Void> displayPermanently(String text) {
        return null;
    }

    @Override
    public Try<Void> displayBriefly(String text) {
        return null;
    }

    @Override
    public Try<Void> displayBrieflyAndThePermanently(String briefText, String permanentText) {
        return null;
    }
}