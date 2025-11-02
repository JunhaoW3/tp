package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.util.Pair;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;
import seedu.address.model.reminder.GeneralReminderSorter;
import seedu.address.model.reminder.Reminder;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    private final FilteredList<Person> archivedPersons;
    private final ObservableList<Pair<Person, Reminder>> generalReminderList;
    private boolean viewingArchivedList = false;
    private Predicate<Person> currentFilter;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
        archivedPersons = new FilteredList<>(this.addressBook.getPersonList(), Person::isArchived);
        updateFilteredPersonList(person -> !person.isArchived());
        this.currentFilter = p -> !p.isArchived();
        this.generalReminderList = FXCollections.observableArrayList();
        for (Person p : filteredPersons) {
            ArrayList<Reminder> pReminderList = p.getReminders();
            this.generalReminderList.addAll(pReminderList.stream()
                    .map(reminder -> new Pair<>(p, reminder)).toList());
        }
        this.generalReminderList.sort(new GeneralReminderSorter());
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    @Override
    public boolean hasArchivedPerson(Person person) {
        requireNonNull(person);
        return addressBook.getPersonList().stream().anyMatch(p -> p.isArchived() && p.isSamePerson(person));
    }

    @Override
    public void deletePerson(Person target) {
        addressBook.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        addressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        addressBook.setPerson(target, editedPerson);
    }

    @Override
    public void sortPersons(Comparator<Person> comparator) {
        addressBook.sortPersons(comparator);
    }

    @Override
    public void addGeneralReminder(Person person, Reminder target) {
        requireAllNonNull(person, target);
        this.generalReminderList.add(new Pair<Person, Reminder>(person, target));
        this.generalReminderList.sort(new GeneralReminderSorter());
        logger.info(String.format("Result: Reminder {%s} for {%s} also added to General Reminders", target, person));
    }

    @Override
    public void deleteGeneralReminder(Person person, Reminder target) {
        requireAllNonNull(person, target);
        this.generalReminderList.remove(new Pair<>(person, target));
        logger.info(
                String.format("Result: Reminder {%s} for {%s}  also deleted from General Reminders", target, person));
    }

    @Override
    public boolean isViewingArchivedList() {
        return viewingArchivedList;
    }

    @Override
    public void setViewingArchivedList(boolean viewing) {
        this.viewingArchivedList = viewing;
    }

    @Override
    public Predicate<Person> getCurrentFilter() {
        return currentFilter;
    }

    @Override
    public void setCurrentFilter(Predicate<Person> predicate) {
        requireNonNull(predicate);
        this.currentFilter = predicate;
        updateFilteredPersonList(predicate);
    }

    @Override
    public void setToArchiveList() {
        setCurrentFilter(Person::isArchived);
        setViewingArchivedList(true);
    }

    @Override
    public void setToActiveList() {
        setCurrentFilter(p -> !p.isArchived());
        setViewingArchivedList(false);
    }

    @Override
    public void setToGeneralList() {
        setCurrentFilter(p -> true);
        setViewingArchivedList(false);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public ObservableList<Person> getArchivedPersonList() {
        return archivedPersons;
    }

    @Override
    public ObservableList<Pair<Person, Reminder>> getGeneralReminderList() {
        return this.generalReminderList;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }
    @Override
    public void refreshFilteredPersonList() {
        if (viewingArchivedList) {
            updateFilteredPersonList(Person::isArchived);
        } else {
            updateFilteredPersonList(person -> !person.isArchived());
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return addressBook.equals(otherModelManager.addressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons);
    }

}
