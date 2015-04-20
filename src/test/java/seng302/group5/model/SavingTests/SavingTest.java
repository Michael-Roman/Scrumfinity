package seng302.group5.model.SavingTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seng302.group5.Main;
import seng302.group5.model.Person;
import seng302.group5.model.Project;
import seng302.group5.model.Saving;
import seng302.group5.model.Skill;
import seng302.group5.model.Team;

/**
 * Created by Michael on 3/19/2015.
 */
public class SavingTest {

  public Main main;
  public File newFile = new File(System.getProperty("user.home".concat("//Documents")), "Test1.xml");

  @Before
  public void setUp(){
    main = new Main();
    ObservableList<Skill> skillSet = FXCollections.observableArrayList();
    skillSet.add(new Skill("C", "C saving"));
    main.getPeople().add(new Person("msr51", "Mike", "Roman", skillSet));
    main.getProjects().addAll(
        new Project("xyz01", "supah proj", "This is the best thing ever"),
        new Project("xyz01", "supah proj", "This is the best thing ever"),
        new Project("xyz01", "supah proj", "This is the best thing ever"));
    main.getSkills().add(new Skill("Ballin'", "Straight from Compton"));
    main.getTeams().add(new Team("Boys2men", main.getPeople(), "Best group of all time"));
  }

  @Test
  public void testSavingCreate(){
    Saving.saveDataToFile(newFile, main);
    assertTrue(newFile.exists());
  }

  @Test
  public void testLoadingContents(){
    testSavingCreate();
    Main orig = main;
    main.getProjects().clear();
    main.getPeople().clear();
    Saving.loadDataFromFile(newFile, main);
    for (Person i: main.getPeople()) {
      assertTrue(orig.getPeople().contains(i));
    }
    for (Project i: main.getProjects()) {
      assertTrue(orig.getProjects().contains(i));
    }
    for (Skill i: main.getSkills()) {
      assertTrue(orig.getSkills().contains(i));
    }
    for (Team i: main.getTeams()) {
      assertTrue(orig.getTeams().contains(i));
    }
  }
}