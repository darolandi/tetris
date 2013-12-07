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
public class TetrominoInfo
{
  
  public static final int BLOCK_COUNT = 4;
  public static final int TYPE_COUNT = TetrominoType.values().length;
  
  private static final Color I_COLOR = new Color(0, 255, 255);
  private static final Color Z_COLOR = new Color(255, 0, 0);
  private static final Color S_COLOR = new Color(0, 255, 0);
  private static final Color O_COLOR = new Color(255, 255, 0);
  private static final Color T_COLOR = new Color(255, 0, 255);
  private static final Color L_COLOR = new Color(255, 127, 0);
  private static final Color J_COLOR = new Color(0, 0, 255);
  
  private static final Image I_IMAGE;
  private static final Image Z_IMAGE;
  private static final Image S_IMAGE;
  private static final Image O_IMAGE;
  private static final Image T_IMAGE;
  private static final Image L_IMAGE;
  private static final Image J_IMAGE;
  static
  {
    try
    {
      I_IMAGE = new Image("images/I.png");
      Z_IMAGE = new Image("images/Z.png");
      S_IMAGE = new Image("images/S.png");
      O_IMAGE = new Image("images/O.png");
      T_IMAGE = new Image("images/T.png");
      L_IMAGE = new Image("images/L.png");
      J_IMAGE = new Image("images/J.png");      
    }
    catch(SlickException slickException)
    {
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
  public static Color getColor(TetrominoType type)
  {
    switch(type)
    {
      case I: return I_COLOR;
      case Z: return Z_COLOR;
      case S: return S_COLOR;
      case O: return O_COLOR;
      case T: return T_COLOR;
      case L: return L_COLOR;
      case J: return J_COLOR;
      default: throw new IllegalArgumentException();
    }
  }
  
  /**
   * Returns Block Image for that Tetromino type
   * 
   * @param type Tetromino Type
   * @return Block Image for that Tetromino type
   */
  public static Image getImage(TetrominoType type)
  {
    switch(type)
    {
      case I: return I_IMAGE;
      case Z: return Z_IMAGE;
      case S: return S_IMAGE;
      case O: return O_IMAGE;
      case T: return T_IMAGE;
      case L: return L_IMAGE;
      case J: return J_IMAGE;
      default: throw new IllegalArgumentException();
    }
  }
  
  /**
   * Returns spawn point for that Tetromino type
   * 
   * @param type Tetromino Type
   * @return Spawn point for that Tetromino type
   */
  public static Point getSpawnPoint(TetrominoType type)
  {
    switch(type)
    {
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
  
  public static Point[][] getPoints(TetrominoType type)
  {
    switch(type)
    {
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
