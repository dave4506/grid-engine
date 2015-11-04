import processing.core.*;
import processing.data.JSONArray;
import processing.data.JSONObject;
import java.util.ArrayList;
import processing.net.*;
import g4p_controls.*;
import java.io.UnsupportedEncodingException;
import java.awt.Frame;
/**
 *  This class stores and processes the information recieved by the server
 *  This also renders the map and makes the game playable
 *  @author  David Sun
 *  @version May 20, 2014
 */
public class grid_client extends PApplet
{
  //counters used to montitor player count
  private int cts =0;
  private int counter = 1;
  private int counter1 = 1;

  //client class from processing.net
  private Client c;

  /***************************************************************
    *    The following GUI components (Buttons, Textfield, Textarea) are created by G4P.  
    *************************************************************/
  private GTextField address;
  private GTextField port;
  private GTextField username;
  private GTextField r;
  private GTextField gr;
  private GTextField b;
  private GButton send;
  private GButton colorChange;
  private GButton submit;
  private GButton leave;
  private GTextArea g;
  private GTextArea chat;
  private GTextField chatBar;

  //Font used for the GUI
  PFont f = createFont("Arial",16,true);

  //User sensitive data chatting, player code
  private String chatList;
  private String code;

  //booleans to montitor game status
  private boolean connected = false;
  private boolean trying = false;
  private boolean gameStarted = false;

  //Game rendering objects data
  private color_Object red = new color_Object(231, 76, 60);
  private color_Object blue = new color_Object(68,108,179);
  private ArrayList<grid_Object> grid = new ArrayList<grid_Object>();
  private ArrayList<player> players = new ArrayList<player>();
  private ArrayList<particles> particle = new ArrayList<particles>();
  private ArrayList<simpleParticle> simple = new ArrayList<simpleParticle>();

  //player controled logic and boolean
  private player control;
  private boolean pressed = false;
  private boolean clicked = false;
  private boolean changed = false;

  public static void main(String[] args)
  {
    PApplet.main(new String[]{"grid_client"});
  }

  /**
  *  Renders the dimensions and set up the framerate for game to begin
  */
  public void setup() {
    size(1400,1000);
    background(237);
    frameRate(12);
    createLoadingScreen();
  }

  /***************************************************************
    *    The methods below are designed for the GUI's basic functionality
    *************************************************************/

  /**
  *  Renders the initial screen that players will see when they start the game
  */
  public void createLoadingScreen(){
    fill(55);
    text("IP ADDRESS : ",200,180);
    text("PORT : ",200,230);
    text("USERNAME : ",200,280);
    textFont(f,46);
    text("GRID_CLIENT",155,100);
    address = new GTextField(this,(float)200,(float)190,(float)200,(float)18);
    address.setText("10.20.73.200");
    port = new GTextField(this,(float)200,(float)240,(float)200,(float)18);
    port.setText("9000");
    username = new GTextField(this,(float)200,(float)290,(float)200,(float)18);
    submit = new GButton(this,(float)200,(float)340,(float)200,(float)30,"SUBMIT");
    leave = new GButton(this,(float)250,(float)340,(float)200,(float)30,"LEAVE PARTY");
    g = new GTextArea(this,(float)250,(float)150,(float)300,(float)180);
    chatBar = new GTextField(this,(float)50,(float)310,(float)190,(float)18);
    chat = new GTextArea(this,(float)50,(float)150,(float)190,(float)150);
    send = new GButton(this,(float)50,(float)340,(float)100,(float)30,"SEND");
    r = new GTextField(this,(float)50,(float)400,(float)40,(float)18);
    gr = new GTextField(this,(float)100,(float)400,(float)40,(float)18);
    b = new GTextField(this,(float)150,(float)400,(float)40,(float)18);
    colorChange = new GButton(this,(float)200,(float)400,(float)120,(float)20,"CHANGE COLOR");
    g.setTextEditEnabled(false);
    g.setEnabled(connected);
    g.setVisible(connected);
    leave.setEnabled(connected);
    leave.setVisible(connected);
    chatBar.setEnabled(connected);
    chatBar.setVisible(connected);
    chat.setTextEditEnabled(false);
    chat.setEnabled(connected);
    chat.setVisible(connected);
    send.setEnabled(connected);
    send.setVisible(connected);
    r.setEnabled(connected);
    r.setVisible(connected);
    b.setEnabled(connected);
    b.setVisible(connected);
    gr.setEnabled(connected);
    gr.setVisible(connected);
    colorChange.setEnabled(connected);
    colorChange.setVisible(connected);
  }

