package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;

public class ListArchivedCommand extends Command {

    public static final String COMMAND_WORD = "archivelist";
    public static final String MESSAGE_SUCCESS = "Listed all archived persons";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonListToShowArchived();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
