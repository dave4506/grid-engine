/**
 *  This class if te grid_Object for a player
 *  @author  David Sun
 *  @version May 20, 2014
 */
class player extends grid_Object {
  //basic player data
  private String team;
  private String username;
  private int health;
  private String code;
  private boolean active = false;
  private String ip;
  private color_Object helmet;
  private int respawn  = 0;
  private double slope = 0;

  public player(color_Object c,String usern,String cde,int h,String t,color_Object he){
     super(0,0,c);
     super.setType("player");
     super.setHand(true);
     super.setBHelmet(true);
     username = usern;
     code = cde;
     health = h;
     team = t;
     helmet = he;
  }
  /**
   * counts down the respawn timer
   */
  public void tickRespawn(){
    if(respawn == 0)
      respawn = 30;
    else
      respawn --;
  }
  /**
  *  returns the respawn counter
  *  @return int respawn
  */
  public int getRespawn(){
    return respawn;
  }
  /**
  *  determine if it is time for respawn
  *  @return true if it time, 0 false otherwise
  */
  public boolean respawn(){
    return (respawn == 0);
  }
  /**
  *  sets the health of the player
  *  @param the health fo the player to be update to
  */
  public void setHealth(int h){
    health = h;
  }
  /**
  *  returns the health of the player
  *  @return int health
  */
  public int returnHealth(){
    return health;
  }
  /**
  *  set if the player is active or nto
  *  @param the new boolean status
  */
  public void setActive(boolean a){
    active = a;
  }
  /**
  *  returns if the player is active or not
  *  @return boolean active
  */
  public boolean getActive(){
    return active;
  }
  /**
  *  sets the color of the helmet
  *  @param the new color to set helmet to
  */
  public void setHelmet(color_Object c){
    helmet = c;
  }
  /**
  *  returns the helmet color
  *  @return color_Object helmet
  */
  public color_Object returnHelmet(){
    return helmet;
  }
  /**
  *  returns the code of this player for identify who the palyer is to which client
  *  @return int code
  */
  public String returnCode(){
    return code;
  }
  /**
  *  returns which team the player is on
  *  @return String team
  */
  public String returnTeam(){
    return team;
  }
  /**
  *  sets the player to a new team
  *  @param the team name
  */
  public void setTeam(String t){
    team = t;
  }
  /**
  *  returns the username of the player
  *  @return String username
  */
  public String returnUsername(){
    return username;
  }
  /**
  *  returns the ip of the client with this player
  *  @return String ip
  */
  public String ip(){
    return ip;
  }
  /**
  *  sets the ip corresponding to this player and client
  *  @param the new ip to set ip to
  */
  public void setIp(String i){
    ip = i;
  }
  /**
  *  set the slope of where the player is pointing at
  *  @param the new slope to set slope to
  */
  public void setSlope(double s){
    slope = s;
  }
  /**
  *  returns the slope of where the player is pointing at
  *  @return slope
  */
  public double returnSlope(){
    return slope;
  }
  /**
  *  returns a diagnostic string of the players main data
  *  @return String of basic info
  */
  public String toString(){
    return "ip : " + ip + " username : " + username + " team : " + team;
  }
  /**
  *  returns a diagnostic string of the players main data without ip for security reasons
  *  @return String of basic info
  */
  public String clientToString(){
    return " username : " + username + " team : " + team;
  }
}
