package tetris;

/**
 * Collection of x- and y-offsets for use with rendering.
 * 
 * @author Daniel Rolandi
 */
public class Offsets {
  
  public static final float GAME_X = 40.0f;
  public static final float GAME_Y = 40.0f;
  
  public static final float NEXT_TETRO_X = GAME_X*2 + Board.WIDTH*Board.BLOCK_SIZE;
  public static final float NEXT_TETRO_Y = GAME_Y;
  
  public static final float SCORE_X = NEXT_TETRO_X;
  public static final float SCORE_Y = NEXT_TETRO_Y + Board.NEXT_TETRO_SIZE + GAME_X;
  public static final float NEWLINE = 20;
  
  public static final float GAMEOVER_X = NEXT_TETRO_X;
  public static final float GAMEOVER_Y = NEXT_TETRO_Y + GAME_X*5;
  
  public static final float MOUSE_X = NEXT_TETRO_X;
  public static final float MOUSE_Y = NEXT_TETRO_Y + GAME_X*9;
  
  public static final float HELP_X = NEXT_TETRO_X + Board.NEXT_TETRO_SIZE + GAME_X;
  public static final float HELP_Y = NEXT_TETRO_Y;
  
}
