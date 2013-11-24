package tetris;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Color;
import org.newdawn.slick.command.InputProvider;
import org.newdawn.slick.geom.Point;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Defines the board area where Blocks are contained.
 * 
 * @author Daniel Rolandi
 */
public class Board {
  public static final int WIDTH = 10;
  private static final int HEIGHT = 26; // includes Waiting Room
  public static final int HEIGHT_WAITING = 6; // Waiting Room height
  private static final int HEIGHT_GAME = HEIGHT - HEIGHT_WAITING;
  
  private static final Color GAME_BACKGROUND = new Color(0, 0, 0);
  private static final Color GAME_BORDER = new Color(255, 255, 255);
  
  public static final int BLOCK_SIZE = 20; // pixels      
  public static final float NEXT_TETRO_SIZE = 6 * BLOCK_SIZE;
  private static final Color NEXT_TETRO_BACKGROUND = GAME_BACKGROUND;
  private static final Color NEXT_TETRO_BORDER = GAME_BORDER;
  
  private static final int BASE_LOCK_DELAY = 1000;
  private static final int LOCK_DELAY_DECREMENT_PER_LEVEL = 50;
  private static final int CLEARS_PER_LEVEL = 4;
  
  private static int lockDelay; // milliseconds
  private int lockCounter; // milliseconds
  
  public boolean debugMode = false;
  
  private GameContainer gc;
  private Block[][] grid;
  private Tetromino currentTetro;
  private Tetromino nextTetro;
  private ArrayDeque<TetrominoType> nextTypes;
  private ScoreKeeper scoreKeeper;
  private boolean isDefeat;
  private int clearCounter;
  
  
  /**
   * Inits the Board (includes game field and Next-Tetromino).
   * Also inits the control and its listener.
   * 
   * @param gc Game Container.
   */
  public Board(GameContainer gc){
    this.gc = gc;    
    setupControl();
    newGame();
  }
  
  private void setupControl(){
    InputProvider provider = new InputProvider( gc.getInput() );   
    provider.addListener( new Control(this) );
    
    provider.bindCommand( Commands.moveLeftKey, Commands.moveLeft);
    provider.bindCommand( Commands.moveRightKey, Commands.moveRight);
    provider.bindCommand( Commands.moveUpKey, Commands.moveUp);
    provider.bindCommand( Commands.moveDownKey, Commands.moveDown);
    
    provider.bindCommand( Commands.hardDropKey, Commands.hardDrop);
    provider.bindCommand( Commands.rotateLeftKey, Commands.rotateLeft);
    provider.bindCommand( Commands.rotateRightKey, Commands.rotateRight);
    
    provider.bindCommand( Commands.newGameKey, Commands.newGame);
    provider.bindCommand( Commands.debugModeKey, Commands.debugMode);
    provider.bindCommand( Commands.dumpGridKey, Commands.dumpGrid);
  }
  
  /**
   * Starts a fresh, new game.
   */
  public final void newGame(){
    grid = new Block[HEIGHT][WIDTH];
    lockDelay = BASE_LOCK_DELAY;
    lockCounter = 0;
    currentTetro = null;
    nextTypes = new ArrayDeque<>(8);
    scoreKeeper = new ScoreKeeper();
    clearCounter = 0;
    isDefeat = false;
    selectNextTetro();
    
  }  
  
  private void spawnTetromino(){
    currentTetro = nextTetro;
    moveNewTetromino();
    selectNextTetro();
  }
  
  // PRECONDITION: currentTetro pointing to new Tetromino
  private void moveNewTetromino(){
    currentTetro.moveToSpawn(grid);    
  }
  
  private void selectNextTetro(){    
    TetrominoType type = getNextTetroType();
    Point spawnPoint = TetrominoInfo.getSpawnPoint( type );
    float spawnX = spawnPoint.getX();
    float spawnY = spawnPoint.getY();
        
    nextTetro = new Tetromino( type,
            Offsets.NEXT_TETRO_X + (spawnX-2)*BLOCK_SIZE,
            Offsets.NEXT_TETRO_Y + (spawnY-1)*BLOCK_SIZE );
  }
  
  private TetrominoType getNextTetroType(){
    if( nextTypes.isEmpty() ){
      refillNextTypes();
    }
    return nextTypes.removeLast();    
  }
  
  private void refillNextTypes(){
    ArrayList<TetrominoType> newBag = new ArrayList<>(7);
    newBag.addAll(Arrays.asList(TetrominoType.values()));
    Collections.shuffle(newBag);
    for(TetrominoType type : newBag){
      nextTypes.addLast(type);
    }
  }
  
  /**
   * Ticks the Tetris clock for locking.
   * 
   * @param gc 
   */
  public void tick(GameContainer gc){
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
    // if not possible, call thud()
  }
  
  // current playing Tetromino reaches stop
  private void thud(){
    checkDefeat();
    if(isDefeat){
      return;
    }
    attemptClearRows();
    spawnTetromino();    
  }
  
