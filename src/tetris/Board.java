package tetris;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Color;
import org.newdawn.slick.Music;
import org.newdawn.slick.command.InputProvider;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.SlickException;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Defines the board area where Blocks are contained.
 *
 * @author Daniel Rolandi
 */
public class Board 
{
  public static final int WIDTH = 10;
  public static final int HEIGHT = 26; // includes Waiting Room
  public static final int HEIGHT_WAITING = 6; // Waiting Room height
  private static final int HEIGHT_GAME = HEIGHT - HEIGHT_WAITING;

  private static final Color GAME_BACKGROUND = new Color(0, 0, 0);
  private static final Color GAME_BORDER = new Color(255, 255, 255);
  public static final Color WIRE_COLOR = new Color(30, 30, 30);

  public static final int BLOCK_SIZE = 20; // pixels
  private static final int NEXT_TETRO_PIXELS = 6;
  public static final float NEXT_TETRO_SIZE = NEXT_TETRO_PIXELS * BLOCK_SIZE;
  private static final Color NEXT_TETRO_BACKGROUND = GAME_BACKGROUND;
  private static final Color NEXT_TETRO_BORDER = GAME_BORDER;  
  
  private static final Music bgm;
  private static final int DEFAULT_MUSIC_VOLUME = 0; // should be 0 or 1
  static
  {
    try
    {
      bgm = new Music("music/Tetris.ogg");      
    }
    catch(SlickException e)
    {
      throw new IllegalStateException("Could not load music.");
    }
  }
  
  private static final int BASE_LOCK_DELAY = 1000;
  private static final int MIN_LOCK_DELAY = 100;
  private static final int LOCK_DELAY_DECREMENT_PER_LEVEL = 80;
  private static final int CLEARS_PER_LEVEL = 4;

  private static int lockDelay; // milliseconds
  private int lockCounter; // milliseconds  

  public boolean debugMode = false;
  
  private Block[][] grid;
  private Tetromino currentTetro;
  private Tetromino nextTetro;
  private Tetromino ghostTetro;
  private ArrayDeque<TetrominoType> nextTypes;
  private ScoreKeeper scoreKeeper;
  private boolean isDefeat;
  private int clearCounter;
  private int musicVolume;


  /**
   * Inits the Board (includes game field and Next-Tetromino).
   * Also inits the control and its listener.
   *
   * @param gameContainer Game Container.
   */
  public Board(GameContainer gameContainer)
  {    
    bgm.loop();
    musicVolume = DEFAULT_MUSIC_VOLUME;
    bgm.setVolume( DEFAULT_MUSIC_VOLUME );
    setupControl(gameContainer);
    newGame();
    defeat();
  }

  /**
   * Forces game over at this exact movement.
   */
  public void endGame()
  {
    defeat();
  }

  private void setupControl(GameContainer gameContainer)
  {
    Input input = gameContainer.getInput();
    input.enableKeyRepeat();
    InputProvider provider = new InputProvider( input );
    provider.addListener( new Control(this) );

    provider.bindCommand( Commands.moveLeftKey, Commands.moveLeft);
    provider.bindCommand( Commands.moveRightKey, Commands.moveRight);
    provider.bindCommand( Commands.moveDownKey, Commands.moveDown);

    provider.bindCommand( Commands.hardDropKey, Commands.hardDrop);
    provider.bindCommand( Commands.rotateLeftKey, Commands.rotateLeft);
    provider.bindCommand( Commands.rotateRightKey1, Commands.rotateRight);
    provider.bindCommand( Commands.rotateRightKey2, Commands.rotateRight);

    provider.bindCommand( Commands.newGameKey, Commands.newGame);
    provider.bindCommand( Commands.endGameKey, Commands.endGame);
    provider.bindCommand( Commands.debugModeKey, Commands.debugMode);
    provider.bindCommand( Commands.dumpGridKey, Commands.dumpGrid);
    provider.bindCommand( Commands.toggleMusicKey, Commands.toggleMusic );
  }

  /**
   * Starts a fresh, new game.
   */
  public final void newGame()
  {
    grid = new Block[HEIGHT][WIDTH];
    lockDelay = BASE_LOCK_DELAY;
    lockCounter = 0;    
    currentTetro = null;
    ghostTetro = null;
    nextTypes = new ArrayDeque<>(TetrominoInfo.TYPE_COUNT);
    scoreKeeper = new ScoreKeeper();
    clearCounter = 0;
    isDefeat = false;
    selectNextTetro();
  }

  private void spawnTetromino()
  {
    currentTetro = nextTetro;
    moveNewTetromino();
    summonGhostTetromino(Tetromino.DEFAULT_INIT_STATE);
    selectNextTetro();
  }

