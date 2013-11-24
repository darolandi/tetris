/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Point;

/**
 * Represents a Collection (array) of Blocks.
 * Does not contain the actual rendering.
 * Once a Tetromino hits a floor, they become independent Blocks.
 * At that point, this class loses control over those Blocks.
 * 
 * @author Daniel Rolandi
 */
public class Tetromino {
  private TetrominoInfo type;
  private Block[] blocks;
  private float refX;
  private float refY;
  private int state;
  
  public Tetromino(TetrominoInfo type, float x, float y){
    this.type = type;
    refX = x;
    refY = y;    
    state = 0;
    createBlocks();
    syncBlocks();
  }
  
  private void createBlocks(){
    Block[] blocks = new Block[4];
    blocks[0] = new Block(type, refX, refY);
    blocks[1] = new Block(type, refX, refY);
    blocks[2] = new Block(type, refX, refY);
    blocks[3] = new Block(type, refX, refY);
    // notice that the blocks are not at their proper positions yet
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
  
  /**
   * Returns reference to Block by index.
   * @return Reference to Block by index.
   */
  public Block getBlock(int index){
    return blocks[index];
  }
  
  /**
   * Moves this Tetromino to the SpawnPoint in Waiting Room (above Game).
   */
  public void moveToSpawn(Block[][] grid){
    Point spawnPoint = TetrominoInfo.getSpawnPoint(type);
    float spawnX = spawnPoint.getX();
    float spawnY = spawnPoint.getY();
    
    refX = Board.GAME_OFFSETX + spawnX*Board.BLOCK_SIZE;
    refY = Board.GAME_OFFSETY + (spawnY - Board.HEIGHT_WAITING)*Board.BLOCK_SIZE;
    syncBlocks();
    syncGrid(grid);
  }
  
  private void syncBlocks(){
    Point[] points = TetrominoInfo.getPoints(type)[state];    
    
    // loop unrolling for performance
    blocks[0].setPosition(refX + points[0].getX() * Board.BLOCK_SIZE,
                        refY + points[0].getY() * Board.BLOCK_SIZE);
    blocks[1].setPosition(refX + points[1].getX() * Board.BLOCK_SIZE,
                        refY + points[1].getY() * Board.BLOCK_SIZE);
    blocks[2].setPosition(refX + points[2].getX() * Board.BLOCK_SIZE,
                        refY + points[2].getY() * Board.BLOCK_SIZE);
    blocks[3].setPosition(refX + points[3].getX() * Board.BLOCK_SIZE,
                        refY + points[3].getY() * Board.BLOCK_SIZE);    
  }  
  
  private void syncGrid(Block[][] grid){
    // loop unrolling for performance
    grid[ (int)blocks[0].getGridY() ][ (int)blocks[0].getGridX() ] = blocks[0];
    grid[ (int)blocks[1].getGridY() ][ (int)blocks[1].getGridX() ] = blocks[1];
    grid[ (int)blocks[2].getGridY() ][ (int)blocks[2].getGridX() ] = blocks[2];
    grid[ (int)blocks[3].getGridY() ][ (int)blocks[3].getGridX() ] = blocks[3];
  }
  
}
