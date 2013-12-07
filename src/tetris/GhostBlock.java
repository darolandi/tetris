package tetris;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

/**
 * Special Block that can be overwritten by normal Blocks.
 * 
 * @author Daniel Rolandi
 */
public class GhostBlock extends Block
{
  private static final Color GHOST_COLOR = Board.WIRE_COLOR;  
  
  /**
   * Sets the GhostBlock based on the type.
   * @param type Tetromino type.
   * @param x X coordinate of topleft oorner.
   * @param y Y coordinate of topleft corner.
   */
  public GhostBlock(TetrominoType type, float x, float y)
  {
    super(type, x, y);
  }
  
  /**
   * Draws the GhostBlock onto the GameContainer.
   * 
   * @param gameContainer Game Container.
   * @param graphics Graphics context.
   */
  @Override
  public void render(GameContainer gameContainer, Graphics graphics)
  {
    graphics.setColor( GHOST_COLOR );
    graphics.fillRect(x, y, Board.BLOCK_SIZE, Board.BLOCK_SIZE);      
  }    
  
}
