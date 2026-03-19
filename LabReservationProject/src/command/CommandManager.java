package command;

import java.util.ArrayList;
import java.util.List;

/**
 * PATTERN: Command — Invoker
 *
 * Runs commands after checking preconditions.
 * Keeps a history of executed commands for audit logging.
 */
public class CommandManager {

    private final List<ReservationCommand> history = new ArrayList<>();

    /**
     * Executes a command if its preconditions pass.
     * @return true if the command executed successfully, false if preconditions failed.
     */
    public boolean execute(ReservationCommand command) {
        if (!command.canExecute()) {
            System.out.println("[CommandManager] Cannot execute: " + command.getErrorMessage());
            return false;
        }
        try {
            command.execute();
            history.add(command);
            System.out.println("[CommandManager] Executed: " + command.getDescription());
            return true;
        } catch (Exception e) {
            System.out.println("[CommandManager] Error during execution: " + e.getMessage());
            return false;
        }
    }

    public List<ReservationCommand> getHistory() {
        return history;
    }

    public void printHistory() {
        System.out.println("=== Command History ===");
        for (int i = 0; i < history.size(); i++) {
            System.out.println((i + 1) + ". " + history.get(i).getDescription());
        }
    }
}
