package tetris;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

/**
 * Represents the atomic 1x1 cell, aka Block.
 * Contains the actual rendering.
 * 
 * @author Daniel Rolandi
 */
public class Block {
  public static final Color border = new Color(255, 255, 255);
  
  private TetrominoType type;
  private float x;
  private float y;
  
  /**
   * Sets the Block based on the type.
   * @param type Tetromino type.
   */
  public Block(TetrominoType type){
    this.type = type;
  }
  
  /**
   * Draws the Block onto the GameContainer, color-filled and bordered.
   * 
   * @param gc Game Container.
   * @param g Graphics context.
   */
  public void render(GameContainer gc, Graphics g){
    g.setColor( TetrominoType.getColor(type) );
    g.fillRect(x, y, Board.BLOCK_SIZE, Board.BLOCK_SIZE);
    g.setColor( border );
    g.drawRect(x, y, Board.BLOCK_SIZE, Board.BLOCK_SIZE);
  }
  
}
