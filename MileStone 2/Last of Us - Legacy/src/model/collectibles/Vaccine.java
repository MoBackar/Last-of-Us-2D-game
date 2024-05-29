package model.collectibles;

import java.awt.Point;

import engine.Game;
import exceptions.NoAvailableResourcesException;
import model.characters.Hero;
import model.world.*;

public class Vaccine implements Collectible {

	 public void pickUp(Hero h) {
	    	h.getVaccineInventory().add(this);
	    }
		
		public void use(Hero h) throws NoAvailableResourcesException  {
			if(h.getVaccineInventory().isEmpty()) {
				throw new NoAvailableResourcesException("No Supplies in List");
			}
			else {
				h.getVaccineInventory().remove(this);
				Game.zombies.remove(h.getTarget());
				int r=(int)(Math.random()*Game.availableHeroes.size());
				Hero NH=Game.availableHeroes.remove(r);
				Game.heroes.add(NH);
				NH.setLocation(new Point(h.getTarget().getLocation().x,h.getTarget().getLocation().y));
				((CharacterCell)(Game.map[NH.getLocation().x][NH.getLocation().y])).setCharacter(NH);
				h.setTarget(null);

			}
			
		}
	public Vaccine() {
		
	}

}
