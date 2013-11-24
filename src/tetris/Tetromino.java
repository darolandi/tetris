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
  private TetrominoType type;
  private Block[] blocks;
  private float refX;
  private float refY;
  private int state;
  
  public Tetromino(TetrominoType type, float x, float y){
    this.type = type;
    refX = x;
    refY = y;    
    state = 0;
    createBlocks();
    syncBlocks();
  }
  
  private void createBlocks(){
    blocks = new Block[4];
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
   * Returns true if that Block is a part of this Tetromino.
   * 
   * @param block Block in question.
   * @return True if that Block is a part of this Tetromino.
   */
  public boolean hasBlock(Block block){
    // just test by reference is sufficient
    if(block == blocks[0] || block == blocks[1]
            || block == blocks[2] || block == blocks[3]){
      return true;
    }
    return false;
  }
  
  /**
   * Moves this Tetromino to the SpawnPoint in Waiting Room (above Game).
   */
  public void moveToSpawn(Block[][] grid){
    Point spawnPoint = TetrominoInfo.getSpawnPoint(type);
    float spawnX = spawnPoint.getX();
    float spawnY = spawnPoint.getY();
    
    refX = Offsets.GAME_X + spawnX*Board.BLOCK_SIZE;
    refY = Offsets.GAME_Y + (spawnY - Board.HEIGHT_WAITING)*Board.BLOCK_SIZE;    
    
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
  
  // expected to be called only during move-to-spawn
  // or when rotating
  private void syncGrid(Block[][] grid){
    Block tempBlock;
    // loop unrolling for performance
    tempBlock = blocks[0];
    grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() ] = tempBlock;
    tempBlock = blocks[1];
    grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() ] = tempBlock;
    tempBlock = blocks[2];
    grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() ] = tempBlock;
    tempBlock = blocks[3];
    grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() ] = tempBlock;
  }
  
  // expected to be called only when rotating
  private void unsyncGrid(Block[][] grid){
    Block tempBlock;
    // loop unrolling for performance
    tempBlock = blocks[0];
    grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() ] = null;
    tempBlock = blocks[1];
    grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() ] = null;
    tempBlock = blocks[2];
    grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() ] = null;
    tempBlock = blocks[3];
    grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() ] = null;
  }
  
  /**
   * Move the Tetromino to the down.
   * Precondition: Board already checked for empty spaces.
   */
  public void moveDown(){
    refY += Board.BLOCK_SIZE;
    syncBlocks();
  }
  
  /**
   * Move the Tetromino to the left.
   * Precondition: Board already checked for empty spaces.
   */
  public void moveLeft(){
    refX -= Board.BLOCK_SIZE;
    syncBlocks();
  }
  
  /**
   * Move the Tetromino to the right.
   * Precondition: Board already checked for empty spaces.
   */
  public void moveRight(){
    refX += Board.BLOCK_SIZE;
    syncBlocks();
  }
  
  /**
   * Rotates the Tetromino counter-clockwise.
   * @param grid Grid of Blocks from the Board.
   */
  public void rotateLeft(Block[][] grid){
    unsyncGrid(grid);
    state = (state + 3)%4;
    syncBlocks();
    syncGrid(grid);
  }
  
  /**
   * Rotates the Tetromino clockwise.
   * @param grid Grid of Blocks from the Board.
   */
  public void rotateRight(Block[][] grid){
    unsyncGrid(grid);
    state = (state + 1)%4;
    syncBlocks();
    syncGrid(grid);
  }
  
  @Override
  public String toString(){
    StringBuilder result = new StringBuilder("");
    
    result.append("Type: ");
    result.append(type);
    
    result.append("\nrefX: ");
    result.append(refX);
    result.append("\nrefY: ");
    result.append(refY);        
    
    return result.toString();
  }
  
}
