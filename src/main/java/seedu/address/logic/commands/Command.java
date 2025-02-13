package seedu.address.logic.commands;

import java.util.ArrayList;

import seedu.address.logic.CommandObserver;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Represents a command with hidden internal logic and the ability to be executed.
 */
public abstract class Command {

    private static final ArrayList<CommandObserver> commandObservers = new ArrayList<>();

    public static void setCommandObserver(CommandObserver commandObserver) {
        commandObservers.add(commandObserver);
    }

    /**
     * Updates the CommandObservers by calling the updateView method for each of them.
     *
     * @param indexOfTabToView index of tab to be viewed.
     */
    public void updateView(Integer indexOfTabToView) {
        for (CommandObserver commandObserver : commandObservers) {
            commandObserver.updateView(indexOfTabToView);
        }
    }

    /**
     * Updates the CommandObservers by calling the updateClass command.
     *
     * @param indexOfClassToSelect index of class to be selected.
     */
    public void updateClass(Integer indexOfClassToSelect) {
        for (CommandObserver commandObserver : commandObservers) {
            commandObserver.updateClass(indexOfClassToSelect);
        }
    }

    /**
     * Updates the CommandObservers by calling the hideTuitionClassStudentList command.
     */
    public void hideTuitionClassStudentList() {
        for (CommandObserver commandObserver : commandObservers) {
            commandObserver.hideTuitionClassStudentList();
        }
    }

    /**
     * Executes the command and returns the result message.
     *
     * @param model {@code Model} which the command should operate on.
     * @return feedback message of the operation result for display
     * @throws CommandException If an error occurs during command execution.
     */
    public abstract CommandResult execute(Model model) throws CommandException;

}
