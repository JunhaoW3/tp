package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_HEADER;

import java.time.LocalDateTime;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.reminder.Reminder;

/**
 * Adds a reminder to a person
 */
public class AddReminderCommand extends Command {
    public static final String COMMAND_WORD = "reminder";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds a reminder to the client identified by the index number in the displayed client list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_HEADER + "HEADER "
            + PREFIX_DEADLINE + "DEADLINE (must be in yyyy-MM-dd HH:mm format) \n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_HEADER + "birthday "
            + PREFIX_DEADLINE + "2025-12-25 09:00";

    public static final String MESSAGE_ADD_REMINDER_SUCCESS = "Reminder added to %1$s: %2$s";
    public static final String MESSAGE_DUPLICATE_REMINDER = "A similar reminder has already been added for this client";

    public static final String MESSAGE_ADD_REMINDER_SUCCESS_WITH_OLD_DEADLINE = MESSAGE_ADD_REMINDER_SUCCESS
            + ".\nPlease take note that deadline given is before the current date and time.";

    private final Index index;
    private final Reminder reminder;

    /**
     * @param index of the person to be tagged to the reminder
     * @param reminder reminder to be added
     */
    public AddReminderCommand(Index index, Reminder reminder) {
        requireNonNull(index);
        requireNonNull(reminder);
        this.index = index;
        this.reminder = reminder;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());

        // Check if duplicate reminder exists
        if (personToEdit.hasReminder(reminder)) {
            throw new CommandException(MESSAGE_DUPLICATE_REMINDER);
        }

        Person editedPerson = personToEdit.addReminder(reminder);

        model.setPerson(personToEdit, editedPerson);
        model.addGeneralReminder(editedPerson, reminder);

        model.setToActiveList();
        model.refreshFilteredPersonList();
        return this.reminder.getDeadline().isBefore(LocalDateTime.now())
            ? new CommandResult(String.format(MESSAGE_ADD_REMINDER_SUCCESS_WITH_OLD_DEADLINE,
                editedPerson.getName(), reminder))
            : new CommandResult(String.format(MESSAGE_ADD_REMINDER_SUCCESS, editedPerson.getName(), reminder));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddReminderCommand)) {
            return false;
        }

        AddReminderCommand otherAddReminderCommand = (AddReminderCommand) other;
        return index.equals(otherAddReminderCommand.index)
                && reminder.equals(otherAddReminderCommand.reminder);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("reminder", reminder)
                .toString();
    }
}
