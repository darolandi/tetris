package tetris;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Color;
import org.newdawn.slick.command.InputProvider;

/**
 * Defines the board area where Blocks are contained.
 * 
 * @author Daniel Rolandi
 */
public class Board {
  private static final int WIDTH = 10;
  private static final int HEIGHT = 26; // includes Waiting Room
  private static final int HEIGHT_WAITING = 6; // Waiting Room height
  private static final int HEIGHT_GAME = HEIGHT - HEIGHT_WAITING;
  
  private static final float GAME_OFFSETX = 40.0f;
  private static final float GAME_OFFSETY = 40.0f;
  private static final Color GAME_BACKGROUND = new Color(0, 0, 0);
  private static final Color GAME_BORDER = new Color(255, 255, 255);
  
  public static final int BLOCK_SIZE = 20; // pixels
  
  private GameContainer gc;
  private Block[][] grid;
  private Tetromino currentTetromino;
  private TetrominoType nextTetrominoType;
  
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
  }
  
  private void newGame(){
    grid = new Block[WIDTH][HEIGHT];
    // spawn Tetromino
  }
  
  private void spawnTetrominoInitial(){
    
  }
  
  private void spawnTetromino(){
    
  }
  
  private void selectNextTetromino(){
    
  }
  
  public void update(GameContainer gc, int dt){
    
  }
  
  /**
   * Ticks the Tetris clock for locking.
   * 
   * @param gc 
   */
  public void tick(GameContainer gc){
    // move down playing piece
    // if not possible, call thud()
  }
  
  // current playing Tetromino reaches floor
  private void thud(){
    // check for clearing
    // spawn next Tetromino
  }
  
  /**
   * Renders the Tetrominos and the game field.
   * @param gc
   * @param g 
   */
  public void render(GameContainer gc, Graphics g){
    renderGameField(gc, g);
//    renderTetromino(gc, g);    
  }    
  
  private void renderGameField(GameContainer gc, Graphics g){
    g.setColor( GAME_BACKGROUND );
    g.fillRect( GAME_OFFSETX, GAME_OFFSETY, WIDTH * BLOCK_SIZE, HEIGHT_GAME * BLOCK_SIZE );
    g.setColor( GAME_BORDER );
    g.drawRect( GAME_OFFSETX, GAME_OFFSETY, WIDTH * BLOCK_SIZE, HEIGHT_GAME * BLOCK_SIZE );
  }
  
  private void renderTetromino(GameContainer gc, Graphics g){
    for(int row = HEIGHT_WAITING; row < HEIGHT; row++){
      Block temp = grid[row][0];
      if(temp != null){
        temp.render(gc, g);
      }
      
    }
  }
  
}
