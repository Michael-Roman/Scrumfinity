package seng302.group5.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import seng302.group5.Main;
import seng302.group5.model.AgileItem;
import seng302.group5.model.util.Settings;

/**
 * Created by Michael on 3/15/2015.
 * ListManePane fxml contains styling for both List and MainPane, still
 * requires asthetic tweaks, doesn't scale properly.
 */
public class ListMainPaneController {

  @FXML private ListView listView;
  @FXML private TextArea sampleTextArea;
  private Main mainApp;
  private boolean isListShown = true;

  public ListMainPaneController() {};
  private AgileItem selectedItem;

  /**
   * Initialise the fxml, basic setup functions called.
   */
  @FXML
  private void initialize() {
    Settings.currentListType = "Project";
    iniActorList();
    showHideList();
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
              sampleTextArea.clear();
              sampleTextArea.appendText(next.toString());
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
  }

  public Object getSelectedProject() {
    if(isListShown == false || Settings.currentListType != "Project"){
      return null;
    }
    return selectedItem;
  }

  public Object getSelectedPerson() {
    if(isListShown == false || Settings.currentListType != "People"){
      return null;
    }
    return selectedItem;
  }

  public Object getSelectedTeam() {
    if(isListShown == false || Settings.currentListType != "Team") {
      return null;
    }
    return selectedItem;
  }

  public Object getSelectedSkill() {
    if(isListShown == false || Settings.currentListType != "Skills"){
      return null;
    }
    return selectedItem;
  }
  public void setMainApp(Main mainApp) {
    this.mainApp = mainApp;
  }
}
