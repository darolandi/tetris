package tetris;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;

import java.io.File;

/**
 * Application class for the Tetris game.
 * 
 * @author Daniel Rolandi
 */
public class Tetris extends BasicGame{
  private static final int WINDOW_WIDTH = 700;
  private static final int WINDOW_HEIGHT = 480;
  private static final boolean FULLSCREEN = false;
  private static final Color BACKGROUND = new Color(112, 48, 160);    
  
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
  }
  
  /**
   * Updates the board and the ticker (responsible for locking).
   * 
   * @param gc Game Container.
   * @param dt Time interval.
   * @throws SlickException 
   */
  @Override
  public void update(GameContainer gc, int dt) throws SlickException{    
    board.update(gc, dt);
    
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
    renderHelp(gc, g);
  }
  
  private void renderHelp(GameContainer gc, Graphics g){
    g.setColor( Color.white );
    g.drawString("CONTROLS", Offsets.HELP_X, Offsets.HELP_Y);
    g.drawString("left; Move left", Offsets.HELP_X, Offsets.HELP_Y + Offsets.NEWLINE);
    g.drawString("right; Move right", Offsets.HELP_X, Offsets.HELP_Y + 2*Offsets.NEWLINE);
    g.drawString("down; Move down (soft)", Offsets.HELP_X, Offsets.HELP_Y + 3*Offsets.NEWLINE);
    g.drawString("space; Hard drop", Offsets.HELP_X, Offsets.HELP_Y + 4*Offsets.NEWLINE);
    g.drawString("Z; Rotate CCW", Offsets.HELP_X, Offsets.HELP_Y + 5*Offsets.NEWLINE);
    g.drawString("X/up; Rotate CW", Offsets.HELP_X, Offsets.HELP_Y + 6*Offsets.NEWLINE);
    g.drawString("F1; New Game", Offsets.HELP_X, Offsets.HELP_Y + 7*Offsets.NEWLINE);
    g.drawString("F2; End Game", Offsets.HELP_X, Offsets.HELP_Y + 8*Offsets.NEWLINE);
    g.drawString("ESC; Toggle Debug Mode", Offsets.HELP_X, Offsets.HELP_Y + 9*Offsets.NEWLINE);
    g.drawString("G; Dump Grid (debug only)", Offsets.HELP_X, Offsets.HELP_Y + 10*Offsets.NEWLINE);
    g.drawString("M; Toggle mute/unmute BGM", Offsets.HELP_X, Offsets.HELP_Y + 11*Offsets.NEWLINE);        
  }
  
  /**
   * Application method.
   * 
   * @param args Command-line arguments.
   */
  public static void main(String[] args) {
    
    try{
      System.setProperty("java.library.path", new File("lib").getAbsolutePath());
      System.setProperty("org.lwjgl.librarypath", new File("lib/native").getAbsolutePath());
      
      AppGameContainer appgc;
      
      appgc = new AppGameContainer(new Tetris() );
      appgc.setDisplayMode(WINDOW_WIDTH, WINDOW_HEIGHT, FULLSCREEN);
      appgc.start();
    }catch(SlickException e){
      e.printStackTrace();
    }catch(UnsatisfiedLinkError err){
      // ignore, we're expecting
    }
        
  }
}
