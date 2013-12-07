package tetris;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.SlickException;

/**
 * Contains various data for each Tetromino type.
 * 
 * @author Daniel Rolandi
 */
public class TetrominoInfo {    
  
  public static final int BLOCK_COUNT = 4;
  public static final int TYPE_COUNT = TetrominoType.values().length;
  
  private static final Color Icolor = new Color(0, 255, 255);
  private static final Color Zcolor = new Color(255, 0, 0);
  private static final Color Scolor = new Color(0, 255, 0);
  private static final Color Ocolor = new Color(255, 255, 0);
  private static final Color Tcolor = new Color(255, 0, 255);
  private static final Color Lcolor = new Color(255, 127, 0);
  private static final Color Jcolor = new Color(0, 0, 255);
  
  private static final Image Iimage;
  private static final Image Zimage;
  private static final Image Simage;
  private static final Image Oimage;
  private static final Image Timage;
  private static final Image Limage;
  private static final Image Jimage;
  static{
    try{
      Iimage = new Image("images/I.png");
      Zimage = new Image("images/Z.png");
      Simage = new Image("images/S.png");
      Oimage = new Image("images/O.png");
      Timage = new Image("images/T.png");
      Limage = new Image("images/L.png");
      Jimage = new Image("images/J.png");      
    }catch(SlickException e){
      throw new IllegalStateException("Could not load images.");
    }
  }
    
  private static final Point point44 = new Point(4, 4);
  private static final Point[][] allPoints;  
  static{
    Point[][] allPointsTemp = new Point[4][4];
    for(int x = 0; x < 4; x++){
      for(int y = 0; y < 4; y++){
        allPointsTemp[x][y] = new Point(x, y);
      }
    }
    allPoints = allPointsTemp;
  }
  
  // NOTE: Another alternative is using rotation for each time
  // but precomputed values uses less computation.
  // row = state, col = blockID
  
  private static final Point[][] pointsI = {
    {allPoints[0][1], allPoints[1][1], allPoints[2][1], allPoints[3][1]},
    {allPoints[2][0], allPoints[2][1], allPoints[2][2], allPoints[2][3]},
    {allPoints[3][2], allPoints[2][2], allPoints[1][2], allPoints[0][2]},
    {allPoints[1][3], allPoints[1][2], allPoints[1][1], allPoints[1][0]}
  };
  
  private static final Point[][] pointsZ = {
    {allPoints[0][1], allPoints[1][1], allPoints[1][2], allPoints[2][2]},
    {allPoints[1][0], allPoints[1][1], allPoints[0][1], allPoints[0][2]},
    {allPoints[2][1], allPoints[1][1], allPoints[1][0], allPoints[0][0]},
    {allPoints[1][2], allPoints[1][1], allPoints[2][1], allPoints[2][0]}
  };
  
  private static final Point[][] pointsS = {
    {allPoints[2][1], allPoints[1][1], allPoints[1][2], allPoints[0][2]},
    {allPoints[1][2], allPoints[1][1], allPoints[0][1], allPoints[0][0]},
    {allPoints[0][1], allPoints[1][1], allPoints[1][0], allPoints[2][0]},
    {allPoints[1][0], allPoints[1][1], allPoints[2][1], allPoints[2][2]}
  };
  
  private static final Point[][] pointsO = {
    {allPoints[0][0], allPoints[1][0], allPoints[0][1], allPoints[1][1]},
    {allPoints[0][0], allPoints[1][0], allPoints[0][1], allPoints[1][1]},
    {allPoints[0][0], allPoints[1][0], allPoints[0][1], allPoints[1][1]},
    {allPoints[0][0], allPoints[1][0], allPoints[0][1], allPoints[1][1]}
  };
  
  private static final Point[][] pointsT = {
    {allPoints[0][1], allPoints[1][1], allPoints[1][0], allPoints[2][1]},
    {allPoints[1][0], allPoints[1][1], allPoints[2][1], allPoints[1][2]},
    {allPoints[2][1], allPoints[1][1], allPoints[1][2], allPoints[0][1]},
    {allPoints[1][2], allPoints[1][1], allPoints[0][1], allPoints[1][0]}
  };
  
  private static final Point[][] pointsL = {
    {allPoints[0][1], allPoints[1][1], allPoints[2][1], allPoints[2][0]},
    {allPoints[1][0], allPoints[1][1], allPoints[1][2], allPoints[2][2]},
    {allPoints[2][1], allPoints[1][1], allPoints[0][1], allPoints[0][2]},
    {allPoints[1][2], allPoints[1][1], allPoints[1][0], allPoints[0][0]}
  };
  
  private static final Point[][] pointsJ = {
    {allPoints[0][1], allPoints[1][1], allPoints[2][1], allPoints[0][0]},
    {allPoints[1][0], allPoints[1][1], allPoints[1][2], allPoints[2][0]},
    {allPoints[2][1], allPoints[1][1], allPoints[0][1], allPoints[2][2]},
    {allPoints[1][2], allPoints[1][1], allPoints[1][0], allPoints[0][2]}
  };
  
   /**
   * Returns Block Color for that Tetromino type
   * 
   * @param type Tetromino Type
   * @return Block Color for that Tetromino type
   */
  public static Color getColor(TetrominoType type){
    switch(type){
      case I: return Icolor;
      case Z: return Zcolor;
      case S: return Scolor;
      case O: return Ocolor;
      case T: return Tcolor;
      case L: return Lcolor;
      case J: return Jcolor;
      default: throw new IllegalArgumentException();
    }
  }
  
  /**
   * Returns Block Image for that Tetromino type
   * 
   * @param type Tetromino Type
   * @return Block Image for that Tetromino type
   */
  public static Image getImage(TetrominoType type){
    switch(type){
      case I: return Iimage;
      case Z: return Zimage;
      case S: return Simage;
      case O: return Oimage;
      case T: return Timage;
      case L: return Limage;
      case J: return Jimage;
      default: throw new IllegalArgumentException();
    }
  }
  
  /**
   * Returns spawn point for that Tetromino type
   * 
   * @param type Tetromino Type
   * @return Spawn point for that Tetromino type
   */
  public static Point getSpawnPoint(TetrominoType type){
    switch(type){
      case I: return allPoints[3][2];
      case Z:
      case S:
      case T:
      case L:
      case J: return allPoints[3][3];
      case O: return point44;
      default: throw new IllegalArgumentException();
    }
  }    
  
  public static Point[][] getPoints(TetrominoType type){
    switch(type){
      case I: return pointsI;
      case Z: return pointsZ;
      case S: return pointsS;
      case T: return pointsT;
      case L: return pointsL;
      case J: return pointsJ;
      case O: return pointsO;
      default: throw new IllegalArgumentException();
    }
  }
  
}
