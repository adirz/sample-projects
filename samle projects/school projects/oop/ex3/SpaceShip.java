package deafault;
import java.awt.Image;
import oop.ex3.*;

/**
 * SpaceShip.java is an abstract class, class for the other spaceships 
 * because it is never used as is, and need functions that get implemented
 * differently in different successors
 */
public abstract class SpaceShip{

	/**
	 * @param 
	 */
	public static int FIRE_GAP = 8;
	public static int MAX_HEALTH = 20;
	public static int DEATH_HEALTH = 0;
	public static int MAX_ENERGY = 200;
	public static int MIN_ENERGY = 0;
	public static int BASHING_ENERGY = 20;
	public static int CHARGING_ENERGY = 1;
	public static int HIT_ENERGY = -10;
	public static int FIRE_ENERGY = -20;
	public static int TELEPORT_ENERGY = -150;
	public static int SHIELD_ENERGY = -3;
	
	protected SpaceShipPhysics phys;
	private int maxEnergyLvl;
	private int curEnergyLvl;
	private int health;
	private int lastFire;
	protected boolean shieldUp;
	private boolean dead;
   
	/**
	 * builds a new spaceship
	 */
	public SpaceShip(){
		reset();
	}
	
    /**
     * Does the actions of this ship for this round. 
     * This is called once per round by the SpaceWars game driver.
     * 
     * @param game the game object to which this ship belongs.
     */	
    public abstract void doAction(SpaceWars game);

    /**
     * @return the current energy level
     */
    public int getCurEnergy(){return curEnergyLvl;}
    
    /**
     * @return the current health level
     */
	public int getCurLife() {return health;}
	
    /**
     * @return the current maximum energy level
     */
	public int getMaxEnergy() {return maxEnergyLvl;}
    
	/**
	 * when hit, changes health and energy as necessery
	 */
    private void reduceHealth(){
		if(health > DEATH_HEALTH +1){
	    	health --;
	    	maxEnergyLvl += HIT_ENERGY;
		}else{
			health = DEATH_HEALTH;
			dead = true;
		}
    }
    
    /**
     * check if you can add @param quant to your energy, if so adds it and
     * @return that you can. otherwise returns false
     */
    private boolean changeEnergy(int quant){
    	if(curEnergyLvl + quant < MIN_ENERGY){
    		return false;
    	}
    	curEnergyLvl += quant;
    	return true;
    }
    
    /**
     * every turn time this function is called: it takes energy as is used in
     * the turn indirectly, and gives the resting energy
     */
    protected void constEnergy(){
    	if(lastFire < FIRE_GAP){
    			lastFire ++;
    	}
    	if(shieldUp){
    		if(!changeEnergy(SHIELD_ENERGY))
    			shieldUp = false;
    	}
    	if(curEnergyLvl < maxEnergyLvl)
    		curEnergyLvl += CHARGING_ENERGY;
    }
    
    /**
     * This method is called every time a collision with this ship occurs
     * changes energy and health depanding if the shield is up
     */
    public void collidedWithAnotherShip(){
    	if(!dead){
    		if(shieldUp){
    			changeEnergy(BASHING_ENERGY);
    			maxEnergyLvl += BASHING_ENERGY;
    		}else{
    			reduceHealth();    			
    		}
    	}
    }

    /** 
     * This method is called whenever a ship has died. It resets the ship's 
     * attributes, and starts it at a new random position.
     */
    public void reset(){
		phys = new SpaceShipPhysics();
		health = MAX_HEALTH;
		maxEnergyLvl = MAX_ENERGY;
		curEnergyLvl = MAX_ENERGY;
		lastFire = FIRE_GAP;
		shieldUp = false;
		dead = false;
    }

    /**
     * Checks if this ship is dead.
     * @return true if the ship is dead and rest. false otherwise.
     */
    public boolean isDead() {
        if(dead){
        	reset();
        	return true;
        }
        return false;
    }

    /**
     * Gets the physics object that controls this ship.
     * 
     * @return the physics object that controls the ship.
     */
    public SpaceShipPhysics getPhysics() {
        return phys;
    }

    /**
     * This method is called by the SpaceWars game object when ever this ship
     * gets hit by a shot.
     */
    public void gotHit() {
    	if(!dead){
    		if(shieldUp){
    			reduceHealth();   			
			}
    	}
    }

    /**
     * Gets the image of this ship. This method should return the image of the
     * ship with or without the shield. This will be displayed on the GUI at
     * the end of the round.
     * 
     * @return the image of this ship.
     */
    public abstract Image getImage();

    /**
     * Attempts to fire a shot.
     * @param game the game object.
     */
    public void fire(SpaceWars game){
    	if(lastFire == FIRE_GAP){
    		if(changeEnergy(FIRE_ENERGY)){
        		game.addShot(phys);
        		lastFire = 0;
        	}
    	}
    }

    /**
     * Attempts to turn on the shield.
     */
    public void shieldOn() {
        if(changeEnergy(SHIELD_ENERGY)){
        	shieldUp = true;
        }
    }

    /**
     * Attempts to teleport.
     */
    public void teleport() {
       if(changeEnergy(TELEPORT_ENERGY)){
    	   phys = new SpaceShipPhysics();
       }
    }
    
}