  /**
  *  Updates a players color to their own prefered color
  *  @param obj that contains the new X and Y position of the player
  */
  public void updateColor(){
    JSONObject obj = new JSONObject();
    obj.setString("STATUS","update_helmet");
    obj.setString("USERNAME",username.getText());
    obj.setInt("R",Integer.parseInt(r.getText()));
    obj.setInt("G",Integer.parseInt(gr.getText()));
    obj.setInt("B",Integer.parseInt(b.getText()));
    convert_send(convert_JSONArray(obj));
  }

  /**
  *  Chooses a random color for the player. Defines them in the game so they
  *  They know who they are on their team
  */
  public void randomizeColor(){
    int index = (int)(1+Math.random()*255);
    String color1 = Integer.toString(index);
    r.setText(color1);
    int index1 = (int)(1+Math.random()*255);
    String color2 = Integer.toString(index1);
    gr.setText(color2);
    int index2 = (int)(1+Math.random()*255);
    String color3 = Integer.toString(index2);
    b.setText(color3);
  }

  /**
  *  Determines which screen it would render based on the status of the game.
  *  If game has not started waitingScreen will come up.
  */
  public void waitingScreen(){
    if(gameStarted == false){
      background(237);
      if(connected == false){
        textFont(f,16);
        text("IP ADDRESS : ",200,180);
        text("PORT : ",200,230);
        text("USERNAME : ",200,280);
        textFont(f,46);
        text("GRID_CLIENT",155,100);
      }else{
        textFont(f,46);
        text("Waiting for players.",125,100);
        int inr = Integer.parseInt(r.getText());
        int ing = Integer.parseInt(gr.getText());
        int inb = Integer.parseInt(b.getText());
        fill(inr,ing,inb);
        rect(350,400,100,18);
        fill(0);
      }
      address.setEnabled(!connected);
      address.setVisible(!connected);
      port.setEnabled(!connected);
      port.setVisible(!connected);
      username.setEnabled(!connected);
      username.setVisible(!connected);
      submit.setEnabled(!connected);
      submit.setVisible(!connected);
      leave.setEnabled(connected);
      leave.setVisible(connected);
      g.setEnabled(connected);
      g.setVisible(connected);
      chatBar.setEnabled(connected);
      chatBar.setVisible(connected);
      chat.setEnabled(connected);
      chat.setVisible(connected);
      send.setEnabled(connected);
      send.setVisible(connected);
      r.setEnabled(connected);
      r.setVisible(connected);
      b.setEnabled(connected);
      b.setVisible(connected);
      gr.setEnabled(connected);
      gr.setVisible(connected);
      colorChange.setEnabled(connected);
      colorChange.setVisible(connected);
    }else{
      background(237);
      address.markForDisposal();
      port.markForDisposal();
      username.markForDisposal();
      submit.markForDisposal();
      leave.markForDisposal();
      g.markForDisposal();
      chatBar.markForDisposal();
      chat.markForDisposal();
      send.markForDisposal();
      r.markForDisposal();
      b.markForDisposal();
      gr.markForDisposal();
      colorChange.markForDisposal();
      drawgrid();
    }
  }

  /**
  *  After client connects, this method creates a player and sends it to the server
  */
  public void createPlayerJSON(){
    JSONObject obj = new JSONObject();
    obj.setString("STATUS","create_player");
    obj.setString("USERNAME",username.getText());
    code = code_generate();
    obj.setString("CODE",code);
    obj.setInt("R",Integer.parseInt(r.getText()));
    obj.setInt("G",Integer.parseInt(gr.getText()));
    obj.setInt("B",Integer.parseInt(b.getText()));
    convert_send(convert_JSONArray(obj));
  }

