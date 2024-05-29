package engine;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import model.characters.*;
import model.characters.Character;
import model.world.Cell;


public class Game {
	public static ArrayList<Hero> availableHeroes;
	public static ArrayList<Hero> heroes;
	public static ArrayList<Zombie> zombies;
	public static Cell [][] map;
	public static void loadHeroes(String path) throws IOException,FileNotFoundException{
		String Line = "";
			BufferedReader br = new BufferedReader(new FileReader(path));
			
			while ((Line = br.readLine()) != null) {
				String[] value = Line.split(",");
				Hero Char=null;
				switch(value[1]) {
					case("FIGH"):
						Char=new Fighter(value[0],Integer.parseInt(value[2]),Integer.parseInt(value[4]),Integer.parseInt(value[3]));break;
					case("MED"):
						Char=new Medic(value[0],Integer.parseInt(value[2]),Integer.parseInt(value[4]),Integer.parseInt(value[3]));break;
					case("EXP"):
						Char=new Explorer(value[0],Integer.parseInt(value[2]),Integer.parseInt(value[4]),Integer.parseInt(value[3]));break;
				}
				availableHeroes.add(Char);
			}
			
		
		
	}
}
