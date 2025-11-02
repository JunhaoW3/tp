package seedu.address.model.reminder;

import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Objects;

/**
 * Represents a single reminder for a person
 * Guarantees: immutable; is valid as declared in {@link #isValidDeadline} and {@link #isValidHeader(String)}
 */
public class Reminder {

    public static final String VALIDATION_REGEX = "[^\\s].*";
    public static final String HEADER_MESSAGE_CONSTRAINTS = "Reminder can take any value but cannot be blank.";

    public static final String DATE_INPUT_PATTERN = "uuuu-MM-dd HH:mm";
    public static final String DATE_INPUT_MESSAGE = "yyyy-MM-dd HH:mm";
    public static final String DATE_INPUT_REGEX = "\\d{4}-\\b(0[1-9]|1[0-2])\\b-\\d{2} \\d{2}:\\d{2}";
    public static final String DEADLINE_MESSAGE_FORMAT_CONSTRAINT = "Deadline should be in the following format: "
            + DATE_INPUT_MESSAGE;
    public static final String DEADLINE_MESSAGE_INVALID_CONSTRAINT = "Deadline given is an invalid date or time.";

    public final LocalDateTime deadline;
    public final String header;

    /**
     * Constructs a {@code Reminder}.
     *
     * @param header A valid header.
     * @param deadline A valid deadline.
     */
    public Reminder(String header, String deadline) {
        requireAllNonNull(header, deadline);
        checkArgument(isValidHeader(header), HEADER_MESSAGE_CONSTRAINTS);
        isValidDeadline(deadline);
        this.header = header;
        this.deadline = LocalDateTime.parse(deadline,
                DateTimeFormatter.ofPattern(DATE_INPUT_PATTERN).withResolverStyle(ResolverStyle.STRICT));
    }

    /**
     * Returns true if string is a non-empty header
     */
    public static boolean isValidHeader(String header) {
        return header.matches(VALIDATION_REGEX);
    }

    /**
     * Returns true if string is a valid deadline after today (both format and date)
     */
    public static boolean isValidDeadline(String test) {
        checkArgument(isValidDeadlineFormat(test), DEADLINE_MESSAGE_FORMAT_CONSTRAINT);
        checkArgument(isValidDeadlineDate(test), DEADLINE_MESSAGE_INVALID_CONSTRAINT);
        return true;
    }

    /**
     * Returns true if string is of correct format
     */
    public static boolean isValidDeadlineFormat(String test) {
        return test.matches(DATE_INPUT_REGEX);
    }

    /**
     * Returns true if string in correctly parsed format is a valid date
     */
    public static boolean isValidDeadlineDate(String test) {
        try {
            LocalDateTime.parse(test,
                    DateTimeFormatter.ofPattern(DATE_INPUT_PATTERN).withResolverStyle(ResolverStyle.STRICT));
        } catch (DateTimeException e) {
            return false;
        }
        return true;
    }

    /**
     * Guarantees immutable from final field
     */
    public String getHeader() {
        return this.header;
    }

    /**
     * Guarantees immutable from final field
     */
    public LocalDateTime getDeadline() {
        return this.deadline;
    }

    public int compareDeadline(Reminder otherReminder) {
        return this.deadline.compareTo(otherReminder.deadline);
    }

    /**
     * Case-sensitive comparison of {@code headers} to ensure sorting has a proper order
     */
    public int compareHeader(Reminder otherReminder) {
        return this.header.compareTo(otherReminder.header);
    }

    @Override
    public int hashCode() {
        return Objects.hash(header, deadline);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_INPUT_PATTERN);
        String formattedDeadline = this.deadline.format(formatter);
        return String.format("%s, due by %s", this.header, formattedDeadline);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        //instanceof handles null
        if (!(other instanceof Reminder)) {
            return false;
        }

        // equality of headers is case-insensitive to prevent similar reminders to be added to the same client
        Reminder r = (Reminder) other;
        return r.deadline.equals(this.deadline) && r.header.equalsIgnoreCase(this.header);
    }
}
