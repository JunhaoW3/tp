package seedu.address.ui;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.Region;

/**
 * Panel containing the list of reminders for a selected client.
 *
 * <p>This class follows the same pattern as other list panels in AB-3:
 * it uses an FXML with no {@code fx:controller}, and sets a cell factory that
 * tries to load a {@link ReminderCard}. If loading fails (e.g. in headless CI),
 * it falls back to plain text so tests can still run without JavaFX.</p>
 */
public class ReminderListPanel extends UiPart<Region> {

    /** Relative path under {@code src/main/resources/view}. */
    public static final String FXML = "ReminderListPanel.fxml";

    @FXML
    private ListView<String> reminderListView;

    /**
     * Constructs a panel bound to {@code reminders}. If {@code reminders} is {@code null},
     * an empty observable list is used.
     *
     * @param reminders observable list of reminder strings (may be {@code null})
     */
    public ReminderListPanel(ObservableList<String> reminders) {
        super(FXML);

        ObservableList<String> noteList = reminders != null ? reminders : FXCollections.observableArrayList();
        reminderListView.setItems(noteList);
        reminderListView.setCellFactory(list -> new ReminderListViewCell());

        reminderListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        reminderListView.getSelectionModel().clearSelection();
        reminderListView.setFocusTraversable(false);
        reminderListView.setFixedCellSize(-1);

        // Disable parent scrolling when mouse is hovering over meeting notes list
        reminderListView.setOnScroll(event -> event.consume());


    }

    public void setItems(ObservableList<String> items) {
        reminderListView.setItems(items != null ? items : FXCollections.observableArrayList());
    }

    private class ReminderListViewCell extends ListCell<String> {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setGraphic(null);
                setText(null);
                return;
            }

            try {
                int idx = getIndex() + 1;
                setText(null);
                Region cardRoot = new ReminderCard(idx, item).getRoot();
                cardRoot.prefWidthProperty().bind(reminderListView.widthProperty().subtract(20));
                cardRoot.maxWidthProperty().bind(reminderListView.widthProperty().subtract(20));
                setGraphic(cardRoot);
            } catch (Exception e) {
                int idx = getIndex() + 1;
                setGraphic(null);
                setText(ReminderTextUtil.formatItem(idx, item));
            }
        }
    }
}
