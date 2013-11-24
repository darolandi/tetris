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
  
  private static final Point point32 = new Point(3, 2);
  private static final Point point33 = new Point(3, 3);
  private static final Point point44 = new Point(4, 4);
  
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
      case I: return point32;
      case Z:
      case S:
      case T:
      case L:
      case J: return point33;
      case O: return point44;
      default: throw new IllegalArgumentException();
    }
  }    
  
  public static Point[][] getPoints(TetrominoInfo){
    
  }
  
}
