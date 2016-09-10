package deafault;

public class BasherShip extends ResponsiveShip {

	@Override
	public void responsiveAction() {
		phys.move(true, leftOrRight(true));
		constEnergy();
		if(!shieldUp)
			shieldOn();
	}

	@Override
	public void regularAction() {
		phys.move(true, leftOrRight(true));	
		shieldUp = false;
	}

	@Override
	public boolean chooseAction() {
		return (distance < RESPONSE_DIS);
	}
}
