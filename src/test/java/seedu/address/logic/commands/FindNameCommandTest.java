package seedu.address.logic.commands;

//import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
//import static seedu.address.commons.core.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
//import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
//import static seedu.address.testutil.TypicalTimestable.CARL;
//import static seedu.address.testutil.TypicalTimestable.ELLE;
//import static seedu.address.testutil.TypicalTimestable.FIONA;
import static seedu.address.testutil.TypicalTimestable.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.predicates.NameContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindNameCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        NameContainsKeywordsPredicate firstPredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("first"));
        NameContainsKeywordsPredicate secondPredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("second"));

        FindNameCommand findFirstCommand = new FindNameCommand(firstPredicate);
        FindNameCommand findSecondCommand = new FindNameCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindNameCommand findFirstCommandCopy = new FindNameCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different predicate with different list of keywords -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    //todo ZW fix ur tests
    //@Test
    //public void execute_zeroKeywords_noPersonFound() {
    //    String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
    //    NameContainsKeywordsPredicate predicate = preparePredicate(" ");
    //    FindNameCommand command = new FindNameCommand(predicate);
    //    expectedModel.updateFilteredPersonList(predicate);
    //    assertCommandSuccess(command, model, expectedMessage, expectedModel);
    //    assertEquals(Collections.emptyList(), model.getFilteredStudentList());
    //}

    //@Test
    //public void execute_multipleKeywords_multiplePersonsFound() {
    //    String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
    //    NameContainsKeywordsPredicate predicate = preparePredicate("Kurz Elle Kunz");
    //    FindNameCommand command = new FindNameCommand(predicate);
    //    expectedModel.updateFilteredPersonList(predicate);
    //    assertCommandSuccess(command, model, expectedMessage, expectedModel);
    //    assertEquals(Arrays.asList(CARL, ELLE, FIONA), model.getFilteredStudentList());
    //}

    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordsPredicate}.
     */
    private NameContainsKeywordsPredicate preparePredicate(String userInput) {
        return new NameContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
