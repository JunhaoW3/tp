package seedu.address.model.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.meetingnote.MeetingNote;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.InsurancePolicy;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.reminder.Reminder;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static final ArrayList<Reminder> EMPTY_REMINDERS = new ArrayList<>();
    public static final ArrayList<MeetingNote> EMPTY_MEETING_NOTES = new ArrayList<>();

    public static final ArrayList<Reminder> SAMPLE_REMINDERS_ONE = createReminders(
            "Follow up with client on insurance quote", "2026-11-10 09:00",
            "Submit updated policy document", "2026-11-15 17:30",
            "Check premium renewal details", "2026-12-01 10:00");
    public static final ArrayList<Reminder> SAMPLE_REMINDERS_TWO = createReminders(
            "Call to confirm appointment", "2027-11-03 14:00",
            "Prepare meeting agenda for investment review", "2028-11-05 08:30",
            "Send birthday greeting to client", "2029-11-18 09:30");
    public static final ArrayList<MeetingNote> SAMPLE_MEETING_NOTES_ONE = createMeetingNotes(
            "Scheduled next review session for December.", "2025-10-10 16:45",
            "Client requested quotation for additional coverage.", "2025-10-05 11:00",
            "Discussed client's insurance needs and recommended FamilyCare plan.", "2025-10-01 10:30");
    public static final ArrayList<MeetingNote> SAMPLE_MEETING_NOTES_TWO = createMeetingNotes(
            "Planned to introduce new product line next quarter.", "2025-09-30 09:30",
            "Client requested to adjust monthly savings plan.", "2025-09-25 10:00",
            "Explained investment risk profile and portfolio diversification.", "2025-09-20 14:15");
    private static ArrayList<Reminder> createReminders(String... data) {
        ArrayList<Reminder> list = new ArrayList<>();
        for (int i = 0; i < data.length; i += 2) {
            list.add(new Reminder(data[i], data[i + 1]));
        }
        return list;
    }

    private static ArrayList<MeetingNote> createMeetingNotes(String... data) {
        ArrayList<MeetingNote> list = new ArrayList<>();
        for (int i = 0; i < data.length; i += 2) {
            list.add(new MeetingNote(data[i], data[i + 1]));
        }
        return list;
    }

    /**
     * Returns an array of sample {@code Person} objects for populating the sample address book.
     * Each person includes example reminders, meeting notes, insurance policies, and tags.
     */
    public static Person[] getSamplePersons() {
        return new Person[]{
            new Person(new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                        new Address("Blk 30 Geylang Street 29, #06-40"),
                        getTagSet("friends"), SAMPLE_REMINDERS_ONE,
                        new InsurancePolicy("AIB HealthShield Gold Max"), SAMPLE_MEETING_NOTES_ONE, true,
                        false),
            new Person(new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com"),
                        new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),
                        getTagSet("colleagues", "friends"), SAMPLE_REMINDERS_TWO,
                        new InsurancePolicy("AIB Secure Flexi Term"), SAMPLE_MEETING_NOTES_TWO, true,
                        false),
            new Person(new Name("Charlotte Oliveiro"), new Phone("93210283"), new Email("charlotte@example.com"),
                        new Address("Blk 11 Ang Mo Kio Street 74, #11-04"),
                        getTagSet("neighbours"), EMPTY_REMINDERS,
                        new InsurancePolicy("Default Policy"), EMPTY_MEETING_NOTES, false, false),
            new Person(new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                        new Address("Blk 436 Serangoon Gardens Street 26, #16-43"),
                        getTagSet("family"), EMPTY_REMINDERS,
                        new InsurancePolicy("AIB Absolute Critical Cover"), SAMPLE_MEETING_NOTES_ONE, false,
                        false),
            new Person(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
                        new Address("Blk 47 Tampines Street 20, #17-35"),
                        getTagSet("classmates"), SAMPLE_REMINDERS_TWO,
                        new InsurancePolicy("AIB Accident Cashback Plan"), EMPTY_MEETING_NOTES, false,
                        false),
            new Person(new Name("Roy Balakrishnan"),
                        new Phone("92624417"),
                        new Email("royb@example.com"),
                        new Address("Blk 45 Aljunied Street 85, #11-31"),
                        getTagSet("colleagues"),
                        SAMPLE_REMINDERS_ONE,
                        new InsurancePolicy("AIB Lifetime Cover"),
                        SAMPLE_MEETING_NOTES_TWO,
                        false,
                        false)
        };
    }

    /**
     * Returns a sample {@code AddressBook} populated with sample {@code Person} data.
     */
    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