  /**
  *  sends a Chat JSON to the server allowing other players to communicate
  */
  public void sendChat(){
    JSONObject obj = new JSONObject();
    obj.setString("STATUS","add_chat");
    obj.setString("USERNAME",username.getText());
    obj.setString("CONTENT",chatBar.getText());
    convert_send(convert_JSONArray(obj));
    chatBar.setText("");
  }
  /**
  *  A G4P control handler. Adds the action that happnes after any button is clicked.
  *  @param the button that wass clicked
  *  @param the event that occured relating to the button
  */
  public void handleButtonEvents(GButton button, GEvent event) {
    if(button == submit && event == GEvent.CLICKED && connected == false && username.getText().length() > 0){
      c = new Client(this, address.getText(), Integer.parseInt(port.getText()));
      randomizeColor();
      createPlayerJSON();
      trying = true;
    }
    if(button == leave && event == GEvent.CLICKED){
      c.stop();
      connected = false;
      waitingScreen();
      g.setText("");
    }
    if(button == send && event == GEvent.CLICKED){
      sendChat();
    }
    if(button == colorChange && event == GEvent.CLICKED){
      updateColor();
      waitingScreen();
    }
  }
  /**
  *  Starts the game after the server begins the game.
  */
  public void generateNewWindow(){
    gameStarted = true;
    waitingScreen();
    JSONObject obj = new JSONObject();
    obj.setString("STATUS","ready");
    convert_send(convert_JSONArray(obj));
  }

  /**
  * Helper method for the Textfields
  * Adds new text to the textfield with indentation
  * @param the obj with the conntent to be updated with
  */
  public void appendText(JSONObject obj){
    String[] a = chat.getTextAsArray();
    String[] arr = new String[a.length + 1];
    for(int i = 0; i< a.length; i++){
      arr[i] = a[i];
    }
    arr[arr.length - 1] = obj.getString("CONTENT") + "\n";
    chat.setText(arr);
  }
  /**
  * When player is booted, the method is called and the client stops connection
  * @param the obj with the ip that needs to be booted
  */
  public void disconnected(JSONObject obj){
    System.out.println(obj.getString("IP"));
    System.out.println(c.ip());
    if(obj.getString("IP").equals(c.ip())){
      c.stop();
      connected = false;
      waitingScreen();
      g.setText("");
    }
  }
  /**
  * Refreshed the player screen, with the current roster of players
  * @param the obj with the latest player
  */
  public void refresh_player(JSONObject obj){
    String[] a = g.getTextAsArray();
    String[] arr = new String[a.length + 1];
    for(int i = 0; i< a.length; i++){
      arr[i] = a[i];
    }
    if(!obj.getString("CODE").equals(code)){
      arr[arr.length - 1] = counter1 + ".) " + obj.getString("INFO") + " \n";
    }else{
      arr[arr.length - 1] = counter1 + ".) (you)" + obj.getString("INFO") + " \n";
    }
    g.setText(arr);
  }
  /**
  * Generates a code for the player that identifies them throughout the game
  * @return the string containing the code
  */
  public String code_generate(){
    String alpha = "abcdefghijklmnopqrstuvwxyz1234567890";
    int counter = 0;
    String c = "";
    while( counter < 8 ){
      int index = (int)(Math.random()*36);
      c +=(alpha.charAt(index)+"-");
      counter++;
    }
    return c;
  }
  /***************************************************************
    *    The methods below are designed for the actual game rendering functionality
    *************************************************************/

  /**
  *  Draws the grid when the game is started.
  */
  public void drawgrid(){
    strokeWeight(1);
    for(int i = 25; i < 1400; i += 25){
      stroke(153);
      line(i,0,i,900);
    }
    for(int i = 25; i < 900; i += 25){
      stroke(153);
      line(0,i,1400,i);
    }
  }
  /**
  *  Draws all the particles that are recieved by other players
  */
  public void drawPart(){
    for(simpleParticle sp : simple){
      color_Object cc = sp.returnColor();
      stroke(cc.returnR(),cc.returnG(),cc.returnB(),cc.returnA());
      strokeWeight(sp.returnWeight());
      line(sp.returnSX(),sp.returnSY(),sp.returnTX(),sp.returnTY());
    }
    simple = new ArrayList<simpleParticle>();
  }

