/**
 *  This class stores the cordinate of an object rendered on the grid_map
 *  @author  David Sun
 *  @version May 20, 2014
 */
class cords{

  private int x = -1;
  private int y = -1;
  public cords(int a,int b){
    x = a;
    x = b;
  }
  /**
   *  Returns the X value of the cords
   *
   *  @return the X value
   */
  public int returnX(){
    return y;
  }
  /**
   *  Returns the Y value of the cords
   *
   *  @return the Y value
   */
  public int returnY(){
    return x;
  }
  /**
   *  Returns the X value of the cords
   *
   *  @param the value to change to
   *  @return the X value
   */
  public void setX(int d){
    x = d;
  }
  /**
   *  Returns the Y value of the cords
   *  
   *  @param the value to change to
   *  @return the Y value
   */
  public void setY(int d){
    y = d;
  }
}
