/**
 * The basic class that contains a shape structure of grid_Objects to use for drawing
 *  @author  David Sun
 *  @version May 20, 2014
 */
class shape {

  private int[][] arr;
  public shape(int[][] a){
    arr = a;
  }
  /**
  *  returns the arr containing the shape to be drawn
  *  @return int[][] arr
  */
  public int[][] returnArr(){
    return arr;
  }
}
