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
    if( command.equals(Commands.debugMode) ){
      board.debugMode = !board.debugMode;
    }else if( command.equals(Commands.moveDown) ){
      board.moveDown();
    }else if( command.equals(Commands.moveLeft) ){
      board.moveLeft();
    }else if( command.equals(Commands.moveRight) ){
      board.moveRight();
    }else if( command.equals(Commands.hardDrop) ){
      board.hardDrop();
    }else if( command.equals(Commands.newGame) ){
      board.newGame();
    }else if( command.equals(Commands.rotateLeft) ){
      board.rotateLeft();
    }else if( command.equals(Commands.rotateRight) ){
      board.rotateRight();
    }
    
    if(board.debugMode){
      
      if( command.equals(Commands.dumpGrid) ){
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
  public void controlReleased(Command command){    
  }
  
}