  private void checkDefeat(){
    // defeat happens if any Block crosses into the Waiting Room
    for(int col = 0; col < WIDTH; col++){
      if(grid[HEIGHT_WAITING - 1][col] != null){
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
        scoreKeeper.levelUp( clearCounter/CLEARS_PER_LEVEL );
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
  
  /**
   * Renders the Tetrominos and the game field.
   * @param gc Game Container.
   * @param g Graphics context.
   */
  public void render(GameContainer gc, Graphics g){
    renderGameField(gc, g);
    renderNextTetroField(gc, g);
    renderTetromino(gc, g);
    scoreKeeper.render(gc, g);
    
    if(isDefeat){
      renderGameOver(gc, g);
    }
    if(debugMode){
      renderMouse(gc, g);
    }
  }    
      
  private void renderGameField(GameContainer gc, Graphics g){
    g.setColor( GAME_BACKGROUND );
    g.fillRect( Offsets.GAME_X, Offsets.GAME_Y, WIDTH * BLOCK_SIZE, HEIGHT_GAME * BLOCK_SIZE );
    g.setColor( GAME_BORDER );
    g.drawRect( Offsets.GAME_X, Offsets.GAME_Y, WIDTH * BLOCK_SIZE, HEIGHT_GAME * BLOCK_SIZE );
  }    
  
  private void renderNextTetroField(GameContainer gc, Graphics g){
    g.setColor( NEXT_TETRO_BACKGROUND );
    g.fillRect( Offsets.NEXT_TETRO_X, Offsets.GAME_Y, NEXT_TETRO_SIZE, NEXT_TETRO_SIZE );
    g.setColor( NEXT_TETRO_BORDER );
    g.drawRect( Offsets.NEXT_TETRO_X, Offsets.GAME_Y, NEXT_TETRO_SIZE, NEXT_TETRO_SIZE );
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
    g.setColor( Color.white);
    g.drawString(input.getMouseX() + ", " + input.getMouseY(), Offsets.MOUSE_X, Offsets.MOUSE_Y);
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
    Block tempBlock = currentTetro.getBlock(0);
    grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() ] = null;
    tempBlock = currentTetro.getBlock(1);
    grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() ] = null;
    tempBlock = currentTetro.getBlock(2);
    grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() ] = null;
    tempBlock = currentTetro.getBlock(3);
    grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() ] = null;
    
    tempBlock = currentTetro.getBlock(0);
    grid[ (int)tempBlock.getGridY() +1 ][ (int)tempBlock.getGridX() ] = tempBlock;    
    tempBlock = currentTetro.getBlock(1);    
    grid[ (int)tempBlock.getGridY() +1 ][ (int)tempBlock.getGridX() ] = tempBlock;    
    tempBlock = currentTetro.getBlock(2);    
    grid[ (int)tempBlock.getGridY() +1 ][ (int)tempBlock.getGridX() ] = tempBlock;    
    tempBlock = currentTetro.getBlock(3);    
    grid[ (int)tempBlock.getGridY() +1 ][ (int)tempBlock.getGridX() ] = tempBlock;
    
