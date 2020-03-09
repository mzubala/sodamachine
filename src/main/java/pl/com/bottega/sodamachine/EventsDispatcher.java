package pl.com.bottega.sodamachine;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class EventsDispatcher {

    private final DriverFacade driverFacade;
    private final MachineController machineController;
    private Event lastEvent;

    EventsDispatcher(DriverFacade driverFacade, MachineController machineController) {
        this.driverFacade = driverFacade;
        this.machineController = machineController;
    }

    void dispatch() {
        driverFacade.nextEvent()
            .onSuccess((eventOption) ->
                eventOption.filter(this::isNew).peek((event) -> {
                    lastEvent = event;
                    event.dispatch(machineController);
                })
            ).onFailure((error) -> {
                log.error("Error fetching next event", error);
        });
    }

    private boolean isNew(Event event) {
        return !event.equals(lastEvent);
    }
}
