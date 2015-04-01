package seng302.group5.model.undoredo;

import java.util.ArrayList;
import java.util.Stack;

import seng302.group5.Main;
import seng302.group5.model.AgileItem;
import seng302.group5.model.Person;
import seng302.group5.model.Project;

/**
 * Created by Michael on 3/17/2015.
 *
 * Work in progess, may scrap alltogether
 */
public class UndoRedoHandler {

  /**
   * Local enum for denoting whether the action is an undo or a redo
   */
  private enum UndoOrRedo {
    UNDO, REDO
  }

  private Main mainApp;

  private Stack<UndoRedoObject> undoStack;
  private Stack<UndoRedoObject> redoStack;

  public UndoRedoHandler(Main mainApp) {
    this.mainApp = mainApp;
    undoStack = new Stack<>();
    redoStack = new Stack<>();
  }

  /**
   * Add a new action onto the undo stack
   *
   * @param undoRedoObject The action object
   */
  public void newAction(UndoRedoObject undoRedoObject) {
    undoStack.push(undoRedoObject);
    redoStack.clear();
  }

  /**
   * Generic undo function to undo the latest action
   *
   * @throws Exception Error message if data is invalid
   */
  public void undo() throws Exception {
    if (undoStack.isEmpty()) {
      System.out.println("Nothing to undo");
      return;
    }
    redoStack.push(undoStack.peek());
    UndoRedoObject undoRedoObject = undoStack.pop();

    handleUndoRedoObject(undoRedoObject, UndoOrRedo.UNDO);
  }

  /**
   * Generic redo function to redo the latest undone action
   *
   * @throws Exception Error message if data is invalid
   */
  public void redo() throws Exception {
    if (redoStack.isEmpty()) {
      System.out.println("Nothing to redo");
      return;
    }
    undoStack.push(redoStack.peek());
    UndoRedoObject undoRedoObject = redoStack.pop();

    handleUndoRedoObject(undoRedoObject, UndoOrRedo.REDO);
  }

  /**
   * Handle a undo or redo call
   *
   * @param undoRedoObject Object containing the action data
   * @param undoOrRedo Whether undoing or redoing the action
   * @throws Exception Error message if data is invalid
   */
  private void handleUndoRedoObject(UndoRedoObject undoRedoObject,
                                    UndoOrRedo undoOrRedo) throws Exception {

    Action action = undoRedoObject.getAction();

    // TODO: remove temp debug printing below
    String undoOrRedoStr;
    if (undoOrRedo == UndoOrRedo.UNDO) {
      undoOrRedoStr = "undo";
    } else {
      undoOrRedoStr = "redo";
    }

    switch(action) {
      case PROJECT_CREATE:
        //TODO: delete a project
        System.out.println(String.format("I am %sing a project creation", undoOrRedoStr)) ; // temp
        handleProjectCreate(undoRedoObject, undoOrRedo);
        break;

      case PROJECT_EDIT:
        System.out.println(String.format("I am %sing a project edit", undoOrRedoStr));      // temp
        handleProjectEdit(undoRedoObject, undoOrRedo);
        break;

      case PROJECT_DELETE:
        System.out.println(String.format("I am %sing a project deletion", undoOrRedoStr));  // temp
        // Some possible delete code for future
//        if (data.size() < 3) {
//          throw new Exception("Can't recreate project - Less than 3 variables");
//        }
//        Project project = new Project(data.get(0), data.get(1), data.get(2));
//        mainApp.addProject(project);
        break;

      case PERSON_CREATE:
        System.out.println(String.format("I am %sing a person creation", undoOrRedoStr)) ; // temp
        handlePersonCreate(undoRedoObject, undoOrRedo);
        break;

      case PERSON_EDIT:
        System.out.println(String.format("I am %sing a person edit", undoOrRedoStr)) ; // temp
        handlePersonEdit(undoRedoObject, undoOrRedo);
        break;

      case UNDEFINED:
        throw new Exception("Unreadable UndoRedoObject");

      default:
        throw new Exception("Undo/Redo case is not handled");
    }
  }

  /**
   * Undo or redo a project creation
   *
   * @param undoRedoObject Object containing the action data
   * @param undoOrRedo Whether undoing or redoing the action
   * @throws Exception Error message if data is invalid
   */
  private void handleProjectCreate(UndoRedoObject undoRedoObject,
                                   UndoOrRedo undoOrRedo) throws Exception {

    // Get the data and ensure it has data for the project
    ArrayList<AgileItem> data = undoRedoObject.getData();
    if (data.size() < 1) {
      throw new Exception("Can't undo/redo project creation - No variables");
    }

    // Get the project ID which is currently in the list and the project to change
    Project project = (Project) data.get(0);
    String projectID = project.getProjectID();

    // Make the changes and refresh the list
    if (undoOrRedo == UndoOrRedo.UNDO) {
      // TODO: delete for undo
      // Find the project in the list and ensure it exists so it can be deleted
      Project projectToDelete = null;
      for (Project projectInList : mainApp.getProjects()) {
        if (projectInList.getProjectID().equals(projectID)) {
          projectToDelete = projectInList;
          break;
        }
      }
      if (projectToDelete == null) {
        throw new Exception("Can't undo project creation - Can't find the created project");
      }
      // TODO: Call deleteItem(projectToDelete);
    } else {
      Project projectToAdd = new Project(project);
      mainApp.addProject(projectToAdd);
    }
    mainApp.refreshList();
  }

