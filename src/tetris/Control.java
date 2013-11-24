package tetris;

import org.newdawn.slick.command.Command;
import org.newdawn.slick.command.InputProviderListener;

/**
 * Listens to and handles keyboard inputs.
 * 
 * @author Daniel Rolandi
 */
public class Control implements InputProviderListener{
  private Board board;
    
  public Control(Board board){
    this.board = board;
  }
  
  /**
   * Call methods in the Board based on the Command.
   * @param command Command received.
   */
  @Override
  public void controlPressed(Command command){
    
  }
  
  /**
   * Empty implementation because they are not used.
   * @param command Command received.
   */
  @Override
  public void controlReleased(Command command){    
  }
  
}
