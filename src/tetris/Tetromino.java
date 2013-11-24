/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

/**
 * Represents a Collection (array) of Blocks.
 * Does not contain the actual rendering.
 * 
 * @author Daniel Rolandi
 */
public class Tetromino {
  private TetrominoInfo type;
  private Block[] blocks;
  private float refX;
  private float refY;
  
  public Tetromino(TetrominoInfo type, float x, float y){
    this.type = type;
    refX = x;
    refY = y;
    blocks = new Block[4];
  }
  
  /**
   * Renders all Blocks in this Tetromino.
   * Tetrominos in game are rendered by Board.
   * This method is to intended to be used for Next-Tetromino.
   * 
   * @param gc Game Container.
   * @param g Graphics context.
   */
  public void render(GameContainer gc, Graphics g){
    blocks[0].render(gc, g);
    blocks[1].render(gc, g);
    blocks[2].render(gc, g);
    blocks[3].render(gc, g);
  }
  
  /**
   * Returns reference X coordinate.
   * @return Reference X coordinate.
   */
  public float getX(){
    return refX;
  }
  
  /**
   * Returns reference Y coordinate.
   * @return Reference Y coordinate.
   */
  public float getY(){
    return refY;
  }
  
}
