package tetris;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Color;
import org.newdawn.slick.command.InputProvider;
import org.newdawn.slick.geom.Point;

/**
 * Defines the board area where Blocks are contained.
 * 
 * @author Daniel Rolandi
 */
public class Board {
  private static final int WIDTH = 10;
  private static final int HEIGHT = 26; // includes Waiting Room
  public static final int HEIGHT_WAITING = 6; // Waiting Room height
  private static final int HEIGHT_GAME = HEIGHT - HEIGHT_WAITING;
  
  public static final float GAME_OFFSETX = 40.0f;
  public static final float GAME_OFFSETY = 40.0f;
  private static final Color GAME_BACKGROUND = new Color(0, 0, 0);
  private static final Color GAME_BORDER = new Color(255, 255, 255);
  
  public static final int BLOCK_SIZE = 20; // pixels
  
  public boolean debugMode = false;
  
  private static final float NEXT_TETRO_OFFSETX = GAME_OFFSETX*2 + WIDTH*BLOCK_SIZE;
  private static final float NEXT_TETRO_OFFSETY = GAME_OFFSETY;
  private static final Color NEXT_TETRO_BACKGROUND = GAME_BACKGROUND;
  private static final Color NEXT_TETRO_BORDER = GAME_BORDER;    
  
  private GameContainer gc;
  private Block[][] grid;
  private Tetromino currentTetro;
  private Tetromino nextTetro;
  
  
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
  
  private void newGame(){
    grid = new Block[HEIGHT][WIDTH];
    currentTetro = null;
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
    TetrominoInfo type = TetrominoInfo.getRandom();
    Point spawnPoint = TetrominoInfo.getSpawnPoint( type );
    float spawnX = spawnPoint.getX();
    float spawnY = spawnPoint.getY();
        
    nextTetro = new Tetromino( type,
            NEXT_TETRO_OFFSETX + (spawnX-2)*BLOCK_SIZE,
            NEXT_TETRO_OFFSETY + (spawnY-1)*BLOCK_SIZE );
  }
  
  public void update(GameContainer gc, int dt){
    
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
    if(currentTetro == null){
      spawnTetromino();
    }else if( canMoveDown() ){
      // notice redundant canMoveDown() call
      moveDown();      
    }else{
      thud();
    }    
    // if not possible, call thud()
  }
  
  // current playing Tetromino reaches floor
  private void thud(){
    // check for clearing
    spawnTetromino();
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
    
    if(debugMode){
      renderMouse(gc, g);
    }
  }    
      
  private void renderGameField(GameContainer gc, Graphics g){
    g.setColor( GAME_BACKGROUND );
    g.fillRect( GAME_OFFSETX, GAME_OFFSETY, WIDTH * BLOCK_SIZE, HEIGHT_GAME * BLOCK_SIZE );
    g.setColor( GAME_BORDER );
    g.drawRect( GAME_OFFSETX, GAME_OFFSETY, WIDTH * BLOCK_SIZE, HEIGHT_GAME * BLOCK_SIZE );
  }    
  
  private void renderNextTetroField(GameContainer gc, Graphics g){
    g.setColor( NEXT_TETRO_BACKGROUND );
    g.fillRect( NEXT_TETRO_OFFSETX, GAME_OFFSETY, 6 * BLOCK_SIZE, 6 * BLOCK_SIZE );
    g.setColor( NEXT_TETRO_BORDER );
    g.drawRect( NEXT_TETRO_OFFSETX, GAME_OFFSETY, 6 * BLOCK_SIZE, 6 * BLOCK_SIZE );
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
  
  private void renderMouse(GameContainer gc, Graphics g){
    Input input = gc.getInput();
    g.setColor( Color.white);
    g.drawString(input.getMouseX() + ", " + input.getMouseY(), 550, 450);
  }
  
  /**
   * Move the Tetromino to the down.
   */
  public void moveDown(){
    if(! canMoveDown()){      
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
    grid[ (int)tempBlock.getGridY() +1 ][ (int)tempBlock.getGridX() ] = tempBlock;    
    tempBlock = currentTetro.getBlock(1);    
    grid[ (int)tempBlock.getGridY() +1 ][ (int)tempBlock.getGridX() ] = tempBlock;    
    tempBlock = currentTetro.getBlock(2);    
    grid[ (int)tempBlock.getGridY() +1 ][ (int)tempBlock.getGridX() ] = tempBlock;    
    tempBlock = currentTetro.getBlock(3);    
    grid[ (int)tempBlock.getGridY() +1 ][ (int)tempBlock.getGridX() ] = tempBlock;
    
    currentTetro.moveDown();
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
