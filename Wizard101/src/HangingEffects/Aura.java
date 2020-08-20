package HangingEffects;

import java.util.ArrayList;
import java.util.HashSet;

public class Aura extends Effect{
	private String type; //charm, ward
						 //Basically effectType since aura is taken
	private int rounds=4;
	private int currentRound;
	private HashSet<Effect> effects=new HashSet<Effect>();
	public Aura(String type) {
		super("star", "aura", -1, "self");
		this.type=type;
		currentRound=rounds;
	}
	public Aura(String type,int rounds) {//custom rounds. NOT added in Deck class
		super("star", "aura", -1, "self");
		this.type=type;
		this.rounds=rounds;
		currentRound=rounds;
	}
	public String getType(){
		return type;
	}
	public int getRounds(){
		return rounds;
	}
	public int getCurrentRound(){
		return currentRound;
	}
	public void decreaseRound(){
		--currentRound;
	}
	public void addEffect(Effect h){effects.add(h);}
	public HashSet<Effect> getEffect(){return effects;}
}
