package deafault;
import java.awt.Image;

import oop.ex3.GameGUI;


public class SpecialShip extends SpaceShip {
	
	/**
	 * RESPONSE_ANGLE - the angle to fire
	 * RESPONSE_DIS - the distance to raise the shield
	 * HUMAN_TEAM - if the ship is on the human team
	 */
	private static double RESPONSE_ANGLE = 0.2;
	private static double RESPONSE_DIS = 0.2;
	private static boolean HUMAN_TEAM = true;

	private double distance;
	protected double angle;
	protected boolean team;
	
	/**
	 * constructor, makes the ship team @param team
	 */
	public SpecialShip(boolean team){
		super();
		this.team = team;
	}
	
	/**
	 * @return if the ship is on the human team
	 */
	public boolean getTeam(){
		return team;
	}
	
	/**
	 * receives @param closest ship, checks if it is enemy or a friend
	 * @return
	 */
	private boolean toFight(SpaceShip closest){
		if((closest.getClass().equals(HumanShip.class) && team != HUMAN_TEAM))
			return true;
		if(closest.getClass().equals(SpecialShip.class)){
			if(team != ((SpecialShip)closest).getTeam())
				return true;
		}
		return false;
	}
	
	/**
	 * Receives @param towards. if true it returns which way to turn to catch
	 * it. if false returns which way to turn to avoid it.
	 */
	private int leftOrRight(boolean towards){
		if(towards){
			if(angle > 0)
				return 1;
			if(angle < 0)
				return -1;
			return 0;
		}
		if(angle > 0)
			return -1;
		return 1;
	}
	
	/**
	 * The special ship's brain. detemins what to do according to the
	 * @parm game arena
	 */
	@Override
	public void doAction(SpaceWars game) {
		SpaceShip closest = game.getClosestShipTo(this);
		distance = phys.distanceFrom(closest.phys);
		angle = phys.angleTo(closest.phys);
		if(distance < RESPONSE_DIS)
			shieldOn();
		else
			shieldUp = false;
		if(toFight(closest)){
			angle = phys.angleTo(closest.getPhysics());
			if(Math.abs(angle) < RESPONSE_ANGLE){
				phys.move(true, leftOrRight(true));	
				fire(game);
			}else
				phys.move(true, leftOrRight(true));
			constEnergy();
		}else{
			phys.move(true, leftOrRight(false));
			constEnergy();
		}
	}
	
	/**
	 * returns the enemy Image (because I can't add my own pics), with shield
	 * if its up.
	 */
	@Override
	public Image getImage(){
		if(shieldUp)
			return GameGUI.ENEMY_SPACESHIP_IMAGE_SHIELD;
		return GameGUI.ENEMY_SPACESHIP_IMAGE;
	}

}
