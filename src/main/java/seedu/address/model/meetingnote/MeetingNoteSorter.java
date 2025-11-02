package seedu.address.model.meetingnote;

import java.util.Comparator;

/**
 * A sorter class that works on reminders
 */
public class MeetingNoteSorter implements Comparator<MeetingNote> {
    @Override
    public int compare(MeetingNote mn1, MeetingNote mn2) {
        return mn2.getCreatedAt().compareTo(mn1.getCreatedAt());
    }
}
