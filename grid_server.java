import processing.core.*;
import processing.data.JSONArray;
import processing.data.JSONObject;
import java.util.ArrayList;
import processing.net.*;
import g4p_controls.*;
import java.io.UnsupportedEncodingException;

/**
 *  This class stores and calls the logic as a server
 *  This also creates the map, contain all the managers and make the game work
 *  @author  David Sun
 *  @version May 20, 2014
 */

public class grid_server extends PApplet
{
  //Server variables
  private Server s;
  private GTextArea g;
  private int counter = 0;
  private color_Object red = new color_Object(231, 76, 60);
  private color_Object blue = new color_Object(68,108,179);
  private int players = 8;
  private int redCt = 0;
  private String[] output = {""};

  /***************************************************************
    *    The following GUI components are created by G4P.  
    *************************************************************/
  private GButton submit;
  private GTextField username;
  private GButton boot;
  private GButton start;

  //Game variables
  private boolean started = false;
  private boolean gameCreated = false;
  private int recount = 0;
  private ArrayList<grid_Object> grid = new ArrayList<grid_Object>();
  private ArrayList<player> plys = new ArrayList<player>();
  private JSONArray hand = new JSONArray();
  private boolean rendered = false;
  private grid_manager gm;
  private player_manager pm;
  private particle_manager jm;

  public static void main(String[] args)
  {
    PApplet.main(new String[]{"grid_server"});
  }

  /**
  *  Renders the setup of the GUI and starts a server
  */
  public void setup() {
    size(600,500);
    background(237);
    frameRate(24);
    PFont f = createFont("Arial",16,true);
    textFont(f,46);
    fill(55);
    text("GRID_SERVER",135,50);
    textFont(f,36);
    text(Server.ip(),165,100);
    textFont(f,24);
    text("9000",270,140);
    s = new Server(this, 9000);
    gm = new grid_manager(this);
    pm = new player_manager(plys,gm,this);
    jm = new particle_manager(pm,gm,this);
    text("Player list:",150,180);
    g = new GTextArea(this,(float)140,(float)200,(float)310,(float)200);
    g.setTextEditEnabled(false);
    textFont(f,12);
    text("Change : ",140,425);
    username = new GTextField(this,(float)200,(float)410,(float)140,(float)18);
    submit = new GButton(this,(float)350,(float)410,(float)100,(float)18,"CHANGE");
    boot = new GButton(this,(float)350,(float)440,(float)100,(float)18,"BOOT");
    start = new GButton(this,(float)140,(float)440,(float)200,(float)18,"START GAME");

  }
  /**
  *  This is the second graphic controls after the game has started, so no booting and changing teams
  */
  public void display(){
    background(237);
    PFont f = createFont("Arial",16,true);
    textFont(f,46);
    fill(55);
    text("GRID_SERVER",135,50);
    textFont(f,36);
    text(Server.ip(),165,100);
    textFont(f,24);
    text("9000",270,140);
    text("Output:",150,180);
    g.setText(output);
    username.setEnabled(false);
    username.setVisible(false);
    submit.setEnabled(false);
    submit.setVisible(false);
    boot.setEnabled(false);
    boot.setVisible(false);
    start.setEnabled(false);
    start.setVisible(false);
  }
  /**
  *  A System.out.println equivalent for the server GUI
  *  @param the string to be printed
  */
  public void printOut(String s){
    if(started == true){
      String[] arr = new String[output.length + 1];
      for(int i = 1; i< arr.length; i++){
        arr[i] = output[i-1];
      }
      arr[0] = s;
      output = arr;
      g.setText(output);
    }
  }
  /**
  *  When a client disconnects, this method is called which removes the corresponding
  *  player out of the roster
  *  @param the client that disconnected
  */
  public void disconnectEvent(Client c) {
    int i = 0;
    loop:
    for(player p : plys){
      if(p.ip().equals(c.ip())){
        plys.remove(i);
        counter--;
        pm.decrement();
        printOut(c.ip() + " disconnected!");
        break loop;
      }
      i++;
    }
    refresh_players();
  }
  /**
  *  Updates the text area with the current roster of the players and send the updated
  *  roster to all clients
  */
  public void refresh_players(){
    if(started == false){
      String[] n = new String[plys.size()];
      int i = 0;
      JSONArray arr = new JSONArray();
      JSONObject o = new JSONObject();
      for(player p : plys){
        n[i] = (p.toString()) + "\n";
        JSONObject obj = new JSONObject();
        obj.setString("STATUS","list_player");
        obj.setString("INFO",p.clientToString());
        obj.setString("CODE",p.returnCode());
        obj.setInt("FIRST",1);
        if(i == 0){
          obj.setInt("FIRST",0);
        }
        arr.append(obj);
        i++;
      }
      g.setText(n);
      convert_send(arr);
    }
  }