  /**
   * Wipes all Blocks at Ghost Tetro's position.
   */
  public void killGhostTetro()
  {
    if(ghostTetro != null)
    {
      ghostTetro.kill(grid);
      ghostTetro = null;
    }
  }

  // PRECONDITION: currentTetro pointing to new Tetromino
  private void moveNewTetromino()
  {
    currentTetro.moveToSpawn(grid);
  }

  /**
   * Creates a Ghost Tetromino under the current Tetromino.
   * 
   * @param Staring orientation state.
   */
  public void summonGhostTetromino(int state)
  {
    ghostTetro = new Tetromino( currentTetro.getType(),
            currentTetro.getX(),
            currentTetro.getY(),
            true,
            state);
    hardDropGhost();
  }

  private void selectNextTetro()
  {
    TetrominoType type = getNextTetroType();
    Point spawnPoint = TetrominoInfo.getSpawnPoint( type );
    float spawnX = spawnPoint.getX();
    float spawnY = spawnPoint.getY();

    nextTetro = new Tetromino( type,
            Offsets.NEXT_TETRO_X + (spawnX-Offsets.SPAWN_X)*BLOCK_SIZE,
            Offsets.NEXT_TETRO_Y + (spawnY-Offsets.SPAWN_Y)*BLOCK_SIZE,
            false);
  }

  private TetrominoType getNextTetroType()
  {
    if( nextTypes.isEmpty() )
    {
      refillNextTypes();
    }
    return nextTypes.removeLast();
  }

  private void refillNextTypes()
  {
    ArrayList<TetrominoType> newBag = new ArrayList<>(7);
    newBag.addAll(Arrays.asList(TetrominoType.values()));
    Collections.shuffle(newBag);
    for(TetrominoType type : newBag)
    {
      nextTypes.addLast(type);
    }
  }

  // current playing Tetromino reaches stop
  private void tetroReachesFloor()
  {
    checkDefeat();
    if(isDefeat)
    {
      return;
    }
    attemptClearRows();
    spawnTetromino();
  }

  private void checkDefeat()
  {    
    // defeat happens if any non-ghost Block crosses into the Waiting Room
    for(int col = 0; col < WIDTH; col++)
    {
      Block testBlock = grid[HEIGHT_WAITING - 1][col];
      if(testBlock != null && !(testBlock instanceof GhostBlock) )
      {
        defeat();
        break;
      }
    }
  }

  private void defeat()
  {
    isDefeat = true;
    disableTetroControl();
  }
  
  private void disableTetroControl()
  {
    currentTetro = null;
  }

  private void attemptClearRows()
  {
    boolean didClearRow;
    ArrayDeque<Integer> clearedRows = new ArrayDeque<>(TetrominoInfo.BLOCK_COUNT);
    
    int row = HEIGHT-1;
    while(row >= HEIGHT_WAITING)
    {

      didClearRow = true;
      for(int col = 0; col < WIDTH; col++)
      {
        if(grid[row][col] == null)
        {
          didClearRow = false;
          break;
        }
      }
      if(didClearRow)
      {
        clearedRows.addFirst(row);
      }
      row--;
    }

    if(clearedRows.size() > 0)
    {
      clearRows( clearedRows );      
    }
  }

  private void clearRows(ArrayDeque<Integer> clearedRows)
  {
    // Blocks far above can get removed several times
    // but the optimal algorithm is not worth the time right now
    for(Integer row : clearedRows)
    {
      dropDownRowsAt(row);
    }
    
    scoreKeeper.clearedRows( clearedRows.size() );
    clearCounter += clearedRows.size();
    if(clearCounter >= CLEARS_PER_LEVEL)
    {
      int levelIncrease = clearCounter/CLEARS_PER_LEVEL;
      scoreKeeper.levelUp( levelIncrease );
      lockDelay = Math.max(MIN_LOCK_DELAY, lockDelay - LOCK_DELAY_DECREMENT_PER_LEVEL * levelIncrease);
      clearCounter %= CLEARS_PER_LEVEL;
    }
  }

  private void dropDownRowsAt(int startRow)
  {
    /*
    would be much more efficient if we could
    store the max height of the Tetris tower
    and only loop that much    
    */
    for(int row = startRow; row >= 1; row--)
    {
      for(int col = 0; col < WIDTH; col++)
      {
        grid[row][col] = grid[row - 1][col];

        Block movedBlock = grid[row][col];
        if(movedBlock != null)
        {
          movedBlock.setPosition( movedBlock.getX(), movedBlock.getY() + Board.BLOCK_SIZE );
        }
      }
    }
  }

