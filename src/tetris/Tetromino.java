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
public class Tetromino
{
  public static final int DEFAULT_INIT_STATE = 0;
  
  private TetrominoType type;
  private Block[] blocks;
  private float refX;
  private float refY;
  private int state;
  private boolean ghost;
  
  public Tetromino(TetrominoType type, float x, float y, boolean ghost)
  {
    this(type, x, y, ghost, DEFAULT_INIT_STATE);
  }
  
  public Tetromino(TetrominoType type, float x, float y, boolean ghost, int state)
  {
    this.type = type;
    refX = x;
    refY = y;    
    this.state = state;
    this.ghost = ghost;
    if(ghost)
    {
      createGhostBlocks();
    }
    else{
      createBlocks();
    }    
    syncBlocks();
  }
  
  private void createBlocks()
  {
    blocks = new Block[TetrominoInfo.BLOCK_COUNT];
    for(int blockIndex = 0; blockIndex < TetrominoInfo.BLOCK_COUNT; blockIndex++)
    {
      blocks[blockIndex] = new Block(type, refX, refY);
    }    
    // notice that the blocks are not at their proper positions yet
  }
  
  private void createGhostBlocks()
  {
    blocks = new Block[TetrominoInfo.BLOCK_COUNT];
    for(int blockIndex = 0; blockIndex < TetrominoInfo.BLOCK_COUNT; blockIndex++)
    {
      blocks[blockIndex] = new GhostBlock(type, refX, refY);
    }    
    // notice that the blocks are not at their proper positions yet
  }
  
  /**
   * Renders all Blocks in this Tetromino.
   * Tetrominos in game are rendered by Board.
   * This method is to intended to be used for Next-Tetromino.
   * 
   * @param gameContainer Game Container.
   * @param graphics Graphics context.
   */
  public void render(GameContainer gameContainer, Graphics graphics)
  {
    for(int blockIndex = 0; blockIndex < TetrominoInfo.BLOCK_COUNT; blockIndex++)
    {
      blocks[blockIndex].render(gameContainer, graphics);
    }    
  }
  
  /**
   * Returns reference X coordinate.
   * @return Reference X coordinate.
   */
  public float getX()
  {
    return refX;
  }
  
  /**
   * Returns reference Y coordinate.
   * @return Reference Y coordinate.
   */
  public float getY()
  {
    return refY;
  }
  
  /**
   * Returns reference to Block by index.
   * @return Reference to Block by index.
   */
  public Block getBlock(int index)
  {
    return blocks[index];
  }
  
  /**
   * Returns true if that Block is a part of this Tetromino.
   * 
   * @param block Block in question.
   * @return True if that Block is a part of this Tetromino.
   */
  public boolean hasBlock(Block block)
  {
    // just test by reference is sufficient
    for(int blockIndex = 0; blockIndex < TetrominoInfo.BLOCK_COUNT; blockIndex++)
    {
      if(block == blocks[blockIndex])
      {
        return true;
      }
    }
    return false;
  }
  
  /**
   * Moves this Tetromino to the SpawnPoint in Waiting Room (above Game).
   */
  public void moveToSpawn(Block[][] grid)
  {
    Point spawnPoint = TetrominoInfo.getSpawnPoint(type);
    float spawnX = spawnPoint.getX();
    float spawnY = spawnPoint.getY();
    
    refX = Offsets.GAME_X + spawnX*Board.BLOCK_SIZE;
    refY = Offsets.GAME_Y + (spawnY - Board.HEIGHT_WAITING)*Board.BLOCK_SIZE;    
    
    syncBlocks();
    syncGrid(grid);
  }
  
  private void syncBlocks()
  {
    Point[] points = TetrominoInfo.getPoints(type)[state];        
    for(int blockIndex = 0; blockIndex < TetrominoInfo.BLOCK_COUNT; blockIndex++)
    {
      blocks[blockIndex].setPosition(refX + points[blockIndex].getX() * Board.BLOCK_SIZE,
                        refY + points[blockIndex].getY() * Board.BLOCK_SIZE);
    }
  }  
  
