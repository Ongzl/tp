package seedu.address.logic.commands.classcommands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.commands.classcommands.EditClassCommand.EditClassDescriptor;
import static seedu.address.logic.commands.classcommands.EditClassCommand.MESSAGE_DUPLICATE_PERSON;

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
import seedu.address.model.tuitionclass.exceptions.DuplicateStudentInClassException;

public class AddToClassCommand extends Command {

    public static final String COMMAND_WORD = "addtoclass";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds students to a class by the index number used "
            + "in the displayed class and person list.\n"
            + "Parameters: CLASS_INDEX "
            + "STUDENT_INDEX...\n"
            + "Example: " + COMMAND_WORD + " "
            + "1 "
            + "2 3 5 "
            + "(adds students indexed 2, 3 and 5 to class indexed 1)";

    public static final String MESSAGE_ADD_SUCCESS = "Successfully added students to class ";

    private final Index toEditClassIndex;
    private final List<Index> studentIndices;

    /**
     * Constructs AddToClassCommand.
     *
     * @param indexArray ArrayList of index.
     */
    public AddToClassCommand(ArrayList<Index> indexArray) {
        requireNonNull(indexArray);

        //the first index is the index of class in filtered class list that students would be added to
        toEditClassIndex = indexArray.get(0);

        //the remaining indices are those of the students in filtered student list
        studentIndices = indexArray.subList(1, indexArray.size());
    }

    //TODO: need consider if class alr has that name
    @Override
    public CommandResult execute(Model model) throws CommandException {

        //get names to be added
        List<Student> lastShownStudentList = model.getFilteredStudentList();
        checkIndicesAreValid(studentIndices, lastShownStudentList);
        ArrayList<Name> namesToAdd = createNameList(studentIndices, lastShownStudentList);

        //get class to add to
        List<TuitionClass> lastShownClassList = model.getFilteredTuitionClassList();
        TuitionClass classToAddTo = lastShownClassList.get(toEditClassIndex.getZeroBased());

        //get updated student list
        StudentNameList currentStudentNameList = classToAddTo.getStudentList();
        StudentNameList updatedStudentNameList = new StudentNameList();

        try {
            updatedStudentNameList.addAll(currentStudentNameList);
            updatedStudentNameList.addAll(namesToAdd);
        } catch (DuplicateStudentInClassException e) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON + e.getMessage());
        }


        //create edit class descriptor
        EditClassDescriptor editClassDescriptor = new EditClassDescriptor();
        editClassDescriptor.setUniqueNameList(updatedStudentNameList);

        //swap out old tuition class with new tuition class
        TuitionClass editedClass = EditClassCommand.createEditedClass(classToAddTo, editClassDescriptor);
        model.setClass(classToAddTo, editedClass);

        return new CommandResult(String.format(MESSAGE_ADD_SUCCESS, editedClass));
    }

    private ArrayList<Name> createNameList(List<Index> studentIndices, List<Student> lastShownStudentList) {
        ArrayList<Name> nameList = new ArrayList<>();
        studentIndices.stream().forEach(index -> {
            Student student = lastShownStudentList.get(index.getZeroBased());
            nameList.add(student.getName());
        });
        return nameList;
    }

    private void checkIndicesAreValid(List<Index> studentIndices, List<Student> lastShownStudentList)
            throws CommandException {
        int size = lastShownStudentList.size();
        for (Index index : studentIndices) {
            if (index.getZeroBased() >= size) {
                throw new CommandException(Messages.MESSAGE_INVALID_CLASS_DISPLAYED_INDEX);
            }
        }
    }


}
