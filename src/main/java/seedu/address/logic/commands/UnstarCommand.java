package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.person.Person.STARRED_STATUS_COMPARATOR;

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

/**
 * Removes starred status of a person identified using their displayed index from the address book.
 */
public class UnstarCommand extends Command {
    public static final String COMMAND_WORD = "unstar";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Removes starred status of the client identified by the index number "
            + "used in the displayed client list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_UNSTARRED_PERSON_SUCCESS = "Starred status removed from Client: %1$s";
    public static final String MESSAGE_PERSON_IS_UNSTARRED = "Chosen client is not starred";

    private static final Logger logger = LogsCenter.getLogger(UnstarCommand.class);
    private final Index targetIndex;

    /**
     * Constructs a {@code UnstarCommand} that removes star status
     * from the {@code Person} at the specified {@code Index}.
     */

    public UnstarCommand(Index targetIndex) {
        requireNonNull(targetIndex);
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();
        assert lastShownList != null : "Filtered person list should not be null";
        int zeroBasedTargetIndex = targetIndex.getZeroBased();

        if (zeroBasedTargetIndex >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        Person personToUnstar = lastShownList.get(zeroBasedTargetIndex);

        // Assert person is not null
        assert personToUnstar != null : "Client to unstar is null. Index: " + zeroBasedTargetIndex;

        // Check if person starred status has been removed
        if (!personToUnstar.isStarred()) {
            throw new CommandException(MESSAGE_PERSON_IS_UNSTARRED);
        }

        logger.log(Level.INFO, "Removing star status of person at index: " + targetIndex.getOneBased());
        logger.log(Level.FINE, "Person before removing star status: " + personToUnstar);
        Person unstarredPerson = personToUnstar.rebuildWithStarredStatus(false);

        assert unstarredPerson != null : "Unstarred person should not be null after removing star status";
        assert !unstarredPerson.isStarred() : "Newly unstarred person must have isStarred = false";
        model.setPerson(personToUnstar, unstarredPerson);
        model.sortPersons(STARRED_STATUS_COMPARATOR);
        model.refreshFilteredPersonList();
        return new CommandResult(String.format(MESSAGE_UNSTARRED_PERSON_SUCCESS, Messages.format(unstarredPerson)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UnstarCommand)) {
            return false;
        }

        UnstarCommand otherUnstarCommand = (UnstarCommand) other;
        return targetIndex.equals(otherUnstarCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
