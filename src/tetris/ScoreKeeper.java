package tetris;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Color;

/**
 * Deals with Scorekeeping and rendering.
 * 
 * @author Daniel Rolandi
 */
public class ScoreKeeper
{  
    
  private static final Color TEXT_COLOR = new Color(255, 255, 255);
  private static final String LEVEL_TEXT_DEFAULT = "Level: "; 
  private static final String CLEARED_TEXT_DEFAULT = "Cleared: ";
  private static final String SCORE_TEXT_DEFAULT = "Score: ";
  
  private static final int LEVEL_TEXT_PADDING = 7;
  private static final int CLEARED_TEXT_PADDING = 5;
  private static final int SCORE_TEXT_PADDING = 7;
  
  private static final float BASE_SCORE = 100.0f;
  // NOTE: ROW_MULTIPLIER must scale with BLOCK_COUNT
  private static final float[] ROW_MULTIPLIER = {1.0f, 1.5f, 2.0f, 2.5f};
  private static final float LEVEL_BONUS = 0.2f;
  static
  {
    if(ROW_MULTIPLIER.length != TetrominoInfo.BLOCK_COUNT)
    {
      throw new IllegalStateException("ScoreKeeper: ROW_MULTIPLIER size doesn't match BLOCK_COUNT");
    }
  }
    
  private int level;
  private int clearedCount;
  private float score;
  
  public ScoreKeeper()
  {
    level = 0;
    clearedCount = 0;
    score = 0.0f;    
  }
  
  /**
   * Adds score based on how many rows were cleared using a Tetromino.
   * 
   * @param rows Count of rows that were cleared.
   */
  public void clearedRows(int rows)
  {
    clearedCount += rows;
    score += rows * BASE_SCORE * (ROW_MULTIPLIER[rows-1] + LEVEL_BONUS*level);
  }
  
  /**
   * Increases level by levelIncrease.
   * 
   * @param levelIncrease Number of increase.
   */
  public void levelUp(int levelIncrease)
  {
    level += levelIncrease;
  }
  
  /**
   * Renders the score text.
   * 
   * @param gameContainer Game Container.
   * @param graphics Graphics context.
   */  
  public void render(GameContainer gameContainer, Graphics graphics)
  {
    graphics.setColor( TEXT_COLOR );
    graphics.drawString( LEVEL_TEXT_DEFAULT + String.format("%"+LEVEL_TEXT_PADDING+"d", level), Offsets.SCORE_X, Offsets.SCORE_Y);
    graphics.drawString( CLEARED_TEXT_DEFAULT + String.format("%"+CLEARED_TEXT_PADDING+"d", clearedCount),
            Offsets.SCORE_X, Offsets.SCORE_Y + Offsets.NEWLINE);
    graphics.drawString( SCORE_TEXT_DEFAULT + String.format("%"+SCORE_TEXT_PADDING+".0f", score),
            Offsets.SCORE_X, Offsets.SCORE_Y + 2*Offsets.NEWLINE );    
  }    
  
}
