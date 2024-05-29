package model.collectibles;
import exceptions.*;
import model.characters.Hero;

public class Supply implements Collectible  {

    public void pickUp(Hero h) {
    	h.getSupplyInventory().add(this);
    }
	
	public void use(Hero h) throws NoAvailableResourcesException  {
		if(h.getSupplyInventory().isEmpty()) {
			throw new NoAvailableResourcesException("No Supplies in List");
		}
		else {
			h.getSupplyInventory().remove(this);
		}
		
	}

	
	public Supply() {
		
	}


	
		
		

}