  /**
  *  The draw method for the game. Draws the grid, the objs, and the GUI
  */
  public void redraw(){
    background(237);
    drawgrid();
    int ct1 = 0;
    for(grid_Object gr:grid){
      drawObject(gr);
      ct1 ++;
    }
    for(player gr:players){
      if(gr.getActive())
        drawObject(gr);
    }
    drawPart();
    rectMode(CORNER);
    fill(237);
    rect(0,900,1400,100);
    fill(0);
    if(control != null){
      text(control.returnUsername(),50,950);
      textFont(f,16);
      text(control.returnTeam(),50,970);
      text("Color : ",700,950);
      color_Object hc = control.returnHelmet();
      fill(hc.returnR(),hc.returnG(),hc.returnB(),hc.returnA());
      rect(800,930,40,40);
      textFont(f,26);
      fill(0);

      text("Health : " + control.returnHealth(),400,950);
      if(control.getActive()){
        text("Active!",300,950);
      }else{
        text("Waiting for spawn.",1000,950);
      }
    }
    else{
      text("Waiting for spawn.",1000,950);
    }
  }
  /**
  *  Returns the obj at the cords
  *  @param the row that should be searched at
  *  @param the col that should be searched at
  *   @return the grid_Object found there or null if there is none
  */
  public grid_Object returnAsset(int col,int row){
    for(grid_Object gr: grid)
      if(gr.returnCol() == col && gr.returnRow() == row)
        return gr;
    return null;
  }
  /**
  *  Draw the object on the grid based system(does it for all grid_Object)
  *  @param the grid_Object that needs to be drawn
  */
  public void drawObject(grid_Object gr){
      int x = gr.returnCol()*25 + 13;
      int y = gr.returnRow()*25 + 13;
      color_Object c = gr.returnColor();
      if (gr.returnDraw() == true){
        color_Object cc = gr.border_color();
        stroke(cc.returnR(),cc.returnG(),cc.returnB(),cc.returnA());
        strokeWeight(gr.border_thick());
      }
      fill(c.returnR(),c.returnG(),c.returnB(),c.returnA());
      rectMode(CENTER);
      rect(x,y,23,23);
      if(gr.returnHand() == true && gr.returnCode().equals(code)){
        double slope = (double)(mouseY-y)/(mouseX-x);
        hand(control,slope,mouseX);
      }
      if(gr.returnBHelmet() == true){
        color_Object hc = gr.returnHelmet();
        stroke(237,237,237,0);
        fill(hc.returnR(),hc.returnG(),hc.returnB(),hc.returnA());
        ellipseMode(CENTER);
        ellipse(x,y,12,12);
      }
      stroke(237,237,237,257);
      strokeWeight(0);
  }
  /**
  *  Draw the hand of the player
  *  @param the grid_Object that needs to be drawn too
  *  @param the slope of the line in relation to the mouse loca
  *  @param the mouse's X position relative to the palyer
  */
  public void hand(grid_Object gr,double slope,int hand){
    int distance = 9;
    int x = gr.returnCol()*25 + 13;
    int y = gr.returnRow()*25 + 13;
    stroke(0, 0,0);
    int negative = 1;
    if(hand-x > 0){
      negative = 1;
    }else{
      negative = -1;
    }
    double x_temp = x + negative*Math.sqrt((distance*distance)/(slope*slope + 1));
    int y2 = (int)Math.round(slope*(x_temp - x) + y);
    int x2 = (int)Math.round(x_temp);

    if (slope == Double.POSITIVE_INFINITY){
      x2 = x;
      y2 = y + 8;
    }
    if (slope == Double.NEGATIVE_INFINITY){
      x2 = x;
      y2 = y - 8;
    }
    strokeWeight(5);
    line(x,y,x2,y2);
    stroke(237,237,237,257);
    strokeWeight(0);
  }


  /***************************************************************
    *    The methods below are designed for the logic of the game
    *************************************************************/

  /**
  *  Updates a players position after it was moved.
  *  @param obj that contains the new X and Y position of the player
  */
  public void updatePlayer(JSONObject obj){
    loop:
    for(player gr: players){
      if(gr.returnCode().equals(obj.getString("CODE"))){
        gr.setRow(obj.getInt("Y"));
        gr.setCol(obj.getInt("X"));
        break loop;
      }
    }
  }
  /**
  *  Updates a players health after it was hit by a projectile
  *  @param obj that contains the player that was hit and its new health
  */
  public void hit(JSONObject obj){
    for(player p : players){
      if(obj.getString("CODE").equals(p.returnCode())){
          p.setActive(false);
      }
    }
  }
  /**
  *  Updates a players status ACTIVE(whether they are playing or waitng for spawn)
  *  @param obj that contains the player and their active status
  */
  public void active(JSONObject obj){
    for(player p : players){
      if(obj.getString("CODE").equals(p.returnCode())){
          p.setActive(obj.getBoolean("ACTIVE"));
      }
    }
  }
  /**
  *  After player's particle is sent to the server, other players recieve a
  *  simplified particle to render on their screens
  *  @param obj that contains the basic information of the particle
  */
  public void addSimple(JSONObject obj){
    System.out.println("ADD Particles");
    int x1 = obj.getInt("SX");
    int y1 = obj.getInt("SY");
    int x2 = obj.getInt("TX");
    int y2 = obj.getInt("TY");
    System.out.println("x1 " + x1 + " y1 " + y1 + " x2 " + x2 + " y2" + y2);
    color_Object c1 = new color_Object(obj.getInt("R"),obj.getInt("G"),obj.getInt("B"));
    int weight = obj.getInt("WEIGHT");
    simple.add(new simpleParticle(x1,y1,x2,y2,c1,weight,obj.getString("CODE")));
  }