  /**
  *  Updates the text area with the current roster of the players and  don't send the updated
  *  roster to all clients
  */
  public JSONArray refresh_player_only(){
    JSONArray arr = new JSONArray();

    if(started == false){
      int i = 0;
      String[] n = new String[plys.size()];

      for(player p : plys){
        n[i] = (counter+".) " + p.toString()) + "\n";

        JSONObject obj = new JSONObject();
        obj.setString("STATUS","list_player");
        obj.setString("INFO",p.clientToString());
        obj.setString("CODE",p.returnCode());
        arr.append(obj);
        obj.setInt("FIRST",1);
        if(i == 0){
          obj.setInt("FIRST",0);
        }
        i++;
      }
      g.setText(n);
    }
    return arr;
  }
  /**
  *  The main method that processes the client data and call actions based on the response
  */
  public void draw(){
    Client c = s.available();
    if (c !=null) {
      String bs = c.readStringUntil(93);;
      JSONArray arr = JSONArray.parse(bs);
      for(int i = 0; i<arr.size(); i++){
        JSONObject obj = arr.getJSONObject(i);
        String status = obj.getString("STATUS");
        System.out.println("Recieved " + status + " task from: " + c.ip());
        printOut("Recieved " + status + " task from: " + c.ip());
        if(status.equals("create_player")){
          if(started == false){
            pm.increment();
            addPlayer(obj,c.ip());
          }
          else{
            rejected(c.ip());
          }
        }
        if(status.equals("add_chat")){
          updateChat(obj);
        }
        if(status.equals("update_helmet")){
          updateHelmet(obj);
        }
        if(status.equals("ready")&&rendered == false){
          rendered = true;
          loadMap();
          ArrayList<player> p = pm.spawn_all();
          printOut("Spawning : " + p.size());
          if (p.size() != 0){
            sendSpawn(p);
          }
          gameCreated = true;
        }
        if(status.equals("update_player")){
          pm.updatePlayer(obj);
        }
        if(status.equals("hand")){
          hand.append(obj);
        }
        if(status.equals("create_bullet")){
          jm.addSimple(obj);
        }
        if(status.equals("updating")){
          recount++;
        }
        if(status.equals("hit")){
          pm.hit(obj);
        }
      }
    }
    if(gameCreated == true&&jm.size() != 0){
      jm.updateSimple();
    }
    if(gameCreated == true&&recount >= plys.size()){
      recount = 0;
      jm.reset();
    }
    if(pm.need()&&gameCreated == true){
      ArrayList<player> p = pm.spawn_all();
      pm.setActive(p);
    }
    if(gameCreated == true&&hand.size()>0){
      convert_send(hand);
    }
  }
  /**
  * After spawns are generated, this method sends all the players to the client
  * on a spawn location
  */
  public void sendSpawn(ArrayList<player> p){
    for(int i = 0; i< p.size(); i++){
      player gr = p.get(i);
      JSONObject obj = new JSONObject();
      obj.setString("STATUS","create");
      obj.setInt("X",gr.returnCol());
      obj.setInt("Y",gr.returnRow());
      obj.setString("TYPE","player");
      obj.setString("USERNAME",gr.returnUsername());
      obj.setString("CODE",gr.returnCode());
      obj.setString("TEAM",gr.returnTeam());
      obj.setInt("HEALTH",gr.returnHealth());
      color_Object he = gr.returnHelmet();
      color_Object colo = gr.returnColor();
      obj.setInt("HR",he.returnR());
      obj.setInt("HG",he.returnG());
      obj.setInt("HB",he.returnB());
      obj.setInt("R",colo.returnR());
      obj.setInt("G",colo.returnG());
      obj.setInt("B",colo.returnB());
      obj.setBoolean("ACTIVE",gr.getActive());
      JSONArray arr = convert_JSONArray(obj);
      convert_send(arr);
    }
  }
  /**
  * If game has started, no more players can be joined.  This method sends a rejected status to the client
  * @param the ip of the client that connected
  */
  public void rejected(String ip){
    JSONObject obj = new JSONObject();
    obj.setString("STATUS","rejected");
    obj.setString("IP",ip);
    obj.setString("REASON","Game has started.");
    JSONArray arr = convert_JSONArray(obj);
    convert_send(arr);
  }
  /**
  * If game has not started, then player can join.  This method sends a rejected status to the client
  * @param the ip of the client that connected
  */
  public void success(String ip){
    JSONObject obj = new JSONObject();
    obj.setString("STATUS","success");
    obj.setString("IP",ip);
    obj.setString("REASON","Connected.");
    JSONArray arr = convert_JSONArray(obj);
    JSONArray a = refresh_player_only();
    for(int i = 0; i< a.size();i++){
      arr.append(a.getJSONObject(i));
    }
    convert_send(arr);
  }
  /**
  * Updates the helmet color of a player from a client color change
  * @param the obj containing the new color
  */
  public void updateHelmet(JSONObject obj){
    for(player p: plys){
      if(p.returnUsername().equals(obj.getString("USERNAME"))){
        color_Object h = new color_Object(obj.getInt("R"),obj.getInt("G"),obj.getInt("B"));
        p.setHelmet(h);
      }
    }
  }
  /**
  * Sends the latest chat to all clients to display
  * @param the obj containing the contents of the chat and the sender
  */
  public void updateChat(JSONObject obj){
    obj.setString("STATUS","update_chat");
    String s = obj.getString("CONTENT");
    obj.setString("CONTENT",obj.getString("USERNAME") + " : " + s);
    convert_send(convert_JSONArray(obj));
  }
  /**
  *  A G4P control handler. Adds the action that happnes after any button is clicked.
  *  @param the button that wass clicked
  *  @param the event that occured relating to the button
  */
  public void handleButtonEvents(GButton button, GEvent event) {
    if(button == submit && event == GEvent.CLICKED){
      loop:
      for(player p: plys){
        if(p.returnUsername().equals(username.getText())){
          if(p.returnTeam().equals("red")){
            p.setTeam("blue");
            p.setColor(blue);
            redCt--;
          }else{
            p.setColor(red);
            p.setTeam("red");
            redCt++;
          }
          refresh_players();
          break loop;
        }
      }
    }
    if(button == start && event == GEvent.CLICKED && plys.size() > 0){
      started = true;
      display();
      loading();
      gm.generateMap();
      printOut("COMPLETE");
      grid = gm.returnGrid();
      successGen();
    }
    if(button == boot && event == GEvent.CLICKED){
      int i = 0;
      String clientip = "";
      loop:
      for(player p : plys){
        if(p.returnUsername().equals(username.getText())){
          plys.remove(i);
          clientip = p.ip();
          counter--;
          break loop;
        }
        i++;
      }
      disconnect(clientip);
      refresh_players();
    }
  }
  /**
  * The response to all clients after a map is successfully created
  */
  public void successGen(){
    JSONObject obj = new JSONObject();
    obj.setString("STATUS","success_map");
    obj.setString("REASON","map created.");
    JSONArray arr = convert_JSONArray(obj);
    convert_send(arr);
  }
  /**
  *  After maps is created, this converts it to a JSONObject and sends it to all clients
  */
  public void loadMap(){
    printOut("Blocks created : " + grid.size());
    int counter = 0;
    for(int i = 0; i< grid.size(); i++){
      grid_Object gr = grid.get(i);
      JSONObject obj = new JSONObject();
      obj.setString("STATUS","create");
      obj.setInt("X",gr.returnCol());
      obj.setInt("Y",gr.returnRow());
      if(gr.returnType().equals("wall")){
        obj.setString("TYPE","wall");
      }
      if(gr.returnType().equals("spawn")){
        obj.setString("TYPE","spawn");
        obj.setString("TEAM",gr.returnTeam());
      }
      counter ++;
      JSONArray arr = convert_JSONArray(obj);
      no_convert_send(arr);

    }
  }
  /**
  * When game map is rendering, this method is called
  * clients recieve a loading response
  */
  public void loading(){
    JSONObject obj = new JSONObject();
    obj.setString("STATUS","loading");
    obj.setString("REASON","rendering game");
    JSONArray arr = convert_JSONArray(obj);
    convert_send(arr);
  }
  /**
  * Method call forcefully disconnects a client from the server (boots)
  * @param the ip of the client that connected
  */
  public void disconnect(String ip){
    JSONObject obj = new JSONObject();
    obj.setString("STATUS","disconnect");
    obj.setString("IP",ip);
    JSONArray arr = convert_JSONArray(obj);
    convert_send(arr);
  }
  /**
  *  Converts a JSONObject to an JSONArray
  *  @param obj that needs to be convert
  *  @return the new JSONArray that contains the obj.
  */
  public JSONArray convert_JSONArray(JSONObject obj){
    JSONArray arr = new JSONArray();
    arr.append(obj);
    return arr;
  }
  /**
  *  Converts a JSONArray to an String and sends it to the server and print it to
  *  the server panel
  *  @param Array that will be sent and converted
  */
  public void convert_send(JSONArray obj){
    System.out.println("Sending " + obj.getJSONObject(0).getString("STATUS") + " task.");
    s.write(obj.toString());
  }
  /**
  *  Converts a JSONArray to an String and sends it to the server
  *  @param Array that will be sent and converted
  */
  public void no_convert_send(JSONArray obj){
      s.write(obj.toString());
  }
  /**
  * After a client joins, a player is generated and added to the players arraylist
  * @param the obj containing their username and starting helmet color
  * @param the ip of the client that connected
  */
  public void addPlayer(JSONObject obj,String ip){
    color_Object c = new color_Object(0);
    String team = "";
    if(redCt < players/2){
      c = red;
      redCt++;
      team = "red";
    }else{
      c = blue;
      team = "blue";
    }
    color_Object h = new color_Object(obj.getInt("R"),obj.getInt("G"),obj.getInt("B"));
    player p = new player(c,obj.getString("USERNAME"),obj.getString("CODE"),40,team,h);
    p.setIp(ip);
    plys.add(p);
    counter++;
    success(ip);
  }
}
