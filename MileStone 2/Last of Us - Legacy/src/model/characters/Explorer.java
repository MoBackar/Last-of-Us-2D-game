package model.characters;
import engine.*;

import exceptions.InvalidTargetException;
import exceptions.NoAvailableResourcesException;

public class Explorer extends Hero {
	

	public Explorer(String name,int maxHp, int attackDmg, int maxActions) {
		super( name, maxHp,  attackDmg,  maxActions) ;
		
	}
	public void useSpecial() throws NoAvailableResourcesException,InvalidTargetException {
		if(this.getSupplyInventory().isEmpty()) {
			throw new NoAvailableResourcesException("No Supplies");
		}
		else {
			this.setSpecialAction(true);
			this.getSupplyInventory().get(0).use(this);
			for(int i=0;i<Game.map.length;i++) {
				for(int j=0;j<Game.map[i].length;j++) {
					Game.map[i][j].setVisible(true);
				}
			}
		}
	}
	

	
	

	
}