  /**
  *  When the player fires, it creates a particle and adds it to the local array of Particles
  *  @param The X cordinate it was pointed at
  *  @param The Y cordinate it was pointed at
  */
  public void addParticle(int x1,int y1){
    int distance = 9;
    int x = control.returnCol()*25 + 13;
    int y = control.returnRow()*25 + 13;
    double slope = (double)(y1-y)/(x1-x);
    particles p = new particles(new color_Object(243, 156, 18),x,y,slope,40,40,(y1-y<0),2);
    particle.add(p);
  }

  /**
  *  When the player moves, the player's position ss changed
  */
  public boolean move_User(){
    if(pressed == false || counter == 3){
      counter = 0;
      int prow = control.returnRow();
      int pcol = control.returnCol();
      int row = control.returnRow();
      int col = control.returnCol();
      if(key == 'w'){
          row --;
          if(row < 0){
            row = 35;
          }
      }
      if(key == 'a'){
          col --;
          if(col < 0){
            col = 55;
          }
      }
      if(key == 's'){
          row ++;
          if(row >= 36){
            row = 0;
          }
      }
      if(key == 'd'){
          col ++;
          if(col >= 56){
            col = 0;
          }
      }
      if(returnAsset(col,row) != null){
        if(returnAsset(col,row).returnType().equals("wall")){
          return false;
        }
        if(returnAsset(col,row).returnType().equals("spawn")){
          return false;
        }
        if(returnAsset(col,row).returnType().equals("player")){
          return false;
        }
      }else{
        control.setRow(row);
        control.setCol(col);
        changed = true;
        return true;
      }
    }
    return false;
  }

  /**
  *  When the player does move, the new position is sent to the server
  */
  public void sendChange(){
    if(changed == true){
      JSONObject obj = new JSONObject();
      obj.setString("STATUS","update_player");
      obj.setString("CODE",code);
      obj.setInt("X",control.returnCol());
      obj.setInt("Y",control.returnRow());
      convert_send(convert_JSONArray(obj));
      changed = false;
    }
  }

  /**
  *  Toggles when a key is pressed or not
  */
  public void keyReleased() {
    pressed = false;
  }
  /**
  *  Toggles when the mouse is released
  */
  public void mouseReleased() {
    clicked = false;
  }
  /**
  * When a new obj is recieved, it is created and placed in the Arraylist for rendering
  * @param the obj that has the data for the new object to be created
  */
  public void create(JSONObject obj){
    System.out.println("Creating " + obj.getString("TYPE") + " grid_Object.");
    if(obj.getString("TYPE").equals("wall")){
      grid.add(new wall(obj.getInt("Y"),obj.getInt("X")));
    }
    if(obj.getString("TYPE").equals("spawn")){
      color_Object cs = new color_Object(0);
      if(obj.getString("TEAM").equals("red")){
        cs = red;
      }
      if(obj.getString("TEAM").equals("blue")){
        cs = blue;
      }
      grid.add(new spawn(obj.getInt("Y"),obj.getInt("X"),obj.getString("TEAM"),cs));
    }
    if(obj.getString("TYPE").equals("player")){
      color_Object c1 = new color_Object(obj.getInt("R"),obj.getInt("G"),obj.getInt("B"));
      color_Object h1 = new color_Object(obj.getInt("HR"),obj.getInt("HG"),obj.getInt("HB"));
      player p = new player(c1,obj.getString("USERNAME"),obj.getString("CODE"),obj.getInt("HEALTH"),obj.getString("TEAM"),h1);
      p.setRow(obj.getInt("Y"));
      p.setCol(obj.getInt("X"));
      p.setActive(obj.getBoolean("ACTIVE"));
      players.add(p);
      if(obj.getString("CODE").equals(code)){
        control = p;
      }
    }
  }
  /**
  *  All particles fired by the native player are processed on the client side and sent to the server
  */
  public void updateParticles(){
    JSONObject obj = new JSONObject();
    obj.setString("STATUS","updating");
    JSONArray arr = new JSONArray();
    arr.append(obj);
    for(int i = 0;i<particle.size();i++){
      particles p = particle.get(i);
      p.update(grid,players);
      if(p.terminate()){
        if(p.returnEnd() != null){
          if(p.returnEnd().returnType().equals("player")){
            JSONObject o = new JSONObject();
            o.setString("STATUS","hit");
            o.setString("CODE",p.returnEnd().returnCode());
          }
        }
        particle.remove(i);
        arr.append(obj);
        System.out.println("Removing..." + i);
      }else{
        color_Object cc = p.returnC();
        stroke(cc.returnR(),cc.returnG(),cc.returnB(),cc.returnA());
        strokeWeight(p.returnProjectile());
        line(p.returnstartX(),p.returnstartY(),p.returntempX(),p.returntempY());
        JSONObject o = new JSONObject();
        o.setString("STATUS","create_bullet");
        o.setString("CODE",code);
        o.setInt("WEIGHT",p.returnProjectile());
        o.setInt("R",cc.returnR());
        o.setInt("G",cc.returnG());
        o.setInt("B",cc.returnB());
        o.setInt("SX",p.returnstartX());
        o.setInt("SY",p.returnstartY());
        o.setInt("TX",p.returntempX());
        o.setInt("TY",p.returntempY());
        System.out.println("SENDING");
        arr.append(o);
      }
    }
      convert_send(arr);
  }