  /**
   * Advances the clock.
   *
   * @param gameContainer Game Container.
   * @param deltaTime Time interval.
   */
  public void update(GameContainer gameContainer, int deltaTime)
  {
    updateTicker(gameContainer, deltaTime);
  }

  private void updateTicker(GameContainer gameContainer, int deltaTime)
  {
    lockCounter += deltaTime;
    if(lockCounter >= lockDelay)
    {
      lockCounter = 0;
      tick(gameContainer);
    }    
  }
    
  private void tick(GameContainer gameContainer)
  {
    if(debugMode)
    {
      System.out.println("Tick!");
    }
    if(isDefeat)
    {
      return;
    }
    if(currentTetro == null)
    {
      spawnTetromino();
    }
    else if( canMoveDown() )
    {
      moveDownWithoutCheck();
    }
    else
    {
      tetroReachesFloor();
    }    
  }
    
  /**
   * Renders the Tetrominos and the game field.
   * @param gameContainer Game Container.
   * @param graphics Graphics context.
   */
  public void render(GameContainer gameContainer, Graphics graphics)
  {
    renderGameField(gameContainer, graphics);
    renderNextTetroField(gameContainer, graphics);
    renderTetromino(gameContainer, graphics);
    renderGameFieldBorder(gameContainer, graphics);
    scoreKeeper.render(gameContainer, graphics);

    if(isDefeat)
    {
      renderGameOver(gameContainer, graphics);
    }
    if(debugMode)
    {
      renderMouse(gameContainer, graphics);
      renderLockDelay(gameContainer, graphics);
    }
  }

  private void renderGameField(GameContainer gameContainer, Graphics graphics)
  {
    graphics.setColor( GAME_BACKGROUND );
    graphics.fillRect( Offsets.GAME_X, Offsets.GAME_Y, WIDTH * BLOCK_SIZE, HEIGHT_GAME * BLOCK_SIZE );
    renderGameWires(gameContainer, graphics);
  }

  private void renderGameWires(GameContainer gameContainer, Graphics graphics)
  {    
    graphics.setColor( WIRE_COLOR );
    for(int row = 1; row < HEIGHT_GAME; row++)
    {
      float wire_row = Offsets.GAME_Y + row*BLOCK_SIZE;
      graphics.drawLine( Offsets.GAME_X, wire_row, Offsets.GAME_X + WIDTH*BLOCK_SIZE, wire_row);
    }
    for(int col = 1; col < WIDTH; col++)
    {
      float wire_col = Offsets.GAME_X + col*BLOCK_SIZE;
      graphics.drawLine( wire_col, Offsets.GAME_Y, wire_col, Offsets.GAME_Y + HEIGHT_GAME*BLOCK_SIZE);
    }
  }

  private void renderGameFieldBorder(GameContainer gameContainer, Graphics graphics)
  {
    graphics.setColor( GAME_BORDER );
    graphics.drawRect( Offsets.GAME_X, Offsets.GAME_Y, WIDTH * BLOCK_SIZE, HEIGHT_GAME * BLOCK_SIZE );
  }

  private void renderNextTetroField(GameContainer gameContainer, Graphics graphics)
  {
    graphics.setColor( NEXT_TETRO_BACKGROUND );
    graphics.fillRect( Offsets.NEXT_TETRO_X, Offsets.GAME_Y, NEXT_TETRO_SIZE, NEXT_TETRO_SIZE );
    renderNextTetroWires(gameContainer, graphics);
    graphics.setColor( NEXT_TETRO_BORDER );
    graphics.drawRect( Offsets.NEXT_TETRO_X, Offsets.GAME_Y, NEXT_TETRO_SIZE, NEXT_TETRO_SIZE );
  }

  private void renderNextTetroWires(GameContainer gameContainer, Graphics graphics)
  {    
    graphics.setColor( WIRE_COLOR );
    for(int row = 1; row < NEXT_TETRO_PIXELS; row++)
    {
      float wire_row = Offsets.NEXT_TETRO_Y + row*BLOCK_SIZE;
      graphics.drawLine( Offsets.NEXT_TETRO_X, wire_row, Offsets.NEXT_TETRO_X + NEXT_TETRO_SIZE, wire_row);
    }
    for(int col = 1; col < NEXT_TETRO_PIXELS; col++)
    {
      float wire_col = Offsets.NEXT_TETRO_X + col*BLOCK_SIZE;
      graphics.drawLine( wire_col, Offsets.NEXT_TETRO_Y, wire_col, Offsets.NEXT_TETRO_Y + NEXT_TETRO_SIZE);
    }
  }

