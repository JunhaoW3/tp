package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

public class UnarchiveCommand extends Command {

    public static final String COMMAND_WORD = "unarchive";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Unarchives the person identified by the index number used in the archived list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Unarchived person: %1$s";
    public static final String MESSAGE_INVALID_INDEX = "The person index provided is invalid in the archived list";

    private final Index targetIndex;

    public UnarchiveCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> archivedList = model.getAddressBook().getArchivedPersonList();

        if (targetIndex.getZeroBased() >= archivedList.size()) {
            throw new CommandException(MESSAGE_INVALID_INDEX);
        }

        Person personToUnarchive = archivedList.get(targetIndex.getZeroBased());
        model.unarchivePerson(personToUnarchive);
        return new CommandResult(String.format(MESSAGE_SUCCESS, personToUnarchive));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        //instance handles nulls
        if (!(other instanceof UnarchiveCommand)) {
            return false;
        }

        UnarchiveCommand otherUnarchiveCommand = (UnarchiveCommand) other;
        return targetIndex.equals(otherUnarchiveCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
