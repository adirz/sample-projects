package deafault;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class SpaceWarsTest {
	private static final String SHIP_TYPE = "SHIP_TYPE";
	private SpaceShip[] ships;
	private SpaceWars game;
	private String[] args;
	
	@Before
	public void initializeTerm() {
		args = new String[2];
		args[0] = "h";
		args[1] = "a";
		ships = SpaceShipFactory.createSpaceShips(args);
		game = new SpaceWars(args);
	}
	
	@Test
	public void createsAlive(){
		testCreator(ships[0]);
		assertEquals(false, ships[0].isDead());
	}
	
	@Test
	public void testBaisics(){
		//basics functions are the same for all ships
		basicsFunctions(ships[0]);
	}

	@Test
	public void testCollisions(){
		//when shield is off
		collisionTest(ships[0],false);
		//when shield is on
		collisionTest(ships[0],true);
	}
	
	public static void testCreator(SpaceShip ship) {		
		assertNotNull(ship);
	}
	
	private void measure(int post, int pre, int diff,
			String message) {
		try{
			assertTrue((post - pre) == diff);
		}catch(Error assertionError){
			System.out.print(message+"\nexpected: "+ diff +
					"\nactual: "+ (post - pre) +"\n");
			throw(new AssertionError());
		}
		
	}
	
	public void basicsFunctions(SpaceShip ship) {
		String errorMsg = SHIP_TYPE + " " + ship.getClass().toString()+
				" basics functions test line ";
		ship.reset();
		int preEnergy = ship.getCurEnergy();
		int postEnergy;
		
		//checks if it teleport and the teleportation cost
		double locX = ship.phys.getX();
		double locY = ship.phys.getY();
		ship.teleport();
		assertTrue(ship.phys.getX() != locX || ship.phys.getY() != locY);
		postEnergy = ship.getCurEnergy();
		measure(postEnergy, preEnergy, SpaceShip.TELEPORT_ENERGY,
				errorMsg+"1");

		//checks if the ships charges energy
		preEnergy = ship.getCurEnergy();
		ship.constEnergy();
		postEnergy = ship.getCurEnergy();
		measure(postEnergy, preEnergy, SpaceShip.CHARGING_ENERGY,
				errorMsg+"2");
		
		//checks the cost of firing and the gap time between fires 
		ship.reset();
		preEnergy = ship.getCurEnergy();
		ship.fire(game);
		postEnergy = ship.getCurEnergy();
		measure(postEnergy, preEnergy, SpaceShip.FIRE_ENERGY,
				errorMsg+"3a");
		for (int i = 0; i < SpaceShip.FIRE_GAP; i++ ) {
			preEnergy = ship.getCurEnergy();
			ship.fire(game);
			postEnergy = ship.getCurEnergy();
			measure(preEnergy, postEnergy, 0, errorMsg+"3b");
			ship.constEnergy();			
		}
		preEnergy = ship.getCurEnergy();
		ship.fire(game);
		postEnergy = ship.getCurEnergy();
		measure(postEnergy, preEnergy, SpaceShip.FIRE_ENERGY,
				errorMsg+"3c");
		
		//check shield up and cost
		ship.reset();
		preEnergy = ship.getCurEnergy();
		ship.shieldOn();
		assertTrue(ship.shieldUp);
		postEnergy = ship.getCurEnergy();
		measure(postEnergy, preEnergy, SpaceShip.SHIELD_ENERGY,
				errorMsg+"4a");
		
		//check to see you can't use more energy than you got
		ship.reset();
		while(ship.getCurEnergy() > -SpaceShip.TELEPORT_ENERGY){
			ship.teleport();
		}
		locX = ship.phys.getX();
		locY = ship.phys.getY();
		ship.teleport();
		assertTrue(ship.phys.getX() == locX && ship.phys.getY() == locY);
		
	}

	
	//checks if the shield protects the ships, gives them bashing energy,
	//takes away getting hit energy and dies when out of life
	public void collisionTest(SpaceShip ship, boolean shield) {
		String errMsg = SHIP_TYPE + " " + ship.getClass().toString()+
				" collision test line ";
		ship.reset();
		if(shield)
			ship.shieldOn();
		int preEnergy = ship.getMaxEnergy();
		int postEnergy;
		int preLife = ship.getCurLife();
		int postLife;
		
		ship.collidedWithAnotherShip();
		postLife = ship.getCurLife();
		postEnergy = ship.getMaxEnergy();
		if(!shield){
			measure(postEnergy, preEnergy, SpaceShip.HIT_ENERGY,errMsg+"1");
			measure(postLife, preLife, -1, errMsg+"2");
		}else{
			measure(postEnergy, preEnergy,-2*SpaceShip.HIT_ENERGY,errMsg+"1");
			measure(postLife, preLife, 0, errMsg+"2");
		}

		for (int i = 0; i < SpaceShip.MAX_HEALTH; i++) {
			ship.collidedWithAnotherShip();
		}
		assertEquals(!shield, ship.isDead());
		if(shield){
			ship.reset();
			ship.shieldOn();
			preEnergy = ship.getCurEnergy();
			ship.collidedWithAnotherShip();
			postEnergy = ship.getCurEnergy();
			measure(postEnergy, preEnergy, SpaceShip.BASHING_ENERGY,
					errMsg+"3");	
		}
	}
}
