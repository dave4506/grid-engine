/**
 *  This grid_Object class contains the methods and data for all objects
 *  @author  David Sun
 *  @version May 20, 2014
 */

 class grid_Object {
   private int row = -1;
   private int col = -1;
   private color_Object colors = null;
   private boolean moveable = true;
   private String type = "";
   private boolean custom_draw = false;
   private boolean helmet = false;
   private boolean hand = false;
   public int hander = 0;

   public grid_Object(int row,int col,color_Object colors){
     this.row = row;
     this.col = col;
     this.colors = colors;
   }
   /**
   *  Set if the helmet will be rendered when drawn
   *  @param the new boolean status
   */
   public void setBHelmet(boolean f){
     helmet = f;
   }
   /**
   *  Returns the status of the helmet
   *  @return the boolean status
   */
   public boolean returnBHelmet(){
     return helmet;
   }
   /**
   *  Returns the slope of where the mouse is pointing at
   *  @return the slope
   */
   public double returnSlope(){
     return 0.0;
   }
   /**
   *  Sets the row of the object
   *  @param the new update row
   */
   public void setRow(int r){
     row = r;
   }
   /**
   *  Sets the col of the object
   *  @param the new update col
   */
   public void setCol(int c){
     col = c;
   }
   /**
   *  Returns the code of the grid_Object
   *  @return the code of the gird_object (defualt)
   */
   public String returnCode(){
     return "";
   }
   /**
   *  Returns the type of grid_Object this is
   *  @return the type string
   */
   public String returnType(){
     return this.type;
   }
   /**
   *  Returns the row of the grid_Object
   *  @return the row of the gird_object (defualt)
   */
   public int returnRow(){
     return row;
   }
   /**
   *  Returns the col of the grid_Object
   *  @return the col of the gird_object (defualt)
   */
   public int returnCol(){
     return col;
   }
   /**
   *  Returns the hemlet color  of the grid_Object
   *  @return the colorObject default null
   */
   public color_Object returnHelmet(){
     return null;
   }
   /**
   *  Set the color Object for the color
   *  @param the new color_Object to update the new color with
   */
   public void setColor(color_Object c){
     colors = c;
   }
   /**
   *  Returns the color of the grid-object
   *  @return the colorObject of the grid_Object
   */
   public color_Object returnColor(){
     return colors;
   }

   /**
   *  Set the grid_Object's posisiton as immutable or not
   *  @param set the boolea status with the new boolean
   */
   public void setMove(boolean m){
     moveable = m;
   }

   /**
   *  returns the grid_Object's posisiton as immutable or not
   *  @return set the boolea status with the new boolean
   */
   public boolean returnMove(){
     return moveable;
   }

   /**
   *  Set the grid_Object's object type i.e. wall player spawn
   *  @param the String of grid_Object type
   */
   public void setType(String type){
     this.type = type;
   }

   /**
   *  set if there is anycustom drawing protocols for the drawing of the grid_Object
   *  @param boolean tru custom draws and false vice-versa
   */
   public void setDraw(boolean d){
     custom_draw = d;
   }

   /**
   *  Return if there is any custom drawing protocols
   *  @return the custom_draw variable
   */
   public boolean returnDraw(){
     return custom_draw;
   }

   /**
   *  returns the borderColor for custom draw
   *  @return a default colorObject at (0,0,0)
   */
   public color_Object border_color(){
     return new color_Object(0,0,0,0);
   }
   /**
   *  returns the thicknesss of the color_Object
   *  @return the int thickness default at zero
   */
   public int border_thick(){
     return 0;
   }

   /**
   *  Return the team that corresponds with the grid_object
   *  @param the String of grid_Object team default 'nil'
   */
   public String returnTeam(){
     return "nil";
   }
   /**
   *  Set if hand will be drawn for the grid-Object 
   *  @return the boolean status of the grid_Object
   */
   public boolean returnHand(){
     return hand;
   }
   /**
   *  Set the boolean if hand will be drawn or not
   *  @param the boolean for the updated hand boolean
   */
   public void setHand(boolean h){
     hand = h;
   }
 }
