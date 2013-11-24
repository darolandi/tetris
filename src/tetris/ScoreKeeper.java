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
  private static final float SCORE_OFFSETX = Board.NEXT_TETRO_OFFSETX;
  private static final float SCORE_OFFSETY = Board.NEXT_TETRO_OFFSETY + Board.NEXT_TETRO_SIZE + Board.GAME_OFFSETX;
  private static final float NEWLINE_OFFSET = 20;
  private static final Color SCORETEXT_COLOR = new Color(255, 255, 255);
  private static final String SCORETEXT_DEFAULT = "Score:";
  
  private static final float BASE_SCORE = 100.0f;
  private static final float[] ROW_MULTIPLIER = {1.0f, 1.5f, 2.0f, 2.5f};
  
  private float score;
  
  public ScoreKeeper(){
    score = 0.0f;
  }
  
  /**
   * Adds score based on how many rows were cleared using a Tetromino.
   * 
   * @param rows Count of rows that were cleared.
   */
  public void clearedRows(int rows){
    score += rows * BASE_SCORE * ROW_MULTIPLIER[rows-1];
  }
  
  public void render(GameContainer gc, Graphics g){
    g.setColor( SCORETEXT_COLOR );
    g.drawString( SCORETEXT_DEFAULT, SCORE_OFFSETX, SCORE_OFFSETY );
    g.drawString( String.format("% 12.0f", score), SCORE_OFFSETX, SCORE_OFFSETY + NEWLINE_OFFSET );
  }    
  
}
