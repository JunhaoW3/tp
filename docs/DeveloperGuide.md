---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---


# FinHub Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

FinHub was adapted from AddressBook-Level3 (AB3) created by the [SE-EDU initiative](https://se-education.org)

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component" width="800"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="650" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components).

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

<br>

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Reminders Feature

#### Challenge
* Reminders was the first feature that we deemed as important for MVP - as we decided that reminders will be stored as an `ArrayList<Reminder>` for each `Person`, we needed to expand the current model, logic and storage to include it.

#### Implementation Details

Reminders are set up as a `Reminder.java` class with two key internal fields.
* `String` header
* `LocalDateTime` deadline

<br>

The `add`, `delete` and `edit` reminders commands are then designed as separate commands based on a new field in the `Person.java` class where reminders are internally stored as an `ArrayList<Reminder>` for every `Person` object initialised.

<br>

##### Command Implementation
* ###### Add Reminder
    * The user will execute `reminder CLIENT_INDEX h/HEADER d/yyyy-MM-dd HH:mm` which initialises a new `Reminder.java` with the given header and deadline after parsing of the user input is done by `AddReminderCommandParser.java` and validation of header and deadline by `Reminder.java`.

<br>

* A newly initialised `AddReminderCommand.java` will then have the fields before `AddReminderCommand#exceute` is called.
    * `CLIENT_INDEX`
    * and the previously initialised `Reminder.java`

<br>

* Upon execution of the `AddReminderCommand`, the method `Person#addReminder` is called on the `Person` with the given `CLIENT_INDEX` in the model which takes in the new `Reminder.java` as parameter and initialises a new `ArrayList<Reminder>` with the `Reminder.java` added to the previous `ArrayList<Reminder>` of the `Person` and returns a new `Person` object with the newly updated `ArrayList<Reminder>`

<br>

--------------------------------------------------------------------------------------------------------------------

* ###### Delete Reminder
    * The user will execute `rDelete CLIENT_INDEX REMINDER_INDEX` which are based on the indexes on the displayed GUI after parsing of the user input is done by `DeleteReminderCommandParser.java`.

<br>

* This initialises a new `DeleteReminderCommand.java` with two fields before `DeleteReminderCommand#exceute` is called.
    * `CLIENT_INDEX`
    * `REMINDER_INDEX`

<br>

* Upon execution of the `DeleteReminderCommand`, the method `Person#removeReminder` is called on the `Person` with the given `CLIENT_INDEX` in the model which takes in the `Reminder.java` as parameter and initialises a new `ArrayList<Reminder>` with the `Reminder.java` removed from the previous `ArrayList<Reminder>` of the `Person` and returns a new `Person` object with the newly updated `ArrayList<Reminder>`

<br>

--------------------------------------------------------------------------------------------------------------------

* ###### Edit Reminder
    * The user will execute `rEdit CLIENT_INDEX REMINDER_INDEX h/HEADER d/yyyy-MM-dd HH:mm` which initialises a new `Reminder.java` with the given header and deadline after parsing of the user input is done by `EditReminderCommandParser.java` and validation of header and deadline by `Reminder.java`.

<br>

* This initialises a new `EditReminderCommand.java` with three fields before `EditReminderCommand#execute` is called.
    * `CLIENT_INDEX`
    * `REMINDER_INDEX` which is the index of the reminder to be edited.
    * `EDITED_REMINDER` which is the new `Reminder.java` as parsed and initialised before.

<br>

* Upon execution of the `EditReminderCommand`
    1. The method `Person#removeReminder` is called on the `Person` with the given `CLIENT_INDEX` in the model which utilises `REMINDER_INDEX` to locate the `Reminder.java` in the `Person` and removes the reminder similar to how `DeleteReminderCommand` is implemented.

<br>

    2. The method `Person#addReminder` is then called on the `Person` with the given `ClIENT_INDEX` in the model which takes in `EDITED_REMINDER` as parameter and adds it to the `Person` similar to how `AddReminderCommand` is implemented.

<br>

--------------------------------------------------------------------------------------------------------------------

### Meeting Notes Feature

#### Challenges
* Next, we wanted a convenient way for users to record and review key discussions with clients in a simple yet efficient way. We needed to decide what order to store and display meeting notes — whether to prioritise a simple internal logic or a more intuitive user interface.

#### Implementation Details

Meeting Notes are set up as a `MeetingNote.java` class with two key internal fields.
* `String` TEXT
* `LocalDateTime` date and time at which the meeting note was created.

<br>

The `add` and `delete` meeting note commands are then designed as separate commands based on a new field in the `Person.java` class where meeting notes are internally stored as an `ArrayList<MeetingNote>` for every `Person` object initialised.

<br>

##### Command Implementation
* ###### Add Meeting Note
    * The user will execute `note CLIENT_INDEX TEXT` which initialises a new `MeetingNote.java` with the given text and the current LocalDateTime after parsing of the user input is done by `AddMeetingNoteCommandParser.java` and validation of the text by `MeetingNote.java`.

  <br>

    * A newly initialised `AddMeetingNoteCommand.java` will then have the fields before `AddMeetingNoteCommand#exceute` is called.
        * `CLIENT_INDEX`
        * and the previously initialised `MeetingNote.java`

  <br>

    * Upon execution of the `AddMeetingNoteCommand`, the method `Person#addMeetingNote` is called on the `Person` with the given `CLIENT_INDEX` in the model which takes in the new `MeetingNote.java` as parameter and initialises a new `ArrayList<MeetingNote>` with the `MeetingNote.java` added to the previous `ArrayList<MeetingNote>` of the `Person` and returns a new `Person` object with the newly updated `ArrayList<MeetingNote>`.

<br>

--------------------------------------------------------------------------------------------------------------------

* ###### Delete Meeting Note
    * The user will execute `nDelete CLIENT_INDEX MEETING_NOTE_INDEX` which are based on the indexes on the displayed GUI after parsing of the user input is done by `DeleteMeetingNoteCommandParser.java`.

  <br>

    * This initialises a new `DeleteMeetingNoteCommand.java` with two fields before `DeleteMeetingNoteCommand#exceute` is called.
        * `CLIENT_INDEX`
        * `MEETING_NOTE_INDEX`

  <br>

    * Upon execution of the `DeleteMeetingNoteCommand`, the method `Person#removeMeetingNote` is called on the `Person` with the given `CLIENT_INDEX` in the model which takes in the `MeetingNote.java` as parameter and initialises a new `ArrayList<MeetingNote>` with the `MeetingNote.java` removed from the previous `ArrayList<MeetingNote>` of the `Person` and returns a new `Person` object with the newly updated `ArrayList<MeetingNote>`.

<br>

--------------------------------------------------------------------------------------------------------------------
<!-- @@author Aloysius Pek Yong Jun -->

### Star Client Feature

#### Challenges
* The goal was to provide an easy way for users to "star" clients. This would allow users to mark clients they want to highlight or prioritize, and later allow them to remove the starred status if needed.
* There was some debate on whether to display starred clients in a separate list or to prioritize them within the existing list. We decided to integrate starred clients into the main list, sorting them to appear at the top, which required adding a sorting logic in `Person.java` too.

#### Implementation Details

To implement the star client feature, we focus on the following areas:
1. **Model**: Each `Person` object has a boolean field `isStarred` to mark whether a client is starred or not. The logic of starring or unstarring a client updates this field.

&nbsp;

2. **Commands**: Two main commands are created:
    - `StarCommand`: For marking a client as starred.
    - `UnstarCommand`: For removing the starred status of a client.

&nbsp;

3. **Parser**: Command parsing logic to ensure that the user's input is valid and parsed correctly into command objects.

&nbsp;

4. **Sorting**: We need to ensure that when clients are starred or unstarred, the list is updated accordingly (both in terms of the internal storage and the displayed user interface).
   <br>

##### Command Implementation
* ###### Star Command
    * **Objective**: Marks a `Person` (Client) as starred based on their displayed index in the list.

  &nbsp;

    * **Command Syntax**: `star CLIENT_INDEX`
        * Parameters:
            * `CLIENT_INDEX`: The index of the client (starts from 1 in the displayed list).
        * Usage Example:
            *  `star 1`: Stars the client at index 1.

  &nbsp;

    * **Key Steps**:
        1. *Input parsing:*
            * The `StarCommandParser.java` parses the input string. If the input is empty, a `ParseException` is thrown.
            * The `ParserUtil#parseIndex(String args)` method is used to parse the client index, which is logged for debugging.

      &nbsp;

        2. *Check if Already Starred:*
            * The command retrieves the `Person` object using the parsed index.
            * The `Person#isStarred()` method checks if the client is already starred.
            * If the client is already starred, a `CommandException` is thrown with the message "Chosen client is already starred."

      &nbsp;

        3. *Update Starred Status:*
            * If the client is unstarred, The command updates the client's starred status by calling the `Person#rebuildWithStarredStatus(boolean isStarred)` method. This method creates a new Person object with the updated starred status (set to `true`).
            * The updated `Person` is saved back into the model using `Model#setPerson(Person target, Person editedPerson)`.

      &nbsp;

        4. *Re-sort the Client List:*
            * After starring a client, the list of clients is re-sorted by calling `Model#sortPersons(Comparator<Person> comparator)`. This ensures that all starred clients are prioritized and appear at the top of the list.

      &nbsp;

        5. *Return Command Result:*
            * The command returns a `CommandResult` with a success message, confirming that the client has been starred.

<br>

* ###### Unstar Command
    * **Objective**: Removes the starred status of a `Person` (Client), based on their displayed index in the list.

  &nbsp;

    * **Command Syntax**: `unstar CLIENT_INDEX`
        * Parameters:
            * `CLIENT_INDEX`: The index of the client (starts from 1 in the displayed list).
        * Usage Example:
            *  `unstar 1`: Removes star status from the client at index 1.

  &nbsp;

    * **Key Steps**:
        1. *Input parsing:*
            * The `UnstarCommandParser.java` parses the input string. If the input is empty, a `ParseException` is thrown.
            * The `ParserUtil#parseIndex(String args)` method is used to parse the client index, which is logged for debugging.

      &nbsp;

        2. *Check if Already Unstarred:*
            * The command retrieves the `Person` object using the parsed index.
            * The `Person#isStarred()` method checks if the client is already unstarred.
            * If the client is already unstarred, a `CommandException` is thrown with the message "Chosen client is not starred."

      &nbsp;

        3. *Update Starred Status:*
            * If the client is starred, The command updates the client's starred status by calling the `Person#rebuildWithStarredStatus(boolean isStarred)` method. This method creates a new Person object with the updated starred status (set to `false`).
            * The updated `Person` is saved back into the model using `Model#setPerson(Person target, Person editedPerson)`.

      &nbsp;

        4. *Re-sort the Client List:*
            * After unstarring a client, the list of clients is re-sorted by calling `Model#sortPersons(Comparator<Person> comparator)`. This ensures that the unstarred client is moved to its appropriate position in the list.

      &nbsp;

        5. *Return Command Result:*
            * The command returns a `CommandResult` with a success message, confirming that the starred status has been removed from the client.

<br>

#### Sequence Diagram
The sequence diagram below illustrates the flow of interactions when the user enters the `star 1` command. It shows how the command is parsed, the person at the specified index is retrieved, and the starred status is updated in the model. Key components include the `CommandBox`, `LogicManager`, `StarCommandParser`, and `Model`. Due to the diagram's detailed nature, it may appear small. If it's unclear, zooming in will provide better clarity of the individual components and interactions.

<puml src="diagrams/star-feature/StarSequenceDiagram.puml" alt="Interactions for the `star 1` Command" />


<br>
<br>

#### Activity Diagram
The activity diagram outlines the detailed workflow that happens when the `star 1` command is executed. It shows the decision points for validating the index, checking if the person is already starred, and updating the model accordingly. If successful, the system returns a success message.

<puml src="diagrams/star-feature/StarActivityDiagram.puml"/>

<!-- @@author-->

<br>
<br>

--------------------------------------------------------------------------------------------------------------------
### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
    * Pros: Easy to implement.
    * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
    * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
    * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


<br>

--------------------------------------------------------------------------------------------------------------------
### Insurance Policy Feature

#### Challenge
* We introduced an **`InsurancePolicy`** field that must behave like other AB3-style value objects (trimmed, validated, immutable), while also:
    - Integrating with **Add**/**Edit** client flows (parsing, error messages).
    - Rendering consistently in the UI without breaking existing cards/layouts.

#### Implementation Details

`InsurancePolicy` is implemented as a value object:

- **Class:** `InsurancePolicy.java`
- **Internal field:**
    * `String value` (trimmed in the constructor, guaranteed valid thereafter)

- **Constraints:**
    * Must contain **at least one** letter or digit.
    * Allowed characters: letters, digits, spaces, and these symbols: `+ / & ( ) ' . , -`
    * Leading/trailing spaces are **trimmed** before validation.

##### Command Integration
* ###### Add Client
    * The user will execute `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS ip/INSURANCE_POLICY [t/TAG]…​`. `AddCommandParser.java` extracts `ip/…` and calls `ParserUtil.parseInsurancePolicy(...)`, which constructs `InsurancePolicy.java` or throws with MESSAGE_CONSTRAINTS.
      `Person` is built with the validated and non-optional `InsurancePolicy` field and added to the clients list.

--------------------------------------------------------------------------------------------------------------------
* ##### Edit Client
    * The user will execute `edit CLIENT_INDEX ip/INSURANCE_POLICY`. `EditCommandParser.java` treats `ip/…` as an optional edited field; if present, it validates and sets the new `InsurancePolicy.java`.
      A new `Person` instance is created with the updated policy (immutability preserved), replacing the old one in `UniquePersonList.java`

--------------------------------------------------------------------------------------------------------------------
## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

<br>

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* Insurance Agent who manage multiple clients
* Who may need to find contacts of particular clients
* Who has to keep track of many meetings with clients

**Value proposition**: Centralised platform for keeping track of all work-related information, organising large amounts of information and helping with progress management of clients.

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                | I want to …​                                                          | So that I can…​                                                                                                                                    |
|----------|------------------------|-----------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------|
| `* * *`  | user                   | save the data I input                                                 | not have to input them again on start-up                                                                                                           |
| `* * *`  | user                   | search clients by name                                                | easily find a specific client’s information                                                                                                        |
| `* * *`  | user                   | delete reminders                                                      | remove any outdated/non-necessary/wrongly set-up reminders                                                                                         |
| `* * *`  | user                   | add a new client's contact                                            | keep track of his information                                                                                                                      |
| `* * *`  | user                   | delete a client's contact                                             | keep my contacts clean                                                                                                                             |
| `* * *`  | user                   | set reminders for policy renewal dates, birthdays, or important dates | maintain strong client relationships and be reminded to follow up                                                                                  |
| `* * *`  | user                   | use CLI                                                               | easily find what I am looking for rather than navigating a GUI                                                                                     |
| `* * *`  | user                   | record clients' insurance policy                                      | easily review what coverage your clients already have and identify gaps or overlaps.                                                               |
| `* *`    | user                   | search clients by phone number                                        | find specific clients through their phone number                                                                                                   |
| `* *`    | user                   | search clients by email                                               | find specific clients through their email                                                                                                          |
| `* *`    | user                   | mark my client as onboarded                                           | easily keep track of which clients are already onboarded and who is yet to be onboarded                                                            |
| `* *`    | user                   | receive alerts                                                        | maintain regular engagement                                                                                                                        |
| `* *`    | user                   | see upcoming policy renewal dates                                     | proactively reach out to clients before policy expires                                                                                             |
| `* *`    | user                   | keep track of my client's deadline that is coming soon                | better prioritise and manage my time                                                                                                               |
| `* *`    | forgetful user         | be alerted when I try to add a duplicate client                       | keep my contacts organised                                                                                                                         |
| `* *`    | user                   | edit a client's information                                           | update changing information                                                                                                                        |
| `* *`    | user                   | record client meeting notes                                           | remember key discussion points with each client                                                                                                    |
| `* *`    | user                   | view revenue/profit per client                                        | identify my most valuable clients.                                                                                                                 |
| `* *`    | user                   | record client preferences for preferred communication channel         | contact them in the best way possible                                                                                                              |
| `* *`    | user                   | group my clients by policy type                                       | quickly filter relevant contacts                                                                                                                   |
| `* *`    | user                   | edit reminders                                                        | make changes to reminders when I change my mind                                                                                                    |
| `*`      | user                   | be able to tag a client with a custom label                           | customize the grouping of clients                                                                                                                  |
| `*`      | user                   | bookmark “star clients” for quick access                              | jump to top clients immediately                                                                                                                    |
| `*`      | user                   | assign priority levels to tasks                                       | manage time more efficiently                                                                                                                       |
| `*`      | user                   | view a client history timeline                                        | see a chronological record of interactions                                                                                                         |
| `*`      | user                   | see in-app tutorials                                                  | easily familiarise with FinHub's features                                                                                                          |
| `*`      | user with many clients | save the data I enter                                                 | save time re-entering all data each time I open the app                                                                                            |
| `*`      | user                   | view a dashboard summary of activities                                | get an overview of my workload                                                                                                                     |
| `*`      | user                   | keep track of insurance claims requested by clients                   | easily see what claims are made for the different clients, as well as when the claim has been requested                                            |
| `*`      | user                   | archive inactive clients                                              | keep my workspace uncluttered                                                                                                                      |
| `*`      | user                   | export data as a spreadsheet                                          | view it more easily                                                                                                                                |
| `*`      | user                   | access FinHub with a password                                         | keep my client's information confidential                                                                                                          |


### Use cases

(For all use cases below, the **System** is `FinHub` and the **Actor** is the `user`, unless specified otherwise)

**Use case: UC01 - Search client by name**

**MSS**

1.  The user searches for the client by their name.
2.  FinHub shows details of clients with matching names.

    Use case ends.

**Extensions**

* 1a. FinHub detects an error in the command entered.
    * 1a1. FinHub displays an error message and prompts the user to input again.
    * 1a2. The user re-enters the command to search the client by name.

      Steps 1a1-1a2 are repeated until the command and data entered are correct.

      Use case resumes at step 2.

* 2a. The list is empty.

  Use case ends.

**Use case: UC02 - Search client by email**

**MSS**

1.  The user searches for the client by their email.
2.  FinHub shows details of clients with matching emails.

    Use case ends.

**Extensions**

* 1a. FinHub detects an error in the command entered.
    * 1a1. FinHub displays an error message and prompts the user to input again.
    * 1a2. The user re-enters the command to search the client by email.

      Steps 1a1-1a2 are repeated until the command and data entered are correct.

      Use case resumes at step 2.

* 2a. The list is empty.

  Use case ends.

**Use case: UC03 - Search client by phone number**

**MSS**

1.  The user searches for the client by their phone number.
2.  FinHub shows details of clients with matching phone number.

    Use case ends.

**Extensions**

* 1a. FinHub detects an error in the command entered.
    * 1a1. FinHub displays an error message and prompts the user to input again.
    * 1a2. The user re-enters the command to search the client by phone number.

      Steps 1a1-1a2 are repeated until the command and data entered are correct.

      Use case resumes at step 2.

* 2a. The list is empty.

  Use case ends.

**Use case: UC04 - Mark client as complete**

**MSS**

1. The user <u>search client by their name (UC01)</u>.
2. FinHub displays a list of clients.
3. The user selects the client to be marked as complete.
4. FinHub successfully marks the client as complete and displays a success message.

   Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. FinHub detects an error in the command entered.
    * 3a1. FinHub displays an error message and prompts the user to input again.
    * 3a2. The user re-enters the command to mark client as completed.

      Steps 3a1-3a2 are repeated until the command and data entered are correct.

      Use case resumes at step 4.

**Use case: UC05 - Add a client's contact**

**MSS**

1.  The user enters the client's details (name, telephone number, email address, insurance policy).
2.  FinHub validates the input.
3.  FinHub adds the new client into the address book.
4.  FinHub displays a confirmation message.

    Use case ends.

**Extensions**

* 2a. The user enters invalid/missing inputs.
    * 2a1. FinHub prompts the user to enter the correct details.

      Use case resumes at step 1.

* 2b. The user enters an email/telephone number that has been added before.
    * 2b1. FinHub warns that a duplicate entry is not allowed.

      Use case ends.


**Use case: UC06 - Delete a client's contact**

**Precondition**: Client list must not be empty.

**MSS**

1.  The user <u>searches for the client to delete by their name (UC01)</u>.
2.  The user selects the client to be deleted.
3.  FinHub removes the client from the address book.
4.  FinHub displays a success message.

    Use case ends.

**Extensions**

* 2a. The user selects an invalid client.
    * 2a1. FinHub warns that an invalid client has been selected, and prompts the user to select again.

      Step 2a1 is repeated until a valid client has been selected.

      Use case resumes at step 3.

**Use case: UC07 - Edit a client's information**

**Precondition**: Client list must not be empty.

**MSS**

1.  The user <u>searches for the client by their name (UC01)</u>.
2.  The user selects the client to be edited, and enters one or more updated fields.
3.  FinHub validates the updated data.
4.  FinHub displays a success message.

    Use case ends.

**Extensions**

* 2a. The user selects an invalid client.
    * 2a1. FinHub warns that an invalid client has been selected, and prompts the user to select again.

      Step 2a1 is repeated until a correct client has been selected.

      Use case resumes at step 3


* 2b. The user enters invalid fields.
    * 2b1. FinHub prompts the user to enter the correct details.

      Use case resumes at step 2.

**Use case: UC08 - Delete reminder**

**Precondition**: Client list must not be empty.

**MSS**

1.  The user <u>searches for the client to edit by their name (UC01)</u>.
2.  The user selects which client and which reminder to delete.
3.  FinHub displays a success message.
4.  FinHub shows the client the list without the deleted reminder.

    Use case ends.

**Extensions**

* 2a. The user selects an invalid client or reminder.
    * 2a1. FinHub warns and specifies which is the invalid selection, and prompts the user to select again.

      Step 2a1 is repeated until a correct selection.

      Use case resumes at step 3.

**Use case: UC09 - Receive alerts if client has not been contacted in a specified period**

**MSS**

1.  The user starts up the application.
2.  FinHub displays a message containing all the clients that has not been contacted in a specific period of time.
3.  FinHub returns to landing display.

    Use case ends.

**Extensions**

* 2a. There is no clients that are under this criteria.
    * 2a1. FinHub displays an empty message.

      Use case resumes at step 3.

**Use case: UC10 - List of all upcoming policy renewal dates**

**MSS**

1.  The user starts up the application.
2.  FinHub displays a message showing the few upcoming policy renewal dates in chronological order within a set interval.
3.  FinHub returns to landing display.

    Use case ends.

**Extensions**

* 2a. There are no policy renewal dates in the data.
    * 2a1. FinHub displays an empty message.

      Use case resumes at step 3.

**Use case: UC11 - Assign priority levels to tasks**

**MSS**

1.  FinHub will display a list of tasks saved.
2.  The user will select the task that they want to assign a priority level to and the priority level.
3.  FinHub will update the task with the corresponding level.
4.  FinHub returns to landing display.

    Use case ends.

**Extensions**

* 2a. The user selects either invalid task or invalid priority level.
    * 2a1. FinHub warns and specifies which is the invalid selection, and prompts the user to select correct task/ priority level.

      Step 2a1 is repeated until a correct task and priority level has been selected.

      Use case resumes at step 3.

**Use case: UC12 - Add reminder for clients**

**Precondition**: Client list must not be empty.

**MSS**

1.  The user <u>searches for the client by their name (UC01)</u>.
2.  The user selects which client and set reminder for the client by their index, and enters the reminder and the date and time to remind.
3.  FinHub validates the updated data.
4.  FinHub displays a success message.

    Use case ends.

**Extensions**

* 2a. The user selects an invalid client or reminder.
    * 2a1. FinHub warns and specifies which is the invalid selection, and prompts the user to select again.

      Step 2a1 is repeated until a correct selection.

      Use case resumes at step 3.

* 2b. The user enters invalid date/time inputs.

    * 2b1. FinHub prompts the user to enter proper date/time.

      Step 2b1 is repeated until a correct input has been entered.

      Use case resumes at step 3.

**Use case: UC13 - Edit reminder for clients**

**Precondition**: Client list must not be empty.

**MSS**

1.  The user <u>searches for the client by their name (UC01)</u>.
2.  The user selects which client and edit reminder for the client by their index, and enters the amended reminder and the date and time to remind.
3.  FinHub validates the updated data.
4.  FinHub displays a success message.

    Use case ends.

**Extensions**

* 2a. The user selects an invalid client or reminder.
    * 2a1. FinHub warns and specifies which is the invalid selection, and prompts the user to select again.

      Step 2a1 is repeated until a correct selection.

      Use case resumes at step 3.

* 2b. The user enters invalid date/time inputs.

    * 2b1. FinHub prompts the user to enter proper date/time.

      Step 2b1 is repeated until a correct input has been entered.

      Use case resumes at step 3.

**Use case: UC14 - Add client meeting notes**

**Precondition**: Client list must not be empty.

**MSS**

1.  The user <u>searches for the client by their name (UC01)</u>.
2.  The user selects the option to add meeting notes for the client, and enters the meeting notes.
3.  FinHub validates the updated data.
4.  FinHub displays a success message.

    Use case ends.

**Extensions**

* 2a. The user selects an invalid client.
    * 2a1. FinHub warns that user does not exist and prompts the user to select again.

      Step 2a1 is repeated until a correct selection.

      Use case resumes at step 3.

* 2b. The user enters an invalid note.
    * 2b1. FinHub warns that the note entered is invalid and prompts the user to enter again.

      Step 2b1 is repeated until a valid note.

      Use case resumes at step 3.


**Use case: UC15 - Delete client meeting notes**

**Precondition**: Client list must not be empty.

**MSS**

1.  The user <u>searches for the client by their name (UC01)</u>.
2.  The user selects which client and which meeting note to delete.
3.  FinHub displays a success message.

    Use case ends.

**Extensions**

* 2a. The user selects an invalid client.
    * 2a1. FinHub warns that user does not exist and prompts the user to select again.

      Step 2a1 is repeated until a correct selection.

      Use case resumes at step 3.

* 2a. The user enters an invalid meeting note.
    * 23a1. FinHub warns that the meeting note is invalid and prompts the user to enter another note.

      Step 3a1 is repeated until a correct selection.

      Use case resumes at step 4.

**Use Case: UC16 - Archive client**

**Precondition**: User is viewing active list.

**MSS**

1. The user <u>searches for the client by their name (UC01)</u>.
2. FinHub displays a list of clients.
3. The user selects the client they want to archive.
4. FinHub successfully archives the client and displays a success message.

   Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. FinHub detects an error in the command entered.
    * 3a1. FinHub displays an error message and prompts the user to input again.
    * 3a2. The user re-enters the command to archive the client

      Steps 3a1-3a2 are repeated until the command and data entered are correct.

      Use case resumes at step 4.

**Use Case: UC17 - Unarchiving a client**

**Precondition**: User is viewing archive list.

**MSS**

1. The user <u>searches for the client by their name (UC01)</u>.
2. FinHub displays a list of clients.
3. The user selects the client they wish to unarchive.
4. FinHub successfully unarchives the client and displays a success message.

   Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. FinHub detects an error in the command entered.
    * 3a1. FinHub displays an error message and prompts the user to input again.
    * 3a2. The user re-enters the command to unarchive the client.

      Steps 3a1-3a2 are repeated until the command and data entered are correct.

      Use case resumes at step 4.

**Use Case: UC18 - Star a client**

**MSS**

1. The user <u>searches for the client by their name (UC01)</u>.
2. FinHub displays a list of clients.
3. The user selects the client they wish to star.
4. FinHub successfully stars the client and displays a success message.

   Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. FinHub detects an error in the command entered.
    * 3a1. FinHub displays an error message and prompts the user to input again.
    * 3a2. The user re-enters the command to star the client.

      Steps 3a1-3a2 are repeated until the command and data entered are correct.

      Use case resumes at step 4.

* 3b The client has already been starred.
    * 3b1 FinHub will display an error message and does not make any changes.

      Use case ends.

**Use Case: UC19 - Unstar a client**

**MSS**

1. The user <u>searches for the client by their name (UC01)</u>.
2. FinHub displays a list of clients.
3. The user selects the client they wish to unstar.
4. FinHub successfully remove star status from the client and displays a success message.

   Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. FinHub detects an error in the command entered.
    * 3a1. FinHub displays an error message and prompts the user to input again.
    * 3a2. The user re-enters the command to unstar the client.

      Steps 3a1-3a2 are repeated until the command and data entered are correct.

      Use case resumes at step 4.

* 3b The client has already been unstarred.
    * 3b1 FinHub will display an error message and does not make any changes.

      Use case ends.

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2.  Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4. The product should be for a single user.
5. The data should be stored locally and should be in a human editable text file, instead of a database management system.
6. The software should work without requiring an installer.
7. The GUI should work well (i.e., should not cause any resolution-related inconveniences to the user) for standard screen resolutions 1920x1080 and higher, and for screen scales 100% and 125%.
8. The GUI should be usable (i.e., all functions can be used even if the user experience is not optimal) for resolutions 1280x720 and higher, and for screen scales 150%.
9. The product should be packaged into a `.jar` file.
10. The product file size should be reasonable and should not exceed 100Mb.
11. The product is not required to cover communication with clients from the app, policy and financial calculation and payment and billing system.

### Glossary

* **Archive**: Clients who are archived are inactive, but not deleted
* **Client**: Customer who has signed or is going to sign an insurance policy with the user
* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Private contact detail**: A contact detail that is not meant to be shared with others
* **Reminder**: A note linked to a specific client, with a message and due date
* **User**: Insurance agent using the address book

<br>

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

    1. Download the jar file and copy into an empty folder

    1. Double-click the jar file Expected: Shows the GUI with a set of sample clients. The window size may not be optimum.

1. Saving window preferences

    1. Resize the window to an optimum size. Move the window to a different location. Close the window.

    1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

<br>

--------------------------------------------------------------------------------------------------------------------

### Adding a reminder
* Prerequisites: Make sure the list of clients is displayed using the `activelist` command.

&nbsp;

* Test Case: Adds a reminder to a client with a valid index
    * Assumption: Valid Inputs
    * User Input: `reminder 1 h/Follow up with client on insurance quote d/2026-11-10 09:00`
    * Expected Outcome:
        * A reminder with the header {Follow up with client on insurance quote} and deadline {2026-11-10 09:00} is added to the client at index 1.
        * A success message is displayed: `Reminder added to {Person's Name}: {Follow up with client on insurance quote}, due by {2026-11-10 09:00}`
        * The reminder list for the client at index 1 is re-sorted by the closest deadlines first, followed by the header name.

&nbsp;

* Test Case: Adds a reminder to a client with an invalid index
    * Assumption: Invalid Client Index (index exceeds number of clients currently displayed)
    * User Input: `reminder 100000 h/Follow up with client on insurance quote d/2026-11-10 09:00`
    * Expected Outcome:
        * A failure message is displayed: `The client index provided is invalid — it exceeds the number of clients currently displayed`

&nbsp;

* Test Case: Adds a reminder to a client with a valid index and deadline but invalid header
    * Assumption: Valid Client Index but Invalid Header (empty)
    * User Input: `reminder 1 h/ d/2026-11-10 09:00`
    * Expected Outcome:
        * A failure message is displayed: `Reminder can take any value but cannot be blank.`

&nbsp;

* Test Case: Adds a reminder to a client with a valid index and header but invalid deadline format
    * Assumption: Valid Client Index but invalid deadline format (not formatted to "yyyy-MM-dd HH:mm")
    * User Input: `reminder 1 h/Follow up with client on insurance quote d/2029-10-10 1000`
    * Expected Outcome:
        * A failure message is displayed: `Deadline should be in the following format: yyyy-MM-dd HH:mm`

<br>

--------------------------------------------------------------------------------------------------------------------

### Deleting a reminder
* Prerequisites: Make sure the list of clients is displayed using the `activelist` command

&nbsp;

* Test Case: Deletes a reminder with a valid index from a client with a valid index
    * Assumptions: Valid Inputs & Chosen Client has reminders to be removed
    * User Input: `rDelete 1 1`
    * Expected Outcome:
        * The first reminder of the first client will be deleted.
        * A success message is displayed: `Deleted Client {Person's Name}'s Reminder 1: {Deleted Reminder}`
        * The reminder list for the client at index 1 will not contain the {Deleted Reminder}.

&nbsp;

* Test Case: Deletes a reminder with a valid index from a client with an invalid index
    * Assumption: Invalid Client Index (index exceeds number of clients currently displayed)
    * User Input: `rDelete 100000 1`
    * Expected Outcome:
        * A failure message is displayed: `The client index provided is invalid — it exceeds the number of clients currently displayed`

&nbsp;

* Test Case: Deletes a reminder with an invalid index from a client with a valid index
    * Assumption: Invalid Reminder Index (index exceeds number of reminders of the client's reminder list)
    * User Input: `rDelete 1 10000000`
    * Expected Outcome:
        * A failure message is displayed: `The reminder index provided is invalid — it exceeds the number of reminders this client currently has`

<br>

--------------------------------------------------------------------------------------------------------------------

### Editing a reminder
* Prerequisites: Make sure the list of clients is displayed using the `activelist` command

&nbsp;

* Test Case: Edits the reminder at a valid index of the reminder list of a client at a valid index
    * Assumption: Valid Inputs & Edited Reminder is **different** from Previous Reminder
    * User Input: `rEdit 1 1 h/Submit updated policy document d/2026-11-15 17:30`
    * Expected Outcome:
        * The first reminder of the client at index 1 will be replaced with a new reminder with the header [Submit updated policy document] and deadline [2026-11-15 17:30].
        * A success message is displayed: `Edited Client {Person's Name}'s Reminder 1: from {Previous Reminder} to {Edited Reminder}`

&nbsp;

* Test Case: Edits a reminder with a valid index from a client with an invalid index
    * Assumption: Invalid Client Index (index exceeds number of clients currently displayed)
    * User Input: `rEdit 100000 1 h/Submit updated policy document d/2026-11-15 17:30`
    * Expected Outcome:
        * A failure message is displayed: `The client index provided is invalid — it exceeds the number of clients currently displayed`

&nbsp;

* Test Case: Edits a reminder with an invalid index from a client with a valid index
    * Assumption: Invalid Reminder Index (index exceeds number of reminders of the client's reminder list)
    * User Input: `rEdit 1 10000000 h/Submit updated policy document d/2026-11-15 17:30`
    * Expected Outcome:
        * A failure message is displayed: `The reminder index provided is invalid — it exceeds the number of reminders this client currently has`

<br>

--------------------------------------------------------------------------------------------------------------------

### Adding a meeting note
* Prerequisites: Make sure the list of clients is displayed using the `activelist` command.

&nbsp;

* Test Case: Adds a meeting note to a client with a valid index
    * Assumption: Valid Inputs
    * User Input: `note 1 Client is interested in the premium health policy`
    * Expected Outcome:
        * A meeting note {Client is interested in the premium health policy} is added to the top of the meeting note list of the client at index 1 .
        * A success message is displayed: `Meeting note added to {Person's Name}: [date and time when note is created] CLient is interested in the premium health policy`

&nbsp;

* Test Case: Adds a meeting note to a client with an invalid index
    * Assumption: Invalid Client Index (index exceeds number of clients currently displayed)
    * User Input: `note 10000 Client is interested in the premium health policy`
    * Expected Outcome:
        * A failure message is displayed: `The client index provided is invalid — it exceeds the number of clients currently displayed`

&nbsp;

* Test Case: Adds a meeting note to a client with a valid index but invalid text
    * Assumption: Valid Client Index but Invalid text (empty)
    * User Input: `note 1`
    * Expected Outcome:
        * A failure message is displayed: `Invalid command format!
          note: Adds a meeting note to the client identified by the index number in the displayed client list.
          Parameters: INDEX (must be a positive integer) TEXT (cannot be empty)
          Example: note 1 Client wants to know more about xx policy`

<br>

--------------------------------------------------------------------------------------------------------------------

### Deleting a meeting note
* Prerequisites: Make sure the list of clients is displayed using the `activelist` command

&nbsp;

* Test Case: Deletes a meeting note with a valid index from a client with a valid index
    * Assumptions: Valid Inputs & Chosen Client has meeting notes to be removed
    * User Input: `nDelete 1 1`
    * Expected Outcome:
        * The first meeting note of the first client will be deleted.
        * A success message is displayed: `Deleted Client {Person's Name}'s Meeting Note 1: [date and time when note is created] {Deleted Meeting Note}`
        * The meeting note list for the client at index 1 will not contain the {Deleted Meeting Note}.

&nbsp;

* Test Case: Deletes a reminder with a valid index from a client with an invalid index
    * Assumption: Invalid Client Index (index exceeds number of clients currently displayed)
    * User Input: nDelete 100000 1
    * Expected Outcome:
        * A failure message is displayed: `The client index provided is invalid — it exceeds the number of clients currently displayed`

&nbsp;

* Test Case: Deletes a reminder with an invalid index from a client with a valid index
    * Assumption: Invalid Reminder Index (index exceeds number of reminders of the client's reminder list)
    * User Input: rDelete 1 10000000
    * Expected Outcome:
        * A failure message is displayed: `The meeting note index provided is invalid — it exceeds the number of meeting notes this client currently has`

<br>


### Adding a client
* Prerequisites: -

&nbsp;

* Test Case: Add a client
    * Assumption: Both the phone number `98765432` and email `johnd@example.com` does not exist in FinHub
    * Input: `add n/John Doe p/98765432 e/johnd@example.com a/311, Clementi Ave 2, #02-25 ip/AIB Overall Lifeshield Plan t/friends`
    * Expected Outcome:
        * The client, John Doe, is added.
        * A success message is displayed: `New client added: John Doe; Phone: 98765432; Email: johnd@example.com; Address: 311, Clementi Ave 2, #02-25; Insurance Policy: AIB Overall Lifeshield Plan; Tags: [friends]`.

&nbsp;

* Test Case: Add a client with a phone number that exists in FinHub
    * Assumption: The phone number `99272758` already exist in FinHub
    * Input: `add n/John Doe p/99272758 e/johnd123@example.com a/311, Clementi Ave 2, #02-25 ip/AIB Overall Lifeshield Plan t/friends`
    * Expected Outcome:
        * A failure message is displayed: `This client's phone number or email already exists in FinHub`.

&nbsp;

* Test Case: Add a client with an email that exists in FinHub
    * Assumption: The email `berniceyu@example.com` already exist in FinHub
    * Input: `add n/John Doe p/99776442 e/berniceyu@example.com a/311, Clementi Ave 2, #02-25 ip/AIB Overall Lifeshield Plan t/friends`
    * Expected Outcome:
        * A failure message is displayed: `This client's phone number or email already exists in FinHub`.

&nbsp;

* Test Case: Add a client with a valid insurance policy
    * Assumption: the client has not been added yet
    * Input: `add n/Betsy Crowe e/betsycrowe@example.com a/Palace Street p/1234567 t/classmate ip/AIB LifePlan`
    * Expected Outcome:
        * The client, Betsy Crowe, is added.
        * A success message is displayed: `New client added: Betsy Crowe; Phone: 1234567; Email: betsycrowe@example.com; Address: Palace Street; Insurance Policy: AIB LifePlan; Tags: [classmate]`.

&nbsp;

* Test Case: Add a client with an invalid insurance policy (just whitespace)
    * Input: `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01 ip/ `
    * Expected Outcome:
        * A failure message is displayed: `Policy name may only contain letters, digits, spaces, and + / & ( ) ' . , - and must include at least one letter or digit.`.

&nbsp;

* Test Case: Try adding a client with invalid command format (no insurance policy)
    * Input: `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01`
    * Expected Outcome:
        * A failure message is displayed: `Invalid command format!
add: Adds a client to FinHub. Parameters: n/NAME p/PHONE e/EMAIL a/ADDRESS ip/INSURANCE_POLICY [t/TAG]...
Example: add n/John Doe p/98765432 e/johnd@example.com a/311, Clementi Ave 2, #02-25 ip/AIB Overall Lifeshield Plan t/friends t/owesMoney`

<br>

### Editing a client
* Prerequisites: The client has already been added.

&nbsp;

* Test Case: Edit a client with a new valid phone number
    * Assumption: The new phone number does not exist in FinHub, and **Alex Yeoh** (client with index 1) is displayed in the list with details as defined in the sample data.
    * Input: `edit 1 p/82339844`
    * Expected Outcome:
        * Client 1's phone number is updated to `82339844`
        * A success message is displayed: `Edited Client: Alex Yeoh; Phone: 82339844; Email: alexyeoh@example.com; Address: Blk 30 Geylang Street 29, #06-40; Insurance Policy: AIB HealthShield Gold Max; Tags: [friends]`.

&nbsp;

* Test Case: Edit a client with an existing phone number
    * Assumption: The phone number `99272758` exist in FinHub
    * Input: `edit 1 p/99272758`
    * Expected Outcome:
        * A failure message is displayed: `This client's phone number or email already exists in FinHub.`.

&nbsp;

* Test Case: Edit a client with a new valid email
    * Assumption: The new email does not exist in FinHub, and **Alex Yeoh** (client with index 1) is displayed in the list with details as defined in the sample data.
    * Input: `edit 1 e/AlexYeoh6767@gmail.com`
    * Expected Outcome:
        * Client 1's email is updated to `AlexYeoh6767@gmail.com`
        * A success message is displayed: `Edited Client: Alex Yeoh; Phone: 87438807; Email: AlexYeoh6767@gmail.com; Address: Blk 30 Geylang Street 29, #06-40; Insurance Policy: AIB HealthShield Gold Max; Tags: [friends]`.

&nbsp;

* Test Case: Edit a client with an existing email
    * Assumption: The email `berniceyu@example.com` exist in FinHub
    * Input: `edit 1 e/berniceyu@example.com`
    * Expected Outcome:
        * A failure message is displayed: `This client's phone number or email already exists in FinHub.`.

&nbsp;

* Test Case: Edit a client with a new valid insurance policy
    * Input: `edit 1 ip/AIB LifePlan`
    * Expected Outcome:
        * Client 1, Alex's insurance policy is now AIB LifePlan
        * A success message is displayed: `Edited Client: Alex Yeoh; Phone: 87438807; Email: alexyeoh@example.com; Address: Blk 30 Geylang Street 29, #06-40; Insurance Policy: AIB LifePlan; Tags: [friends]`.

&nbsp;

* Test Case: Edit a client with an invalid insurance policy (just whitespace)
    * Input: `edit 1 ip/ `
    * Expected Outcome:
        * A failure message is displayed: `Policy name may only contain letters, digits, spaces, and + / & ( ) ' . , - and must include at least one letter or digit.`.

<br>

--------------------------------------------------------------------------------------------------------------------

### Archiving a client
* Prerequisites: Make sure the list of clients is displayed using the `activelist` command.
  The list should include at least one client.

&nbsp;

* Test Case: Archiving a client with a valid index
    * Input: `archive 1`
    * Expected Outcome:
        * The client at index 1 is archived.
        * A success message is displayed: `Archived Client: [Client]`.
        * The list is re-sorted with the remaining clients.

&nbsp;

* Test Case: Archiving a client with a invalid index
    * Input: `archive 0`
    * Expected Outcome:
        * A failure message is displayed: `Any indices provided should be positive integers.
Enter the command word again without any arguments to view the correct command format.`.

&nbsp;

* Test Case: Try archiving with an invalid command (no index)
    * Input: `archive`
    * Expected Outcome:
        * A failure message is displayed: `Invalid command format!
archive: archives the client identified by the index number used in the displayed client list.
Parameters: INDEX (must be a positive integer)`.

<br>

--------------------------------------------------------------------------------------------------------------------

### Unarchiving a client
* Prerequisites: Make sure the list of clients is displayed using the `archivelist` command.
  The list should include at least one client.

&nbsp;

* Test Case: Unarchiving a client with a valid index
    * Input: `unarchive 1`
    * Expected Outcome:
        * The client at index 1 is unarchived.
        * A success message is displayed: `Unarchived Client:[Client]`.
        * The list is re-sorted with the remaining clients.

&nbsp;

* Test Case: Unarchive a client with an invalid index
    * Input: `unarchive 0`
    * Expected Outcome:
        * A failure message is displayed: `Any indices provided should be positive integers.
Enter the command word again without any arguments to view the correct command format.`.

&nbsp;

* Test Case: Try unarchiving with an invalid command (no index)
    * Input: `unarchive`
    * Expected Outcome:
        * A failure message is displayed: `Invalid command format!
unarchive: unarchives client identified by the index number used in the displayed client list.
Parameters: INDEX (must be a positive integer)`.

<br>

--------------------------------------------------------------------------------------------------------------------
<!-- @@author Aloysius Pek Yong Jun -->

### Starring a client
* Prerequisites: Make sure the list of clients is displayed using the `activelist` command. The list should include at least one client who is not starred.

&nbsp;

* Test Case: Star a client with a valid index
    * Assumption: client 1 is not starred
    * Input: `star 1`
    * Expected Outcome:
        * The client at index 1 is starred.
        * A success message is displayed: `Starred Client: {Client}`.
        * The list is re-sorted such that the starred client appears first.

&nbsp;

* Test Case: Star a client with an invalid index
    * Input: `star 0`
    * Expected Outcome:
        * A failure message is displayed: `Any indices provided should be positive integers.
Enter the command word again without any arguments to view the correct command format`.

&nbsp;

* Test Case: Try starring a client who is already starred
    * Assumption: client 1 is starred
    * Input: `star 1`
    * Expected Outcome:
        * A failure message is displayed: `Chosen client is already starred`.

&nbsp;

* Test Case: Try starring with an invalid command (no index)
    * Input: `star`
    * Expected Outcome:
        * A failure message is displayed: `Invalid command format!
star: stars the client identified by the index number used in the displayed client list.
Parameters: INDEX (must be a positive integer)`.

<br>

--------------------------------------------------------------------------------------------------------------------

### Unstarring a client
* Prerequisites: Make sure the list of clients is displayed using the `activelist` command. The list should include at least one client who is starred.

&nbsp;

* Test Case: Unstar a client with a valid index
    * Assumption: client 1 is starred
    * Input: `unstar 1`
    * Expected Outcome:
        * The client at index 1 is unstarred.
        * A success message is displayed: `Starred status removed from Client: {Client}`.
        * The list is re-sorted such that the starred client appears first.

&nbsp;

* Test Case: Unstar a client with an invalid index
    * Input: `unstar 0`
    * Expected Outcome:
        * A failure message is displayed: `Any indices provided should be positive integers.
Enter the command word again without any arguments to view the correct command format`.

&nbsp;

* Test Case: Try unstar a client who is already has no starred status
    * Assumption: client 1 has no starred status
    * Input: `unstar 1`
    * Expected Outcome:
        * A failure message is displayed: `Chosen client is not starred`.

&nbsp;

* Test Case: Try unstarring with an invalid command (no index)
    * Input: `unstar`
    * Expected Outcome:
        * A failure message is displayed: `Invalid command format!
unstar: Removes starred status of the client identified by the index number used in the displayed client list.
Parameters: INDEX (must be a positive integer)`.

<br>

<!-- @@author-->

--------------------------------------------------------------------------------------------------------------------

## Appendix: Effort

**Scope vs AB3**
- AB3 manages one entity type; FinHub extends the model with **per-client Reminders** and **Meeting Notes**, adds **archiving/star** flows, and an **InsurancePolicy** field. This required new parsers, commands, JSON adapters, UI panels, and tests.

**Difficulty**

1. **Per-client list management (two indexes)**
    - **Why hard:** Commands like `rEdit PERSON_INDEX r/REMINDER_INDEX` touch **two lists**: the filtered people list and a person’s reminder list. Either can be re-indexed after filtering or edits.
    - **What we did:**
        - Resolve indices **late** (inside `Command#execute`) using the **current** filtered lists.
        - Distinguish errors: “invalid **person** index” vs “invalid **reminder** index”.
        - Tests cover off-by-one, empty lists, and filtered views.

2. **Filtered views for Active vs Archived (no duplicated state)**
    - **Why hard:** We want `activelist` / `archivelist` without cloning data or drifting out of sync.
    - **What we did:**
        - Single source of truth (`AddressBook`) with a `isArchived` flag.
        - Switch filters with `updateFilteredPersonList(predicate)`; reapply sorts (e.g., starred first) after mutations.

3. **JSON adapters for nested lists & backward compatibility**
    - **Why hard:** We must serialise/deserialise nested `reminders[]` and `notes[]`, and **not break older JSON files** that don’t have these fields.
    - **What we did:**
        - `JsonAdaptedReminder` / `JsonAdaptedMeetingNote` with **tolerant readers** (missing arrays → `[]`; unknown fields → ignored).
        - Fail clearly on corrupted sub-entries; don’t crash the whole file.
        - Migration tests: “old to new” round-trip.

4. **Multiple GUI panels (scrolling/height bugs)**
    - **Why hard:** We now render **three vertical areas**: Client list, Reminder list, Meeting note list. With wrapped text and dynamic content, JavaFX showed:
        - Double/vanishing scrollbars
        - Clipped multi-line text
        - Lists refusing to grow beyond ~3 lines
        - Scroll focus jumping when lists update
    - **What we did (practical fixes):**
        - Avoid `ScrollPane` **inside** `ListView` where possible (one scroll owner).
        - For wrapped labels: `label.setWrapText(true)` and `label.setMinHeight(Region.USE_PREF_SIZE)`.
        - Let cell heights grow: `listView.setFixedCellSize(Region.USE_COMPUTED_SIZE)`.
        - Manage growth in the parent layout: use `VBox.setVgrow(child, Priority.ALWAYS)` only on the container you want to grow; remove conflicting FXML `prefHeight`/`vgrow`.
        - Reapply comparator/filter after mutations to prevent layout thrashing.

**Challenge**

1. **Unclear error messages**
    - **Problem:** “Invalid index” was ambiguous—was it the person or the reminder index?
    - **Fix:** Separate messages (“Invalid **person** index”; “Invalid **reminder** index”) and add tests that assert exact wording.

2. **Filter + command order causing index drift**
    - **Problem:** After `archivelist`, running `star` could reference the old (unfiltered) indices.
    - **Fix:** Resolve indices **after** predicates are applied; standardise a **post-mutation refresh** (reapply predicate + optional sort).

3. **Older JSON files breaking on new fields**
    - **Problem:** Early saves lacked `reminders`/`notes`, causing deserialisation errors.
    - **Fix:** Default missing arrays to `[]`, ignore unknown fields, and report only the corrupted entry (don’t crash everything).

4. **Scrolling regressions after long result messages**
    - **Problem:** `ResultDisplay` grew horizontally and triggered unwanted scrollbars.
    - **Fix:** Wrap long text, avoid nested scrolling, let widths/height be `USE_COMPUTED_SIZE`, and cap max widths where necessary.


**Reused work**
- AB3 infrastructure (UI shell, storage scaffold, test harness, model entities and logic flow).
  _Net effect_: reduced boilerplate; effort focused on domain/features.

**Effort Required**

**Design & Architecture (≈ 1–2 person-days)**
- Identified where **new responsibilities** live (model vs parser vs UI), keeping AB3 layering intact.
- Defined **error messaging strategy** (model-centric constraints; consistent messages).
- Decided on **single-source-of-truth** for people + **view predicates** for Active/Archived/Starred.
- Chose **tolerant JSON readers** (forward/backward compatibility) + strict writers.

**Model & Validation (≈ 1–3 person-days)**
- Implemented value objects/entities:
    - `Reminder` (header + datetime, canonical formatter).
    - `MeetingNote` (immutable text).
    - `InsurancePolicy` (trim + regex; explicit constraints).
- Added invariants + unit tests (null/empty, whitespace, format, “close to now” edges).
- Ensured **immutability** or safe copying for nested lists.

**Logic & Parsing (≈ 2–4 person-days)**
- New commands & parsers: `reminder`, `rEdit`, `rDelete`, `note`, `nDelete`, `archive`, `unarchive`, `activelist`, `archivelist`, `star`, `unstar`.
- **Late index resolution** in `Command#execute` to avoid stale indices under filters.
- Distinct error surfaces for **person index** vs **inner list index**.
- Tests for parsing ambiguity, missing prefixes, and filtered-list behaviors.

**UI Engineering (multi-panel, scrolling) (≈ 3–5 person-days)**
- Panels: `ReminderListPanel`, `GeneralReminderListPanel`, `MeetingNoteListPanel`; cards for both.
- Fixed **nested scroll bugs** (double scrollbars, clipped text, scroll focus jumps):
    - Avoid `ScrollPane` inside `ListView`.
    - `setWrapText(true)` + `setMinHeight(Region.USE_PREF_SIZE)` on labels; `setFixedCellSize(Region.USE_COMPUTED_SIZE)` on lists.
    - Single **growth owner** in layouts (no conflicting `vgrow`/`prefHeight`).
- Reapplied comparator/filter **once** after mutations to reduce layout thrashing.
- Visual markers for **starred/archived** without breaking virtualization.

**Storage & Migration (≈ 2–3 person-days)**
- Adapters: `JsonAdaptedReminder`, `JsonAdaptedMeetingNote`, updated `JsonAdaptedPerson`.
- **Backward compatibility**: default missing arrays to `[]`, ignore unknown fields; fail only on truly corrupted sub-entries.
- Round-trip tests: new → JSON → new; **old JSON → new model**; mixed lists with corrupted entries.

**Testing (≈ 3–5 person-days)**
- **Model**: value object constraints, equals/hashCode, string forms.
- **Logic**: success/failure paths, filtered/archived/starred interactions, two-index commands.
- **Storage**: deserialisation with missing/extra fields, corrupted sub-entries, non-ASCII.
- **UI** (selective): render of multi-line reminder/note cards; no horizontal overflow; no crash on long texts.
- Regression tests for **scrolling bugs** (where feasible) and **message wording**.

**Tooling, CI, Docs (≈ 1–2 person-days)**
- Gradle tasks wired for tests and checks; CI green on model/logic/storage suites.
- Updated DG & UG, manual testing appendix, and acknowledgements; PlantUML stubs added.


**Achievements**

**Reliability & Data Safety**
- **Tolerant JSON readers** mean older saves still load; corrupted sub-items no longer crash the app—users keep the rest of their data.
- **Late index resolution** eliminates a class of “deleted wrong row” bugs in filtered views.

**Usability & UX**
- **Clear, targeted errors** (format + time; person vs reminder index) reduce user confusion.
- **Multi-panel UI** now handles long, wrapped content without clipping or random scrollbars.
- **Star/Archive indicators** are visible and consistent with list filters.

**Code Quality & Maintainability**
- Constraints enforced at the **model boundary** (single source of truth).
- **No duplicated lists** for filters—predicates maintain a coherent view over one dataset.
- Cohesive adapters (`JsonAdapted*`) isolate serialization concerns, easing future schema changes.

**Performance & Responsiveness**
- **Virtualized lists** remain snappy even with many reminders/notes.
- Post-mutation **one-shot refresh** prevents relayout storms.

**Developer Experience**
- **Deterministic tests**: reduced flakiness from “close to now” cases.
- DG/UG now **point to the right classes and flows**, making onboarding easier.

**Why this effort was necessary**

- Agents can **trust** reminders (they won’t silently schedule in the past) and **keep** their old data through releases.
- The UI supports **realistic, verbose notes** without breaking layout—a must for actual client work.
- The codebase stays **evolvable**: one dataset, clear boundaries, adapters ready for schema growth.