  /***************************************************************
    *    The methods below are for the basic communication between the server and client.
    *************************************************************/
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
  *  Converts a JSONArray to an String and sends it to the server
  *  @param Array that will be sent and converted
  */
  public void convert_send(JSONArray obj){
    System.out.println("Sending " + obj.getJSONObject(0).getString("STATUS") + " task.");
    c.write(obj.toString());
  }
  /***************************************************************
    *    The method below is the main method that links all other methods together
    *************************************************************/

  /**
  *  The main method that processes the server data and call actions based on the response
  *  Method also renders the game each time information is updated.
  *  When mouse or keyboard is used this method calls the certain method for response.
  */
  public void draw(){
    if(connected == true || trying == true){
      if (c.available() > 0) {
          String bc = c.readStringUntil(93);
          JSONArray arr = JSONArray.parse(bc);
          if(arr != null){
            for(int i = 0; i<arr.size(); i++){
              JSONObject obj = arr.getJSONObject(i);
              String status = obj.getString("STATUS");
              System.out.println("Recieved " + status + " task.");
              if(gameStarted == false){
                if(status.equals("list_player")){
                  if(obj.getInt("FIRST") == 0){
                    g.setText("");
                    counter1 = 1;
                  }
                  refresh_player(obj);
                  counter1++;
                }
                if(status.equals("disconnect")){
                  disconnected(obj);
                }
                if(status.equals("update_chat")){
                  appendText(obj);
                }
                if(status.equals("rejected")){
                  if(obj.getString("IP").equals(c.ip())){
                    textFont(f,16);
                    text("Sorry! " + obj.getString("REASON"),200,460);
                    c.stop();
                    trying = false;
                  }
                }
                if(status.equals("success")){
                  if(obj.getString("IP").equals(c.ip())){
                    textFont(f,16);
                    text("Connected! ",200,160);
                    connected = true;
                    waitingScreen();
                  }
                }
                if(status.equals("loading")){
                  textFont(f,46);
                  text("Loading... ",200,460);
                }
              }
              if(status.equals("success_map")){
                generateNewWindow();
              }
              if(status.equals("create")){
                create(obj);
              }
              if(status.equals("update_player")){
                if(!obj.getString("CODE").equals(code)){
                  System.out.println("Moving other players");
                  updatePlayer(obj);
                }
              }
              if(status.equals("hand")){
                if(!obj.getString("CODE").equals(code))
                  updateHand(obj);
              }
              if(status.equals("create_bullet")){
                if(!obj.getString("CODE").equals(code)){
                  addSimple(obj);
                }
              }
              if(status.equals("hit")){
                hit(obj);
              }
              if(status.equals("active")){
                active(obj);
              }
            }
          }
        }
    }
    if(gameStarted == true){
      sendChange();
      redraw();
      if(particle.size() > 0){
        updateParticles();
      }
      if(simple.size() > 0){
        drawPart();
      }
      if (keyPressed && (key =='w' || key == 'a' || key =='s' || key =='d')){
         boolean status = move_User();
         counter ++;
         pressed = true;
      }
      if (mousePressed == true&&clicked == false) {
        clicked = true;
        addParticle(mouseX,mouseY);
      }
    }
  }
}
