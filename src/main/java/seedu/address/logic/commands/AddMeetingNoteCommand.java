package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.meetingnote.MeetingNote;
import seedu.address.model.person.Person;

/**
 * Adds a meeting note to a person
 */
public class AddMeetingNoteCommand extends Command {
    public static final String COMMAND_WORD = "note";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds a meeting note to the client identified by the index number in the displayed client list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "TEXT (cannot be empty) \n"
            + "Example: " + COMMAND_WORD + " 1 "
            + "Client wants to know more about xx policy";

    public static final String MESSAGE_ADD_MEETING_NOTE_SUCCESS = "Meeting note added to %1$s: %2$s";
    public static final String MESSAGE_DUPLICATE_MEETING_NOTE =
            "A similar meeting note has already been added to this client less than 2 hours ago";

    private final Index index;
    private final MeetingNote meetingNote;

    /**
     * @param index of the person to be tagged to the meeting note
     * @param meetingNote meeting note to be added
     */
    public AddMeetingNoteCommand(Index index, MeetingNote meetingNote) {
        requireAllNonNull(index, meetingNote);
        this.index = index;
        this.meetingNote = meetingNote;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();
        assert lastShownList != null : "Filtered client list should not be null";

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());

        // Check if duplicate meeting note was added less than 2 hours before
        if (personToEdit.hasMeetingNote(meetingNote)) {
            throw new CommandException(MESSAGE_DUPLICATE_MEETING_NOTE);
        }

        Person editedPerson = personToEdit.addMeetingNote(meetingNote);
        assert editedPerson != null : "Edited client should not be null after adding meeting note";

        model.setPerson(personToEdit, editedPerson);
        model.setToActiveList();
        model.refreshFilteredPersonList();
        return new CommandResult(String.format(MESSAGE_ADD_MEETING_NOTE_SUCCESS, editedPerson.getName(), meetingNote));
    }

    public MeetingNote getMeetingNote() {
        return this.meetingNote;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddMeetingNoteCommand)) {
            return false;
        }

        AddMeetingNoteCommand otherMeetingNoteCommand = (AddMeetingNoteCommand) other;
        return index.equals(otherMeetingNoteCommand.index)
                && meetingNote.equals(otherMeetingNoteCommand.meetingNote);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("meeting note", meetingNote)
                .toString();
    }
}
