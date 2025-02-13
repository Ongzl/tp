package seedu.address.model;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.person.Name;
import seedu.address.model.person.Student;
import seedu.address.model.tuitionclass.TuitionClass;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Student> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /** {@code Predicate} that always evaluate to true */
    Predicate<TuitionClass> PREDICATE_SHOW_ALL_CLASS = unused -> true;



    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasPerson(Student student);

    /**
     * Deletes the given person.
     * The person must exist in the address book.
     */
    void deletePerson(Student target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the address book.
     */
    void addPerson(Student student);

    /**
     * Returns true if a class with the same identity as {@code TuitionClass} exists in the address book.
     */
    boolean hasTuitionClass(TuitionClass tuitionClass);

    /**
     * Adds the given tuition class.
     * {@code TuitionClass} must not already exist in the address book.
     */
    void addTuitionClass(TuitionClass tuitionClass);

    /**
     * Deletes the given tuition class.
     * The class must exist in the address book.
     */
    void deleteTuitionClass(TuitionClass target);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    void setPerson(Student target, Student editedStudent);

    /** Returns an unmodifiable view of the filtered student list */
    ObservableList<Student> getFilteredStudentList();

    /**
     * Updates the filter of the filtered student list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Student> predicate);

    void updateFilteredClassList(Predicate<TuitionClass> predicateShowAllClass);

    /** Returns an unmodifiable view of the filtered tuition class list */
    ObservableList<TuitionClass> getFilteredTuitionClassList();

    /**
     * Replaces the given class {@code target} with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The Class identity of {@code editedClass} must not be the same as another existing class in the address book.
     */
    void setClass(TuitionClass target, TuitionClass editedClass);

    /**
     * Replaces the filteredTuitionClass with the classes.
     */
    void setClasses(List<TuitionClass> classes);

    /**
     * Replaces the filtered student list with the classes.
     */
    void setStudents(List<Student> students);

    /** Replaces the filtered student list */
    void replaceFilteredStudentList(List<Student> studentList);

    /** Replaces the filtered tuition class list */
    void replaceFilteredTuitionClassList(List<TuitionClass> tuitionClasses);

    /** executes update cascade after change of student name for {@code StudentNameList} */
    void updateClassStudentLists(Name newName, Name oldName);
}
