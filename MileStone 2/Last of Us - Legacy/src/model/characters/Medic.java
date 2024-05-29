package model.characters;

import exceptions.*;

public class Medic extends Hero {
	//Heal amount  attribute - quiz idea
	

	public Medic(String name,int maxHp, int attackDmg, int maxActions) {
		super( name, maxHp,  attackDmg,  maxActions) ;
		
		
	}
	public void useSpecial() throws NoAvailableResourcesException,InvalidTargetException {
		if(this.getTarget() instanceof Zombie) {
			throw new InvalidTargetException("Target Not Hero");
		}
			else if(this.getSupplyInventory().isEmpty()) {
			throw new NoAvailableResourcesException("No Supplies");
		}
		else {
			if(((int)(this.getLocation().distance(this.getTarget().getLocation())))>1){
				throw new InvalidTargetException("Out of Range");
			}
			if(this.getTarget() instanceof Hero) {
				this.setSpecialAction(true);
				this.getSupplyInventory().get(0).use(this);
				this.getTarget().setCurrentHp(this.getTarget().getMaxHp());
			}
		}
	}


}
