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
  private static final int DEFAULT_INIT_STATE = 0;
  
  private TetrominoType type;
  private Block[] blocks;
  private float refX;
  private float refY;
  private int state;
  private boolean ghost;
  
  public Tetromino(TetrominoType type, float x, float y, boolean ghost){
    this(type, x, y, ghost, DEFAULT_INIT_STATE);
  }
  
  public Tetromino(TetrominoType type, float x, float y, boolean ghost, int state){
    this.type = type;
    refX = x;
    refY = y;    
    this.state = state;
    this.ghost = ghost;
    if(ghost){
      createGhostBlocks();
    }else{
      createBlocks();
    }    
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
  
  private void createGhostBlocks(){
    blocks = new Block[4];
    blocks[0] = new GhostBlock(type, refX, refY);
    blocks[1] = new GhostBlock(type, refX, refY);
    blocks[2] = new GhostBlock(type, refX, refY);
    blocks[3] = new GhostBlock(type, refX, refY);
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
  
  /**
   * Plugs this Block into the Grid.
   * Expected to be called only during move-to-spawn
   * or when rotating or when moving currentRetro over ghostRetro.
   * 
   * @param grid Grid of Blocks from the Board.
   */  
  public void syncGrid(Block[][] grid){
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
  
  // expected to be called only during rotating
  // or when killing Tetromino
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
   * 
   * @param grid Grid of Blocks from the Board.
   * @param board Connection to Board.
   */
  public void rotateLeft(Block[][] grid, Board board){
    unsyncGrid(grid);    
    if( canRotateLeft(grid, board)){      
      state = (state + 3)%4;
      board.killGhostTetro();
      board.summonGhostTetromino(state);
      syncBlocks();      
    }
    syncGrid(grid);
  }
  
  /**
   * Returns true if this Tetromino can rotate counter-clockwise.
   * 
   * @param grid Grid of Blocks from the Board.
   * @param board Connection to Board.
   * @return True if this Tetromino can rotate counter-clockwise.
   */
  public boolean canRotateLeft(Block[][] grid, Board board){
    int futureState = (state + 3)%4;
    return canRotate(grid, futureState, board);
  }
  
  private boolean canRotate(Block[][] grid, int futureState, Board board){
    Point[] points = TetrominoInfo.getPoints(type)[futureState];
    int gridX;
    int gridY;
    Block targetBlock;
    
    // loop unrolling for performance
    gridX = (int)( (refX - Offsets.GAME_X)/Board.BLOCK_SIZE + points[0].getX() );
    gridY = (int)( (refY - Offsets.GAME_Y)/Board.BLOCK_SIZE + points[0].getY() + Board.HEIGHT_WAITING );
    if( outOfBounds(gridX, gridY) ){
      return false;
    }
    targetBlock = grid[gridY][gridX];
    if( board.unpathableBlock(targetBlock) ){
      return false;
    }
    
    gridX = (int)( (refX - Offsets.GAME_X)/Board.BLOCK_SIZE + points[1].getX() );
    gridY = (int)( (refY - Offsets.GAME_Y)/Board.BLOCK_SIZE + points[1].getY() + Board.HEIGHT_WAITING );
    if( outOfBounds(gridX, gridY) ){
      return false;
    }
    targetBlock = grid[gridY][gridX];
    if( board.unpathableBlock(targetBlock) ){
      return false;
    }
    
    gridX = (int)( (refX - Offsets.GAME_X)/Board.BLOCK_SIZE + points[2].getX() );
    gridY = (int)( (refY - Offsets.GAME_Y)/Board.BLOCK_SIZE + points[2].getY() + Board.HEIGHT_WAITING );
    if( outOfBounds(gridX, gridY) ){
      return false;
    }
    targetBlock = grid[gridY][gridX];
    if( board.unpathableBlock(targetBlock) ){
      return false;
    }
    
    gridX = (int)( (refX - Offsets.GAME_X)/Board.BLOCK_SIZE + points[3].getX() );
    gridY = (int)( (refY - Offsets.GAME_Y)/Board.BLOCK_SIZE + points[3].getY() + Board.HEIGHT_WAITING );
    if( outOfBounds(gridX, gridY) ){
      return false;
    }
    targetBlock = grid[gridY][gridX];
    if( board.unpathableBlock(targetBlock) ){
      return false;
    }
    
    return true;
  }
  
  private boolean outOfBounds(int gridX, int gridY){
    return gridX < 0 || gridX >= Board.WIDTH || gridY < 0 || gridY >= Board.HEIGHT;
  }    
  
  /**
   * Rotates the Tetromino clockwise.
   * 
   * @param grid Grid of Blocks from the Board.
   * @param board Connection to Board.
   */
  public void rotateRight(Block[][] grid, Board board){
    unsyncGrid(grid);
    if( canRotateRight(grid, board)){
      state = (state + 1)%4;
      board.killGhostTetro();
      board.summonGhostTetromino(state);
      syncBlocks();      
    }
    syncGrid(grid);    
  }
  
  /**
   * Returns true if this Tetromino can rotate clockwise.
   * 
   * @param grid Grid of Blocks from the Board.
   * @param board Connection to Board.
   * @return True if this Tetromino can rotate clockwise.
   */
  public boolean canRotateRight(Block[][] grid, Board board){
    int futureState = (state + 1)%4;
    return canRotate(grid, futureState, board);
  }
  
  /**
   * Returns the orientation state of this Tetromino.
   * 
   * @return The orientation state of this Tetromino.
   */
  public int getState(){
    return state;
  }
  
  /**
   * Returns the type of this Tetromino.
   * 
   * @return The type of this Tetromino.
   */
  public TetrominoType getType(){
    return type;
  }
  
  /**
   * Kills this Tetromino, removing from Grid permanently.
   * Expected to be used with only Ghost Tetromino.
   * 
   * @param grid Grid of Blocks from the Board.
   */
  public void kill(Block[][] grid){
    unsyncGrid(grid);
    blocks[0] = null;
    blocks[1] = null;
    blocks[2] = null;
    blocks[3] = null;
    blocks = null;
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
