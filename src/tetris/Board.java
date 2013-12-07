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
   * @param gc Game Container.
   */
  public Board(GameContainer gc)
  {    
    bgm.loop();
    musicVolume = DEFAULT_MUSIC_VOLUME;
    bgm.setVolume( DEFAULT_MUSIC_VOLUME );
    setupControl(gc);
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

  private void setupControl(GameContainer gc)
  {
    Input input = gc.getInput();
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
    nextTypes = new ArrayDeque<>(8);
    scoreKeeper = new ScoreKeeper();
    clearCounter = 0;
    isDefeat = false;
    selectNextTetro();
  }

  private void spawnTetromino()
  {
    currentTetro = nextTetro;
    moveNewTetromino();
    summonGhostTetromino(0);
    selectNextTetro();
  }

  /**
   * Wipes all Blocks at Ghost Tetro's position.
   */
  public void killGhostTetro()
  {
    if(ghostTetro != null){
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
   * @param state Orientation state of Tetromino.
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

  private void selectNextTetro(){
    TetrominoType type = getNextTetroType();
    Point spawnPoint = TetrominoInfo.getSpawnPoint( type );
    float spawnX = spawnPoint.getX();
    float spawnY = spawnPoint.getY();

    nextTetro = new Tetromino( type,
            Offsets.NEXT_TETRO_X + (spawnX-2)*BLOCK_SIZE,
            Offsets.NEXT_TETRO_Y + (spawnY-1)*BLOCK_SIZE,
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
  private void thud()
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
    Block tempBlock;
    // defeat happens if any non-ghost Block crosses into the Waiting Room
    for(int col = 0; col < WIDTH; col++)
    {
      tempBlock = grid[HEIGHT_WAITING - 1][col];
      if(tempBlock != null && !(tempBlock instanceof GhostBlock) )
      {
        defeat();
        break;
      }
    }
  }

  private void defeat(){
    isDefeat = true;
    currentTetro = null; // remove control from Player
  }

  private void attemptClearRows(){
    boolean didClearRow;
    ArrayDeque<Integer> clearedRows = new ArrayDeque<>(5);
    int row = HEIGHT-1;

    while(row >= HEIGHT_WAITING){

      didClearRow = true;
      for(int col = 0; col < WIDTH; col++){
        if(grid[row][col] == null){
          didClearRow = false;
          break;
        }
      }
      if(didClearRow){
        clearedRows.addFirst(row);
      }
      row--;
    }

    if(clearedRows.size() > 0){
      clearRows( clearedRows );
      scoreKeeper.clearedRows( clearedRows.size() );
      clearCounter += clearedRows.size();
      if(clearCounter >= CLEARS_PER_LEVEL){
        int levelIncrease = clearCounter/CLEARS_PER_LEVEL;
        scoreKeeper.levelUp( levelIncrease );
        lockDelay = Math.max(MIN_LOCK_DELAY, lockDelay - LOCK_DELAY_DECREMENT_PER_LEVEL * levelIncrease);
        clearCounter %= CLEARS_PER_LEVEL;
      }
    }
  }

  private void clearRows(ArrayDeque<Integer> clearedRows){
    // Blocks far above can get removed several times
    // but the optimal algorithm is not worth the time right now
    for(Integer row : clearedRows){
      dropDownRowsAt(row);
    }
  }

  private void dropDownRowsAt(int startRow){
    // would be much more efficient if we could
    // store the max height of the Tetris tower
    // and only loop that much
    Block blockRowTo[], blockRowFrom[], temp;

    for(int row = startRow; row >= 1; row--){
      blockRowTo = grid[row];
      blockRowFrom = grid[row - 1];
      for(int col = 0; col < WIDTH; col++){
        blockRowTo[col] = blockRowFrom[col];

        temp = blockRowTo[col];
        if(temp != null){
          temp.setPosition( temp.getX(), temp.getY() + Board.BLOCK_SIZE );
        }
      }
    }
  }

  /**
   * Advances the clock.
   *
   * @param gc Game Container.
   * @param dt Time interval.
   */
  public void update(GameContainer gc, int dt){
    updateTicker(gc, dt);
  }

  private void updateTicker(GameContainer gc, int dt){
    lockCounter += dt;
    if(lockCounter >= lockDelay){
      lockCounter = 0;
      tick(gc);
    }    
  }
    
  private void tick(GameContainer gc){
    if(debugMode){
      System.out.println("Tick!");
    }
    if(isDefeat){
      return;
    }
    if(currentTetro == null){
      spawnTetromino();
    }else if( canMoveDown() ){
      moveDownWithoutCheck();
    }else{
      thud();
    }    
  }
    
  /**
   * Renders the Tetrominos and the game field.
   * @param gc Game Container.
   * @param g Graphics context.
   */
  public void render(GameContainer gc, Graphics g){
    renderGameField(gc, g);
    renderNextTetroField(gc, g);
    renderTetromino(gc, g);
    renderGameFieldBorder(gc, g);
    scoreKeeper.render(gc, g);

    if(isDefeat){
      renderGameOver(gc, g);
    }
    if(debugMode){
      renderMouse(gc, g);
      renderLockDelay(gc, g);
    }
  }

  private void renderGameField(GameContainer gc, Graphics g){
    g.setColor( GAME_BACKGROUND );
    g.fillRect( Offsets.GAME_X, Offsets.GAME_Y, WIDTH * BLOCK_SIZE, HEIGHT_GAME * BLOCK_SIZE );
    renderGameWires(gc, g);
  }

  private void renderGameWires(GameContainer gc, Graphics g){
    float temp;
    g.setColor( WIRE_COLOR );
    for(int row = 1; row < HEIGHT_GAME; row++){
      temp = Offsets.GAME_Y + row*BLOCK_SIZE;
      g.drawLine( Offsets.GAME_X, temp, Offsets.GAME_X + WIDTH*BLOCK_SIZE, temp);
    }
    for(int col = 1; col < WIDTH; col++){
      temp = Offsets.GAME_X + col*BLOCK_SIZE;
      g.drawLine( temp, Offsets.GAME_Y, temp, Offsets.GAME_Y + HEIGHT_GAME*BLOCK_SIZE);
    }
  }

  private void renderGameFieldBorder(GameContainer gc, Graphics g){
    g.setColor( GAME_BORDER );
    g.drawRect( Offsets.GAME_X, Offsets.GAME_Y, WIDTH * BLOCK_SIZE, HEIGHT_GAME * BLOCK_SIZE );
  }

  private void renderNextTetroField(GameContainer gc, Graphics g){
    g.setColor( NEXT_TETRO_BACKGROUND );
    g.fillRect( Offsets.NEXT_TETRO_X, Offsets.GAME_Y, NEXT_TETRO_SIZE, NEXT_TETRO_SIZE );
    renderNextTetroWires(gc, g);
    g.setColor( NEXT_TETRO_BORDER );
    g.drawRect( Offsets.NEXT_TETRO_X, Offsets.GAME_Y, NEXT_TETRO_SIZE, NEXT_TETRO_SIZE );
  }

  private void renderNextTetroWires(GameContainer gc, Graphics g){
    float temp;
    g.setColor( WIRE_COLOR );
    for(int row = 1; row < NEXT_TETRO_PIXELS; row++){
      temp = Offsets.NEXT_TETRO_Y + row*BLOCK_SIZE;
      g.drawLine( Offsets.NEXT_TETRO_X, temp, Offsets.NEXT_TETRO_X + NEXT_TETRO_SIZE, temp);
    }
    for(int col = 1; col < NEXT_TETRO_PIXELS; col++){
      temp = Offsets.NEXT_TETRO_X + col*BLOCK_SIZE;
      g.drawLine( temp, Offsets.NEXT_TETRO_Y, temp, Offsets.NEXT_TETRO_Y + NEXT_TETRO_SIZE);
    }
  }

  private void renderTetromino(GameContainer gc, Graphics g){
    nextTetro.render(gc, g);

    for(int row = HEIGHT_WAITING; row < HEIGHT; row++){
      for(int col = 0; col < WIDTH; col++){
        Block temp = grid[row][col];
        if(temp != null){
          temp.render(gc, g);
        }
      }
    }

  }

  private void renderGameOver(GameContainer gc, Graphics g){
    g.setColor( Color.white);
    g.drawString("GAME OVER !", Offsets.GAMEOVER_X, Offsets.GAMEOVER_Y);
  }

  private void renderMouse(GameContainer gc, Graphics g){
    Input input = gc.getInput();
    g.setColor( Color.white );
    g.drawString("Mouse: " + input.getMouseX() + ", " + input.getMouseY(), Offsets.MOUSE_X, Offsets.MOUSE_Y);
  }

  private void renderLockDelay(GameContainer gc, Graphics g){
    g.setColor( Color.white );
    g.drawString("Lock Delay: " + lockDelay, Offsets.MOUSE_X, Offsets.MOUSE_Y + Offsets.NEWLINE);
  }

  /**
   * Move the Tetromino to the down.
   */
  public void moveDown(){
    if(! canMoveDown()){
      return;
    }
    moveDownWithoutCheck();
  }

  private void moveDownWithoutCheck(){
    moveDownWithoutCheck(currentTetro);
  }

  private void moveDownWithoutCheck(Tetromino tetromino){
    
    for(int blockIndex = 0; blockIndex < TetrominoInfo.BLOCK_COUNT; blockIndex++)
    {
      Block tempBlock = tetromino.getBlock( blockIndex );
      grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() ] = null;
    }
    
    // NOTE: This separation of loops is intentional, to prevent overwriting.
    for(int blockIndex = 0; blockIndex < TetrominoInfo.BLOCK_COUNT; blockIndex++)
    {
      Block tempBlock = tetromino.getBlock( blockIndex );
      grid[ (int)tempBlock.getGridY() +1 ][ (int)tempBlock.getGridX() ] = tempBlock;
    }

    tetromino.moveDown();
  }

  /**
   * While can still move down, do move down.
   */
  public void hardDrop(){
    while(canMoveDown()){
      moveDownWithoutCheck();
    }
    lockCounter = 0;
    thud();
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

  private boolean canMoveDown(Tetromino tetromino){
    if(tetromino == null){
      return false;
    }

    for(int blockIndex = 0; blockIndex < TetrominoInfo.BLOCK_COUNT; blockIndex++)
    {
      Block tempBlock = tetromino.getBlock(blockIndex);
      int targetRow = (int)tempBlock.getGridY() +1;
      if(targetRow >= HEIGHT)
      {
        return false;
      }
      Block targetBlock = grid[ (int)tempBlock.getGridY() +1 ][ (int)tempBlock.getGridX() ];
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
      Block tempBlock = currentTetro.getBlock( blockIndex );
      grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() ] = null;
    }
    
    // NOTE: This separation of loops is intentional, to prevent overwriting.
    for(int blockIndex = 0; blockIndex < TetrominoInfo.BLOCK_COUNT; blockIndex++)
    {
      Block tempBlock = currentTetro.getBlock( blockIndex );
      grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() -1 ] = tempBlock;
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
      Block tempBlock = currentTetro.getBlock(blockIndex);
      int targetCol = (int)tempBlock.getGridX() -1;
      if(targetCol < 0)
      {
        return false;
      }
      Block targetBlock = grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() -1 ];
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
      Block tempBlock = currentTetro.getBlock( blockIndex );
      grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() ] = null;
    }
    
    // NOTE: This separation of loops is intentional, to prevent overwriting.
    for(int blockIndex = 0; blockIndex < TetrominoInfo.BLOCK_COUNT; blockIndex++)
    {
      Block tempBlock = currentTetro.getBlock( blockIndex );
      grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() +1 ] = tempBlock;
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
      Block tempBlock = currentTetro.getBlock(blockIndex);
      int targetCol = (int)tempBlock.getGridX() +1;
      if(targetCol >= WIDTH)
      {
        return false;
      }
      Block targetBlock = grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() +1 ];
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
