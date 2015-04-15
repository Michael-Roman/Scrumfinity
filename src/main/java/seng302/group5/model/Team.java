package seng302.group5.model;

import org.mockito.cglib.core.Local;

import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**Basic model of a Team.
 *
 * Created by Zander on 24/03/2015.
 */
public class Team implements AgileItem {

  private String teamID;
  private String teamDescription;
  private LocalDate startDate;
  private LocalDate endDate;
  private Project currentProject = null;

  private ObservableList<Person> teamMembers = FXCollections.observableArrayList();

  /**
   * Default constructor.
   */
  public Team() {
    this.teamID = "";
    this.teamDescription = "";
  }

  /**
   * Team constructor.
   *
   * @param teamID Unique, non-null team ID.
   * @param teamMembers List of people in the team.
   * @param teamDescription Description of the team.
   */
  public Team(String teamID, ObservableList<Person> teamMembers, String teamDescription) {
    this.teamID = teamID;
    this.teamMembers = teamMembers;
    this.teamDescription = teamDescription;
  }


  /**
   * Constructor to create a clone of an existing person
   *
   * @param clone Person to clone
   */
  public Team(Team clone) {
    this.teamID = clone.getTeamID();
    this.teamDescription = clone.getTeamDescription();
    this.teamMembers = FXCollections.observableArrayList(clone.getTeamMembers());
  }

  public String getTeamID() {
    return this.teamID;
  }

  public void setTeamID(String teamID) {
    this.teamID = teamID;
  }

  public String getTeamDescription() {
    return this.teamDescription;
  }

  public void setTeamDescription(String teamDescription) {
    this.teamDescription = teamDescription;
  }

  public ObservableList<Person> getTeamMembers() {
    return teamMembers;
  }

  public void setTeamMembers(ObservableList<Person> teamMembers) {
    this.teamMembers = teamMembers;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public void setCurrentProject(Project project) {
    this.currentProject = project;
  }

  public LocalDate getStartDate() {
    return this.startDate;
  }

  public LocalDate getEndDate() {
    return this.endDate;
  }

  public Project getCurrentProject() {
    return this.currentProject;
  }

  public void delete(){}

  public void create(){}

  /**
   * Overrides to toString method with the
   * ID of team.
   *
   * @return Unique ID of team.
   */
  @Override
  public String toString() {
    return teamID;
  }
}
