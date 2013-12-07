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
public class Commands
{
  public static final Command MOVE_LEFT = new BasicCommand("move_left");
  public static final Command MOVE_RIGHT = new BasicCommand("move_right");
  public static final Command MOVE_DOWN = new BasicCommand("move_down");  
  
  public static final Command HARD_DROP = new BasicCommand("hard_drop");
  public static final Command ROTATE_LEFT = new BasicCommand("rotate_left");
  public static final Command ROTATE_RIGHT = new BasicCommand("rotate_right");
  
  public static final Command NEW_GAME = new BasicCommand("new_game");
  public static final Command END_GAME = new BasicCommand("end_game");
  public static final Command DEBUG_MODE = new BasicCommand("debug_mode");
  public static final Command DUMP_GRID = new BasicCommand("dump_grid");
  public static final Command TOGGLE_MUSIC = new BasicCommand("toggle_music");
  
  public static final KeyControl MOVE_LEFT_KEY = new KeyControl(Input.KEY_LEFT);
  public static final KeyControl MOVE_RIGHT_KEY = new KeyControl(Input.KEY_RIGHT);
  public static final KeyControl MOVE_DOWN_KEY = new KeyControl(Input.KEY_DOWN);  
  
  public static final KeyControl HARD_DROP_KEY = new KeyControl(Input.KEY_SPACE);
  public static final KeyControl ROTATE_LEFT_KEY = new KeyControl(Input.KEY_Z);
  public static final KeyControl ROTATE_RIGHT_KEY1 = new KeyControl(Input.KEY_X);
  public static final KeyControl ROTATE_RIGHT_KEY2 = new KeyControl(Input.KEY_UP);
  
  public static final KeyControl NEW_GAME_KEY = new KeyControl(Input.KEY_F1);
  public static final KeyControl END_GAME_KEY = new KeyControl(Input.KEY_F2);
  public static final KeyControl DEBUG_MODE_KEY = new KeyControl(Input.KEY_ESCAPE);
  public static final KeyControl DUMP_GRID_KEY = new KeyControl(Input.KEY_G);
  public static final KeyControl TOGGLE_MUSIC_KEY = new KeyControl(Input.KEY_M);
}
