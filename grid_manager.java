import java.util.ArrayList;
/**
 *  This class contains all the logic for the map
 *  @author  David Sun
 *  @version May 20, 2014
 */
class grid_manager{

  private grid_server serve;
  private map_default map;
  private shape_default shapes;
  private ArrayList<grid_Object> grid = new ArrayList<grid_Object>();

  public grid_manager(grid_server s){
    serve = s;
    map = new map_default();
    shapes = new shape_default();
  }

  /**
   *  Returns the arraylist of grid objects
   */
  public ArrayList<grid_Object> returnGrid(){
    return grid;
  }
  /**
   *  The method that generates the map with shapes and generate the spawn points
   */
  public void generateMap(){
     int[] m = map.random();
     int walls = 0;
     int ct = 0;
     while(walls<1200&&ct<200){
       serve.printOut("walls drawn : " + walls + " ct: " + ct);
       int x = (int)(0+56*Math.random());
       int y = (int)(0+36*Math.random());
       if (m[x/10] == 1){
         shape sh = shapes.random();
         int lengths = sh.returnArr().length;
         if(!shape_contains(sh,x,y)){
            draw_shape_wall(sh,x,y);
            walls += lengths;
         }else{
          ct ++;
         }
       }
     }
     serve.printOut("map complete!");
     serve.printOut("Generating spawn locations...");
     spawns();
     serve.printOut("______________________________________");
     serve.printOut("Map Created.");
  }

  /**
  *  Retursn the obj at the cords
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
  *  Adds the walls that creates the shape to the grid ArrayList
  *  @param the shape used to generate the wall
  *  @param the x position of the upperright most wall object
  *  @param the Y position of the upperright most wall object
  */
  public void draw_shape_wall(shape s,int x,int y){
       int[][] arr = s.returnArr();
       for(int i = 0;i<arr.length;i++){
          int tx = x + arr[i][0];
          int ty = y + arr[i][1];
          if (!(ty >= 37 || tx >= 57)){
            grid_Object gr = new wall(ty,tx);
            grid.add(gr);
          }
       }
  }

  /**
  *  Returns a count of a obj around a certain cords
  *  @param the type of grid_oject to count at
  *  @param the x position of the cord to search around
  *  @param the Y position of the cord to search around
  */
  public int count(String name,int col,int row){
     int count = 0;
     ArrayList<grid_Object> grd = around(col,row);
     for(grid_Object g : grd){
       String names = g.returnType();
       if(names.equals(name))
         count ++;
     }
     return count;
  }

  /**
  *  Returns a count of a obj in the whole grid
  *  @param the type of grid_oject to count at
  */
  public ArrayList<grid_Object> query(String name){
   ArrayList<grid_Object> go = new ArrayList<grid_Object>();
   for(grid_Object g : grid){
     String names = g.returnType();
     if(names.equals(name))
       go.add(g);
    }
   return go;
  }

  /**
  *  Returns if the cords is empty or not
  *  @param the x position of the cord to search
  *  @param the Y position of the cord to search
  */
  public boolean contains(int col,int row){
    for(grid_Object gr: grid){
        if(gr.returnCol() == col && gr.returnRow() == row)
          return true;
    }
    return false;
  }
  /**
  *  Returns an arraylist of objs around a certain grid_Object
  *  @param the x position of the cord to search
  *  @param the Y position of the cord to search
  */
  public ArrayList<grid_Object> around(int x, int y){
    ArrayList<grid_Object> grd = new ArrayList<grid_Object>();
    if (y != 0){
      if(contains(x,y-1)!= false)
          grd.add(returnAsset(x,y-1));
      if(x != 0){
         if(contains(x-1,y-1)!=false)
            grd.add(returnAsset(x-1,y-1));
      }
      if (x != 56){
         if(contains(x+1,y-1)!=false)
            grd.add(returnAsset(x+1,y-1));
      }
    }
    if (y != 36){
      if(contains(x,y+1)!=false)
          grd.add(returnAsset(x,y+1));
      if(x != 0){
         if(contains(x-1,y+1)!=false)
            grd.add(returnAsset(x-1,y+1));
      }
      if (x != 56){
         if(contains(x+1,y+1)!=false)
            grd.add(returnAsset(x+1,y+1));
      }
    }
    if (x != 0){
      if(contains(x-1,y)!=false)
        grd.add(returnAsset(x-1,y));
    }
    if (x != 56){
      if(contains(x+1,y)!=false)
        grd.add(returnAsset(x+1,y));
    }
    return grd;
  }

  /**
  *  Determines if the shape can be built
  *  @param the shape used to generate the wall
  *  @param the x position of the upperright most wall object
  *  @param the Y position of the upperright most wall object
  *  @return true if it can be built, false if it cant
  */
  public boolean shape_contains(shape s,int x,int y){
    int[][] update = new int[s.returnArr().length][2];
    int[][] arr = s.returnArr();
    for(int i = 0;i<update.length;i++){
          int tx = x + arr[i][0];
          int ty = y + arr[i][1];
          if (!(ty >= 37 || tx >= 57)){
            update[i][0] = tx;
            update[i][1] = ty;
          }
    }
    for(int i = 0;i<update.length;i++){
      if(contains(update[i][0],update[i][1]) == false){
        ArrayList<grid_Object> grd = around(update[i][0],update[i][1]);
        for(grid_Object g : grd){
           if(g.returnType().equals("wall")||g.returnType().equals("spawn")){
             for(int j = 0;i<update.length;j++){
                if((g.returnRow()== arr[i][1] && g.returnCol() == arr[i][0]) == false){
                  return true;
                }
             }
           }
        }
      }else{
         return true;
      }
    }
    return false;
  }
  /**
  *  Adds the spawn points of the game
  *  @param the shape used to generate the spawn
  *  @param the x position of the upperright most wall object
  *  @param the Y position of the upperright most wall object
  *  @param the team name of the spawn point
  *  @param the color of the spawn's border
  */
  public void draw_shape_spawn(shape s,int x,int y,String name,color_Object c){
       int[][] arr = s.returnArr();
       for(int i = 0;i<arr.length;i++){
          int tx = x + arr[i][0];
          int ty = y + arr[i][1];

          if (!(ty >= 37 || tx >= 57)){
            grid_Object gr = new spawn(ty,tx,name,c);
            grid.add(gr);
          }
       }
  }
  /**
  *  Adds 8 spawn points on both side of the game. 
  */
  public void spawns(){
    int counter = 0;
    while(counter<8){
       int x = (int)(0+56*Math.random());
       int y = (int)(0+36*Math.random());
       int v = 0;
       if (counter > 3)
         v = 5;

       if (x/10 == v ){
         int[][] a = {{0,0}};
         shape b = new shape(a);
         if(!shape_contains(b,x,y)){
            String name = "red";
            color_Object t = new color_Object(231, 76, 60);
            if(counter < 4){
             name = "blue";
             t = new color_Object(68,108,179);
            }

            serve.printOut("spawns drawn : " + counter + " at cords: X: " + x + " Y: " + y);
            draw_shape_spawn(b,x,y,name,t);
            counter += 1;
         }
       }
     }
  }
}
