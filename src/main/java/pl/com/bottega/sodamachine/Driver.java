package pl.com.bottega.sodamachine;

/**
 * Contains low level methods allowing to communicate with the machine hardware.
 */
interface Driver {

    /**
     * Reads the last event that happened within the soda machine.
     *
     * The event is returned as a byte array where:
     * - first 8 bytes represent the timestamp of the event
     * - 9th byte represent the event that happened
     * - 10th byte represents the event data
     *
     * Event values:
     * - 1 - coin inserted, event data byte represents coin's kind (1 - 10 cents, 2 - 20 cents, 3 - 50 cents, 4 - 100 cents)
     * - 2 - cancel button pressed
     * - 3 - drink button pressed, event data represents the number of the button pressed (1-10)
     *
     * @return bytes representing the current state of the soda machine
     */
    byte[] read() throws DriverException;

    /**
     * Sends a command to the machine.
     *
     * The command is sent in the form of a byte array where:
     * - 1st byte represents the command to be sent
     * - subsequent bytes represent the command params
     *
     * Allowed commands are:
     * 1 - display text, subsequent bytes represent the ASCII text to be displayed (max 64 characters)
     * 2 - accept coins inserted to the machine and put them into the ledger
     * 3 - remove coins inserted to the machine
     * 4 - dispense one coin from the ledger, subsequent byte represents the coin type to be dispensed
     * 5 - dispense drink, subsequent byte represents the drink slot number
     *
     * @param command bytes representing the command to be sent
     */
    void write(byte[] command) throws DriverException;

    /**
     * Returns coins that have been inserted to the machine and are awaiting to be put into the ledger.
     *
     * @return a byte array representing counts of each coin type
     */
    byte[] awaitingCoins() throws DriverException;

    /**
     * Returns current state of the coins ledger in the machine.
     *
     * @return a byte array representing counts of each coin type
     */
    byte[] coinsLedger() throws DriverException;

    /**
     * Represents an error in communication with the machine hardware.
     */
    class DriverException extends Exception {
        private final int errorCode;

        public DriverException(int errorCode) {
            this.errorCode = errorCode;
        }

        public int getErrorCode() {
            return errorCode;
        }
    }
}