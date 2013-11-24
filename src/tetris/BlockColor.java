package tetris;

import org.newdawn.slick.Color;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

/**
 * Abstracts away the Collection of Colors.
 * 
 * @author Daniel Rolandi
 */
public class BlockColor {
  private static final Color Icolor = new Color(0, 255, 255);
  private static final Color Zcolor = new Color(255, 0, 0);
  private static final Color Scolor = new Color(0, 255, 0);
  private static final Color Ocolor = new Color(255, 255, 0);
  private static final Color Tcolor = new Color(255, 0, 255);
  private static final Color Lcolor = new Color(255, 180, 0);
  private static final Color Jcolor = new Color(0, 180, 255);
  
  public static final Map<TetrominoType, Color> color;
  static{
    HashMap<TetrominoType, Color> tempColor = new HashMap<>();
    tempColor.put( TetrominoType.I, Icolor);
    tempColor.put( TetrominoType.Z, Zcolor);
    tempColor.put( TetrominoType.S, Scolor);
    tempColor.put( TetrominoType.O, Ocolor);
    tempColor.put( TetrominoType.T, Tcolor);
    tempColor.put( TetrominoType.L, Lcolor);
    tempColor.put( TetrominoType.J, Jcolor);
    color = Collections.unmodifiableMap(tempColor);
  }
  
  public static final Color border = new Color(255, 255, 255);
  
}
