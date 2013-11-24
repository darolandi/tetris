package tetris;

import org.newdawn.slick.GameContainer;
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
  private static final int HEIGHT_WAITING = 6; // Waiting Room height
  private static final int HEIGHT_GAME = HEIGHT - HEIGHT_WAITING;
  
  private static final float GAME_OFFSETX = 40.0f;
  private static final float GAME_OFFSETY = 40.0f;
  private static final Color GAME_BACKGROUND = new Color(0, 0, 0);
  private static final Color GAME_BORDER = new Color(255, 255, 255);
  
  public static final int BLOCK_SIZE = 20; // pixels
  
  private static final float NEXT_TETRO_OFFSETX = GAME_OFFSETX*2 + WIDTH*BLOCK_SIZE;
  private static final float NEXT_TETRO_OFFSETY = GAME_OFFSETY;
  private static final Color NEXT_TETRO_BACKGROUND = GAME_BACKGROUND;
  private static final Color NEXT_TETRO_BORDER = GAME_BORDER;    
  
  private GameContainer gc;
  private Block[][] grid;
  private Tetromino currentTetromino;
  private Tetromino nextTetromino;
  
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
    currentTetromino = null;
    selectNextTetromino();
  }  
  
  private void spawnTetromino(){
    currentTetromino = nextTetromino;
    moveNewTetromino();
    selectNextTetromino();
  }
  
  // PRECONDITION: currentTetromino pointing to new Tetromino
  private void moveNewTetromino(){
    currentTetromino.moveToSpawn();
  }
  
  private void selectNextTetromino(){
    TetrominoInfo type = TetrominoInfo.getRandom();
    Point spawnPoint = TetrominoInfo.getSpawnPoint( type );
    float spawnX = spawnPoint.getX();
    float spawnY = spawnPoint.getY();
    
    nextTetromino = new Tetromino( type,
            NEXT_TETRO_OFFSETX + spawnX*BLOCK_SIZE,
            NEXT_TETRO_OFFSETY + spawnY*BLOCK_SIZE );
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
   * @param gc Game Container.
   * @param g Graphics context.
   */
  public void render(GameContainer gc, Graphics g){
    renderGameField(gc, g);    
//    renderTetromino(gc, g);
    renderNextTetrominoField(gc, g);
  }    
      
  private void renderGameField(GameContainer gc, Graphics g){
    g.setColor( GAME_BACKGROUND );
    g.fillRect( GAME_OFFSETX, GAME_OFFSETY, WIDTH * BLOCK_SIZE, HEIGHT_GAME * BLOCK_SIZE );
    g.setColor( GAME_BORDER );
    g.drawRect( GAME_OFFSETX, GAME_OFFSETY, WIDTH * BLOCK_SIZE, HEIGHT_GAME * BLOCK_SIZE );
  }
  
  private void renderNextTetrominoField(GameContainer gc, Graphics g){
    g.setColor( NEXT_TETRO_BACKGROUND );
    g.fillRect( NEXT_TETRO_OFFSETX, GAME_OFFSETY, 6 * BLOCK_SIZE, 6 * BLOCK_SIZE );
    g.setColor( NEXT_TETRO_BORDER );
    g.drawRect( NEXT_TETRO_OFFSETX, GAME_OFFSETY, 6 * BLOCK_SIZE, 6 * BLOCK_SIZE );
  }    
  
  private void renderTetromino(GameContainer gc, Graphics g){
    nextTetromino.render(gc, g);
    
    for(int row = HEIGHT_WAITING; row < HEIGHT; row++){
      Block temp = grid[row][0];
      if(temp != null){
        temp.render(gc, g);
      }
      
    }
  }
  
}
