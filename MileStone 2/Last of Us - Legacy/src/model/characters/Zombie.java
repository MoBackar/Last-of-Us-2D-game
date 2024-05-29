package model.characters;

import java.awt.Point;

import engine.Game;
import model.world.CharacterCell;

public class Zombie extends Character {
	static int ZOMBIES_COUNT = 1;
	
	public Zombie() {
		super("Zombie " + ZOMBIES_COUNT, 40, 10);
		ZOMBIES_COUNT++;
	}
	public void attack(){
		for(int i=0;i<Game.map.length;i++) {
			for(int j=0;j<Game.map[i].length;j++) {
				if(this.ADJ(new Point(i,j))) {
					if(Game.map[i][j] instanceof CharacterCell) {
						if(((CharacterCell)Game.map[i][j]).getCharacter() instanceof Hero) {
							this.setTarget(((CharacterCell)Game.map[i][j]).getCharacter());
						}
					}
				}
			}
			
		}
		if(this.getTarget()!=null) {
			this.getTarget().setCurrentHp(this.getTarget().getCurrentHp()-this.getAttackDmg());
			this.getTarget().defend(this);
			this.onCharacterDeath();this.getTarget().onCharacterDeath();
			
		}
	}
	public void defend(Character c) {
		c.setCurrentHp(c.getCurrentHp()-(this.getAttackDmg()/2));
	}

	public void onCharacterDeath() {
		if(this.getCurrentHp()==0) {
			Game.respawnZ(1);
			Game.zombies.remove(this);
			((Game.map[this.getLocation().x][this.getLocation().y]))=new CharacterCell(null);
			
			
		}
	}
}


