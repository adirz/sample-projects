package deafault;

public class RunnerShip extends ResponsiveShip {
	
	@Override
	public void responsiveAction() {
		teleport();
		phys.move(true, leftOrRight(false));
	}

	@Override
	public void regularAction() {
		phys.move(true, leftOrRight(false));
	}

	@Override
	public boolean chooseAction() {
		return (distance < RESPONSE_DIS && theirAngle < RESPONSE_ANGLE);
	}

}