  /**
   * Undo or redo a project edit
   *
   * @param undoRedoObject Object containing the action data
   * @param undoOrRedo Whether undoing or redoing the action
   * @throws Exception Error message if data is invalid
   */
  private void handleProjectEdit(UndoRedoObject undoRedoObject,
                                 UndoOrRedo undoOrRedo) throws Exception {

    // Get the data and ensure it has data for the projects both before and after
    ArrayList<AgileItem> data = undoRedoObject.getData();
    if (data.size() < 2) {
      throw new Exception("Can't undo/redo project edit - Less than 2 variables");
    }

    // Get the project ID which is currently in the list and the project to edit
    Project currentProject;
    String currentProjectID;
    Project newProject;

    if (undoOrRedo == UndoOrRedo.UNDO) {
      currentProject = (Project) data.get(1);
      currentProjectID = currentProject.getProjectID();
      newProject = (Project) data.get(0);
    } else {
      currentProject = (Project) data.get(0);
      currentProjectID = currentProject.getProjectID();
      newProject = (Project) data.get(1);
    }

    // Find the project in the list and ensure it exists
    Project projectToEdit = null;
    for (Project projectInList : mainApp.getProjects()) {
      if (projectInList.getProjectID().equals(currentProjectID)) {
        projectToEdit = projectInList;
        break;
      }
    }
    if (projectToEdit == null) {
      throw new Exception("Can't undo/redo project edit - Can't find the edited project");
    }

    // Make the changes and refresh the list
    projectToEdit.setProjectID(newProject.getProjectID());
    projectToEdit.setProjectName(newProject.getProjectName());
    projectToEdit.setProjectDescription(newProject.getProjectDescription());
    mainApp.refreshList();
  }

  /**
   * Undo or redo a person creation
   *
   * @param undoRedoObject Object containing the action data
   * @param undoOrRedo Whether undoing or redoing the action
   * @throws Exception Error message if data is invalid
   */
  private void handlePersonCreate(UndoRedoObject undoRedoObject,
                                  UndoOrRedo undoOrRedo) throws Exception {

    // Get the data and ensure it has data for the person
    ArrayList<AgileItem> data = undoRedoObject.getData();
    if (data.size() < 1) {
      throw new Exception("Can't undo/redo person creation - No variables");
    }

    // Get the person ID which is currently in the list and the person to change
    Person person = (Person) data.get(0);
    String personID = person.getPersonID();

    // Make the changes and refresh the list
    if (undoOrRedo == UndoOrRedo.UNDO) {
      // TODO: delete for undo
      // Find the person in the list and ensure it exists so it can be deleted
      Person personToDelete = null;
      for (Person personInList : mainApp.getPeople()) {
        if (personInList.getPersonID().equals(personID)) {
          personToDelete = personInList;
          break;
        }
      }
      if (personToDelete == null) {
        throw new Exception("Can't undo person creation - Can't find the created person");
      }
      mainApp.deletePerson(personToDelete);
    } else {
      Person personToAdd = new Person(person);
      mainApp.addPerson(personToAdd);
    }
    mainApp.refreshList();
  }

  /**
   * Undo or redo a person edit
   *
   * @param undoRedoObject Object containing the action data
   * @param undoOrRedo Whether undoing or redoing the action
   * @throws Exception Error message if data is invalid
   */
  private void handlePersonEdit(UndoRedoObject undoRedoObject,
                                UndoOrRedo undoOrRedo) throws Exception {

    // Get the data and ensure it has data for the people both before and after
    ArrayList<AgileItem> data = undoRedoObject.getData();
    if (data.size() < 2) {
      throw new Exception("Can't undo/redo person edit - Less than 2 variables");
    }

    // Get the person ID which is currently in the list and the person to edit
    Person currentPerson;
    String currentPersonID;
    Person newPerson;

    if (undoOrRedo == UndoOrRedo.UNDO) {
      currentPerson = (Person) data.get(1);
      currentPersonID = currentPerson.getPersonID();
      newPerson = (Person) data.get(0);
    } else {
      currentPerson = (Person) data.get(0);
      currentPersonID = currentPerson.getPersonID();
      newPerson = (Person) data.get(1);
    }

    // Find the person in the list and ensure it exists
    Person personToEdit = null;
    for (Person personInList : mainApp.getPeople()) {
      if (personInList.getPersonID().equals(currentPersonID)) {
        personToEdit = personInList;
        break;
      }
    }
    if (personToEdit == null) {
      throw new Exception("Can't undo/redo person edit - Can't find the edited person");
    }

    // Make the changes and refresh the list
    personToEdit.setPersonID(newPerson.getPersonID());
    personToEdit.setFirstName(newPerson.getFirstName());
    personToEdit.setLastName(newPerson.getLastName());
    mainApp.refreshList();
  }

}