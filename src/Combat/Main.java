package Combat;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

import Cards.Card;
import HangingEffects.Bubble;
import HangingEffects.Charm;
import HangingEffects.Damage;
import HangingEffects.Effect;
import HangingEffects.Heal;
import HangingEffects.Ward;
import Player.Player;

public class Main extends CPUAlgorithm{
	private boolean end=false;
	private boolean playerTurn;
	protected Arena arena;
	public Main(Arena a){
		super(a);
		arena=a;
	}	
	private void generateFirst(){
		if(r.nextInt(1)==0){
			playerTurn=true;
		}else{playerTurn=false;}
	}
	protected int promptHand(){
		int c=-1;
		boolean selected=false;
		while(selected==false){
			Scanner prompt = new Scanner(System.in);
			System.out.println("Select a card");
			String s=prompt.next();
			try{
				c=Integer.parseInt(s)-1;
			}catch(NumberFormatException e){
				if(s.equalsIgnoreCase("pass")){
					pass();
					break;
				}
			}
			if(c<8&&c>=0){
				try{selected=pipCheck(arena.getPlayer(0).getHand(c),arena.getPlayer(0));}
				catch(NullPointerException e){}
			}
		}
		return c;
	}
	protected int promptPosition(int c,Player p){
		boolean position=true;
		int pos=-10;
		String s="";
		Scanner prompt = new Scanner(System.in);
		Card card=p.getHand(c);
		while(position==true){
			try{
				if(card.getSchool().equals("sun")){
					System.out.println("Select a Card. Type \"cancel\" to cancel.");
					s=prompt.next();
					pos=Integer.parseInt(s);
					if(!p.getHand(pos).toString().equals("Enchant")){
						useEnchant(p,pos,c);
						display();
						position=false;
						pos=-1;
					}
				}else{
					System.out.println("Select Position. Type \"cancel\" to cancel.");
					s=prompt.next();
					pos=Integer.parseInt(s);
					if(pos<8&&pos>=0){
						if(positionChecker(card,arena.getPlayer(0),pos)==true){
							position=false;
						}
					}
				}
			}catch(NumberFormatException e){
				if(s.equalsIgnoreCase("cancel")){
					return -1;
				}
			}
		}
		return pos;
	}
	public void pass(){
		pass=true;
	}
	public void generatePips(){
		for(int i=0;i<7;i++){
			try{
				arena.getPlayer(i).generatePip();
			}catch(NullPointerException e){}
		}
	}
	public void printHand(){
		System.out.println("     1     2     3     4     5     6     7     (Type \"pass\" to pass)");
			for(int i=0;i<7;i++){
				String s;
				try{
					s=arena.getPlayer(0).getHand(i).toString();
				}catch(NullPointerException e){s="blank";}
					if(s.length()>6){
						System.out.print(s.substring(0,6)+" ");
					}else{System.out.print(s+" ");}
				
			}	
	}
	public void display(){
		String s="";
		for(int i=0;i<8;i++){
			if(i==4){s+="\n";}
			try{
				s+=arena.getPlayer(i).getName()+"("+arena.getPlayer(i).getPips()+") HP: "+arena.getHealth(i)+"; ";
			}catch(NullPointerException e){}
		}
		System.out.println(s);
		printHand();
		
	}

	public void play(){
		//non-GUI
		generateFirst();
		while(end==false){
			generatePips();
			display();
			//Prompts
			boolean promptCleared=false;
			int pos=-1;
			int c=-1;
			while(promptCleared==false){
				c=promptHand();
				if(c!=-1&&pass==false){
					if(arena.getPlayer(0).getHand(c).getChooseable()==true){
						pos=promptPosition(c,arena.getPlayer(0));
						if(pos>-1){
							promptCleared=true;
						}	
					}else{
						promptCleared=true;
					}
				}else if(pass==true){
					promptCleared=true;
				}
			}
			//Round starts
			useDoTRound(0);
			useCard(c,arena.getPlayer(0),pos);
			arena.deathChecker();
			redrawHand(arena.getPlayer(0));
			//Enemy and ally turn
			for(int i=1;i<8;i++){
				try{
					useDoTRound(i);
					arena.deathChecker();
					cpuAlgorithmRun(i);
				}catch(NullPointerException e){
					//System.out.println("Skiped player "+i);
				}
			}
			end=arena.endCheck();
		}
		clearEffects();
		endText();
	}
	public Arena getArena(){
		return arena;
	}
	public static void main(String args[]){
		Player p = new Player("Kevin Fireflame","fire", 90);
		p.add("Firecat", 4);
		//p.add("Fireblade", 2);
		//p.add("Fire_Elf",1);
		//p.add("Tower_Shield", 1);
		//p.add("Elemental_Blade");
		//p.add("Feint");
		//p.add("Meteor_Strike",1);
		//p.add("King_Artorius",2);
		p.add("Colossal",3);
		//p.addCharm((Charm)p.deck().find("Fireblade").getEffect().get(0));
		//p.addWard((Ward)p.deck().find("Tower_Shield").getEffect().get(0));
		Damage card=(Damage)p.find("Firecat").getEffect().get(0);
		
		Player e = new Player("Devin Chong","fire",85);
		e.toggleCPU();
		e.add("Firecat", 2);
		e.add("Fireblade", 2);
		e.add("Tower_Shield", 7);
		e.add("Elemental_Blade");
		e.add("Feint");
		e.add("Meteor_Strike",1);	
		Main m=new Main(new Arena(p,e));
		m.play();
	}
	//Problems:
	//paintComponent NOT called
	//Add PASS Button
	//Add all spells(+Download Images)
	
	//Add Sun Spells (Tools/Main)
	//-Create new Effect
	//-Card removal
	//Add Pierce
	//Add Aura (Tools)
	//-Test multiple uses (rounds overlap?)
	//Add Moon Spells
	//Add Manipulation: Summon, Stun, Steal, etc.
	//CPU prompt Al-gore ithm
	//-Solution: Add Aggro
	
	//Bonus: Make decks resetable
	//Make Load-spells more efficient
}
