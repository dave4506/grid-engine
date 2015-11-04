/**
 *  Simplified particle containing no logic, only stores the data of a particle
 *  Used by clients
 *  @author  David Sun
 *  @version May 20, 2014
 */
class simpleParticle{

  private int sx;
  private int sy;
  private int tx;
  private int ty;
  private int weight;
  private color_Object color;
  private String code;

  public simpleParticle(int x,int y,int x1,int y1,color_Object c,int w,String co){
    sx = x;
    sy = y;
    tx = x1;
    ty = y1;
    color = c;
    weight = w;
    code = co;
  }
  /**
  *  returns the code of the pplayer who shot the particle
  *  @return String code
  */
  public String returnCode(){
    return code;
  }
  /**
  *  returns the starting point of the particle X
  *  @return int sx
  */
  public int returnSX(){
    return sx;
  }
  /**
  *  returns the staring point of the particle Y
  *  @return int sy
  */
  public int returnSY(){
    return sy;
  }
  /**
  *  returns the endpoint of the particle X
  *  @return int tx
  */
  public int returnTX(){
    return tx;
  }
  /**
  *  returns the endpoint of the particle Y
  *  @return int ty
  */
  public int returnTY(){
    return ty;
  }
  /**
  *  returns the size of the projectile
  *  @return int weight
  */
  public int returnWeight(){
    return weight;
  }
  /**
  *  returns the color of the projectile
  *  @return color_Object color
  */
  public color_Object returnColor(){
    return color;
  }
}