    currentTetro.moveDown();
  }
  
  /**
   * While can still move down, do move down.
   */
  public void hardDrop(){
    while(canMoveDown()){
      moveDownWithoutCheck();
    }
  }
  
  private boolean canMoveDown(){
    if(currentTetro == null){
      return false;
    }
    
    // loop unrolling for performance
    Block tempBlock = currentTetro.getBlock(0);
    int targetRow = (int)tempBlock.getGridY() +1;
    if(targetRow >= HEIGHT){
      return false;
    }
    Block targetBlock = grid[ (int)tempBlock.getGridY() +1 ][ (int)tempBlock.getGridX() ];
    if(targetBlock != null && !currentTetro.hasBlock(targetBlock) ){
      return false;
    }
    
    tempBlock = currentTetro.getBlock(1);
    targetRow = (int)tempBlock.getGridY() +1;
    if(targetRow >= HEIGHT){
      return false;
    }
    targetBlock = grid[ (int)tempBlock.getGridY() +1 ][ (int)tempBlock.getGridX() ];
    if(targetBlock != null && !currentTetro.hasBlock(targetBlock) ){
      return false;
    }
    
    tempBlock = currentTetro.getBlock(2);
    targetRow = (int)tempBlock.getGridY() +1;
    if(targetRow >= HEIGHT){
      return false;
    }
    targetBlock = grid[ (int)tempBlock.getGridY() +1 ][ (int)tempBlock.getGridX() ];
    if(targetBlock != null && !currentTetro.hasBlock(targetBlock) ){
      return false;
    }
    
    tempBlock = currentTetro.getBlock(3);
    targetRow = (int)tempBlock.getGridY() +1;
    if(targetRow >= HEIGHT){
      return false;
    }
    targetBlock = grid[ (int)tempBlock.getGridY() +1 ][ (int)tempBlock.getGridX() ];
    if(targetBlock != null && !currentTetro.hasBlock(targetBlock) ){
      return false;
    }
    
    return true;
  }
  
  /**
   * Move the Tetromino to the left.
   */
  public void moveLeft(){
    if(! canMoveLeft()){
      return;
    }
    
    Block tempBlock = currentTetro.getBlock(0);
    grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() ] = null;
    tempBlock = currentTetro.getBlock(1);
    grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() ] = null;
    tempBlock = currentTetro.getBlock(2);
    grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() ] = null;
    tempBlock = currentTetro.getBlock(3);
    grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() ] = null;
    
    tempBlock = currentTetro.getBlock(0);
    grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() -1 ] = tempBlock;    
    tempBlock = currentTetro.getBlock(1);    
    grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() -1 ] = tempBlock;    
    tempBlock = currentTetro.getBlock(2);    
    grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() -1 ] = tempBlock;    
    tempBlock = currentTetro.getBlock(3);    
    grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() -1 ] = tempBlock;
    
    currentTetro.moveLeft();
  }
  
  private boolean canMoveLeft(){
    if(currentTetro == null){
      return false;
    }
    
    // loop unrolling for performance
    Block tempBlock = currentTetro.getBlock(0);
    int targetCol = (int)tempBlock.getGridX() -1;
    if(targetCol < 0){
      return false;
    }
    Block targetBlock = grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() -1 ];
    if(targetBlock != null && !currentTetro.hasBlock(targetBlock) ){
      return false;
    }
    
    tempBlock = currentTetro.getBlock(1);
    targetCol = (int)tempBlock.getGridX() -1;
    if(targetCol < 0){
      return false;
    }
    targetBlock = grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() -1 ];
    if(targetBlock != null && !currentTetro.hasBlock(targetBlock) ){
      return false;
    }
    
    tempBlock = currentTetro.getBlock(2);
    targetCol = (int)tempBlock.getGridX() -1;
    if(targetCol < 0){
      return false;
    }
    targetBlock = grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() -1 ];
    if(targetBlock != null && !currentTetro.hasBlock(targetBlock) ){
      return false;
    }
    
    tempBlock = currentTetro.getBlock(3);
    targetCol = (int)tempBlock.getGridX() -1;
    if(targetCol < 0){
      return false;
    }
    targetBlock = grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() -1 ];
    if(targetBlock != null && !currentTetro.hasBlock(targetBlock) ){
      return false;
    }
    
    return true;
  }
  
  /**
   * Move the Tetromino to the right.
   */
  public void moveRight(){    
    if(! canMoveRight()){
      return;
    }
    
    Block tempBlock = currentTetro.getBlock(0);
    grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() ] = null;
    tempBlock = currentTetro.getBlock(1);
    grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() ] = null;
    tempBlock = currentTetro.getBlock(2);
    grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() ] = null;
    tempBlock = currentTetro.getBlock(3);
    grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() ] = null;
    
    tempBlock = currentTetro.getBlock(0);
    grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() +1 ] = tempBlock;    
    tempBlock = currentTetro.getBlock(1);    
    grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() +1 ] = tempBlock;    
    tempBlock = currentTetro.getBlock(2);    
    grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() +1 ] = tempBlock;    
    tempBlock = currentTetro.getBlock(3);    
    grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() +1 ] = tempBlock;
    
    currentTetro.moveRight();
  }
  
  private boolean canMoveRight(){
    if(currentTetro == null){
      return false;
    }
    
    // loop unrolling for performance
    Block tempBlock = currentTetro.getBlock(0);
    int targetCol = (int)tempBlock.getGridX() +1;
    if(targetCol >= WIDTH){
      return false;
    }
    Block targetBlock = grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() +1 ];
    if(targetBlock != null && !currentTetro.hasBlock(targetBlock) ){
      return false;
    }
    
    tempBlock = currentTetro.getBlock(1);
    targetCol = (int)tempBlock.getGridX() +1;
    if(targetCol >= WIDTH){
      return false;
    }
    targetBlock = grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() +1 ];
    if(targetBlock != null && !currentTetro.hasBlock(targetBlock) ){
      return false;
    }
    
    tempBlock = currentTetro.getBlock(2);
    targetCol = (int)tempBlock.getGridX() +1;
    if(targetCol >= WIDTH){
      return false;
    }
    targetBlock = grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() +1 ];
    if(targetBlock != null && !currentTetro.hasBlock(targetBlock) ){
      return false;
    }
    
    tempBlock = currentTetro.getBlock(3);
    targetCol = (int)tempBlock.getGridX() +1;
    if(targetCol >= WIDTH){
      return false;
    }
    targetBlock = grid[ (int)tempBlock.getGridY() ][ (int)tempBlock.getGridX() +1 ];
    if(targetBlock != null && !currentTetro.hasBlock(targetBlock) ){
      return false;
    }
    
    return true;
  }
  
  /**
   * Dumps the contents of the grid for debugging.
   */
  public void dumpGrid(){
    for(int row = 0; row < HEIGHT; row++){
      System.out.print(row + "\t");
      for(int col= 0; col < WIDTH;col++){
        System.out.print( ( grid[row][col] == null) ? "0" : "#" );
      }
      System.out.println();
    }
  }
  
}