  private void renderTetromino(GameContainer gameContainer, Graphics graphics)
  {
    nextTetro.render(gameContainer, graphics);

    for(int row = HEIGHT_WAITING; row < HEIGHT; row++)
    {
      for(int col = 0; col < WIDTH; col++)
      {
        Block blockToRender = grid[row][col];
        if(blockToRender != null)
        {
          blockToRender.render(gameContainer, graphics);
        }
      }
    }

  }

  private void renderGameOver(GameContainer gameContainer, Graphics graphics)
  {
    graphics.setColor( Color.white);
    graphics.drawString("GAME OVER !", Offsets.GAMEOVER_X, Offsets.GAMEOVER_Y);
  }

  private void renderMouse(GameContainer gameContainer, Graphics graphics)
  {
    Input input = gameContainer.getInput();
    graphics.setColor( Color.white );
    graphics.drawString("Mouse: " + input.getMouseX() + ", " + input.getMouseY(), Offsets.MOUSE_X, Offsets.MOUSE_Y);
  }

  private void renderLockDelay(GameContainer gameContainer, Graphics graphics)
  {
    graphics.setColor( Color.white );
    graphics.drawString("Lock Delay: " + lockDelay, Offsets.MOUSE_X, Offsets.MOUSE_Y + Offsets.NEWLINE);
  }

  /**
   * Move the Tetromino to the down.
   */
  public void moveDown()
  {
    if(! canMoveDown())
    {
      return;
    }
    moveDownWithoutCheck();
  }

  private void moveDownWithoutCheck()
  {
    moveDownWithoutCheck(currentTetro);
  }

  private void moveDownWithoutCheck(Tetromino tetromino)
  {
    
    for(int blockIndex = 0; blockIndex < TetrominoInfo.BLOCK_COUNT; blockIndex++)
    {
      Block oldBlock = tetromino.getBlock( blockIndex );
      grid[ (int)oldBlock.getGridY() ][ (int)oldBlock.getGridX() ] = null;
    }
    
    // NOTE: This separation of loops is intentional, to prevent overwriting.
    for(int blockIndex = 0; blockIndex < TetrominoInfo.BLOCK_COUNT; blockIndex++)
    {
      Block moveBlock = tetromino.getBlock( blockIndex );
      grid[ (int)moveBlock.getGridY() +1 ][ (int)moveBlock.getGridX() ] = moveBlock;
    }

    tetromino.moveDown();
  }

  /**
   * While can still move down, do move down.
   */
  public void hardDrop()
  {
    while(canMoveDown())
    {
      moveDownWithoutCheck();
    }
    lockCounter = 0;
    tetroReachesFloor();
  }

  private void hardDropGhost()
  {
    while(canMoveDown(ghostTetro))
    {
      moveDownWithoutCheck(ghostTetro);
    }
  }

  private void moveTetroOverGhost()
  {
    currentTetro.syncGrid(grid);
  }

  private boolean canMoveDown()
  {
    return canMoveDown(currentTetro);
  }

  private boolean canMoveDown(Tetromino tetromino)
  {
    if(tetromino == null)
    {
      return false;
    }

    for(int blockIndex = 0; blockIndex < TetrominoInfo.BLOCK_COUNT; blockIndex++)
    {
      Block moveBlock = tetromino.getBlock(blockIndex);
      int targetRow = (int)moveBlock.getGridY() +1;
      if(targetRow >= HEIGHT)
      {
        return false;
      }
      Block targetBlock = grid[ (int)moveBlock.getGridY() +1 ][ (int)moveBlock.getGridX() ];
      if( unpathableBlock(targetBlock) )
      {
        return false;
      }
    }    

    return true;
  }

  /**
   * Returns true if that Block cannot be overwritten.
   *
   * @param targetBlock Target Block.
   * @param movingTetro Non-ghost Tetromino trying to move.
   * @return True if that Block cannot be overwritten.
   */
  public boolean unpathableBlock(Block targetBlock)
  {
    return unpathableBlock(targetBlock, currentTetro);
  }

  /**
   * Returns true if that Block cannot be overwritten.
   *
   * @param targetBlock Target Block.
   * @param movingTetro Non-ghost Tetromino trying to move.
   * @return True if that Block cannot be overwritten.
   */
  public boolean unpathableBlock(Block targetBlock, Tetromino movingTetro)
  {
    return targetBlock != null && !(targetBlock instanceof GhostBlock) && !movingTetro.hasBlock(targetBlock);
  }

