package seedu.address.logic.commands.classcommands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Name;
import seedu.address.model.person.Student;
import seedu.address.model.tuitionclass.StudentNameList;
import seedu.address.model.tuitionclass.TuitionClass;

public class RemoveFromClassCommand extends Command {
    public static final String COMMAND_WORD = "removefromclass";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Removes students from a class by the index"
            + " number used "
            + "in the displayed class and person list.\n"
            + "Parameters: CLASS_INDEX "
            + "STUDENT_INDEX...\n"
            + "Example: " + COMMAND_WORD + " "
            + "2 "
            + "3 4 5 "
            + "(removes students indexed 3, 4 and 5 from class indexed 2)";

    public static final String MESSAGE_REMOVE_SUCCESS = "Successfully removed students from class ";

    private final Index toEditClassIndex;
    private final List<Index> studentIndicesToRemove;

    /**
     * Constructs a remove from class command.
     *
     * @param indexArray Array with index of class to remove from and index of students.
     */
    public RemoveFromClassCommand(ArrayList<Index> indexArray) {
        requireNonNull(indexArray);

        //the first index is the index of class in filtered class list that students would be removed from
        toEditClassIndex = indexArray.get(0);

        //the remaining indices are those of the students in filtered student list
        studentIndicesToRemove = indexArray.subList(1, indexArray.size());
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {

        //get class to remove from
        List<TuitionClass> lastShownClassList = model.getFilteredTuitionClassList();
        TuitionClass classToRemoveFrom = lastShownClassList.get(toEditClassIndex.getZeroBased());

        //get names to be removed
        StudentNameList currentStudentNameList = classToRemoveFrom.getStudentList();
        checkIndicesAreValid(studentIndicesToRemove, currentStudentNameList);
        List<Student> filteredStudentList = model.getFilteredStudentList();
        currentStudentNameList.sortListByList(filteredStudentList);
        ArrayList<Name> namesToRemove = createNewNameList(studentIndicesToRemove, currentStudentNameList);


        //get updated student list
        StudentNameList updatedStudentNameList = new StudentNameList();
        updatedStudentNameList.addAll(currentStudentNameList);
        updatedStudentNameList.removeAll(namesToRemove);

        //create edit class descriptor
        EditClassCommand.EditClassDescriptor editClassDescriptor = new EditClassCommand.EditClassDescriptor();
        editClassDescriptor.setUniqueNameList(updatedStudentNameList);

        //swap out old tuition class with new tuition class
        TuitionClass editedClass = EditClassCommand.createEditedClass(classToRemoveFrom, editClassDescriptor);
        model.setClass(classToRemoveFrom, editedClass);

        return new CommandResult(String.format(MESSAGE_REMOVE_SUCCESS, editedClass));
    }

    private ArrayList<Name> createNewNameList(List<Index> studentIndices, StudentNameList nameList) {
        ArrayList<Name> newNameList = new ArrayList<>();
        studentIndices.stream().forEach(index -> {
            Name name = nameList.get(index.getZeroBased());
            newNameList.add(name);
        });
        return newNameList;
    }

    private void checkIndicesAreValid(List<Index> studentIndices, StudentNameList nameList)
            throws CommandException {
        int size = Integer.valueOf(nameList.size());
        for (Index index : studentIndices) {
            if (index.getZeroBased() >= size) {
                throw new CommandException(Messages.MESSAGE_INVALID_CLASS_DISPLAYED_INDEX);
            }
        }
    }
}
