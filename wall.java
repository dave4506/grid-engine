/**
 *  The wall grid_Object for the game
 *  @author  David Sun
 *  @version May 20, 2014
 */
class wall extends grid_Object {
   public wall(int row,int col){
     super(row,col,new color_Object(140));
     //makes it immutable
     super.setMove(false);
     //sets type to wall
     super.setType("wall");
   }
 }
