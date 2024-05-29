package engine;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import model.characters.*;
import model.characters.Explorer;
import model.characters.Fighter;
import model.characters.Hero;
import model.characters.Medic;
import model.characters.Zombie;
import model.world.*;
import java.util.*;

import model.collectibles.*;

public class Game {
	
	public static Cell [][] map=new Cell[15][15] ;
	public static ArrayList <Hero> availableHeroes = new ArrayList<Hero>();
	public static ArrayList <Hero> heroes =  new ArrayList<Hero>();
	public static ArrayList <Zombie> zombies =  new ArrayList<Zombie>();

	
	
		
	public static void loadHeroes(String filePath)  throws IOException {
		
		
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine();
		while (line != null) {
			String[] content = line.split(",");
			Hero hero=null;
			switch (content[1]) {
			case "FIGH":
				hero = new Fighter(content[0], Integer.parseInt(content[2]), Integer.parseInt(content[4]), Integer.parseInt(content[3]));
				break;
			case "MED":  
				hero = new Medic(content[0], Integer.parseInt(content[2]), Integer.parseInt(content[4]), Integer.parseInt(content[3])) ;
				break;
			case "EXP":  
				hero = new Explorer(content[0], Integer.parseInt(content[2]), Integer.parseInt(content[4]), Integer.parseInt(content[3]));
				break;
			}
			availableHeroes.add(hero);
			line = br.readLine();
			
			
		}
		br.close();	
	}
	public static void startGame(Hero h) {
		for(int i=0;i<Game.map.length;i++) {
			for(int j=0;j<Game.map[i].length;j++) {
				map[i][j]=new CharacterCell(null);
			}
		}
			((CharacterCell)(map[0][0])).setCharacter(h);
			h.setLocation(new Point(0,0));
			(map[0][0]).setVisible(true);
			availableHeroes.remove(h);
			heroes.add(h);
			respawnZ(10);
			spawnT(5);
			spawnV(5);
			spawnS(5);
			h.visible();
		
	}

	public static boolean checkWin() {
		int count_H=0;
		int count_V=0;
		for(int i=0;i<Game.map.length;i++) {
			for(int j=0;j<Game.map[i].length;j++) {
				if(map[i][j] instanceof CollectibleCell) {
					if(((CollectibleCell)map[i][j]).getCollectible() instanceof Vaccine) {
						count_V++;
					}
				}
				if(map[i][j] instanceof CharacterCell) {
					if(((CharacterCell)map[i][j]).getCharacter() instanceof Hero) {
						count_H++;
					}
				}
			}
		}
		
		if ((count_V==0) && (count_H >=5)) {
			for(int i=0;i<heroes.size();i++) {
				if(!(heroes.get(i)).getVaccineInventory().isEmpty()){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	public static boolean checkGameOver() {
		int count_H=0;
		int count_V=0;
		int count_VI=0;
		for(int i=0;i<Game.map.length;i++) {
			for(int j=0;j<Game.map[i].length;j++) {
				if(map[i][j] instanceof CharacterCell) {
					if(((CharacterCell)map[i][j]).getCharacter() instanceof Hero) {
						count_H++;
					}	
					
				}
				if(map[i][j] instanceof CollectibleCell) {
					if(((CollectibleCell)map[i][j]).getCollectible() instanceof Vaccine) {
						count_V++;
					}
				}
			}
		}
		if(count_H==0) {
			return true;
		}
		else {
			 
				for(int i=0;i<heroes.size();i++) {
					if(!(heroes.get(i)).getVaccineInventory().isEmpty()){
						count_VI=count_VI+heroes.get(i).getVaccineInventory().size();
					}
				}
				if(count_V==0&&count_VI==0&&count_H<5) {
					return true;
				}
				else {
					return false;	
				}	
					
		}
			
	}
	
	
	
	public static void endTurn() {
		for(int i=0;i<heroes.size();i++) {
			heroes.get(i).setSpecialAction(false);
		}
		for(int m=0;m<zombies.size();m++) {
			Zombie Z=zombies.get(m);
			Z.attack();
			Z.setTarget(null);
		}
		for(int i=0;i<Game.map.length;i++) {
			for(int j=0;j<Game.map[i].length;j++) {
				map[i][j].setVisible(false);
			}
		}
		respawnZ(1);
		for(int i=0;i<heroes.size();i++) {
			heroes.get(i).visible();
			int x=heroes.get(i).getLocation().x;
			int y=heroes.get(i).getLocation().y;
			map[x][y].setVisible(true);
			heroes.get(i).setActionsAvailable(heroes.get(i).getMaxActions());
			heroes.get(i).setTarget(null);
		}
		
		
		
	}
	
	
	
	public static void respawnZ(int K){
		boolean flag;
		for(int i=0;i<K;i++) {
			flag=false;
			while(!flag) {
				int x=(int)(Math.random()*15);
				int y=(int)(Math.random()*15);
				if(map[x][y] instanceof CharacterCell) {
					if(((CharacterCell)map[x][y]).getCharacter()==null) {
						Zombie Z= new Zombie();
						Z.setLocation(new Point(x,y));
						zombies.add(Z);
						((CharacterCell)map[x][y]).setCharacter(Z);
						flag=true;
					}
				}
			}
		}
		
	}
	public static void spawnT(int K){
		boolean flag;
		for(int i=0;i<K;i++) {
			flag=false;
			while(!flag) {
				int x=(int)(Math.random()*15);
				int y=(int)(Math.random()*15);
				if(map[x][y] instanceof CharacterCell) {
					if(((CharacterCell)map[x][y]).getCharacter()==null) {
						map[x][y]=new TrapCell();
						flag=true;
					}
				}
			}
		}
		
	}
	public static void spawnV(int K){
		boolean flag;
		for(int i=0;i<K;i++) {
			flag=false;
			while(!flag) {
				int x=(int)(Math.random()*15);
				int y=(int)(Math.random()*15);
				if(map[x][y] instanceof CharacterCell) {
					if(((CharacterCell)map[x][y]).getCharacter()==null) {
						map[x][y]=new CollectibleCell(new Vaccine());
						flag=true;
					}
				}
			}
		}
		
	}
	public static void spawnS(int K){
		boolean flag;
		for(int i=0;i<K;i++) {
			flag=false;
			while(!flag) {
				int x=(int)(Math.random()*15);
				int y=(int)(Math.random()*15);
				if(map[x][y] instanceof CharacterCell) {
					if(((CharacterCell)map[x][y]).getCharacter()==null) {
						map[x][y]=new CollectibleCell(new Supply());
						flag=true;
					}
				}
			}
		}
		
	}
	
}
