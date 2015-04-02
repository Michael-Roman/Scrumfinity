package seng302.group5.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import seng302.group5.Main;
import seng302.group5.model.AgileItem;
import seng302.group5.model.util.Settings;
import seng302.group5.model.Person;
import seng302.group5.model.Project;
import seng302.group5.model.Team;
import seng302.group5.model.Skill;


/**
 * Created by Michael on 3/15/2015.
 * ListManePane fxml contains styling for both List and MainPane, still
 * requires asthetic tweaks, doesn't scale properly.
 */
public class ListMainPaneController {

  @FXML private ListView listView;
  @FXML private TextArea sampleTextArea;
  @FXML private SplitPane splitPane;
  @FXML private AnchorPane listViewPane;
  private Main mainApp;
  private boolean isListShown = true;

  public ListMainPaneController() {}
  private AgileItem selectedItem;

  /**
   * Initialise the fxml, basic setup functions called.
   */
  @FXML
  private void initialize() {
    Settings.currentListType = "Project";
    Settings.setSysDefault();
    iniActorList();
  }

  /**
   * Sets listeners to whatever is in the list.
   */
  private void iniActorList() {
    listView.getSelectionModel().selectedItemProperty().addListener(
        new ChangeListener<AgileItem>() {
          @Override
          public void changed(ObservableValue<? extends AgileItem> observableValue,
                              AgileItem previous, AgileItem next) {
            if (next != null) {
              // Will place checks to update main pane here based on item type selected
              if (Settings.currentListType == "People") {
                sampleTextArea.clear();
                sampleTextArea.appendText("Person information \nPerson ID: ");
                for (Person person : mainApp.getPeople()) {
                  if (person.getPersonID().equals(next.toString())){
                    sampleTextArea.appendText(person.getPersonID());
                    sampleTextArea.appendText("\nFirst Name: ");
                    sampleTextArea.appendText(person.getFirstName());
                    sampleTextArea.appendText("\nLast Name: ");
                    sampleTextArea.appendText(person.getLastName());
                    sampleTextArea.appendText("\nSkills: ");
                  }
                }
              } else if (Settings.currentListType == "Project") {
                sampleTextArea.clear();
                sampleTextArea.appendText("Project information \nProject ID: ");
                for (Project project : mainApp.getProjects()) {
                  if (project.getProjectID().equals(next.toString())) {
                    sampleTextArea.appendText(project.getProjectID());
                    sampleTextArea.appendText("\nProject Name: ");
                    sampleTextArea.appendText(project.getProjectName());
                    sampleTextArea.appendText("\nProject Description: \n");
                    sampleTextArea.appendText(project.getProjectDescription());
                  }
                }
              } else if (Settings.currentListType == "Skills") {
                sampleTextArea.clear();
                sampleTextArea.appendText("Skills information \nSkill Name: ");
                for (Skill skill : mainApp.getSkills()) {
                  if (skill.getSkillName().equals(next.toString())) {
                    sampleTextArea.appendText(skill.getSkillName().toString());
                    sampleTextArea.appendText("\nSkill Description: ");
                    sampleTextArea.appendText(skill.getSkillDescription());
                  }
                }
              } else if (Settings.currentListType == "Team") {
                sampleTextArea.clear();
                sampleTextArea.appendText("Team information \nTeam ID: ");
                for (Team team : mainApp.getTeams()) {
                  if (team.getTeamID().equals(next.toString())) {
                    sampleTextArea.appendText(team.getTeamID());
                    sampleTextArea.appendText("\nTeam Description: ");
                    sampleTextArea.appendText(team.getTeamDescription());
                    sampleTextArea.appendText("\nTeam members: \n");
                    for (Person member : team.getTeamMembers()) {
                      sampleTextArea.appendText(member.getFirstName());
                      sampleTextArea.appendText(" - ");
                      sampleTextArea.appendText(member.getPersonID());
                      sampleTextArea.appendText("\n");
                    }
                  }
                }
              }
              selectedItem = next;
            }
          }
        }
    );
  }

  /**
   * Shows/Hides items in list.
   */
  public void showHideList() {
    if(!isListShown){
      checkListType();
    } else {
      ObservableList<AgileItem> clear = FXCollections.observableArrayList();
      listView.setItems(clear);
      isListShown = false;
      // Hide the pane containing the list
      splitPane.getItems().remove(listViewPane);
    }
  }


  /**
   * Refreshes listView and text area text for when edits occur.
   * TODO Testing
   */
  public void refreshList() {
    listView.setItems(null);
    checkListType();
    listView.getSelectionModel().clearSelection();
    sampleTextArea.clear();
  }

  /**
   * Checks list type currently set as viewed list by user.
   * TODO testing
   */
  public void checkListType(){
    String listType = Settings.currentListType;
    switch (listType) {
      case "Project":
        isListShown = true;
        listView.setItems(mainApp.getProjects());
        break;
      case "People":
        isListShown = true;
        listView.setItems(mainApp.getPeople());
        break;
      case "Skills":
        isListShown = true;
        listView.setItems(mainApp.getSkills());
        break;
      case "Team":
        isListShown = true;
        listView.setItems(mainApp.getTeams());
        break;
    }
    // Show the pane containing the list
    if (splitPane.getItems().size() < 2) {
      splitPane.getItems().add(0, listViewPane);
    }
  }

  public AgileItem getSelected(){
    String listType = Settings.currentListType;
    switch (listType) {
      case "Project":
        if (isListShown == false || listType != "Project") {
          return null;
        }
        return selectedItem;
      case "People":
        if (isListShown == false) {
          System.out.println("im in here!");
          return null;
        }
        return selectedItem;
      case "Skills":
        if (isListShown == false || listType != "Skills") {
          return null;
        }
        return selectedItem;
      case "Team":
        if (isListShown == false || listType != "Team") {
          return null;
        }
        return selectedItem;
    }
    return null;
  }

  public String getCurrentListType() {
    return Settings.currentListType;
  }


  public void setMainApp(Main mainApp) {
    this.mainApp = mainApp;
  }
}