  /**
   * Plugs this Block into the Grid.
   * Expected to be called only during move-to-spawn
   * or when rotating or when moving currentRetro over ghostRetro.
   * 
   * @param grid Grid of Blocks from the Board.
   */  
  public void syncGrid(Block[][] grid)
  {        
    for(int blockIndex = 0; blockIndex < TetrominoInfo.BLOCK_COUNT; blockIndex++)
    {
      Block syncBlock = blocks[blockIndex];
      grid[ (int)syncBlock.getGridY() ][ (int)syncBlock.getGridX() ] = syncBlock;
    }
  }
  
  // expected to be called only during rotating
  // or when killing Tetromino
  private void unsyncGrid(Block[][] grid){
    for(int blockIndex = 0; blockIndex < TetrominoInfo.BLOCK_COUNT; blockIndex++)
    {
      Block syncBlock = blocks[blockIndex];
      grid[ (int)syncBlock.getGridY() ][ (int)syncBlock.getGridX() ] = null;
    }
  }
  
  /**
   * Move the Tetromino to the down.
   * Precondition: Board already checked for empty spaces.
   */
  public void moveDown()
  {
    refY += Board.BLOCK_SIZE;
    syncBlocks();
  }
  
  /**
   * Move the Tetromino to the left.
   * Precondition: Board already checked for empty spaces.
   */
  public void moveLeft()
  {
    refX -= Board.BLOCK_SIZE;
    syncBlocks();
  }
  
  /**
   * Move the Tetromino to the right.
   * Precondition: Board already checked for empty spaces.
   */
  public void moveRight()
  {
    refX += Board.BLOCK_SIZE;
    syncBlocks();
  }
  
  /**
   * Rotates the Tetromino counter-clockwise.
   * 
   * @param grid Grid of Blocks from the Board.
   * @param board Connection to Board.
   */
  public void rotateLeft(Block[][] grid, Board board)
  {
    unsyncGrid(grid);    
    if( canRotateLeft(grid, board))
    {    
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
  public boolean canRotateLeft(Block[][] grid, Board board)
  {
    int futureState = (state + 3)%4;
    return canRotate(grid, futureState, board);
  }
  
  private boolean canRotate(Block[][] grid, int futureState, Board board)
  {
    Point[] points = TetrominoInfo.getPoints(type)[futureState];    
    
    for(int blockIndex = 0; blockIndex < TetrominoInfo.BLOCK_COUNT; blockIndex++)
    {
      int gridX = (int)( (refX - Offsets.GAME_X)/Board.BLOCK_SIZE + points[blockIndex].getX() );
      int gridY = (int)( (refY - Offsets.GAME_Y)/Board.BLOCK_SIZE + points[blockIndex].getY() + Board.HEIGHT_WAITING );
      if( outOfBounds(gridX, gridY) ){
        return false;
      }
      Block targetBlock = grid[gridY][gridX];
      if( board.unpathableBlock(targetBlock) ){
        return false;
      }
    }        
    return true;
  }
  
  private boolean outOfBounds(int gridX, int gridY)
  {
    return gridX < 0 || gridX >= Board.WIDTH || gridY < 0 || gridY >= Board.HEIGHT;
  }    
  
  /**
   * Rotates the Tetromino clockwise.
   * 
   * @param grid Grid of Blocks from the Board.
   * @param board Connection to Board.
   */
  public void rotateRight(Block[][] grid, Board board)
  {
    unsyncGrid(grid);
    if( canRotateRight(grid, board))
    {
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
  public boolean canRotateRight(Block[][] grid, Board board)
  {
    int futureState = (state + 1)%4;
    return canRotate(grid, futureState, board);
  }
  
  /**
   * Returns the orientation state of this Tetromino.
   * 
   * @return The orientation state of this Tetromino.
   */
  public int getState()
  {
    return state;
  }
  
  /**
   * Returns the type of this Tetromino.
   * 
   * @return The type of this Tetromino.
   */
  public TetrominoType getType()
  {
    return type;
  }
  
  /**
   * Kills this Tetromino, removing from Grid permanently.
   * Expected to be used with only Ghost Tetromino.
   * 
   * @param grid Grid of Blocks from the Board.
   */
  public void kill(Block[][] grid)
  {
    unsyncGrid(grid);
    for(int blockIndex = 0; blockIndex < TetrominoInfo.BLOCK_COUNT; blockIndex++)
    {
      blocks[blockIndex] = null;
    }
    blocks = null;
  }
  
  @Override
  public String toString()
  {
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
