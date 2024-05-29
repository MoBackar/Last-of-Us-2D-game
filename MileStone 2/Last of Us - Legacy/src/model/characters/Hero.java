package model.characters;
import 	exceptions.*;

import java.awt.Point;
import java.util.ArrayList;
import model.collectibles.Supply;
import model.collectibles.Vaccine;
import engine.*;
import model.world.*;


public abstract class Hero extends Character {
	

		private int actionsAvailable;
		private int maxActions;
		private ArrayList<Vaccine> vaccineInventory;
		private ArrayList<Supply> supplyInventory;
		private boolean specialAction;
	
		
		public Hero(String name,int maxHp, int attackDmg, int maxActions) {
			super(name,maxHp, attackDmg);
			this.maxActions = maxActions;
			this.actionsAvailable = maxActions;
			this.vaccineInventory = new ArrayList<Vaccine>();
			this.supplyInventory=new ArrayList<Supply>();
			this.specialAction=false;
		
		}

		
	


		public boolean isSpecialAction() {
			return specialAction;
		}



		public void setSpecialAction(boolean specialAction) {
			this.specialAction = specialAction;
		}



		public int getActionsAvailable() {
			return actionsAvailable;
		}



		public void setActionsAvailable(int actionsAvailable) {
			this.actionsAvailable = actionsAvailable;
		}



		public int getMaxActions() {
			return maxActions;
		}



		public ArrayList<Vaccine> getVaccineInventory() {
			return vaccineInventory;
		}


		public ArrayList<Supply> getSupplyInventory() {
			return supplyInventory;
		}
		
		public void attack() throws NotEnoughActionsException,InvalidTargetException {
			if(this.getActionsAvailable()==0) {
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
				this.setActionsAvailable(actionsAvailable-1);
				this.getTarget().defend(this);
				this.onCharacterDeath();this.getTarget().onCharacterDeath();
				}
			
			
		}

		public void onCharacterDeath() {
			if(this.getCurrentHp()==0) {
				((CharacterCell)(Game.map[this.getLocation().x][this.getLocation().y])).setCharacter(null);
				Game.heroes.remove(this);
				Game.map[this.getLocation().x][this.getLocation().y].setVisible(false);
			}
		}
		public void move(Direction d) throws MovementException,NotEnoughActionsException {
			if(this.getActionsAvailable()==0) {
				throw new NotEnoughActionsException("0 actions available");
			}
			else if (this.getCurrentHp()<=0) {
				this.onCharacterDeath();
			}
				
			else {
				int x = this.getLocation().x;
				int y = this.getLocation().y;
				switch (d) {
				case UP : x = 
					x+1;break;
				case DOWN : 
					x= x-1;break;
				case LEFT : 
					y=y-1;break;
				case RIGHT : 
					y=y+1;break;
				}
				if((x>14)|| (y>14)||(x<0)||(y<0)) {
					throw new MovementException("Out Of Bound");
				}
				else if(Game.map[x][y] instanceof CollectibleCell) {
					((CollectibleCell)(Game.map[x][y])).getCollectible().pickUp(this);
					Game.map[x][y]=new CharacterCell(this,false);
					((CharacterCell)(Game.map[this.getLocation().x][this.getLocation().y])).setCharacter(null);
					this.setActionsAvailable(this.getActionsAvailable()-1);
					this.setLocation(new Point(x,y));
					this.visible();
				}
				else if(Game.map[x][y] instanceof TrapCell) {
					this.setCurrentHp(this.getCurrentHp()-((TrapCell)(Game.map[x][y])).getTrapDamage());
					Game.map[this.getLocation().x][this.getLocation().y]=new CharacterCell(null);
					if(this.getCurrentHp()==0) {
						this.onCharacterDeath();
					}
					else if (this.getCurrentHp()>0){
						Game.map[x][y]=new CharacterCell(this);
						Game.map[x][y].setVisible(true);
						this.setActionsAvailable(this.getActionsAvailable()-1);
						this.setLocation(new Point(x,y));
						this.visible();
					}
				}
				else if (Game.map[x][y] instanceof CharacterCell){
					if(((CharacterCell)Game.map[x][y]).getCharacter()==null) {
						Game.map[x][y]=new CharacterCell(this,false);
						((CharacterCell)(Game.map[this.getLocation().x][this.getLocation().y])).setCharacter(null);
						this.setActionsAvailable(this.getActionsAvailable()-1);
						this.setLocation(new Point(x,y));
						this.visible();
					}
					else {
						throw new MovementException("Occupied");
					}
				}
				else {
					throw new MovementException("Occupied");
				}
			}
		}
		
		public void useSpecial() throws NoAvailableResourcesException,InvalidTargetException {
			if(this.getSupplyInventory().isEmpty()) {
				throw new NoAvailableResourcesException("No Supplies");
			}
			else {
				this.getSupplyInventory().get(0).use(this);
				this.setSpecialAction(true);
			}
		}
	
		public void cure() throws InvalidTargetException, NoAvailableResourcesException,NotEnoughActionsException{
			if (this.getTarget() instanceof Hero) {
				throw new InvalidTargetException("target is not Zombie");
			}
			if(this.getActionsAvailable()==0) {
				throw new NotEnoughActionsException("0 actions available");
			}
			if(this.getTarget() instanceof Zombie) {
				if(this.getVaccineInventory().isEmpty()) {
					throw new NoAvailableResourcesException("No Vaccine");
				}
				else if(((int)this.getLocation().distance(this.getTarget().getLocation()))>1){
					throw new InvalidTargetException("target is not in adjacent cell");
				}
				else {
					this.getVaccineInventory().get(0).use(this);
					this.setActionsAvailable(this.getActionsAvailable()-1);
				}
			}
			else {
				throw new InvalidTargetException("target is not Zombie");
			}
		}
		
		public void visible() {
			for(int i=0;i<Game.map.length;i++) {
				for(int j=0;j<Game.map[i].length;j++) {
					if(this.ADJ(new Point(i,j))){
						Game.map[i][j].setVisible(true);
					}
				}
			
			}
		}
		
}
		

