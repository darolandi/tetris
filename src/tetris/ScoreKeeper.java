package tetris;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Color;

/**
 * Deals with Scorekeeping and rendering.
 * 
 * @author Daniel Rolandi
 */
public class ScoreKeeper {  
    
  private static final Color TEXT_COLOR = new Color(255, 255, 255);
  private static final String SCORETEXT_DEFAULT = "Score: ";
  private static final String LEVELTEXT_DEFAULT = "Level: ";
  
  private static final float BASE_SCORE = 100.0f;
  private static final float[] ROW_MULTIPLIER = {1.0f, 1.5f, 2.0f, 2.5f};
  private static final float LEVEL_BONUS = 0.2f;
  
  private float score;
  private int level;
  
  public ScoreKeeper(){
    score = 0.0f;
    level = 0;
  }
  
  /**
   * Adds score based on how many rows were cleared using a Tetromino.
   * 
   * @param rows Count of rows that were cleared.
   */
  public void clearedRows(int rows){
    score += rows * BASE_SCORE * (ROW_MULTIPLIER[rows-1] + LEVEL_BONUS*level);
  }
  
  /**
   * Increases level by levelIncrease.
   * 
   * @param levelIncrease Number of increase.
   */
  public void levelUp(int levelIncrease){
    level += levelIncrease;
  }
  
  /**
   * Renders the score text.
   * 
   * @param gc Game Container.
   * @param g Graphics context.
   */  
  public void render(GameContainer gc, Graphics g){
    g.setColor( TEXT_COLOR );
    g.drawString( LEVELTEXT_DEFAULT + level, Offsets.SCORE_X, Offsets.SCORE_Y);
    g.drawString( SCORETEXT_DEFAULT + String.format("%6.0f", score),
            Offsets.SCORE_X, Offsets.SCORE_Y + Offsets.NEWLINE );    
  }    
  
}
