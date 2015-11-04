import java.util.ArrayList;
import processing.data.JSONArray;
import processing.data.JSONObject;
/**
 *  This class manages all players and handle their moving logic and sent them
 *  to all other clients
 *  @author  David Sun
 *  @version May 20, 2014
 */
class player_manager {
  private ArrayList<player> players;
  private grid_manager gm;
  private int standard_health = 30;
  private grid_server serve;
  private int num_ct = 0;
  public player_manager(ArrayList<player> p,grid_manager g,grid_server s){
    players = p;
    gm = g;
    serve = s;
  }

  /**
   * sets the players to the roster of current players
   * @param the arraylist of players
   */
  public void setPlayers(ArrayList<player> p){
    players = p;
  }
  /**
   * returns the players the roster of current players
   * @return the arraylist of players playing
   */
  public ArrayList<player> returnPlayers(){
    return players;
  }
  /**
   * retursn when the minum number of players is reached
   * @return true if its under false otherwise
   */
  public boolean need(){
    return (num_ct > 0);
  }
  /**
   * takes health off of a player if its hit by a projectile
   * @param obj containing who got shot and buy what type of projectile
   */
  public void hit(JSONObject obj){
    loop:
    for(player p : players){
      if(p.returnCode().equals(obj.getString("CODE"))){
        p.setHealth(p.returnHealth()-30);
        if(p.returnHealth() <= 0){
          p.setActive(false);
          num_ct ++;
          JSONObject o = new JSONObject();
          o.setString("STATUS","hit");
          o.setString("CODE",p.returnCode());
          JSONArray arr = serve.convert_JSONArray(o);
          serve.convert_send(arr);
          }
        }
      }
  }
  /**
   * sets the player to active or not based on health
   * @param the arraylist of players
   */
  public void setActive(ArrayList<player> p){
    for(player pl : p){
      JSONObject o = new JSONObject();
      o.setString("STATUS","active");
      o.setString("CODE",pl.returnCode());
      o.setBoolean("ACTIVE",pl.getActive());
      JSONArray arr = serve.convert_JSONArray(o);
      serve.convert_send(arr);
    }
  }
  /**
   * when players move this processes the JSONObject and sends to the client
   * @param the obj containing the updated player's new data
   */
  public void updatePlayer(JSONObject obj){
    loop:
    for(player gr: players){
      if(gr.returnCode().equals(obj.getString("CODE"))){
        gr.setRow(obj.getInt("Y"));
        gr.setCol(obj.getInt("X"));
        JSONObject o = new JSONObject();
        o.setString("STATUS","update_player");
        o.setString("CODE",gr.returnCode());
        o.setInt("X",gr.returnCol());
        o.setInt("Y",gr.returnRow());
        JSONArray arr = serve.convert_JSONArray(o);
        serve.convert_send(arr);
        break loop;
      }
    }
  }
  /**
   * increments the players count
   */
  public void increment(){
    num_ct++;
  }
  /**
   * decrements the players count
   */
  public void decrement(){
    num_ct--;
  }
  /**
   * spawn a specific player to the grid and return it to all clients
   * @param the code of the player to be spawned
   */
  public void spawn_player(String code){
      loop:
      for(player p: players){
        if(p.returnCode().equals(code)){
          ArrayList<grid_Object> go = gm.query("spawn");
          int index = (int)(Math.random()*(go.size()-1));
          boolean status = true;
          grid_Object s = go.get(index);
          while(status){
             index = (int)(Math.random()*(go.size()-1));
             s = go.get(index);
             if(s.returnTeam().equals(p.returnTeam())){
               if(returnAsset(s.returnCol(),s.returnRow())==null){

                 status = false;
               }
             }
          }
          p.setRow(s.returnRow());
          p.setCol(s.returnCol());
          p.setActive(true);
          num_ct--;
          serve.printOut("Sucessfully spawned " + p.returnUsername() + " player at " + p.returnRow() + " " + p.returnCol() + ".");
          break loop;
        }
      }
  }
  /**
   * spawns the whole roster of player and monitors when a player needs to be spawned
   * @return all the players they were successsfully spawned
   */
  public ArrayList<player> spawn_all(){
    ArrayList<player> play = new ArrayList<player>();
    for(player p: players){
      serve.printOut("active" + p.getActive());
      serve.printOut("respawn" + p.respawn());
      serve.printOut("respawn ct " + p.getRespawn());
      if(p.getActive() == false){
        if(p.respawn()){
          p.tickRespawn();
          spawn_player(p.returnCode());
          play.add(p);
        }
        else
          p.tickRespawn();
      }
    }
    return play;
  }
  /**
  *  Returns the obj at the cords
  *  @param the row that should be searched at
  *  @param the col that should be searched at
  *  @return the grid_Object found there or null if there is none
  */
  public grid_Object returnAsset(int col,int row){
    for(player gr: players)
      if(gr.returnCol() == col && gr.returnRow() == row)
        return gr;
    return null;
  }

}
