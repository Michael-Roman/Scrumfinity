package seng302.group5.controller.dialogControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import seng302.group5.Main;
import seng302.group5.controller.enums.CreateOrEdit;
import seng302.group5.model.Person;
import seng302.group5.model.Sprint;
import seng302.group5.model.Story;
import seng302.group5.model.Task;
import seng302.group5.model.Taskable;
import seng302.group5.model.Team;

/**
 * A controller to handle the creation or editing tasks. Tasks involve a label, description,
 * estimate (hours), status, and allocated people with their logged effort.
 *
 * @author Su-Shing Chen
 */
public class TaskDialogController {

  @FXML private TextField labelField;
  @FXML private TextArea descriptionField;
  @FXML private TextField estimateField;
  @FXML private ComboBox statusComboBox;
  @FXML private ListView<Person> availablePeopleList;
  @FXML private ListView<Person> allocatedPeopleList;
  @FXML private Button btnAddPerson;
  @FXML private Button btnRemovePerson;
  @FXML private HBox btnContainer;
  @FXML private Button btnConfirm;
  @FXML private Button btnCancel;

  private Main mainApp;
  private Taskable parent;
  private Stage thisStage;
  private CreateOrEdit createOrEdit;
  private Task task;
  private Task lastTask;
  private ObservableList<Person> allocatedPeople;
  private ObservableList<Person> availablePeople;

  /**
   * Sets up the controller on start up. TODO: expand when it does more
   *
   * @param parent The agileItem which will contain the task
   * @param thisStage This is the window that will be displayed
   * @param createOrEdit This is an ENUM object to determine if creating or editing
   * @param task The object that will be edited (null if creating)
   */
  public void setupController(Main mainApp, Taskable parent, Stage thisStage,
                              CreateOrEdit createOrEdit, Task task) {
    this.mainApp = mainApp;
    this.parent = parent;
    this.thisStage = thisStage;

    if (task != null) {
      this.task = task;
      this.lastTask = new Task(task);
    } else {
      this.task = null;
      this.lastTask = null;
    }

    String os = System.getProperty("os.name");

    if (!os.startsWith("Windows")) {
      btnContainer.getChildren().remove(btnConfirm);
      btnContainer.getChildren().add(btnConfirm);
    }

    initialiseLists();

    if (createOrEdit == CreateOrEdit.CREATE) {
      thisStage.setTitle("Create New Task");
      btnConfirm.setText("Create");

    } else if (createOrEdit == CreateOrEdit.EDIT) {
      thisStage.setTitle("Edit Task");
      btnConfirm.setText("Save");
      btnConfirm.setDisable(true);
    }
    this.createOrEdit = createOrEdit;

    btnConfirm.setDefaultButton(true);
    thisStage.setResizable(false);

    labelField.textProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue.trim().length() > 20) {
        labelField.setStyle("-fx-text-inner-color: red;");
      } else {
        labelField.setStyle("-fx-text-inner-color: black;");
      }
    });
    // TODO finish
  }

  /**
   * Initialises the models lists todo expand when it does more
   */
  private void initialiseLists() {
    availablePeople = FXCollections.observableArrayList();
    allocatedPeople = FXCollections.observableArrayList();

    Sprint sprint = null;
    if (parent instanceof Sprint) {
      sprint = (Sprint) parent;
    } else if (parent instanceof Story) {
      Story story = (Story) parent;
      for (Sprint mainSprint : mainApp.getSprints()) {
        if (mainSprint.getSprintStories().contains(story)) {
          sprint = mainSprint;
          break;
        }
      }
    }
    if (sprint != null) {
      Team team = sprint.getSprintTeam();
      availablePeople.addAll(team.getTeamMembers());
    }

    availablePeopleList.setItems(availablePeople);
    allocatedPeopleList.setItems(allocatedPeople);
  }

  /**
   * Allocate a team member to the task.
   *
   * @param event Action event.
   */
  @FXML
  protected void btnAddPersonClick(ActionEvent event) {
    Person selectedPerson = availablePeopleList.getSelectionModel().getSelectedItem();
    // todo effort logging
    if (selectedPerson != null) {
      allocatedPeople.add(selectedPerson);
      availablePeople.remove(selectedPerson);

      allocatedPeopleList.getSelectionModel().select(selectedPerson);
//      if (createOrEdit == CreateOrEdit.EDIT) { // TODO
//        checkButtonDisabled();
//      }
    }
  }

  /**
   * Deallocate a team member from the task.
   *
   * @param event Action event.
   */
  @FXML
  protected void btnRemovePersonClick(ActionEvent event) {
    Person selectedPerson = allocatedPeopleList.getSelectionModel().getSelectedItem();
    if (selectedPerson != null) {
      availablePeople.add(selectedPerson);
      allocatedPeople.remove(selectedPerson);

      availablePeopleList.getSelectionModel().select(selectedPerson);
//      if (createOrEdit == CreateOrEdit.EDIT) { // TODO
//        checkButtonDisabled();
//      }
    }
  }

  @FXML
  protected void btnConfirmClick(ActionEvent event) {
    // todo implement and javadoc
    thisStage.close();
  }

  /**
   * Close the dialog and remove all applied changes
   *
   * @param event Action event
   */
  @FXML
  protected void btnCancelClick(ActionEvent event) {
    thisStage.close();
  }
}