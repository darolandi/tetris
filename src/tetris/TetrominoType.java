package tetris;

/**
 * Different types of Tetromino and various data.
 * 
 * @author Daniel Rolandi
 */
public enum TetrominoType
{
  I, Z, S, O, T, L, J;    
  
  /**
   * Returns random Tetromino type.
   * Credit: http://stackoverflow.com/questions/8114174/how-to-randomize-enum-elements
   * 
   * @return Random Tetromino type.
   */
  public static TetrominoType getRandom()
  {
    return values()[ (int)(Math.random() * values().length) ];
  }    
  
}
