package command;

/**
 * PATTERN: Command — Command Interface
 *
 * All reservation operations implement this interface.
 * canExecute() enforces preconditions BEFORE execute() runs.
 */
public interface ReservationCommand {

    /** Validates all preconditions. Must return true before execute() is called. */
    boolean canExecute();

    /** Performs the operation and persists changes to CSV. */
    void execute() throws Exception;

    /** Human-readable description for logging. */
    String getDescription();

    /** Returns the reason canExecute() returned false, or empty string. */
    String getErrorMessage();
}
