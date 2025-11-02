package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static seedu.address.logic.commands.CommandTestUtil.VALID_REMINDER_DEADLINE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_REMINDER_HEADER_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_REMINDER_HEADER_BOB;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.reminder.Reminder;

public class EditReminderCommandTest {
    private static final Index INDEX_FIRST_REMINDER = Index.fromZeroBased(0);
    private static final Index INDEX_SECOND_REMINDER = Index.fromZeroBased(1);
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    @Test
    public void execute_editReminder_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Reminder reminder = new Reminder("Custom Reminder for Edit Test", VALID_REMINDER_DEADLINE);

        // will not require add reminder command
        personToEdit = personToEdit.addReminder(reminder);
        model.setPerson(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()), personToEdit);

        int reminderPosition = personToEdit.getReminders().indexOf(reminder);

        if (reminderPosition == -1) {
            // should be able to find the index since reminder is just added before this line
            fail();
        }

        Index reminderIndex = Index.fromZeroBased(reminderPosition);

        Reminder editedReminder = new Reminder("Custom Edited Reminder for Edit Test", VALID_REMINDER_DEADLINE);

        EditReminderCommand editReminderCommand = new EditReminderCommand(INDEX_FIRST_PERSON,
                reminderIndex,
                editedReminder);

        Person editedPerson = personToEdit.removeReminder(reminder);
        editedPerson = editedPerson.addReminder(editedReminder);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()), editedPerson);

        String expectedMessage = String.format(EditReminderCommand.MESSAGE_EDIT_REMINDER_SUCCESS,
                editedPerson.getName(),
                reminderIndex.getOneBased(),
                reminder,
                editedReminder);
        assertCommandSuccess(editReminderCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidClientIndex_throwsCommandException() {
        Index invalidClientIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        Reminder reminder = new Reminder(VALID_REMINDER_HEADER_AMY, VALID_REMINDER_DEADLINE);
        EditReminderCommand editReminderCommand = new EditReminderCommand(invalidClientIndex,
                INDEX_FIRST_REMINDER,
                reminder);
        assertCommandFailure(editReminderCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidReminderIndex_throwsCommandException() {
        Reminder reminder = new Reminder(VALID_REMINDER_HEADER_AMY, VALID_REMINDER_DEADLINE);
        Person person = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Index invalidReminderIndex = Index.fromOneBased(person.getReminders().size() + 1);
        EditReminderCommand editReminderCommand = new EditReminderCommand(INDEX_FIRST_PERSON,
                invalidReminderIndex,
                reminder);
        assertCommandFailure(editReminderCommand, model, Messages.MESSAGE_INVALID_REMINDER_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        Reminder reminder = new Reminder(VALID_REMINDER_HEADER_AMY, VALID_REMINDER_DEADLINE);
        Reminder differentReminder = new Reminder(VALID_REMINDER_HEADER_BOB, VALID_REMINDER_DEADLINE);

        EditReminderCommand editReminderFirstCommand = new EditReminderCommand(INDEX_FIRST_PERSON,
                INDEX_FIRST_REMINDER,
                reminder);
        EditReminderCommand editReminderSecondCommand = new EditReminderCommand(INDEX_FIRST_PERSON,
                INDEX_FIRST_REMINDER,
                reminder);

        // same object -> returns true
        assertTrue(editReminderFirstCommand.equals(editReminderFirstCommand));

        // same values -> returns true
        assertTrue(editReminderFirstCommand.equals(editReminderSecondCommand));

        // different types -> returns false
        // different reminder
        editReminderSecondCommand = new EditReminderCommand(INDEX_FIRST_PERSON,
                INDEX_FIRST_REMINDER,
                differentReminder);
        assertFalse(editReminderFirstCommand.equals(editReminderSecondCommand));


        // different client index
        editReminderSecondCommand = new EditReminderCommand(INDEX_SECOND_PERSON,
                INDEX_FIRST_REMINDER,
                reminder);
        assertFalse(editReminderFirstCommand.equals(editReminderSecondCommand));

        // different reminder index
        editReminderSecondCommand = new EditReminderCommand(INDEX_FIRST_PERSON,
                INDEX_SECOND_REMINDER,
                reminder);
        assertFalse(editReminderFirstCommand.equals(editReminderSecondCommand));

        // null -> returns false
        assertFalse(editReminderFirstCommand.equals(null));
    }
}
