package seedu.address.storage;

import static seedu.address.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;

import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.reminder.Reminder;

/**
 * Jackson-friendly version of {@link Reminder}.
 */
class JsonAdaptedReminder {

    private final String header;
    private final String deadline;

    /**
     * Constructs a {@code JsonAdaptedReminder} with the given {@code reminderHeader reminderDeadline}.
     */
    @JsonCreator
    public JsonAdaptedReminder(@JsonProperty("header") String header,
                               @JsonProperty("deadline") String deadline) {
        this.header = header;
        this.deadline = deadline;
    }

    /**
     * Converts a given {@code Reminder} into this class for Jackson use.
     */
    public JsonAdaptedReminder(Reminder source) {
        header = source.header;
        deadline = source.deadline.format(DateTimeFormatter.ofPattern(Reminder.DATE_INPUT_PATTERN));
    }

    public String getReminderHeader() {
        return header;
    }

    public String getReminderDeadline() {
        return deadline;
    }

    /**
     * Converts this Jackson-friendly adapted Reminder object into the model's {@code Reminder} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted Reminder.
     */
    public Reminder toModelType() throws IllegalValueException {
        if (header == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "Header"));
        }
        if (!Reminder.isValidHeader(header)) {
            throw new IllegalValueException(Reminder.HEADER_MESSAGE_CONSTRAINTS);
        }
        if (deadline == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "Deadline"));
        }
        try {
            Reminder.isValidDeadline(deadline);
        } catch (IllegalArgumentException e) {
            throw new IllegalValueException(e.getMessage());
        }
        return new Reminder(header, deadline);
    }

}
