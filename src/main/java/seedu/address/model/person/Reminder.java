package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

public class Reminder {

    private final String header;
    private String deadline;

    // Constructor for reminders with deadline and detailed info
    public Reminder(String header, String deadline, String reminderInfo) {
        requireNonNull(header);
        this.header = header;
        this.deadline = deadline;
    }

    // Constructor for reminders with no deadline and detailed info
    public Reminder(String header) {
        requireNonNull(header);
        this.header = header;
    }

    public String getHeader() {
        return this.header;
    }

    public String getDeadline() {
        return this.deadline;
    }

    @Override
    public String toString() {
        return String.format("[%s] on [%s]", this.getHeader(), this.getDeadline());
    }
}
