package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.meetingnote.MeetingNote;
import seedu.address.model.person.Person;

/**
 * Deletes a meeting note attached to a client using both of their displayed indexes.
 */
public class DeleteMeetingNoteCommand extends Command {

    public static final String COMMAND_WORD = "nDelete";
    public static final String COMMAND_WORD_LOWERCASE = "ndelete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the meeting note identified by the index number used in the targeted client's "
            + "meeting notes list.\n"
            + "Parameters: CLIENT_INDEX (must be a positive integer) MEETING_NOTE_INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1 1";

    public static final String MESSAGE_DELETE_MEETING_NOTE_SUCCESS = "Deleted Client %1$s's Meeting note %2$d: %3$s";

    private final Index clientIndex;
    private final Index meetingNoteIndex;

    /**
     * Creates a DeleteMeetingNoteCommand to delete the specified {@code MeetingNote} from the specified {@code Person}
     */
    public DeleteMeetingNoteCommand(Index clientIndex, Index meetingNoteIndex) {
        requireAllNonNull(clientIndex, meetingNoteIndex);
        this.clientIndex = clientIndex;
        this.meetingNoteIndex = meetingNoteIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();
        assert lastShownList != null : "Filtered client list should not be null";

        if (lastShownList.size() < clientIndex.getOneBased()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToDeleteFrom = lastShownList.get(clientIndex.getZeroBased());
        assert personToDeleteFrom != null : "Client to delete from must not be null";
        ArrayList<MeetingNote> meetingNotes = personToDeleteFrom.getMeetingNotes();
        assert meetingNotes != null : "Meeting notes list must not be null";

        if (meetingNotes.size() < meetingNoteIndex.getOneBased()) {
            throw new CommandException(Messages.MESSAGE_INVALID_MEETING_NOTE_DISPLAYED_INDEX);
        }

        MeetingNote meetingNoteToDelete = meetingNotes.get(meetingNoteIndex.getZeroBased());
        Person editedPerson = personToDeleteFrom.removeMeetingNote(meetingNoteToDelete);
        assert editedPerson != null : "Edited client should not be null after adding meeting note";

        model.setPerson(personToDeleteFrom, editedPerson);
        model.setToActiveList();
        model.refreshFilteredPersonList();
        return new CommandResult(String.format(MESSAGE_DELETE_MEETING_NOTE_SUCCESS,
                personToDeleteFrom.getName(),
                meetingNoteIndex.getOneBased(),
                meetingNoteToDelete));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteMeetingNoteCommand)) {
            return false;
        }

        DeleteMeetingNoteCommand otherDeleteMeetingNoteCommand = (DeleteMeetingNoteCommand) other;
        return clientIndex.equals(otherDeleteMeetingNoteCommand.clientIndex)
                && meetingNoteIndex.equals(otherDeleteMeetingNoteCommand.meetingNoteIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("clientIndex", clientIndex)
                .add("meetingNoteIndex", meetingNoteIndex)
                .toString();
    }
}

