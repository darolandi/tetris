package tetris;

import org.newdawn.slick.Color;
import java.awt.Point;

/**
 * Different types of Tetromino.
 * 
 * @author Daniel Rolandi
 */
public enum TetrominoType {
  I, Z, S, O, T, L, J;
  
  /**
   * Returns Block Color for that Tetromino type
   * 
   * @param type Tetromino Type
   * @return Block Color for that Tetromino type
   */
  public static Color getColor(TetrominoType type){
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
  public static Point getSpawnPoint(TetrominoType type){
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
   * Returns random TetrominoType.
   * Credit: http://stackoverflow.com/questions/8114174/how-to-randomize-enum-elements
   * 
   * @return Random TetrominoType.
   */
  public static TetrominoType getRandom(){
    return values()[ (int)(Math.random() * values().length) ];
  }
  
}
