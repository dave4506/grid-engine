/**
 *  This class stores the RGB values of a color and returns them when needed
 *  The colors are immutable after instantiated.
 *  @author  David Sun
 *  @version May 20, 2014
 */
class color_Object {
    private int r = -1;
    private int g = -1;
    private int b = -1;
    private int a = -1;

    public color_Object(int r, int g, int b){
      this.r = r;
      this.g = g;
      this.b = b;
      this.a = 255;
    }
    public color_Object(int r, int g, int b,int a){
      this.r = r;
      this.g = g;
      this.b = b;
      this.a = a;
    }
    public color_Object(int grey){
      this.r = grey;
      this.g = grey;
      this.b = grey;
      this.a = 255;
    }
    /**
     *  Returns the R value of the color
     *
     *  @return the R value
     */
    public int returnR(){
      return r;
    }
    /**
     *  Returns the B value of the color
     *
     *  @return the B value
     */
    public int returnB(){
      return b;
    }
    /**
     *  Returns the G value of the color
     *
     *  @return the G value
     */
    public int returnG(){
      return g;
    }
    /**
     *  Returns the A value of the color
     *
     *  @return the A value
     */
    public int returnA(){
      return a;
    }
  }
