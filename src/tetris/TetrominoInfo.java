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
  
  /**
   * Returns Block Color for that Tetromino type
   * 
   * @param type Tetromino Type
   * @return Block Color for that Tetromino type
   */
  public static Color getColor(TetrominoInfo type){
    switch(type){
      case I: return new Color(0, 255, 255);
      case Z: return new Color(255, 0, 0);
      case S: return new Color(0, 255, 0);
      case O: return new Color(255, 255, 0);
      case T: return new Color(255, 0, 255);
      case L: return new Color(255, 180, 0);
      case J: return new Color(0, 180, 255);
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
      case I: return new Point(3, 4);
      case Z: return new Point(3, 3);
      case S: return new Point(4, 3);
      case O: return new Point(4, 3);
      case T: return new Point(4, 3);
      case L: return new Point(5, 3);
      case J: return new Point(3, 3);
      default: throw new IllegalArgumentException();
    }
  }
  
  /**
   * Returns random Tetromino type.
   * Credit: http://stackoverflow.com/questions/8114174/how-to-randomize-enum-elements
   * 
   * @return Random Tetromino type.
   */
  public static TetrominoInfo getRandom(){
    return values()[ (int)(Math.random() * values().length) ];
  }
  
}
