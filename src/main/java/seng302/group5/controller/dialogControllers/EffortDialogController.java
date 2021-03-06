package seng302.group5.controller.dialogControllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import seng302.group5.controller.enums.CreateOrEdit;
import seng302.group5.model.Effort;
import seng302.group5.model.Person;
import seng302.group5.model.Task;
import seng302.group5.model.undoredo.Action;
import seng302.group5.model.undoredo.UndoRedoObject;
import seng302.group5.model.util.TimeFormat;

public class EffortDialogController {

  @FXML private Button btnConfirm;
  @FXML private Button btnCancel;
  @FXML private HBox btnContainer;

  @FXML private ComboBox<Person> teamMemberCombo;
  @FXML private DatePicker dateField;
  @FXML private TextField timeField;
  @FXML private TextField spentEffortField;
  @FXML private TextArea commentField;

  private ObservableList<Person> allocatedPeople;

  private Task task;
  private TaskDialogController taskDC;
  private Stage thisStage;
  private CreateOrEdit createOrEdit;
  private Effort effort;
  private Effort lastEffort;

  private UndoRedoObject undoRedoObject;

  /**
   * Sets up the EffortDialogController.
   *
   * @param task The task for the effort to be added to.
   * @param allocatedPeople The list of people allocated to the task
   * @param thisStage The stage of the dialog.
   * @param createOrEdit Whether this dialog is for creating or editing.
   * @param effort The object that will be edited (null if creating)
   */
  public void setupController(TaskDialogController taskDC, Task task, List<Person> allocatedPeople,
                              Stage thisStage, CreateOrEdit createOrEdit, Effort effort) {
    this.task = task;
    this.taskDC = taskDC;
    this.thisStage = thisStage;
    this.createOrEdit = createOrEdit;

    if (effort != null) {
      this.effort = effort;
      this.lastEffort = new Effort(effort);
    } else {
      this.effort = null;
      this.lastEffort = null;
    }

    String os = System.getProperty("os.name");

    if (!os.startsWith("Windows")) {
      btnContainer.getChildren().remove(btnConfirm);
      btnContainer.getChildren().add(btnConfirm);
    }

    this.allocatedPeople = FXCollections.observableArrayList(allocatedPeople);
    teamMemberCombo.setItems(this.allocatedPeople);

    if (createOrEdit == CreateOrEdit.CREATE) {
      thisStage.setTitle("Log New Effort");
      btnConfirm.setText("Log");

      dateField.setValue(LocalDate.now());
      timeField.setText(TimeFormat.parseTimeString(LocalTime.now()));

    } else if (createOrEdit == CreateOrEdit.EDIT && effort != null) {
      thisStage.setTitle("Edit Logged Effort");
      btnConfirm.setText("Save");

      //todo verify
      LocalDateTime dateTime = effort.getDateTime();
      teamMemberCombo.setValue(effort.getWorker());
      dateField.setValue(dateTime.toLocalDate());
      timeField.setText(TimeFormat.parseTimeString(dateTime.toLocalTime()));
      spentEffortField.setText(TimeFormat.parseDuration(effort.getSpentEffort()));
      commentField.setText(effort.getComments());
    }

    this.createOrEdit = createOrEdit;
    btnConfirm.setDefaultButton(true);
    thisStage.setResizable(false);

    undoRedoObject = null;
    thisStage.getIcons().add(new Image("Thumbnail.png"));
  }

  /**
   * Generate an UndoRedoObject to represent an effort create or edit action and store it globally.
   * This is so a cancel in a dialog higher in the hierarchy can undo the changes made to the
   * effort.
   */
  private void generateUndoRedoObject() {
    if (createOrEdit == CreateOrEdit.CREATE) {
      undoRedoObject = new UndoRedoObject();
      undoRedoObject.setAction(Action.EFFORT_CREATE);
      undoRedoObject.addDatum(new Effort(effort));
      undoRedoObject.addDatum(task);

      // Store a copy of effort to create in object to avoid reference problems
      undoRedoObject.setAgileItem(effort);

    } else if (createOrEdit == CreateOrEdit.EDIT) {
      undoRedoObject = new UndoRedoObject();
      undoRedoObject.setAction(Action.EFFORT_EDIT);
      undoRedoObject.addDatum(lastEffort);

      // Store a copy of effort to edit in object to avoid reference problems
      undoRedoObject.setAgileItem(effort);
      Effort effortToStore = new Effort(effort);
      undoRedoObject.addDatum(effortToStore);
    }
  }

  /**
   * Adds the effort to the task.
   *
   * @param event Event generated by event listener.
   */
  @FXML
  private void btnConfirmClick(ActionEvent event) {
    StringBuilder errors = new StringBuilder();
    int noErrors = 0;

    Person teamMember = teamMemberCombo.getValue();
    LocalDate date = dateField.getValue();
    LocalTime time;
    int spentEffort;
    String comments = commentField.getText().trim();

    if (teamMember == null) {
      noErrors++;
      errors.append(String.format("%s\n", "No team member selected."));
    }

    if (date == null) {
      noErrors++;
      errors.append(String.format("%s\n", "Date is invalid."));
    }

    time = TimeFormat.parseLocalTime(timeField.getText());
    if (time == null) {
      noErrors++;
      errors.append(String.format("%s\n", "Invalid time format (e.g. 13:45)."));
    }

    spentEffort = TimeFormat.parseMinutes(spentEffortField.getText());
    if (spentEffort < 0) {
      noErrors++;
      errors.append(String.format("%s\n", "Invalid spent effort format (e.g. 1h30m)."));
    }

    if (comments.isEmpty()) {
      noErrors++;
      errors.append(String.format("%s\n", "Comments field is empty."));
    }

    // Display all errors if they exist
    if (noErrors > 0) {
      String title = String.format("%d invalid field", noErrors);
      if (noErrors > 1) {
        title += "s";  // plural
      }
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle(title);
      alert.setHeaderText(null);
      alert.getDialogPane().setPrefHeight(60 + 30 * noErrors);
      alert.setContentText(errors.toString());
      alert.showAndWait();
    } else {
      LocalDateTime dateTime = LocalDateTime.of(date, time);
      if (createOrEdit == CreateOrEdit.CREATE) {
        effort = new Effort(teamMember, spentEffort, comments, dateTime);
        task.addEffort(effort);
      } else if (createOrEdit == CreateOrEdit.EDIT) {
        //todo verify
        effort.setWorker(teamMember);
        effort.setSpentEffort(spentEffort);
        effort.setComments(comments);
        effort.setDateTime(dateTime);
      }
      generateUndoRedoObject(); // todo verify
      thisStage.close();
    }
  }

  /**
   * Closes the Effort dialog box on click of 'Cancel' button.
   *
   * @param event Event generated by event listener.
   */
  @FXML
  private void btnCancelClick(ActionEvent event) {
    thisStage.close();
  }

  /**
   * Get the UndoRedoObject representing the creating or editing of the effort. Use this as a
   * return value of the dialog.
   *
   * @return The UndoRedoObject representing the successful effort edit.
   */
  public UndoRedoObject getUndoRedoObject() {
    return undoRedoObject;
  }
}
