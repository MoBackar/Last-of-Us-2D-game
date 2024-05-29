package model.characters;


import exceptions.*;
import exceptions.NotEnoughActionsException;

public class Fighter extends Hero{

	
	public Fighter(String name,int maxHp, int attackDmg, int maxActions) {
		super( name, maxHp,  attackDmg,  maxActions) ;
		
	}
	public void attack() throws NotEnoughActionsException,InvalidTargetException {
		if(this.getActionsAvailable()==0&&!this.isSpecialAction()) {
			throw new NotEnoughActionsException("0 actions available");
		}
		else if(this.getTarget() ==null || this.getTarget() instanceof Hero) {
			throw new InvalidTargetException("no target available");
		}
		else if(((int)this.getLocation().distance(this.getTarget().getLocation()))>1){
			throw new InvalidTargetException("target is not in adjacent cell");
		}
		else {
			this.getTarget().setCurrentHp(this.getTarget().getCurrentHp()-this.getAttackDmg());
			if(this.isSpecialAction()==false) {
			this.setActionsAvailable(this.getActionsAvailable()-1);
			}
			this.getTarget().defend(this);
			this.onCharacterDeath();this.getTarget().onCharacterDeath();
		}
	}
	

	
	
	

}
