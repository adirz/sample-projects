package deafault;
import java.awt.Image;

import oop.ex3.GameGUI;


public class HumanShip extends SpaceShip {

	/**
	 * works only by what's pressed. simple as that.
	 */
	@Override
	public void doAction(SpaceWars game) {		
		if(game.getGUI().isTeleportPressed())
			teleport();
		
		boolean accel = false;
		int turn = 0;		
		if(game.getGUI().isUpPressed()){
			accel = true;
		}	
		if(game.getGUI().isLeftPressed()){
			turn = 1;
		}else if(game.getGUI().isRightPressed()){
			turn = -1;
		}
		phys.move(accel, turn);
		
		if(game.getGUI().isShieldsPressed()){
			shieldOn();
		}else{
			shieldUp = false;
		}
		
		if(game.getGUI().isShotPressed())
			fire(game);
		constEnergy();
	}
	
	/**
	 * returns the spaceship Image, with shield if its up
	 */
	@Override
	public Image getImage() {
		if(shieldUp)
			return GameGUI.SPACESHIP_IMAGE_SHIELD;
		return GameGUI.SPACESHIP_IMAGE;
	}

}
