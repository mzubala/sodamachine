package pl.com.bottega.sodamachine;

interface MachineController {

    void coinInserted();

    void cancelButtonPressed();

    void drinkButtonPressed(byte nr);
}
