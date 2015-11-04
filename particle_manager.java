import processing.data.JSONArray;
import processing.data.JSONObject;
import java.util.ArrayList;
/**
 *  This class manages all the particles fired by all the players and send it
 *  to all other clients
 *  @author  David Sun
 *  @version May 20, 2014
 */
class particle_manager{
  //the araylist storing all the paricles
  private ArrayList<simpleParticle> simple = new ArrayList<simpleParticle>();
  //all the pointers to all other managers and server
  private grid_manager gm;
  private player_manager pm;
  private grid_server serve;

  public particle_manager(player_manager ps,grid_manager g,grid_server s){
    serve = s;
    pm = ps;
    gm = g;
  }
  /**
   * deletes all the data in the simple particle arraylist for a new frame refresh
   */
  public void reset(){
    simple = new ArrayList<simpleParticle>();
  }
  /**
   * return the size of the arraylist simple (how many particles are being managed)
   * @return the simple.size()
   */
  public int size(){
    return simple.size();
  }
  /**
   * This converts the clients response of a new particle to a simple particle object
   * And append it to the array of particles
   * @param the obj containing the data for a new object
   */
  public void addSimple(JSONObject obj){
    System.out.println("ADD Particles");
    int x1 = obj.getInt("SX");
    int y1 = obj.getInt("SY");
    int x2 = obj.getInt("TX");
    int y2 = obj.getInt("TY");
    color_Object c1 = new color_Object(obj.getInt("R"),obj.getInt("G"),obj.getInt("B"));
    int weight = obj.getInt("WEIGHT");
    simple.add(new simpleParticle(x1,y1,x2,y2,c1,weight,obj.getString("CODE")));
  }
  /**
   * This converts the arraylist to JSONObjects and send it to all the clients 
   */
  public void updateSimple(){
    JSONObject obj = new JSONObject();
    JSONArray arr = new JSONArray();
    for(int i = 0;i<simple.size();i++){
      simpleParticle p = simple.get(i);
      color_Object cc = p.returnColor();
      JSONObject o = new JSONObject();
      o.setString("STATUS","create_bullet");
      serve.printOut("Sending particle created by " + p.returnCode());
      o.setString("CODE",p.returnCode());
      o.setInt("WEIGHT",p.returnWeight());
      o.setInt("R",cc.returnR());
      o.setInt("G",cc.returnG());
      o.setInt("B",cc.returnB());
      o.setInt("SX",p.returnSX());
      o.setInt("SY",p.returnSY());
      o.setInt("TX",p.returnTX());
      o.setInt("TY",p.returnTY());
      arr.append(o);
      }
      if(arr.size() != 0){
        serve.convert_send(arr);
      }
    }
}
