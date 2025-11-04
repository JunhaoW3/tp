package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_HEADER;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.reminder.Reminder;

/**
 * Edits a reminder attached to a client using both of their displayed indexes and the new edited reminder
 */
public class EditReminderCommand extends Command {

    public static final String COMMAND_WORD = "rEdit";
    public static final String COMMAND_WORD_LOWERCASE = "redit";


    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the reminder identified by the index number used in the targeted client's reminder list.\n"
            + "Parameters: CLIENT_INDEX (must be a positive integer) "
            + "REMINDER_INDEX (must be a positive integer) "
            + PREFIX_HEADER + "HEADER "
            + PREFIX_DEADLINE + "DEADLINE (must be in yyyy-MM-dd HH:mm format) \n"
            + "Example: " + COMMAND_WORD + " 1 1 "
            + PREFIX_HEADER + "birthday "
            + PREFIX_DEADLINE + "2025-12-25 09:00";

    public static final String MESSAGE_EDIT_REMINDER_SUCCESS = "Edited Client %1$s's Reminder %2$d: from %3$s to %4$s";

    public static final String MESSAGE_REMINDER_ALREADY_EXISTS = "This reminder already exists.";

    private final Index clientIndex;
    private final Index reminderIndex;
    private final Reminder editedReminder;

    /**
     * @param clientIndex of the person to have the edited reminder
     * @param reminderIndex reminder to be edited
     * @param editedReminder the new edited reminder
     */
    public EditReminderCommand(Index clientIndex, Index reminderIndex, Reminder editedReminder) {
        requireAllNonNull(clientIndex, reminderIndex, editedReminder);
        this.clientIndex = clientIndex;
        this.reminderIndex = reminderIndex;
        this.editedReminder = editedReminder;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();
        if (clientIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        Person target = lastShownList.get(clientIndex.getZeroBased());

        ArrayList<Reminder> reminderList = target.getReminders();
        if (reminderIndex.getZeroBased() >= reminderList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_REMINDER_DISPLAYED_INDEX);
        }
        Reminder toEdit = reminderList.get(this.reminderIndex.getZeroBased());

        if (reminderList.contains(this.editedReminder)) {
            throw new CommandException((MESSAGE_REMINDER_ALREADY_EXISTS));
        }

        Person removedFromPerson = target.removeReminder(toEdit);
        Person addedToPerson = removedFromPerson.addReminder(this.editedReminder);
        //@@author oyanzhi
        model.deleteGeneralReminder(target, toEdit);
        model.addGeneralReminder(target, this.editedReminder);
        model.setPerson(target, addedToPerson);
        //@@author
        model.refreshFilteredPersonList();
        return new CommandResult(String.format(
                MESSAGE_EDIT_REMINDER_SUCCESS,
                target.getName(),
                reminderIndex.getOneBased(),
                toEdit,
                this.editedReminder
        ));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditReminderCommand)) {
            return false;
        }

        EditReminderCommand otherEditReminderCommand = (EditReminderCommand) other;
        return clientIndex.equals(otherEditReminderCommand.clientIndex)
                && reminderIndex.equals(otherEditReminderCommand.reminderIndex)
                && editedReminder.equals(otherEditReminderCommand.editedReminder);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("clientIndex", clientIndex)
                .add("reminderIndex", reminderIndex)
                .add("editedReminder", editedReminder)
                .toString();
    }
}
