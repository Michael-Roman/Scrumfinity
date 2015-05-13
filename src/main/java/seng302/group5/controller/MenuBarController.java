package seng302.group5.controller;

import java.io.File;
import java.util.Optional;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Dialog;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import seng302.group5.Main;
import seng302.group5.controller.enums.CreateOrEdit;
import seng302.group5.model.AgileItem;
import seng302.group5.model.NewLoading;
import seng302.group5.model.NewSaving;
import seng302.group5.model.undoredo.UndoRedoHandler;
import seng302.group5.model.util.ReportWriter;
import seng302.group5.model.util.Settings;


import seng302.group5.model.undoredo.UndoRedoObject;

/**
 * Created by Michael on 3/15/2015. Controller for MenuBar
 *
 * Edited by Craig on 17/03/2015. added save button functionality.
 */
public class MenuBarController {

  @FXML private MenuItem openMenuItem;
  @FXML private MenuItem saveMenuItem;
  @FXML private MenuItem undoMenuItem;
  @FXML private MenuItem redoMenuItem;
  @FXML private MenuItem deleteMenuItem;
  @FXML private MenuItem editMenuItem;

  @FXML private CheckMenuItem showListMenuItem;
  @FXML private CheckMenuItem showProjectsMenuItem;
  @FXML private CheckMenuItem showPeopleMenuItem;
  @FXML private CheckMenuItem showTeamsMenuItem;
  @FXML private CheckMenuItem showSkillsMenuItem;
  @FXML private CheckMenuItem showReleasesMenuItem;
  @FXML private CheckMenuItem showStoriesMenuItem;

  private Main mainApp;

