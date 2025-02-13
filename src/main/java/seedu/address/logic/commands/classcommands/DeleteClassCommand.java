package seedu.address.logic.commands.classcommands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.tuitionclass.TuitionClass;

public class DeleteClassCommand extends Command {
    public static final String COMMAND_WORD = "deleteclass";

    public static final String MESSAGE_SUCCESS = "Class deleted: %1$s";
    public static final String MESSAGE_MISSING_CLASS = "This class does not exist in the address book";


    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the class identified by the index in class list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    private final Index index;

    public DeleteClassCommand(Index index) {
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<TuitionClass> lastShownClassList = model.getFilteredTuitionClassList();
        TuitionClass tuitionClass = lastShownClassList.get(index.getZeroBased());

        if (!model.hasTuitionClass(tuitionClass)) {
            throw new CommandException(MESSAGE_MISSING_CLASS);
        }

        model.deleteTuitionClass(tuitionClass);

        hideTuitionClassStudentList();

        return new CommandResult(String.format(MESSAGE_SUCCESS, tuitionClass));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteClassCommand // instanceof handles nulls
                && index.equals(((DeleteClassCommand) other).index)); // state check
    }
}
