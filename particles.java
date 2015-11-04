import java.util.ArrayList;
/**
 * This is the main particle class that contians the logic to find the trajectory of the particle
 *  @author  David Sun
 *  @version May 20, 2014
 */
class particles {
    private grid_Object gr = null;
    private int speed = 0;
    private int length = 0;
    private int startingX = -1;
    private int startingY = -1;
    private int dist = 0;
    private double slope = -1;
    private int endX = -1;
    private int endY = -1;
    private int tempX = -1;
    private int tempY = -1;
    private int initialX = 0;
    private int initialY = 0;
    private boolean up;
    private color_Object color;
    private int projectile = 1;
    private int star = 0;
    private int pointX = 0;
    private int pointY = 0;
    public particles(color_Object c,int sx,int sy,double s,int sp,int l,boolean u,int p){
      color = c;
      startingX = sx;
      startingY = sy;
      tempX = startingX;
      tempY = startingY;
      slope = s;
      speed = sp;
      length = l;
      up = u;
      dist = speed;
      projectile = p;
      initialX = startingX;
      initialY = startingY;
      pointX = startingX;
      pointY = startingY;
    }
    /***************************************************************
      *   All the methods for getting the data within the class
      *************************************************************/
    /**
    *  returns the size of the projectile
    *  @return int projectile
    */
    public int returnProjectile(){
      return projectile;
    }
    /**
    *  returns the end point of the particle (X)
    *  @return int tempX
    */
    public int returntempX(){
      return tempX;
    }
    /**
    *  returns the end point of the particle (Y)
    *  @return int tempY
    */
    public int returntempY(){
      return tempY;
    }
    /**
    *  returns the staring point of the particle (Y)
    *  @return int startingY
    */
    public int returnstartY(){
      return startingY;
    }
    /**
    *  returns the staring point of the particle (X)
    *  @return int startingX
    */
    public int returnstartX(){
      return startingX;
    }
    /**
    *  returns the termination point of the particle (X)
    *  @return int endX
    */
    public int returnEndX(){
      return endX;
    }
    /**
    *  returns the termination point of the particle (Y)
    *  @return int endY
    */
    public int returnEndY(){
      return endY;
    }
    /**
    *  returns boolean if the particle has reached termination
    *  @return boolean true if terminated false otherwise
    */
    public boolean terminate(){
      return (startingX == endX && startingY == endY);
    }
    /**
    *  returns the object it will come in contact with
    *  @return int endX
    */
    public grid_Object returnEnd(){
      return gr;
    }
    /**
    *  returns the color of the particle
    *  @return color_Object color
    */
    public color_Object returnC(){
      return color;
    }
    /**
    *  The logic that finds the termination of the particle by first finding the
    *  the block it terminates, then the exact point
    *  this method also updates tempX,Y and starting X,Y to the latest points
    *  @param Arraylist of the map
    *  @param Arraylist of the players
    */
    public void update(ArrayList<grid_Object> g,ArrayList<player> pl){
      ArrayList<grid_Object> complete = new ArrayList<grid_Object>();
      complete.addAll(g);
      complete.addAll(pl);
      int yStart = (int)Math.round((startingY/25));
      int xStart = (int)Math.round((startingX/25));
      int tempystart = yStart;
      int prevX = xStart;
      gr = null;
      boolean status = true;
      int lastX = -1;
      int lastY = -1;
      while(status == true){
        double tempContact = 0;
        int contact = 0;
        if (up == true){
          tempContact = (((((tempystart*25)-initialY)/slope)+initialX)/25);
        }else{
          tempContact = (((((tempystart*25+25)-initialY)/slope)+initialX)/25);
        }
        if(tempContact < 0 && tempContact > -1 ){
          contact = (int)(tempContact - 1);
        }else{
          contact = (int)(tempContact);
        }
        int cords = prevX;
          if(contact<xStart){
            loop:
            for(int i = cords;i>=contact;i--){
              if(i != (initialX/25) || tempystart != yStart){
                gr = returnAsset(i,tempystart,complete);
                lastX = i;
                lastY = tempystart;
                if (gr != null||(i<-1||i>=57)){
                  status = false;
                  break loop;
                }
              }
            }
          }else{
            loop:
            for(int i = cords;i<=contact;i++){
              if(i != (initialX/25)|| tempystart != yStart){
                gr = returnAsset(i,tempystart,complete);
                lastX = i;
                lastY = tempystart;
                if (gr != null||(i<-1||i>=57)){
                  status = false;
                  break loop;
                }
              }
            }
          }
        if(status == true){
          prevX = contact;
          if(tempystart < 1 || tempystart>=36){
            status = false;
          }else{
            if (up == true){
              tempystart -= 1;
            }else{
              tempystart += 1;
            }
          }
        }
        if(status == false){
          if(gr == null){
            if(lastY == 0){//likely error
              int x1 = (int)((double)((lastY*25)-initialY)/slope + initialX);
              if((lastX*25)<=x1&&(lastX*25+25)>=x1){
                endX = (int)x1;
                endY = lastY*25;
              }
            }
            if(lastY == 36){//likely error
              int x1 = (int)((double)((lastY*25+25)-initialY)/slope + initialX);
              if((lastX*25)<=x1&&(lastX*25+25)>=x1){
                endX = (int)x1;
                endY = lastY*25 + 25;
              }
            }
            if(lastX <= 0){//likely error
              int y1 = (int)(slope*((lastX*25)-initialX) + initialY);
              if((lastY*25)<=y1&&(lastY*25+25)>=y1){
                endX = lastX*25;
                endY = (int)y1;
              }
            }
            if(lastX >= 56){//likely error
              int y1 = (int)(slope*((lastX*25)-initialX) + initialY);
              if((lastY*25)<=y1&&(lastY*25+25)>=y1){
                endX = lastX*25;
                endY = (int)y1;
              }
            }
          }else{
            int[][] arr = {{lastX*25,lastY*25+25},{lastX*25,lastY*25},{lastX*25+25,lastY*25},{lastX*25+25,lastY*25+25},{lastX*25,lastY*25+25},{lastX*25,lastY*25}};
            int distance = 0;
            int index = 0;
            int sx = initialX;
            int sy = initialY;
            if(initialX == startingX && initialY == startingY){
              changePointer();
              sx = pointX;
              sy = pointY;
            }
            for(int i = 1;i<5;i++){
              int dist = (int)distance(sx,sy,arr[i][0],arr[i][1]);
              if(i == 1 || dist < distance ){
                distance = dist;
                index = i;
              }
            }
            int tempy = (int)(slope*(arr[index][0]-initialX) + initialY);
            int tempx = (int)((double)(arr[index][1]-initialY)/slope + initialX);
            if(index % 2 == 0){
              int i = 0;
              if(isBetween(arr[index][1],arr[index+1][1],tempy)){
                  endX = arr[index][0];
                  endY = tempy;
                  i++;
              }
              if(isBetween(arr[index][0],arr[index-1][0],tempx)){
                  endX = tempx;
                  endY = arr[index][1];
                  i++;
              }
              if(i==2){
                int temp = 0;
                distance = 0;
                for(int j = 1;j<5;j++){
                  int dist = (int)distance(sx,sy,arr[j][0],arr[j][1]);
                  if((j == 1 || dist < distance)&&index!=j){
                    distance = dist;
                    temp = j;
                  }
                }
                if(arr[temp][0] == arr[index][0]){
                  endX = arr[index][0];
                  endY = tempy;
                }
                if(arr[temp][1] == arr[index][1]){
                  endX = tempx;
                  endY = arr[index][1];
                }
              }
            }else{
              int i = 0;
              if(isBetween(arr[index][1],arr[index-1][1],tempy)){
                  endX = arr[index][0];
                  endY = tempy;
                  i++;
              }
              if(isBetween(arr[index][0],arr[index+1][0],tempx)){
                  endX = tempx;
                  endY = arr[index][1];
                  i++;
              }
              if(i==2){
                int temp = 0;
                distance = 0;
                for(int j = 1;j<5;j++){
                  int dist = (int)distance(sx,sy,arr[j][0],arr[j][1]);
                  if((j == 1 || dist < distance)&&index!=j){
                    distance = dist;
                    temp = j;
                  }
                }
                if(arr[temp][0] == arr[index][0]){
                  endX = arr[index][0];
                  endY = tempy;
                }
                if(arr[temp][1] == arr[index][1]){
                  endX = tempx;
                  endY = arr[index][1];
                }
              }
            }
          }
        }
      }
      if(distance()<speed){
        if(star == 0){
          tempX = endX;
          tempY = endY;
          star ++;
        }else{
          star = 0;
          startingX = endX;
          startingY = endY;
        }
      }else{
        if(distance()<(speed+length)){
          changeStarting();
          tempX = endX;
          tempY = endY;
        }else{
          changeStarting();
          changeTemp();
          dist += speed;
        }
      }
    }
    /**
    *  change the starting X and Y by the projectile size
    */
    public void changeStarting(){
      int distance = dist;
      int x = initialX;
      int y = initialY;
      int negative = 1;
      if(endX-initialX > 0){
        negative = 1;
      }else{
        negative = -1;
      }
      double x_temp = x + negative*Math.sqrt((distance*distance)/(slope*slope + 1));
      int y2 = (int)Math.round(slope*(x_temp - x) + y);
      int x2 = (int)Math.round(x_temp);

      if (slope == Double.POSITIVE_INFINITY){
        x2 = x;
        y2 = y + distance;
      }
      if (slope == Double.NEGATIVE_INFINITY){
        x2 = x;
        y2 = y - distance;
      }
      startingX = x2;
      startingY = y2;
    }
    /**
    *  change the tempX and tempY by the distance traveled
    */
    public void changeTemp(){
      int distance = dist + length;
      int x = initialX;
      int y = initialY;
      int negative = 1;
      if(endX-initialX> 0){
        negative = 1;
      }else{
        negative = -1;
      }
      double x_temp = x + negative*Math.sqrt((distance*distance)/(slope*slope + 1));
      int y2 = (int)Math.round(slope*(x_temp - x) + y);
      int x2 = (int)Math.round(x_temp);

      if (slope == Double.POSITIVE_INFINITY){
        x2 = x;
        y2 = y + distance;
      }
      if (slope == Double.NEGATIVE_INFINITY){
        x2 = x;
        y2 = y - distance;
      }
      tempX = x2;
      tempY = y2;
    }
    /***************************************************************
      *    All the helper methods to make the main logic work
      *************************************************************/
    /**
    *  A helper method to find a more precise contact point
    */
    public void changePointer(){
      int distance = 9;
      int x = initialX;
      int y = initialY;
      int negative = 1;
      if(endX-startingX> 0){
        negative = 1;
      }else{
        negative = -1;
      }
      double x_temp = x + negative*Math.sqrt((distance*distance)/(slope*slope + 1));
      int y2 = (int)Math.round(slope*(x_temp - x) + y);
      int x2 = (int)Math.round(x_temp);

      if (slope == Double.POSITIVE_INFINITY){
        x2 = x;
        y2 = y + distance;
      }
      if (slope == Double.NEGATIVE_INFINITY){
        x2 = x;
        y2 = y - distance;
      }
      pointX = x2;
      pointY = y2;
    }
    /**
    * A simple helper method to find distance from starting point to terminate point
    * @param the distance between the two points
    */
    public double distance(){
      return (double)Math.sqrt((startingX-endX)*(startingX-endX)+(startingY-endY)*(startingY-endY));
    }
    /**
    *  returns if the point c is in between a and c (method taken from Stack overflow)
       @return true, if its between, false otherwise
    */
    public static boolean isBetween(int a, int b, int c) {
        return b >= a ? c >= a && c <= b : c >= b && c <= a;
    }
    /**
    *  Returns the obj at the cords
    *  @param the row that should be searched at
    *  @param the col that should be searched at
    *  @param the grid_object list to search through
    *  @return the grid_Object found there or null if there is none
    */
    public grid_Object returnAsset(int col,int row,ArrayList<grid_Object> grid){
      for(grid_Object gr: grid)
        if(gr.returnCol() == col && gr.returnRow() == row)
          return gr;
      return null;
    }
    /**
    * A simple helper method to find distance from any two points
    * @param the X of the first point
    * @param the Y of the first point
    * @param the X of the second point
    * @param the Y of the second point
    */
    public double distance(int x,int y,int x1,int y1){
      return (double)Math.sqrt((x-x1)*(x-x1)+(y-y1)*(y-y1));
    }

}
