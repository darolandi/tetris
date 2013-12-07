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
public class Tetris extends BasicGame
{
  // undefined behavior if set to be smaller than gamefield size
  private static final int WINDOW_WIDTH = 700;
  private static final int WINDOW_HEIGHT = 480;
  private static final boolean FULLSCREEN = false;
  private static final Color BACKGROUND = new Color(112, 48, 160);    
  
  private Board board;
  
  /**
   * Call BasicGame's constructor.
   */
  public Tetris()
  {
    super("Tetris");    
  }
  
  /**
   * Inits game resources before any update or render.
   * Also prepares the tick counter.
   * @param gameContainer Game Container.
   * @throws SlickException 
   */
  @Override
  public void init(GameContainer gameContainer) throws SlickException
  {
    gameContainer.setShowFPS(false);
    board = new Board(gameContainer);    
  }
  
  /**
   * Updates the board and the ticker (responsible for locking).
   * 
   * @param gameContainer Game Container.
   * @param deltaTime Time interval.
   * @throws SlickException 
   */
  @Override
  public void update(GameContainer gameContainer, int deltaTime) throws SlickException
  {    
    board.update(gameContainer, deltaTime);    
  }  
  
  /**
   * Renders the game and the board in it.
   * 
   * @param gameContainer Game Container.
   * @param g Graphics context.
   * @throws SlickException 
   */
  @Override
  public void render(GameContainer gameContainer, Graphics graphics) throws SlickException
  {
    graphics.setBackground(BACKGROUND);
    board.render(gameContainer, graphics);
    renderHelp(gameContainer, graphics);
  }
  
  private void renderHelp(GameContainer gameContainer, Graphics graphics)
  {
    graphics.setColor( Color.white );
    graphics.drawString("CONTROLS", Offsets.HELP_X, Offsets.HELP_Y);
    graphics.drawString("left; Move left", Offsets.HELP_X, Offsets.HELP_Y + Offsets.NEWLINE);
    graphics.drawString("right; Move right", Offsets.HELP_X, Offsets.HELP_Y + 2*Offsets.NEWLINE);
    graphics.drawString("down; Move down (soft)", Offsets.HELP_X, Offsets.HELP_Y + 3*Offsets.NEWLINE);
    graphics.drawString("space; Hard drop", Offsets.HELP_X, Offsets.HELP_Y + 4*Offsets.NEWLINE);
    graphics.drawString("Z; Rotate CCW", Offsets.HELP_X, Offsets.HELP_Y + 5*Offsets.NEWLINE);
    graphics.drawString("X/up; Rotate CW", Offsets.HELP_X, Offsets.HELP_Y + 6*Offsets.NEWLINE);
    graphics.drawString("F1; New Game", Offsets.HELP_X, Offsets.HELP_Y + 7*Offsets.NEWLINE);
    graphics.drawString("F2; End Game", Offsets.HELP_X, Offsets.HELP_Y + 8*Offsets.NEWLINE);
    graphics.drawString("ESC; Toggle Debug Mode", Offsets.HELP_X, Offsets.HELP_Y + 9*Offsets.NEWLINE);
    graphics.drawString("G; Dump Grid (debug only)", Offsets.HELP_X, Offsets.HELP_Y + 10*Offsets.NEWLINE);
    graphics.drawString("M; Toggle mute/unmute BGM", Offsets.HELP_X, Offsets.HELP_Y + 11*Offsets.NEWLINE);        
  }
  
  /**
   * Application method.
   * 
   * @param args Command-line arguments.
   */
  public static void main(String[] args)
  {
    
    try
    {
      System.setProperty("java.library.path", new File("lib").getAbsolutePath());
      System.setProperty("org.lwjgl.librarypath", new File("lib/native").getAbsolutePath());
      
      AppGameContainer appgameContainer;
      
      appgameContainer = new AppGameContainer(new Tetris() );
      appgameContainer.setDisplayMode(WINDOW_WIDTH, WINDOW_HEIGHT, FULLSCREEN);
      appgameContainer.start();
    }
    catch(SlickException slickException)
    {
      slickException.printStackTrace();
    }
    catch(UnsatisfiedLinkError unsatisfiedLink)
    {
      // ignore exception, we're expecting
    }
        
  }
}
