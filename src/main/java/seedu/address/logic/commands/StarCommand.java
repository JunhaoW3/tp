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
 * Stars a person identified using their displayed index from the address book.
 */
public class StarCommand extends Command {
    public static final String COMMAND_WORD = "star";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": stars the client identified by the index number used in the displayed client list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_STARRED_PERSON_SUCCESS = "Starred Client: %1$s";
    public static final String MESSAGE_PERSON_IS_STARRED = "Chosen client is already starred";

    private static final Logger logger = LogsCenter.getLogger(StarCommand.class);
    private final Index targetIndex;

    /**
     * Constructs a {@code StarCommand} that stars the {@code Person} at the specified {@code Index}.
     */
    public StarCommand(Index targetIndex) {
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
        Person personToStar = lastShownList.get(zeroBasedTargetIndex);

        // Assert person is not null
        assert personToStar != null : "Client to star is null. Index: " + zeroBasedTargetIndex;

        // Check if person has already been starred
        if (personToStar.isStarred()) {
            throw new CommandException(MESSAGE_PERSON_IS_STARRED);
        }

        logger.log(Level.INFO, "Starring person at index: " + targetIndex.getOneBased());
        logger.log(Level.FINE, "Person before starring: " + personToStar);
        Person starredPerson = personToStar.rebuildWithStarredStatus(true);

        assert starredPerson != null : "Starred person should not be null after starring them";
        assert starredPerson.isStarred() : "Newly starred person must have isStarred = true";
        model.editPersonGeneralReminderList(personToStar, starredPerson);
        model.setPerson(personToStar, starredPerson);
        model.sortPersons(STARRED_STATUS_COMPARATOR);
        model.refreshFilteredPersonList();
        return new CommandResult(String.format(MESSAGE_STARRED_PERSON_SUCCESS, Messages.format(starredPerson)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof StarCommand)) {
            return false;
        }

        StarCommand otherStarCommand = (StarCommand) other;
        return targetIndex.equals(otherStarCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
