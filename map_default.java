/**
 *  This class stores an array of all map possibilities
 *  @author  David Sun
 *  @version May 20, 2014
 */
class map_default {
  //the map is divided to 6 subsections
  //a one represents the area is spawnable and 0 means it cant
  int[][] map_style = {
     {1,0,1,0,1,0},
     {1,0,1,1,0,1},
     {1,1,1,1,1,1},
     {1,0,0,1,0,1},
     {1,1,0,0,1,1},
     {1,0,1,1,0,1},
     {0,1,1,1,0,0},
     {1,1,1,0,1,1},
     {1,0,1,0,1,1},
     {0,0,1,0,0,0}
   };
  public map_default(){
  }

  /**
  *  This randomly chooses a map type and returns it to the map_manager
  *  @return the array of the six subsections
  */
  public int[] random(){
    int t = (int)(map_style.length*Math.random());
    return map_style[t];
  }
}
