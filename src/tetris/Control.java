package tetris;

import org.newdawn.slick.command.Command;
import org.newdawn.slick.command.InputProviderListener;

/**
 * Listens to and handles keyboard inputs.
 * 
 * @author Daniel Rolandi
 */
public class Control implements InputProviderListener
{
  private Board board;
    
  public Control(Board board)
  {
    this.board = board;
  }
  
  /**
   * Call methods in the Board based on the Command.
   * @param command Command received.
   */
  @Override
  public void controlPressed(Command command)
  {
    if( command.equals(Commands.DEBUG_MODE) )
    {
      board.debugMode = !board.debugMode;
    }
    else if( command.equals(Commands.MOVE_DOWN) )
    {
      board.moveDown();
    }
    else if( command.equals(Commands.MOVE_LEFT) )
    {
      board.moveLeft();
    }
    else if( command.equals(Commands.MOVE_RIGHT) )
    {
      board.moveRight();
    }
    else if( command.equals(Commands.HARD_DROP) )
    {
      board.hardDrop();
    }
    else if( command.equals(Commands.NEW_GAME) )
    {
      board.newGame();    
    }
    else if( command.equals(Commands.END_GAME) )
    {
      board.endGame();
    }
    else if( command.equals(Commands.ROTATE_LEFT) )
    {
      board.rotateLeft();
    }
    else if( command.equals(Commands.ROTATE_RIGHT) )
    {
      board.rotateRight();
    }
    else if( command.equals(Commands.TOGGLE_MUSIC) )
    {
      board.toggleMusic();
    }
    
    if(board.debugMode)
    {      
      if( command.equals(Commands.DUMP_GRID) )
      {
        board.dumpGrid();
      }      
      System.out.println(command);
    }
  }
  
  /**
   * Empty implementation because they are not used.
   * @param command Command received.
   */
  @Override
  public void controlReleased(Command command)
  {
  }
  
}
