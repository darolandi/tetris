/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Point;

/**
 * Represents a Collection (array) of Blocks.
 * Does not contain the actual rendering.
 * Once a Tetromino hits a floor, they become independent Blocks.
 * At that point, this class loses control over those Blocks.
 * 
 * @author Daniel Rolandi
 */
public class Tetromino {    
  private TetrominoInfo type;
  private Block[] blocks;
  private float refX;
  private float refY;
  private int state;
  
  public Tetromino(TetrominoInfo type, float x, float y){
    this.type = type;
    refX = x;
    refY = y;
    blocks = new Block[4];
    state = 0;
    syncBlocks();
  }
  
  /**
   * Renders all Blocks in this Tetromino.
   * Tetrominos in game are rendered by Board.
   * This method is to intended to be used for Next-Tetromino.
   * 
   * @param gc Game Container.
   * @param g Graphics context.
   */
  public void render(GameContainer gc, Graphics g){
    blocks[0].render(gc, g);
    blocks[1].render(gc, g);
    blocks[2].render(gc, g);
    blocks[3].render(gc, g);
  }
  
  /**
   * Returns reference X coordinate.
   * @return Reference X coordinate.
   */
  public float getX(){
    return refX;
  }
  
  /**
   * Returns reference Y coordinate.
   * @return Reference Y coordinate.
   */
  public float getY(){
    return refY;
  }
  
  /**
   * Moves this Tetromino to the SpawnPoint in Waiting Room (above Game).
   */
  public void moveToSpawn(){
    Point spawnPoint = TetrominoInfo.getSpawnPoint(type);
    float spawnX = spawnPoint.getX();
    float spawnY = spawnPoint.getY();
    
    refX = Board.GAME_OFFSETX + spawnX*Board.BLOCK_SIZE;
    refY = Board.GAME_OFFSETY + (spawnY - Board.HEIGHT_WAITING)*Board.BLOCK_SIZE;
    syncBlocks();
  }
  
  private void syncBlocks(){
    switch(type){
      case I: syncI(); break;
      case Z: syncZ(); break;
      case S: syncS(); break;
      case O: syncO(); break;
      case T: syncT(); break;
      case L: syncL(); break;
      case J: syncJ(); break;
    }
  }
  
  private void syncI(){
    switch(state){
      case 0:
        blocks[0].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[1].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[2].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[3].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        break;
      case 1:
        blocks[0].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[1].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[2].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[3].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        break;
      case 2:
        blocks[0].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[1].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[2].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[3].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        break;
      case 3:
        blocks[0].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[1].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[2].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[3].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        break;
    }            
  }
  
  private void syncZ(){
    switch(state){
      case 0:
        blocks[0].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[1].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[2].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[3].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        break;
      case 1:
        blocks[0].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[1].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[2].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[3].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        break;
      case 2:
        blocks[0].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[1].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[2].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[3].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        break;
      case 3:
        blocks[0].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[1].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[2].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[3].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        break;
    }            
  }
  
  private void syncS(){
    switch(state){
      case 0:
        blocks[0].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[1].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[2].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[3].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        break;
      case 1:
        blocks[0].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[1].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[2].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[3].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        break;
      case 2:
        blocks[0].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[1].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[2].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[3].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        break;
      case 3:
        blocks[0].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[1].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[2].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[3].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        break;
    }            
  }
  
  private void syncO(){
    switch(state){
      case 0:
        blocks[0].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[1].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[2].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[3].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        break;
      case 1:
        blocks[0].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[1].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[2].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[3].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        break;
      case 2:
        blocks[0].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[1].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[2].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[3].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        break;
      case 3:
        blocks[0].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[1].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[2].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[3].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        break;
    }            
  }
  
  private void syncT(){
    switch(state){
      case 0:
        blocks[0].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[1].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[2].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[3].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        break;
      case 1:
        blocks[0].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[1].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[2].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[3].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        break;
      case 2:
        blocks[0].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[1].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[2].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[3].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        break;
      case 3:
        blocks[0].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[1].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[2].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[3].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        break;
    }            
  }
  
  private void syncL(){
    switch(state){
      case 0:
        blocks[0].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[1].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[2].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[3].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        break;
      case 1:
        blocks[0].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[1].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[2].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[3].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        break;
      case 2:
        blocks[0].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[1].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[2].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[3].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        break;
      case 3:
        blocks[0].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[1].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[2].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[3].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        break;
    }            
  }
  
  private void syncJ(){
    switch(state){
      case 0:
        blocks[0].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[1].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[2].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[3].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        break;
      case 1:
        blocks[0].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[1].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[2].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[3].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        break;
      case 2:
        blocks[0].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[1].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[2].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[3].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        break;
      case 3:
        blocks[0].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[1].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[2].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        blocks[3].setPosition(refX + 0*Board.BLOCK_SIZE, refY + 1*Board.BLOCK_SIZE);
        break;
    }            
  }
  
}
