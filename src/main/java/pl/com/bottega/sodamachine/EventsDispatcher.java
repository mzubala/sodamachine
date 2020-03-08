package pl.com.bottega.sodamachine;

class EventsDispatcher implements Runnable {

    private final DriverFacade driverFacade;
    private final MachineController machineController;

    EventsDispatcher(DriverFacade driverFacade, MachineController machineController) {
        this.driverFacade = driverFacade;
        this.machineController = machineController;
    }

    @Override
    public void run() {

    }
}
