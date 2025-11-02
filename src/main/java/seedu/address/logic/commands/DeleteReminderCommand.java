package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.reminder.Reminder;

/**
 * Deletes a reminder attached to a client using both of their displayed indexes.
 */
public class DeleteReminderCommand extends Command {

    public static final String COMMAND_WORD = "rDelete";
    public static final String COMMAND_WORD_LOWERCASE = "rdelete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the reminder identified by the index number used in the targeted client's reminder list.\n"
            + "Parameters: CLIENT_INDEX (must be a positive integer) REMINDER_INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1 1";

    public static final String MESSAGE_DELETE_REMINDER_SUCCESS = "Deleted Client %1$s's Reminder %2$d: %3$s";

    private static final Logger logger = LogsCenter.getLogger(DeleteReminderCommand.class);

    private final Index clientIndex;
    private final Index reminderIndex;

    /**
     * Creates an DeleteReminderCommand to delete the specified {@code Reminder} from the specified {@code Person}
     */
    public DeleteReminderCommand(Index clientIndex, Index reminderIndex) {
        requireAllNonNull(clientIndex, reminderIndex);
        this.clientIndex = clientIndex;
        this.reminderIndex = reminderIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();
        assert lastShownList != null : "Filtered person list should not be null";

        if (clientIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToDeleteFrom = lastShownList.get(clientIndex.getZeroBased());
        assert personToDeleteFrom != null : "Person to delete reminder from should not be null. Index: "
                + clientIndex.getZeroBased();

        ArrayList<Reminder> reminderList = personToDeleteFrom.getReminders();
        assert reminderList != null : "Person's reminder list should not be null";

        if (reminderIndex.getZeroBased() >= reminderList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_REMINDER_DISPLAYED_INDEX);
        }

        Reminder reminderToDelete = reminderList.get(reminderIndex.getZeroBased());
        assert reminderToDelete != null : "Reminder to be deleted should not be null. Index: "
                + reminderIndex.getZeroBased();

        logger.log(Level.INFO, "Deleting reminder " + reminderToDelete + " from " + personToDeleteFrom.getName());

        Person editedPerson = personToDeleteFrom.removeReminder(reminderToDelete);
        assert editedPerson != null : "Edited person should not be null";



        model.setPerson(personToDeleteFrom, editedPerson);
        model.deleteGeneralReminder(personToDeleteFrom, reminderToDelete);

        if (personToDeleteFrom.isArchived()) {
            personToDeleteFrom.getReminders().forEach(reminder -> {
                model.addGeneralReminder(personToDeleteFrom, reminder);
            });
            model.deleteGeneralReminder(personToDeleteFrom, reminderToDelete);
        }

        model.setToActiveList();
        model.refreshFilteredPersonList();

        return new CommandResult(String.format(MESSAGE_DELETE_REMINDER_SUCCESS,
                personToDeleteFrom.getName(),
                reminderIndex.getOneBased(),
                reminderToDelete));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteReminderCommand)) {
            return false;
        }

        DeleteReminderCommand otherDeleteReminderCommand = (DeleteReminderCommand) other;
        return clientIndex.equals(otherDeleteReminderCommand.clientIndex)
                && reminderIndex.equals(otherDeleteReminderCommand.reminderIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("clientIndex", clientIndex)
                .add("reminderIndex", reminderIndex)
                .toString();
    }
}

