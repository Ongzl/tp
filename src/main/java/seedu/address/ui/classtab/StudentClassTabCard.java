package seedu.address.ui.classtab;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Student;
import seedu.address.ui.UiPart;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class StudentClassTabCard extends UiPart<Region> {

    private static final String FXML = "classTab/StudentListCard.fxml";
    private static final String ADDRESS_FIELD = "Address: ";
    private static final String MOBILE_FIELD = "Mobile: ";
    private static final String EMAIL_FIELD = "Email: ";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Student student;

    // Student
    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public StudentClassTabCard(Student student, int displayedIndex) {
        super(FXML);
        this.student = student;

        // Student
        id.setText(displayedIndex + ". ");
        name.setText(student.getName().fullName);
        phone.setText(MOBILE_FIELD + student.getPhone().value);
        address.setText(ADDRESS_FIELD + student.getAddress().value);
        email.setText(EMAIL_FIELD + student.getEmail().value);

        student.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof StudentClassTabCard)) {
            return false;
        }

        // state check
        StudentClassTabCard card = (StudentClassTabCard) other;
        return id.getText().equals(card.id.getText())
                && student.equals(card.student);
    }
}
