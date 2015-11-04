/**
 *  The spawn object for the game
 *  @author  David Sun
 *  @version May 30, 2014
 */
class spawn extends grid_Object {
    private color_Object team_color;
    private String team;
    private int border = 3;
    public spawn(int row,int col,String name,color_Object tc){
      super(row,col,new color_Object(0,255,0,100));
      super.setMove(false);
      super.setType("spawn");
      super.setDraw(true);
      team = name;
      team_color = tc;
    }
    /**
    *  returns the team the spawn corresponds to
    *  @return String team
    */
    public String returnTeam(){
      return team;
    }
    /**
    *  returns the color of the border (tells what team the spawn belongs to)
    *  @return color_Object team_color
    */
    public color_Object border_color(){
      return team_color;
    }
    /**
    *  return the thickness of the border drawn
    *  @return int border
    */
    public int border_thick(){
      return border;
    }
}
