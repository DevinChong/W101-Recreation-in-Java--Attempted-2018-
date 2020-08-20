package Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import Cards.Card;
import HangingEffects.Aura;
import HangingEffects.Bubble;
import HangingEffects.Charm;
import HangingEffects.Damage;
import HangingEffects.DamageOverTime;
import HangingEffects.Heal;
import HangingEffects.Sun;
import HangingEffects.Ward;
public class Deck {
	private String school;
	private int level=1;
	private int deckSize=0;
	private ArrayList<Card> spells=new ArrayList<Card>();
	private ArrayList<Card> deck=new ArrayList<Card>();
	
	public Deck(String s, int l){
		school=s;
		level=l;
		loadSpells(s.toLowerCase());
		loadSpells("secondary");
	}
	public ArrayList<Card> getDeck(){return deck;}
	
	public void add(String spell){
		if(deck.size()<64){
			Card c=find(spell);
			if(c==null){System.out.println("Spell \""+spell+"\" not avialable");}
			else{deck.add(c);deckSize++;}
		}else{System.out.println("Deck has reached its maximum limit!");}
	}
	public void add(String spell, int amount){
		if(deck.size()+amount<64){
			for(int i=0;i<amount;i++){add(spell);}
		}else{
			int size=deck.size();
			for(int i=0;i<(65-size);i++){add(spell);}
		}
	}
	public void remove(String spell){
		for(int i=0;i<deck.size();i++){
			if(deck.get(i).toString().equalsIgnoreCase(spell)){
				deck.remove(i);deckSize--;
			}
		}
	}
	public void remove(String spell, int amount){
		for(int i=0;i<amount;i++){remove(spell);}
	}
	public void listSpells(){
		String s="*Available Spells*\n";
		for(int i=0;i<spells.size();i++){s+=spells.get(i).toString()+"\n";}
		s+="\n";
		System.out.println(s);
	}
	public void showDeck(){
		String s="*Spells in Deck*\n";
		int amount=1;
		for(int i=0;i<deck.size();i++){
			try{
				if(deck.get(i+1).toString().equals(deck.get(i).toString())){
					amount++;
				}else{
					s+=amount+" "+deck.get(i)+"\n";
					amount=1;
				}
			}catch(IndexOutOfBoundsException e){s+=amount+" "+deck.get(i)+"\n";amount=1;}
		}System.out.println(s);
	}
	public int getDeckSize(){return deckSize;}
	public Card find(String spell){
		for(int i=0;i<spells.size();i++){
			if(spells.get(i).toString().equalsIgnoreCase(spell)){
				return spells.get(i);
			}
		}
		return null;
	}
	private void loadSpells(String t){
		try{
			Scanner list = new Scanner(new File(t));
			int l=list.nextInt();
			while(l<=level){		//name		school		pip				accuracy	  clickType
					Card c=new Card(list.next(),list.next(),list.nextInt(),list.nextInt(),list.next());
					String type=list.next().toLowerCase();
					while(!type.equals("end")){
						if(type.equals("damage")){c.addEffect(new Damage(list.next(), list.nextInt(),list.nextInt(),list.next()));}
						else if(type.equals("heal")){c.addEffect(new Heal(list.nextInt(),list.nextInt()));}
						else if(type.equals("+charm")){c.addEffect(new Charm(list.next(),list.next(),list.nextInt(),list.next()));}
						else if(type.equals("-charm")){c.addEffect(new Charm(list.next(),list.next(),-1*list.nextInt(),list.next()));}
						else if(type.equals("+ward")){c.addEffect(new Ward(list.next(),list.next(),list.nextInt(),list.next()));}
						else if(type.equals("-ward")){c.addEffect(new Ward(list.next(),list.next(),-1*list.nextInt(),list.next()));}
						else if(type.equals("bubble")){c.addEffect(new Bubble(list.next(),list.next(),list.nextInt()));}
						else if(type.equals("dot")){c.addEffect(new DamageOverTime(list.next(), "D", list.nextInt(),list.nextInt(),list.nextInt(),list.next()));}
						else if(type.equals("hot")){c.addEffect(new DamageOverTime(list.next(), "H", list.nextInt(),list.nextInt(),list.nextInt(),list.next()));}
						else if(type.equals("sun")){c.addEffect(new Sun(list.next(),list.nextInt()));}
						else if(type.equals("aura")){
							Aura aura=new Aura(list.next());
							while(!type.equals("end")){
								if(type.equals("+charm")){aura.addEffect(new Charm(list.next(),list.next(),list.nextInt(),list.next()));}
								else if(type.equals("-charm")){aura.addEffect(new Charm(list.next(),list.next(),-1*list.nextInt(),list.next()));}
								else if(type.equals("+ward")){aura.addEffect(new Ward(list.next(),list.next(),list.nextInt(),list.next()));}
								else if(type.equals("-ward")){aura.addEffect(new Ward(list.next(),list.next(),-1*list.nextInt(),list.next()));}
								type=list.next().toLowerCase();
							}c.addEffect(aura);
						}
						if(!type.equals("end")){
							type=list.next().toLowerCase();
						}
					}
					spells.add(c);
				//Reserve else-if for manipulation spells//
				if(list.hasNext()){
					l=list.nextInt();
				}else{break;}
			}
		}catch(IOException e){System.out.println("\"fire\" file not found");}
	}
}
