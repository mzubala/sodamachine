package pl.com.bottega.sodamachine;

import io.vavr.control.Option;
import io.vavr.control.Try;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

interface Display {

    Try<Void> displayPermanently(String text);

    Try<Void> displayBriefly(String text);

    Try<Void> displayBrieflyAndThePermanently(String briefText, String permanentText);
}

class StandardDisplay implements Display {

    private final DriverFacade driverFacade;
    private final long briefTime;

    private Option<String> permanentTextOption = Option.none();

    private Option<Future> briefDisplayFuture = Option.none();

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private final Semaphore semaphore = new Semaphore(1);

    StandardDisplay(DriverFacade driverFacade, long briefTime) {
        this.driverFacade = driverFacade;
        this.briefTime = briefTime;
    }

    @Override
    public Try<Void> displayPermanently(String text) {
        cancelBriefDisplay();
        return driverFacade.execute(new Command.DisplayCommand(text)).andThen(() -> {
            this.permanentTextOption = Option.of(text);
        });
    }

    @Override
    public Try<Void> displayBriefly(String text) {
        if (permanentTextOption.isEmpty()) {
            return Try.failure(new IllegalStateException("No permanent text displayed"));
        }
        return displayBrieflyAndThePermanently(text, permanentTextOption.get());
    }

    @Override
    public Try<Void> displayBrieflyAndThePermanently(String briefText, String permanentText) {
        cancelBriefDisplay();
        return driverFacade.execute(new Command.DisplayCommand(briefText)).andThen(() -> {
            briefDisplayFuture = Option.of(executorService.submit(() -> {
                try {
                    Thread.sleep(briefTime);
                } catch (InterruptedException e) {
                    System.out.println("Interrupted");
                    return;
                }
                displayPermanently(permanentText);
            }));
        });
    }

    private void cancelBriefDisplay() {
        briefDisplayFuture.peek((future) -> {
            System.out.println("Canceling future");
            future.cancel(true);
        });
    }
}
