package tetris;

import org.newdawn.slick.Input;
import org.newdawn.slick.command.Command;
import org.newdawn.slick.command.BasicCommand;
import org.newdawn.slick.command.KeyControl;

/**
 * Collection of available commands in this Tetris game, with their keys.
 * 
 * @author Daniel Rolandi
 */
public class Commands {
  public static final Command moveLeft = new BasicCommand("moveLeft");
  public static final Command moveRight = new BasicCommand("moveRight");
  public static final Command moveDown = new BasicCommand("moveDown");  
  
  public static final Command hardDrop = new BasicCommand("hardDrop");
  public static final Command rotateLeft = new BasicCommand("rotateLeft");
  public static final Command rotateRight = new BasicCommand("rotateRight");
  
  public static final Command newGame = new BasicCommand("newGame");
  public static final Command debugMode = new BasicCommand("debugMode");
  public static final Command dumpGrid = new BasicCommand("dumpGrid");
  public static final Command toggleMusic = new BasicCommand("toggleMusic");
  
  public static final KeyControl moveLeftKey = new KeyControl(Input.KEY_LEFT);
  public static final KeyControl moveRightKey = new KeyControl(Input.KEY_RIGHT);
  public static final KeyControl moveDownKey = new KeyControl(Input.KEY_DOWN);  
  
  public static final KeyControl hardDropKey = new KeyControl(Input.KEY_SPACE);
  public static final KeyControl rotateLeftKey = new KeyControl(Input.KEY_Z);
  public static final KeyControl rotateRightKey1 = new KeyControl(Input.KEY_X);
  public static final KeyControl rotateRightKey2 = new KeyControl(Input.KEY_UP);
  
  public static final KeyControl newGameKey = new KeyControl(Input.KEY_F1);
  public static final KeyControl debugModeKey = new KeyControl(Input.KEY_ESCAPE);
  public static final KeyControl dumpGridKey = new KeyControl(Input.KEY_G);
  public static final KeyControl toggleMusicKey = new KeyControl(Input.KEY_M);
}
