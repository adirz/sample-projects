package deafault;


public class DrunkardShip extends ResponsiveShip {
	/**
	 * all val are Chances: X out of FULL
	 * 
	 * TIME: how long will stuff last
	 */
	private static int FULL = 1000;
	private static int FLEE = 10;
	private static int BUMP = 600;
	private static int SWARL = 20;
	private static int ACCELERATE = 600;
	private static int OFF_TARGET = 30;
	
	private static int TIME = 20;
	
	private int swarlTime = 0;
	private int offTargetTime = 0;
	
	@Override	
	public void responsiveAction() {
		boolean putShield = false;
		if(Math.random()*FULL < FLEE)
			teleport();
		else{
			if(Math.random()*FULL < SWARL)
				swarlTime = TIME;
			else if(Math.random()*FULL < BUMP)
				putShield = true;
		}
		if(swarlTime > 0){
			swarlTime --;
			phys.move(false, 1);
		}else{
			regularAction();
			if(putShield)
				shieldOn();
		}
	}

	@Override
	public void regularAction() {
		boolean ace = (Math.random()*FULL <= ACCELERATE);
		int dir = leftOrRight(true);
		if((Math.random()*FULL < OFF_TARGET))
			offTargetTime = TIME;
		if(offTargetTime > 0){
			offTargetTime --;
			dir *= -1;
		}
		phys.move(ace, dir);	
	}

	@Override
	public boolean chooseAction() {
		return (distance < RESPONSE_DIS);
	}

}
