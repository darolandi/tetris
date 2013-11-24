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
  public float x;
  public float y;
  
  /**
   * Sets the Block based on the type.
   * @param type Tetromino type.
   * @param x X coordinate of topleft oorner.
   * @param y Y coordinate of topleft corner.
   */
  public Block(TetrominoType type, float x, float y){
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
    if(Board.BLOCK_SIZE != 20){
      g.setColor( TetrominoInfo.getColor(type) );
      g.fillRect(x, y, Board.BLOCK_SIZE, Board.BLOCK_SIZE);
      g.setColor( border );
      g.drawRect(x, y, Board.BLOCK_SIZE, Board.BLOCK_SIZE);
    }else{
      g.drawImage(TetrominoInfo.getImage(type), x, y);
    }    
  }
  
  /**
   * Returns X coordinate of topleft corner.
   * @return X coordinate of topleft corner.
   */
  public float getX(){
    return x;
  }
  
  /**
   * Returns Y coordinate of topleft corner.
   * @return Y coordinate of topleft corner.
   */
  public float getY(){
    return y;
  }
  
  /**
   * Returns col position in the grid.
   * @return Col position in the grid.
   */
  public int getGridX(){
    return (int)( (x - Offsets.GAME_X) / Board.BLOCK_SIZE);
  }
  
  /**
   * Returns row position in the grid.
   * @return Row position in the grid.
   */
  public int getGridY(){
//    return (int)( (y - Offsets.GAME_Y + Board.HEIGHT_WAITING*Board.BLOCK_SIZE) / Board.BLOCK_SIZE);
    return (int)( (y - Offsets.GAME_Y) / Board.BLOCK_SIZE + Board.HEIGHT_WAITING);
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
