package seng302.group5.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import seng302.group5.Main;
import seng302.group5.controller.enums.CreateOrEdit;
import seng302.group5.model.Story;

/**
 * Created by @author Alex Woo
 */
public class ACDialogController {

  @FXML private Button btnConfirm;
  @FXML private Button btnCancel;
  @FXML private TextArea textAC;
  @FXML private HBox btnContainer;

  private StoryDialogController storyDC;
  private Stage thisStage;
  private CreateOrEdit createOrEdit;
  private String ac;

  /**
   * Setup the Acceptance Criteria dialog controller.
   *
   * @param storyDC The story dialog so that a AC can be added to it
   * @param thisStage The stage of the dialog.
   * @param createOrEdit Whether the dialog is for creating or editing a story.
   * @param ac The acceptance criteria to edit. Null if creation.
   */
  public void setupController(StoryDialogController storyDC, Stage thisStage, CreateOrEdit createOrEdit, String ac) {
    this.storyDC = storyDC;
    this.thisStage = thisStage;
    this.createOrEdit = createOrEdit;
    this.ac = ac;

    String os = System.getProperty("os.name");

    if (!os.startsWith("Windows")) {
      btnContainer.getChildren().remove(btnConfirm);
      btnContainer.getChildren().add(btnCancel);
    }

    if (createOrEdit == CreateOrEdit.CREATE) {
      thisStage.setTitle("Create New Acceptance Criteria");
      btnConfirm.setText("Create");
    } else if (createOrEdit == CreateOrEdit.EDIT) {
      thisStage.setTitle("Edit Acceptance Criteria");
      btnConfirm.setText("Save");

      textAC.setText(ac);
      //btnConfirm.setDisable(true);
    }
    this.createOrEdit = createOrEdit;
    btnConfirm.setDefaultButton(true);
    thisStage.setResizable(false);

  }

  /**Adds the AC to the story.
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void btnAddClick(ActionEvent event) {
    if (this.createOrEdit == createOrEdit.CREATE) {
      this.storyDC.appendAcceptanceCriteria(textAC.getText().trim());
    } else if (createOrEdit == CreateOrEdit.EDIT) {
      this.storyDC.changeAcceptanceCriteria(ac, textAC.getText().trim());
    }
    thisStage.close();
  }

  /**
   * Closes the AC dialog box on click of 'Cancel' button.
   *
   * @param event Event generated by event listener.
   */
  @FXML
  protected void btnCancelClick(ActionEvent event) {
    thisStage.close();
  }
}
