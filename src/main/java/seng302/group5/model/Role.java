package seng302.group5.model;

/**
 * Store a role that a team member can have.
 *
 * @author Alex Woo
 */
public class Role {

  private String roleID;
  private String roleName;
  private Skill requiredSkill;
  private int memberLimit;


  /*
   *Constructors for the class
   */
  public Role(String roleID, String roleName) {
    this.roleID = roleID;
    this.roleName = roleName;
    this.requiredSkill = null;
    this.memberLimit = Integer.MAX_VALUE; // Infinity
  }

  public Role(String roleID, String roleName, int memberLimit) {
    this.roleID = roleID;
    this.roleName = roleName;
    this.requiredSkill = null;
    this.memberLimit = memberLimit;
  }

  public Role(String roleID, String roleName, Skill skill) {
    this.roleID = roleID;
    this.roleName = roleName;
    this.requiredSkill = skill;
    this.memberLimit = Integer.MAX_VALUE; // Infinity
  }

  public Role(String roleID, String roleName, Skill skill, int memberLimit) {
    this.roleID = roleID;
    this.roleName = roleName;
    this.requiredSkill = skill;
    this.memberLimit = memberLimit;
  }

  public Role() {
    this.requiredSkill = null;
    this.memberLimit = Integer.MAX_VALUE;
  }


  public String getRoleID() {
    return roleID;
  }

  public void setRoleID(String roleID) {
    this.roleID = roleID;
  }

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  public Skill getRequiredSkill() {
    return requiredSkill;
  }

  public void setRequiredSkill(Skill requiredSkill) {
    this.requiredSkill = requiredSkill;
  }

  public int getMemberLimit() {
    return memberLimit;
  }

  public void setMemberLimit(int memberLimit) {
    this.memberLimit = memberLimit;
  }

  @Override
  public String toString() {
    return roleName;
  }
}