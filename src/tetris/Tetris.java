package tetris;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;

/**
 * Application class for the Tetris game.
 * 
 * @author Daniel Rolandi
 */
public class Tetris extends BasicGame{
  private static final int WINDOW_WIDTH = 640;
  private static final int WINDOW_HEIGHT = 480;
  private static final boolean FULLSCREEN = false;
  private static final Color BACKGROUND = new Color(112, 48, 160);
  
  private static final int LOCK_DELAY = 1000; // milliseconds
  private int lockCounter = 0; // milliseconds
  
  private Board board;
  
  /**
   * Call BasicGame's constructor.
   */
  public Tetris(){
    super("Tetris");    
  }
  
  /**
   * Inits game resources before any update or render.
   * Also prepares the tick counter.
   * @param gc Game Container.
   * @throws SlickException 
   */
  @Override
  public void init(GameContainer gc) throws SlickException{
    gc.setShowFPS(false);
    board = new Board(gc);
    lockCounter = 0 ;
  }
  
  /**
   * Updates the board and the ticker (responsible for locking).
   * 
   * @param gc Game Container.
   * @param dt Interval.
   * @throws SlickException 
   */
  @Override
  public void update(GameContainer gc, int dt) throws SlickException{    
    updateTicker(gc, dt);
  }
  
  private void updateTicker(GameContainer gc, int dt){
    lockCounter += dt;
    if(lockCounter >= LOCK_DELAY){
      lockCounter = 0;
      board.tick(gc);      
    }
  }
  
  /**
   * Renders the game and the board in it.
   * 
   * @param gc Game Container.
   * @param g Graphics context.
   * @throws SlickException 
   */
  @Override
  public void render(GameContainer gc, Graphics g) throws SlickException{
    g.setBackground(BACKGROUND);
    board.render(gc, g);
  }
  
  /**
   * Application method.
   * 
   * @param args Command-line arguments.
   */
  public static void main(String[] args) {
    
    try{
      AppGameContainer appgc;
      
      appgc = new AppGameContainer(new Tetris() );
      appgc.setDisplayMode(WINDOW_WIDTH, WINDOW_HEIGHT, FULLSCREEN);
      appgc.start();
    }catch(SlickException e){
      e.printStackTrace();
    }
        
  }
}