  /**
   * Move the Tetromino to the left.
   */
  public void moveLeft()
  {
    if(! canMoveLeft())
    {
      return;
    }

    for(int blockIndex = 0; blockIndex < TetrominoInfo.BLOCK_COUNT; blockIndex++)
    {
      Block oldBlock = currentTetro.getBlock( blockIndex );
      grid[ (int)oldBlock.getGridY() ][ (int)oldBlock.getGridX() ] = null;
    }
    
    // NOTE: This separation of loops is intentional, to prevent overwriting.
    for(int blockIndex = 0; blockIndex < TetrominoInfo.BLOCK_COUNT; blockIndex++)
    {
      Block moveBlock = currentTetro.getBlock( blockIndex );
      grid[ (int)moveBlock.getGridY() ][ (int)moveBlock.getGridX() -1 ] = moveBlock;
    }

    currentTetro.moveLeft();
    killGhostTetro();
    summonGhostTetromino(currentTetro.getState());
    moveTetroOverGhost();
  }

  /**
   * Returns true if current Tetromino can move left by 1 step.
   * @return True if current Tetromino can move left by 1 step.
   */
  public boolean canMoveLeft()
  {
    if(currentTetro == null)
    {
      return false;
    }

    for(int blockIndex = 0; blockIndex < TetrominoInfo.BLOCK_COUNT; blockIndex++)
    {
      Block moveBlock = currentTetro.getBlock(blockIndex);
      int targetCol = (int)moveBlock.getGridX() -1;
      if(targetCol < 0)
      {
        return false;
      }
      Block targetBlock = grid[ (int)moveBlock.getGridY() ][ (int)moveBlock.getGridX() -1 ];
      if( unpathableBlock(targetBlock) )
      {
        return false;
      }
    }    

    return true;
  }

  /**
   * Move the Tetromino to the right.
   */
  public void moveRight()
  {
    if(! canMoveRight())
    {
      return;
    }

    for(int blockIndex = 0; blockIndex < TetrominoInfo.BLOCK_COUNT; blockIndex++)
    {
      Block oldBlock = currentTetro.getBlock( blockIndex );
      grid[ (int)oldBlock.getGridY() ][ (int)oldBlock.getGridX() ] = null;
    }
    
    // NOTE: This separation of loops is intentional, to prevent overwriting.
    for(int blockIndex = 0; blockIndex < TetrominoInfo.BLOCK_COUNT; blockIndex++)
    {
      Block moveBlock = currentTetro.getBlock( blockIndex );
      grid[ (int)moveBlock.getGridY() ][ (int)moveBlock.getGridX() +1 ] = moveBlock;
    }
      
    currentTetro.moveRight();
    killGhostTetro();
    summonGhostTetromino(currentTetro.getState());
    moveTetroOverGhost();
  }

  /**
   * Returns true if current Tetromino can move right by 1 step.
   * @return True if current Tetromino can move right by 1 step.
   */
  public boolean canMoveRight()
  {
    if(currentTetro == null)
    {
      return false;
    }
        
    for(int blockIndex = 0; blockIndex < TetrominoInfo.BLOCK_COUNT; blockIndex++)
    {
      Block moveBlock = currentTetro.getBlock(blockIndex);
      int targetCol = (int)moveBlock.getGridX() +1;
      if(targetCol >= WIDTH)
      {
        return false;
      }
      Block targetBlock = grid[ (int)moveBlock.getGridY() ][ (int)moveBlock.getGridX() +1 ];
      if( unpathableBlock(targetBlock) )
      {
        return false;
      }
    }

    return true;
  }

  /**
   * Rotates the Tetromino counter-clockwise.
   * Tetromino deals with the collision checking.
   */
  public void rotateLeft()
  {
    if(currentTetro != null)
    {
      currentTetro.rotateLeft(grid, this);
    }
  }

  /**
   * Rotates the Tetromino clockwise.
   * Tetromino deals with the collision checking.
   */
  public void rotateRight()
  {
    if(currentTetro != null)
    {
      currentTetro.rotateRight(grid, this);
    }
  }

  /**
   * Dumps the contents of the grid for debugging.
   */
  public void dumpGrid()
  {
    for(int row = 0; row < HEIGHT; row++)
    {
      System.out.print(row + "\t");
      for(int col= 0; col < WIDTH;col++)
      {
        System.out.print( ( grid[row][col] == null) ? "0" : "#" );
      }
      System.out.println();
    }
  }

  /**
   * Toggle mute/unmute bgm.
   */
  public void toggleMusic()
  {
    musicVolume ^= 1;
    bgm.setVolume( musicVolume );
  }

}