  /**
   * Initialise the fxml, basic setup functions called.
   */
  @FXML
  private void initialize() {
    // Set up the keyboard shortcuts
    openMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
    saveMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
    undoMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN));
    redoMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN));
    deleteMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.DELETE));
    editMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));

    // Alt + digit to change list view
    showProjectsMenuItem.setAccelerator(
        new KeyCodeCombination(KeyCode.DIGIT1, KeyCombination.ALT_DOWN));
    showTeamsMenuItem.setAccelerator(
        new KeyCodeCombination(KeyCode.DIGIT2, KeyCombination.ALT_DOWN));
    showPeopleMenuItem.setAccelerator(
        new KeyCodeCombination(KeyCode.DIGIT3, KeyCombination.ALT_DOWN));
    showSkillsMenuItem.setAccelerator(
        new KeyCodeCombination(KeyCode.DIGIT4, KeyCombination.ALT_DOWN));
    showReleasesMenuItem.setAccelerator(
        new KeyCodeCombination(KeyCode.DIGIT5, KeyCombination.ALT_DOWN));
    showStoriesMenuItem.setAccelerator(
        new KeyCodeCombination(KeyCode.DIGIT6, KeyCombination.ALT_DOWN));

    // Ticks the project menu item
    showProjectsMenuItem.setSelected(true);
    showListMenuItem.setSelected(true);

    // Disable the undo/redo buttons on launch
    undoMenuItem.setDisable(true);
    redoMenuItem.setDisable(true);
  }

  /**
   * Tells ListMainPaneController to hide/show the item list based on current state (stored in
   * ListMainPaneController)
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void showHideList(ActionEvent event) {
    mainApp.getLMPC().showHideList(showListMenuItem);
  }

  /**
   * Creates the dialog for when new project is clicked
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void createProject(ActionEvent event) {
    mainApp.showProjectDialog(CreateOrEdit.CREATE);
  }

  /**
   * Creates the dialog for when new Release is clicked
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void createRelease(ActionEvent event) {
    mainApp.showReleaseDialog(CreateOrEdit.CREATE);
  }

  /**
   * Creates the dialog for when the user tries to edit a item in the list. has alert popups when
   * unable to.
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void editItem(ActionEvent event) {
    String listType = Settings.currentListType;
    switch (listType) {
      case "Projects":
        mainApp.showProjectDialog(CreateOrEdit.EDIT);
        break;
      case "People":
        mainApp.showPersonDialog(CreateOrEdit.EDIT);
        break;
      case "Skills":
        mainApp.showSkillDialog(CreateOrEdit.EDIT);
        break;
      case "Teams":
        mainApp.showTeamDialog(CreateOrEdit.EDIT);
        break;
      case "Releases":
        mainApp.showReleaseDialog(CreateOrEdit.EDIT);
        break;
      case "Stories":
        mainApp.showStoryDialog(CreateOrEdit.EDIT);
        break;
    }
  }

  /**
   * Creates the dialog for when new skill is clicked
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void createSkill(ActionEvent event) {
    mainApp.showSkillDialog(CreateOrEdit.CREATE);
  }

  /**
   * Creates the dialog for when new Person is clicked
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void createPerson(ActionEvent event) {
    mainApp.showPersonDialog(CreateOrEdit.CREATE);
  }

  /**
   * Creates the dialog for when new team is clicked
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void createTeam(ActionEvent event) {
    mainApp.showTeamDialog(CreateOrEdit.CREATE);
  }

  /**
   * Creates a dialog for when a new Story is clicked.
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void createStory(ActionEvent event) {
    mainApp.showStoryDialog(CreateOrEdit.CREATE);
  }

  /**
   * Fxml import for quit button, closes application on click
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void btnQuitClick(ActionEvent event) {
    mainApp.exitScrumfinity();
  }


  /**
   * Save button that attempts to save in the current open file, if no file is open, it opens the
   * file chooser dialog to allow the user to select where they wish to save the file.
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void btnClickSave(ActionEvent event) {
    if (Settings.currentFile != null) {
      try {
        NewSaving save = new NewSaving(mainApp);
        save.saveData(Settings.currentFile);
        mainApp.refreshLastSaved();
        mainApp.setLastSaved(); //for revert
      } catch (Exception a) {
        System.out.println("Current File does not exist");
      }
    } else {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Save Project");
      if (Settings.defaultFilepath != null) {
        fileChooser.setInitialDirectory(Settings.defaultFilepath);
      }
      try {
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null) {
          Settings.currentFile = file;
          NewSaving save = new NewSaving(mainApp);
          save.saveData(file);

          // Refresh the last saved action
          mainApp.refreshLastSaved();
          mainApp.setLastSaved(); //for revert
        }
      } catch (Exception e) {
        System.out.println("No filename specified");
      }
    }
  }


  /**
   * Save as file button in File menu, opens up the file chooser to select where you would like to
   * save.
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void btnClickSaveAs(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save Project");
    if (Settings.defaultFilepath != null) {
      fileChooser.setInitialDirectory(Settings.defaultFilepath);
    }
    try {
      File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

      if (file != null) {
        Settings.currentFile = file;
        NewSaving save = new NewSaving(mainApp);
        save.saveData(file);

        // Refresh the last saved action
        mainApp.refreshLastSaved();
        mainApp.setLastSaved(); //for revert
      }
    } catch (Exception e) {
      System.out.println("No filename specified");
    }
  }


  /**
   * Handles the event of the REVERT button being pushed.
   * @param event Event generated by event listener
   */
  @FXML
  protected void btnRevert(ActionEvent event) {

    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Are you sure?!");
    alert.setHeaderText(null);

    String message = "This cannot be undone! Are you sure you want to do this? We suggest doing a \"Save as..\" first!";
    alert.getDialogPane().setPrefHeight(100);
    alert.setContentText(message);

    ButtonType buttonTypeSaveAs = new ButtonType("Save as then revert");
    ButtonType buttonTypeConfirm = new ButtonType("Revert", ButtonBar.ButtonData.OK_DONE);
    ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

    alert.getButtonTypes().setAll(buttonTypeSaveAs, buttonTypeConfirm, buttonTypeCancel);

    Optional<ButtonType> result = alert.showAndWait();
    if (result.get() == buttonTypeSaveAs) {
      // ... user chose "Save as then revert"
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Save Project");
      if (Settings.defaultFilepath != null) {
        fileChooser.setInitialDirectory(Settings.defaultFilepath);
      }
      try {
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null) {
          NewSaving save = new NewSaving(mainApp);
          save.saveData(file);
          mainApp.revert();
        } else {
          Alert alert2 = new Alert(Alert.AlertType.ERROR);
          alert2.setTitle("No file selected");
          alert2.setHeaderText(null);
          alert2.setContentText("No file was selected for saving. Revert cancelled.");
          alert2.showAndWait();
        }
      } catch (Exception e) {
        Alert alert2 = new Alert(Alert.AlertType.ERROR);
        alert2.setTitle("No file selected");
        alert2.setHeaderText(null);
        alert2.setContentText("No file was selected for saving. Revert cancelled.");
        alert2.showAndWait();
      }
    } else if (result.get() == buttonTypeConfirm) {
      // ... user chose "Revert"
      mainApp.revert();
    }
  }

  /**
   * Open file button in File menu, opens up the file chooser to select which file you wish to
   * open.
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void btnClickOpen(ActionEvent event) {

    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open Project");
    FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("CSV files (*.xml)", "*.xml");
    fileChooser.getExtensionFilters().add(filter);
    if (Settings.defaultFilepath != null) {
      fileChooser.setInitialDirectory(Settings.defaultFilepath);
    }
    try {
      File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
      if (file != null) {
        Settings.currentFile = file;
        mainApp.resetAll();
        NewLoading load = new NewLoading(mainApp);
        load.loadFile(file);

        mainApp.getLMPC().refreshList();
        if (!(Settings.organizationName == "")) {
          mainApp.setMainTitle("Scrumfinity - " + Settings.organizationName);
        } else {
          mainApp.setMainTitle("Scrumfinity");
        }
        mainApp.toggleName();
        showListMenuItem.setSelected(true);
        deselectList(Settings.currentListType);
        mainApp.setLastSaved(); //for revert
      }
    } catch (Exception e) {
      System.out.println("No file selected");
    }
  }

  /**
   * Handles menu item Revert, initiates report creation process.
   */
  @FXML
  protected void btnClickReport() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save Report");
    File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

    ReportWriter report = new ReportWriter();
    report.writeReport(mainApp, file);
  }

  /**
   * Handles menu item Org. Name, lets user assign organization name through dialog.
   */
  @FXML
  protected void btnClickOrganization() {
    // Basic setup
    Dialog<String> orgDialog = new Dialog<>();
    orgDialog.setTitle("Organization Name");
    orgDialog.setHeaderText("Enter organization name");
    orgDialog.setGraphic(null);

    ButtonType acceptButtonType = new ButtonType("Accept", ButtonBar.ButtonData.OK_DONE);
    orgDialog.getDialogPane().getButtonTypes().addAll(acceptButtonType, ButtonType.CANCEL);

    TextField orgField = new TextField();
    if (!Settings.organizationName.isEmpty()) {
      orgField.setText(Settings.organizationName);
    } else {
      orgField.setPromptText("Default");
    }

    Node acceptButton = orgDialog.getDialogPane().lookupButton(acceptButtonType);
    acceptButton.setDisable(true);

    // Deactivates button if no changes.
    orgField.textProperty().addListener((observable, oldValue, newValue) -> {
      acceptButton.setDisable(newValue.equals(Settings.organizationName));
    });

    orgDialog.getDialogPane().setContent(orgField);
    Platform.runLater(orgField::requestFocus);

    orgDialog.setResultConverter(dialogButton -> {
      if (dialogButton == acceptButtonType) {
        return orgField.getText();
      }
      return null;
    });

    Optional<String> result = orgDialog.showAndWait();

    result.ifPresent(newField -> {
      Settings.organizationName = result.get().trim();
      if (Settings.organizationName != "") {
        mainApp.setMainTitle("Scrumfinity - " + Settings.organizationName);
      } else {
        mainApp.setMainTitle("Scrumfinity");
      }
      mainApp.toggleName();
    });
  }

  /**
   * Populates left side with projects.
   */
  @FXML
  protected void btnShowProjects() {
    Settings.currentListType = "Projects";
    deselectList("Projects");
    showProjectsMenuItem.setSelected(true);
    mainApp.getLMPC().refreshList();
  }

  /**
   * Populates left side with people.
   */
  @FXML
  protected void btnShowPeople() {
    Settings.currentListType = "People";
    deselectList("People");
    showPeopleMenuItem.setSelected(true);
    mainApp.getLMPC().refreshList();
  }

  /**
   * Populates left side with skills.
   */
  @FXML
  protected void btnShowSkills() {
    Settings.currentListType = "Skills";
    deselectList("Skills");
    showSkillsMenuItem.setSelected(true);
    mainApp.getLMPC().refreshList();
  }

  /**
   * Populates left side with teams.
   */
  @FXML
  protected void btnShowTeams() {
    Settings.currentListType = "Teams";
    deselectList("Teams");
    mainApp.getLMPC().refreshList();
  }

  /**
   * Populates left side with Releases.
   */
  @FXML
  protected void btnShowReleases() {
    Settings.currentListType = "Releases";
    deselectList("Releases");
    mainApp.getLMPC().refreshList();
  }

  /**
   * Populates left side with Stories
   */
  @FXML
  protected void btnShowStories() {
    Settings.currentListType = "Stories";
    deselectList("Stories");
    mainApp.getLMPC().refreshList();
  }

  /**
   * Used to deselect all lists except for the list you want to be showed (selectedList) Show/hide
   * lists uses this to show no lists.
   *
   * @param selectedList The currently displayed list
   */
  protected void deselectList(String selectedList) {
    showProjectsMenuItem.setSelected(false);
    showPeopleMenuItem.setSelected(false);
    showTeamsMenuItem.setSelected(false);
    showSkillsMenuItem.setSelected(false);
    showReleasesMenuItem.setSelected(false);
    showStoriesMenuItem.setSelected(false);
    if (!selectedList.equals("")) {
      showListMenuItem.setSelected(true);
      switch (selectedList) {
        case "Projects":
          showProjectsMenuItem.setSelected(true);
          break;
        case "People":
          showPeopleMenuItem.setSelected(true);
          break;
        case "Skills":
          showSkillsMenuItem.setSelected(true);
          break;
        case "Teams":
          showTeamsMenuItem.setSelected(true);
          break;
        case "Releases":
          showReleasesMenuItem.setSelected(true);
          break;
        case "Stories":
          showStoriesMenuItem.setSelected(true);
          break;
      }
    }
  }

  /**
   * Deletes the currently selected item in the list. If it is able to.
   */
  @FXML
  protected void btnDelete() {
    AgileItem selectedItem = mainApp.getLMPC().getSelected();
    if (selectedItem == null) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Cannot delete");
      alert.setHeaderText(null);
      alert.setContentText("Deleting failed - No item selected");
      alert.showAndWait();
    } else if (mainApp.getNonRemovable().contains(selectedItem)) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Cannot delete");
      alert.setHeaderText(null);
      alert.setContentText(String.format("The item %s cannot be deleted", selectedItem));
      alert.showAndWait();
    } else {
      mainApp.delete(selectedItem);
      mainApp.refreshList();
    }
  }

  /**
   * Gets rid of the last change.
   */
  @FXML
  protected void btnUndoClick() {
    mainApp.undo();
  }

  /**
   * Does again the last action that was undone.
   */
  @FXML
  protected void btnRedoClick() {
    mainApp.redo();
  }

  /**
   * sets the main app to the param
   *
   * @param mainApp The main application object
   */
  public void setMainApp(Main mainApp) {
    this.mainApp = mainApp;
  }

  /**
   * Refresh the undo and redo menu items based on the state of the undo/redo handler
   *
   * @param undoRedoHandler the undo/redo handler to check
   */
  public void checkUndoRedoMenuItems(UndoRedoHandler undoRedoHandler) {
    // undo menu item
    if (undoRedoHandler.peekUndoStack() == null) {
      undoMenuItem.setDisable(true);
    } else {
      undoMenuItem.setDisable(false);
    }

    // redo menu item
    if (undoRedoHandler.peekRedoStack() == null) {
      redoMenuItem.setDisable(true);
    } else {
      redoMenuItem.setDisable(false);
    }
  }
}
