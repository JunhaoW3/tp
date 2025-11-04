package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.meetingnote.MeetingNote.NOTE_MESSAGE_CONSTRAINTS;
import static seedu.address.model.reminder.Reminder.HEADER_MESSAGE_CONSTRAINTS;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.meetingnote.MeetingNote;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.InsurancePolicy;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.reminder.Reminder;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Any indices provided should be positive integers.\n"
            + "Enter the command word again without any arguments to view the correct command format.";
    public static final String MESSAGE_MORE_THAN_ONE_INDEX = "Please only enter one positive integer for the index.\n"
            + "Enter the command word again without any arguments to view the correct command format.";;
    public static final String MESSAGE_NOT_TWO_INDICES = "Please enter only two positive integers for the indices.\n"
            + "Enter the command word again without any arguments to view the correct command format.";;
    public static final String MULTIPLE_SPACES_REGEX = "\\s+";
    public static final String LOGGING_MESSAGE_PARSE_INDEX = "Successfully parsed index: ";
    public static final String LOGGING_MESSAGE_PARSE_INDICES = "Successfully parsed indices: ";
    public static final String LOGGING_MESSAGE_PARSE_FAILURE = "Failed to parse arguments";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        String[] parts = trimmedIndex.split(MULTIPLE_SPACES_REGEX);
        if (parts.length != 1) {
            throw new ParseException(MESSAGE_MORE_THAN_ONE_INDEX);
        }
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses {@code oneBasedDualIndex} into {@code Index[]} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * Example: "3 3" → [Index(3), Index(3)]
     * @throws ParseException if the specified indices is invalid (not non-zero unsigned integer).
     */
    public static Index[] parseDualIndex(String oneBasedDualIndex) throws ParseException {
        String trimmed = oneBasedDualIndex.trim();
        String[] parts = trimmed.split(MULTIPLE_SPACES_REGEX); // split the indices by one or more spaces
        if (parts.length != 2) {
            throw new ParseException(MESSAGE_NOT_TWO_INDICES);
        }
        //check 1st and 2nd index
        if (!StringUtil.isNonZeroUnsignedInteger(parts[0]) || !StringUtil.isNonZeroUnsignedInteger(parts[1])) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        Index[] indices = {Index.fromOneBased(Integer.parseInt(parts[0])),
                Index.fromOneBased(Integer.parseInt(parts[1]))};
        return indices;
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Parses and validates the given {@code String header}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code header} is invalid.
     */
    public static String parseHeader(String header) throws ParseException {
        requireNonNull(header);
        String trimmedHeader = header.trim();
        if (!Reminder.isValidHeader(trimmedHeader)) {
            throw new ParseException(HEADER_MESSAGE_CONSTRAINTS);
        }
        return trimmedHeader;
    }

    /**
     * Parses and validates the given {@code String deadline}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code deadline} is invalid.
     */
    public static String parseDeadline(String deadline) throws ParseException {
        requireNonNull(deadline);
        String trimmedDeadline = deadline.trim();
        try {
            Reminder.isValidDeadline(trimmedDeadline);
        } catch (IllegalArgumentException e) {
            throw new ParseException(e.getMessage());
        }
        return trimmedDeadline;
    }

    /**
     * Parses a {@code String insurance policy} into an {@code Insurance Policy}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code insurance policy} is invalid.
     */
    public static InsurancePolicy parsePolicy(String policy) throws ParseException {
        requireNonNull(policy);
        String trimmedPolicy = policy.trim();
        if (trimmedPolicy.isEmpty()) {
            throw new ParseException(InsurancePolicy.MESSAGE_CONSTRAINTS);
        }
        if (!InsurancePolicy.isValidPolicy(trimmedPolicy)) {
            throw new ParseException(InsurancePolicy.MESSAGE_CONSTRAINTS);
        }
        return new InsurancePolicy(trimmedPolicy);
    }

    /**
     * Parses and validates the given {@code String note}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code note} is invalid.
     */
    public static String parseNote(String note) throws ParseException {
        requireNonNull(note);
        String trimmedNote = note.trim();
        if (!MeetingNote.isValidNote(trimmedNote)) {
            throw new ParseException(NOTE_MESSAGE_CONSTRAINTS);
        }
        return trimmedNote;
    }
}
