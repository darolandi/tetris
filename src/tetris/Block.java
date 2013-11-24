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
  
  private TetrominoInfo type;
  private float x;
  private float y;
  
  /**
   * Sets the Block based on the type.
   * @param type Tetromino type.
   * @param x X coordinate of topleft oorner.
   * @param y Y coordinate of topleft corner.
   */
  public Block(TetrominoInfo type, float x, float y){
    this.type = type;
    this.x = x;
    this.y = y;
  }
  
  /**
   * Draws the Block onto the GameContainer, color-filled and bordered.
   * 
   * @param gc Game Container.
   * @param g Graphics context.
   */
  public void render(GameContainer gc, Graphics g){    
    g.setColor( TetrominoInfo.getColor(type) );
    g.fillRect(x, y, Board.BLOCK_SIZE, Board.BLOCK_SIZE);
    g.setColor( border );
    g.drawRect(x, y, Board.BLOCK_SIZE, Board.BLOCK_SIZE);
  }
  
  /**
   * Sets topleft corner into the given (x,y) coordinate.
   * 
   * @param x X coordinate of topleft corner.
   * @param y Y coordinate of topleft corner.
   */
  public void setPosition(float x, float y){
    this.x = x;
    this.y = y;
  }
  
}
