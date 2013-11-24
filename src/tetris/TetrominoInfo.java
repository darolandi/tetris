package tetris;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Point;

/**
 * Different types of Tetromino and various data.
 * 
 * @author Daniel Rolandi
 */
public enum TetrominoInfo {
  I, Z, S, O, T, L, J;
  
  private static final Color Icolor = new Color(0, 255, 255);
  private static final Color Zcolor = new Color(255, 0, 0);
  private static final Color Scolor = new Color(0, 255, 0);
  private static final Color Ocolor = new Color(255, 255, 0);
  private static final Color Tcolor = new Color(255, 0, 255);
  private static final Color Lcolor = new Color(255, 180, 0);
  private static final Color Jcolor = new Color(0, 180, 255);
    
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
   * Returns random Tetromino type.
   * Credit: http://stackoverflow.com/questions/8114174/how-to-randomize-enum-elements
   * 
   * @return Random Tetromino type.
   */
  public static TetrominoInfo getRandom(){
    return values()[ (int)(Math.random() * values().length) ];
  }
  
  /**
   * Returns Block Color for that Tetromino type
   * 
   * @param type Tetromino Type
   * @return Block Color for that Tetromino type
   */
  public static Color getColor(TetrominoInfo type){
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
   * Returns spawn point for that Tetromino type
   * 
   * @param type Tetromino Type
   * @return Spawn point for that Tetromino type
   */
  public static Point getSpawnPoint(TetrominoInfo type){
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
  
  public static Point[][] getPoints(TetrominoInfo){
    
  }
  
}
