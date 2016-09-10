package deafault;
import java.awt.Image;

import oop.ex3.GameGUI;
import oop.ex3.SpaceShipPhysics;


public abstract class ResponsiveShip extends SpaceShip {
	
	/**
	 * As demanded are the response distant and angle
	 */
	protected static double RESPONSE_DIS = 0.2;
	protected static double RESPONSE_ANGLE = 0.2;
	
	protected double distance;
	protected double myAngle;
	protected double theirAngle;

	/**
	 * The brain of the AggressiveShip, deciding what to do
	 */
	@Override
	public void doAction(SpaceWars game){
		closest(game);
		if(chooseAction()){
			responsiveAction();
			closest(game);
		}else
			regularAction();
		constEnergy();
	}
	
	/**
	 * Receives @param towards. if true it returns which way to turn to catch
	 * it. if false returns which way to turn to avoid it.
	 */
	protected int leftOrRight(boolean towards){
		if(towards){
			if(myAngle > 0)
				return 1;
			if(myAngle < 0)
				return -1;
			return 0;
		}
		if(myAngle > 0)
			return -1;
		return 1;
	}
	
	/**
	 * from @param game find the closest ship to this one.
	 * sets "distance" to the distance between the closest ship and this one
	 * sets "myAngle" to the direction towards the closest ship
	 * sets "theirAngle" to the angle of the closest ship towards this one
	 */
	public void closest(SpaceWars game){
		SpaceShipPhysics closestPhys;
		closestPhys = game.getClosestShipTo(this).getPhysics();
		distance = phys.distanceFrom(closestPhys);
		myAngle = phys.angleTo(closestPhys);
		theirAngle = closestPhys.angleTo(phys);
	}
	

	/**
	 * decides if the conditions to use the responsive method are met
	 */
	public abstract boolean chooseAction();
	
	/**
	 * the responsive action to be use if the conditions are met
	 */
	public abstract void responsiveAction();

	/**
	 * the response action to be use if the conditions arn't met
	 */
	public abstract void regularAction();
	
	/**
	 * returns the enemy Image, with shield if its up
	 */
	@Override
	public Image getImage(){
		if(shieldUp)
			return GameGUI.ENEMY_SPACESHIP_IMAGE_SHIELD;
		return GameGUI.ENEMY_SPACESHIP_IMAGE;
	}

}
