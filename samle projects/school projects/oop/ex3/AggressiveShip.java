package deafault;

public class AggressiveShip extends ResponsiveShip{

	@Override
	public void doAction(SpaceWars game){
		closest(game);
		if(chooseAction()){
			regularAction();
			fire(game);
		}else
			regularAction();
		constEnergy();
	}
	
	@Override
	public boolean chooseAction() {
		return (Math.abs(myAngle) < RESPONSE_ANGLE);
	}
	
	@Override
	public void responsiveAction() {}

	@Override
	public void regularAction() {
		phys.move(true, leftOrRight(true));		
	}


}
