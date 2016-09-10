package deafault;

public class SpaceShipFactory {
	/**
	 *it creates ships according to the @param args 
	 *if there's an "s" before "h" its in the human team. if there's "s"after
	 *it's'against that team
	 * @return a list of those ships
	 */
    public static SpaceShip[] createSpaceShips(String[] args) {
    	boolean preHuman = true;
    	SpaceShip[] ships = new SpaceShip[args.length];
    	for(int index = 0; index < args.length; index ++){
    		if(args[index].equals("h")){
    			ships[index] = new HumanShip();
    			preHuman = false;
    		}else if(args[index].equals("r"))
    			ships[index] = new RunnerShip();
    		else if(args[index].equals("b"))
    			ships[index] = new BasherShip();
    		else if(args[index].equals("a"))
    			ships[index] = new AggressiveShip();
    		else if(args[index].equals("d"))
    			ships[index] = new DrunkardShip();
    		else if(args[index].equals("s"))
    			ships[index] = new SpecialShip(preHuman);
    	}
        return ships;
    }
}
