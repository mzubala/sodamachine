package pl.com.bottega.sodamachine;

class EventsDispatcher {

    private final DriverFacade driverFacade;
    private final MachineController machineController;

    EventsDispatcher(DriverFacade driverFacade, MachineController machineController) {
        this.driverFacade = driverFacade;
        this.machineController = machineController;
    }

    void dispatch() {

    }
}
