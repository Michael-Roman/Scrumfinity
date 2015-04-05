package seng302.group5.controller;

import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import seng302.group5.Main;
import seng302.group5.controller.enums.CreateOrEdit;
import seng302.group5.model.Person;
import seng302.group5.model.Skill;
import seng302.group5.model.undoredo.Action;
import seng302.group5.model.undoredo.UndoRedoObject;

/**
 * Created by Zander on 18/03/2015.
 */
public class PersonDialogController {

  @FXML private TextField personIDField;
  @FXML private TextField personFirstNameField;
  @FXML private TextField personLastNameField;
  @FXML private Button btnCreatePerson;
  @FXML private Button btnRemoveSkill;
  @FXML private ComboBox skillsList;
  @FXML private ListView personSkillList;

  private Main mainApp;
  private Stage thisStage;
  private CreateOrEdit createOrEdit;
  private Person person;
  private Person lastPerson;


  /**
   * Setup the person dialog controller
   *
   * @param mainApp - The main application object
   * @param thisStage - The stage of the dialog
   * @param createOrEdit - If dialog is for creating or editing a person
   * @param person - The person object if editing, null otherwise
   */
  public void setupController(Main mainApp,
                              Stage thisStage,
                              CreateOrEdit createOrEdit,
                              Person person) {
    this.mainApp = mainApp;
    this.thisStage = thisStage;
    ObservableList<String> listOfSkills = null;
    listOfSkills.addAll(mainApp.getSkills().stream().map(Skill::getSkillName).collect(Collectors.toList()));
    System.out.println(listOfSkills);
    if (createOrEdit == CreateOrEdit.CREATE) {
      thisStage.setTitle("Create New Person");
      skillsList.setItems(listOfSkills);
      btnCreatePerson.setText("Create");
    } else if (createOrEdit == CreateOrEdit.EDIT) {
      thisStage.setTitle("Edit Project");
      btnCreatePerson.setText("Save");

      personIDField.setText(person.getPersonID());
      personFirstNameField.setText(person.getFirstName());
      personLastNameField.setText(person.getLastName());
      personSkillList.setItems(person.getSkillSet());
    }
    this.createOrEdit = createOrEdit;

    if (person != null) {
      this.person = person;
      this.lastPerson = new Person(person);
    } else {
      this.person = null;
      this.lastPerson = null;
    }

    btnCreatePerson.setDefaultButton(true);
  }

  /**
   * Generate an UndoRedoObject to place in the stack
   * @return the UndoRedoObject to store
   */
  private UndoRedoObject generateUndoRedoObject() {
    UndoRedoObject undoRedoObject = new UndoRedoObject();

    if (createOrEdit == CreateOrEdit.CREATE) {
      undoRedoObject.setAction(Action.PERSON_CREATE);
    } else {
      undoRedoObject.setAction(Action.PERSON_EDIT);
      undoRedoObject.addDatum(lastPerson);
    }

    // Store a copy of person to edit in stack to avoid reference problems
    Person personToStore = new Person(person);
    undoRedoObject.addDatum(personToStore);

    return undoRedoObject;
  }

  /**
   * Creates a new Person from the textfield data
   * on click of 'Create' button.
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void btnCreatePersonClick(ActionEvent event) {
    StringBuilder errors = new StringBuilder();
    errors.append("Invalid Fields:");
    int noErrors = 0;

    String personID = "";
    String personFirstName = personFirstNameField.getText().trim();
    String personLastName = personLastNameField.getText().trim();
    ObservableList<Skill> personSkillSet = personSkillList.getItems();

    try {
      personID = parsePersonID(personIDField.getText());
    }
    catch (Exception e) {
      noErrors++;
      errors.append(String.format("\n\t%s", e.getMessage()));
    }

    // Display all errors if they exist
    if (noErrors > 0) {
      String title;
      if (noErrors == 1) {
        title = String.format("%d Invalid Field", noErrors);
      }
      else {
        title = String.format("%d Invalid Fields", noErrors);
      }
      // TODO: Dialogs for errors
      System.out.println(String.format("%s\n%s", title, errors.toString()));
    }
    else {
      if (createOrEdit == CreateOrEdit.CREATE) {
        person = new Person(personID, personFirstName, personLastName, personSkillSet);
        mainApp.addPerson(person);
      } else if (createOrEdit == CreateOrEdit.EDIT) {
        person.setPersonID(personID);
        person.setFirstName(personFirstName);
        person.setLastName(personLastName);
        person.setSkillSet(personSkillSet);
        mainApp.refreshList();
      }

      UndoRedoObject undoRedoObject = generateUndoRedoObject();
      mainApp.newAction(undoRedoObject);

      thisStage.close();
    }
  }

  /**
   * Closes the CreatePerson dialog box in click of
   * 'Cancel' button.
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void btnCancelClick(ActionEvent event) {
    thisStage.close();
  }

  /**
   * Checks that the Person ID entry box contains
   * valid input.
   *
   * @param inputPersonID Person ID from entry field.
   * @return Person ID if ID is valid.
   * @throws Exception Any invalid input.
   */
  private String parsePersonID(String inputPersonID) throws Exception {
    inputPersonID = inputPersonID.trim();

    if (inputPersonID.isEmpty()) {
      throw new Exception("Person ID is empty.");
    }
    else if (inputPersonID.length() > 8) {
      throw new Exception("Person ID is more than 8 characters long");
    }
    else {
      String lastPersonID;
      if (lastPerson == null) {
        lastPersonID = "";
      } else {
        lastPersonID = lastPerson.getPersonID();
      }
      for (Person personInList : mainApp.getPeople()) {
        String personID = personInList.getPersonID();
        if (personID.equals(inputPersonID) && !personID.equals(lastPersonID)) {
          throw new Exception("Person ID is not unique");
        }
      }
    }
    return inputPersonID;
  }
}
