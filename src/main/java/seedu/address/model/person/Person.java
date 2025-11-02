package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.meetingnote.MeetingNote;
import seedu.address.model.meetingnote.MeetingNoteSorter;
import seedu.address.model.reminder.Reminder;
import seedu.address.model.reminder.ReminderSorter;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person implements Comparable<Person> {
    // Static comparator for sorting
    public static final Comparator<Person> STARRED_STATUS_COMPARATOR = Comparator
            .comparing(Person::isStarred, Comparator.reverseOrder())
            .thenComparing(Person::getName);
    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final Set<Tag> tags = new HashSet<>();
    private final ArrayList<Reminder> reminders = new ArrayList<>();
    private final InsurancePolicy policy;
    private final boolean isArchived;
    private final ArrayList<MeetingNote> meetingNotes = new ArrayList<>();
    private final boolean isStarred;
    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags,
                  ArrayList<Reminder> reminders, InsurancePolicy policy, ArrayList<MeetingNote> meetingNotes,
                  boolean isStarred) {
        requireAllNonNull(name, phone, email, address, tags, reminders, policy, meetingNotes, isStarred);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        this.reminders.addAll(reminders);
        this.policy = policy;
        this.meetingNotes.addAll(meetingNotes);
        this.isStarred = isStarred;
        this.isArchived = false;
    }

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags,
                  ArrayList<Reminder> reminders, InsurancePolicy policy, ArrayList<MeetingNote> meetingNotes,
                  boolean isStarred, boolean isArchived) {
        requireAllNonNull(name, phone, email, address, tags, reminders, policy, meetingNotes, isStarred, isArchived);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        this.reminders.addAll(reminders);
        this.policy = policy;
        this.meetingNotes.addAll(meetingNotes);
        this.isStarred = isStarred;
        this.isArchived = isArchived;
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public InsurancePolicy getPolicy() {
        return policy;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns the list of meeting notes tagged to this person
     */
    public ArrayList<MeetingNote> getMeetingNotes() {
        return new ArrayList<>(meetingNotes);
    }

    /**
     * @param meetingNote to be added to the person
     * @return if the person already has a similar meeting note
     */
    public boolean hasMeetingNote(MeetingNote meetingNote) {
        requireNonNull(meetingNote);
        return meetingNotes.contains(meetingNote);
    }

    /**
     * @param meetingNote to be added to the person
     * @return Person that has the meeting note added to them
     */
    public Person addMeetingNote(MeetingNote meetingNote) {
        requireNonNull(meetingNote);

        // Defensive copy of the existing meeting notes to avoid modifying the original set
        ArrayList<MeetingNote> updatedMeetingNotes = new ArrayList<>(meetingNotes);
        updatedMeetingNotes.add(meetingNote);
        updatedMeetingNotes.sort(new MeetingNoteSorter());

        return new Person(name, phone, email, address, tags, reminders, policy, updatedMeetingNotes, isStarred);
    }

    /**
     * @param meetingNote to be removed
     * @return Person with the meeting note removed
     */
    public Person removeMeetingNote(MeetingNote meetingNote) {
        requireNonNull(meetingNote);

        ArrayList<MeetingNote> updatedMeetingNotes = new ArrayList<>(this.meetingNotes);
        updatedMeetingNotes.remove(meetingNote);

        return new Person(name, phone, email, address, tags, reminders, policy, updatedMeetingNotes, isStarred);
    }


    /**
     * Returns the list of reminders tagged to this person
     */
    public ArrayList<Reminder> getReminders() {
        return new ArrayList<>(reminders);
    }

    /**
     * Returns the boolean isStarred state of person
     */
    public boolean isStarred() {
        return this.isStarred;
    }

    /**
     * @param isStarred boolean that isStarred will be set to
     * @return Person that has isStarred set to the parameter
     */
    public Person rebuildWithStarredStatus(boolean isStarred) {
        requireNonNull(isStarred);
        return new Person(name, phone, email, address, tags, reminders, policy, meetingNotes, isStarred);
    }

    /**
     * @param reminder to be added to the person
     * @return if the person already has a similar reminder
     */
    public boolean hasReminder(Reminder reminder) {
        requireNonNull(reminder);
        return reminders.contains(reminder);
    }

    /**
     * @param reminder to be added to the person
     * @return Person that has the reminder added to them
     */
    public Person addReminder(Reminder reminder) {
        requireNonNull(reminder);

        // Defensive copy of the existing reminders to avoid modifying the original set
        ArrayList<Reminder> updatedReminders = new ArrayList<>(reminders);
        updatedReminders.add(reminder);
        updatedReminders.sort(new ReminderSorter());

        return new Person(name, phone, email, address, tags, updatedReminders, policy, meetingNotes, isStarred);
    }

    /**
     * @param reminder to be removed
     * @return Person with the reminder removed
     */
    public Person removeReminder(Reminder reminder) {
        requireNonNull(reminder);

        ArrayList<Reminder> updatedReminders = new ArrayList<>(this.reminders);
        updatedReminders.remove(reminder);

        return new Person(name, phone, email, address, tags, updatedReminders, policy, meetingNotes, isStarred);
    }

    public boolean isArchived() {
        return isArchived;
    }

    /**
     * @return Person to be archived with their reminders cleared
     */
    public Person archive() {
        return new Person(name, phone, email, address, tags, reminders, policy, meetingNotes, isStarred, true);
    }

    public Person unarchive() {
        return new Person(name, phone, email, address, tags, reminders, policy, meetingNotes, isStarred, false);
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && tags.equals(otherPerson.tags)
                && policy.equals(otherPerson.policy);
        //TODO - Update to include reminders
        //may not need to implement as reminders is not core identity of person
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, tags);
        //TODO - Update to include reminders
    }

    @Override
    public int compareTo(Person other) {
        // Default sorting by name
        return this.name.compareTo(other.name);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("insurance policy", policy)
                .add("tags", tags)
                .add("reminders", reminders)
                .add("meeting notes", meetingNotes)
                .add("isStarred", isStarred)
                .toString();
    }

}
