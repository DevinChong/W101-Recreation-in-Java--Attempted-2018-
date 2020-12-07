package Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import Cards.Card;
import HangingEffects.Bubble;
import HangingEffects.Charm;
import HangingEffects.Effect;
import HangingEffects.Ward;
import HangingEffects.DamageOverTime;

public class Player extends Deck{
	private String school,name;
	private int level=1;
	private boolean enemy=false;
	private boolean cpu=false;
	private ArrayList<Boolean> pips=new ArrayList<Boolean>(7);
	private ArrayList<Charm> charms=new ArrayList<Charm>();
	private ArrayList<Ward> wards=new ArrayList<Ward>();
	private ArrayList<DamageOverTime> dots=new ArrayList<DamageOverTime>();
	private Card[] hand = new Card[7];
	
	//stats
	private int health=500, mana=50,pip=40,shadow=0,stun=0,incoming=0,outgoing=0;
	private int[] damage=new int[8],resist=new int[8],accuracy=new int[8],critical=new int[8],block=new int[8],pierce=new int[8],conversion=new int[8];
	

	public Player(String name, String school, int level){
		super(school,level);
		this.name=name;
		this.school=school.toLowerCase();
		this.level=level;
		loadStats();
	}
	public void generatePip(){
		Random r=new Random();
		if(pips.size()!=7){
			if(r.nextInt(100)<pip){
				pips.add(true);
				if(name.indexOf(" ")!=-1){
					System.out.println("***"+name.substring(0, name.indexOf(" "))+" gained a power pip!");
				}else{
					System.out.println("***"+name+" gained a power pip!");
				}
			}else{pips.add(false);}
		}
	}
	public int countPPips(String s){
		int p=0;
		for(int i=pips.size()-1;i>=0;i--){
			if(pips.get(i)==true&&s.equals(school)){
				p+=2;
			}else{p+=1;}
		}
		return p;
	}
	private void loadStats(){
		try{
			Scanner stats = new Scanner(new File("stats"));
			while(stats.hasNextLine()){
				String s=stats.next();
				if(s.charAt(0)=='h'||s.charAt(0)=='H'){
					health=stats.nextInt();
				}else if(s.charAt(0)=='m'||s.charAt(0)=='M'){
					mana=stats.nextInt();
				}else if(s.charAt(0)=='d'||s.charAt(0)=='D'){
					for(int i=0;i<8;i++){damage[i]=stats.nextInt();}
				}else if(s.charAt(0)=='r'||s.charAt(0)=='R'){
					for(int i=0;i<8;i++){resist[i]=stats.nextInt();}
				}else if(s.charAt(0)=='a'||s.charAt(0)=='A'){
					for(int i=0;i<8;i++){accuracy[i]=stats.nextInt();}
				}else if((s.charAt(0)=='c'||s.charAt(0)=='C')&&s.charAt(0)=='r'){
					for(int i=0;i<8;i++){critical[i]=stats.nextInt();}
				}else if(s.charAt(0)=='b'||s.charAt(0)=='B'){
					for(int i=0;i<8;i++){block[i]=stats.nextInt();}
				}else if(s.charAt(0)=='p'||s.charAt(0)=='P'&&s.charAt(2)=='e'){
					for(int i=0;i<8;i++){pierce[i]=stats.nextInt();}
				}else if(s.charAt(0)=='i'||s.charAt(0)=='I'){
					incoming=stats.nextInt();
				}else if(s.charAt(0)=='o'||s.charAt(0)=='O'){
					outgoing=stats.nextInt();
				}else if((s.charAt(0)=='s'||s.charAt(0)=='S')&&s.charAt(1)=='t'){
					stun=stats.nextInt();
				}else if((s.charAt(0)=='c'||s.charAt(0)=='C')&&s.charAt(0)=='o'){
					for(int i=0;i<8;i++){conversion[i]=stats.nextInt();}
				}else if((s.charAt(0)=='p'||s.charAt(0)=='P')&&s.charAt(2)=='p'){
					pip+=stats.nextInt();
				}else if((s.charAt(0)=='s'||s.charAt(0)=='S')&&s.charAt(1)=='h'){
					shadow=stats.nextInt();
				}
			}
		}catch(IOException e){System.out.println("HP: 500, MP:50. Remaining stats will be 0.");}
	}
	public void printStats(){
		String s="*Player Stats*\n";
		s+="HP: "+health+" MP: "+mana+"\n";
		s+="Stat  F  I  S  M  L  D  B  Sh\n";
		s+="Dam: ";for(int i=0;i<8;i++){s+=damage[i]+" ";}s+="\n";
		s+="Res: ";for(int i=0;i<8;i++){s+=resist[i]+" ";}s+="\n";
		s+="Acc: ";for(int i=0;i<8;i++){s+=accuracy[i]+" ";}s+="\n";
		s+="Crit: ";for(int i=0;i<8;i++){s+=critical[i]+" ";}s+="\n";
		s+="Block: ";for(int i=0;i<8;i++){s+=block[i]+" ";}s+="\n";
		s+="Pierce: ";for(int i=0;i<8;i++){s+=pierce[i]+" ";}s+="\n";
		s+="Stun Chance: "+stun+" Incoming/Outgoing: "+incoming+"/"+outgoing+"\n";
		s+="Pip Conv.: ";for(int i=0;i<8;i++){s+=conversion[i]+" ";}s+="\n";
		s+="Power Pip: "+pip+" Shadow Pip: "+shadow;
		System.out.println(s);
	}
	
	public String getName(){return name;}
	public String getSchool(){return school;}
	public int getSetHP(){return health;}
	//public int getHealth(){return health;}
	public int getDamage(String s){return damage[getSchoolNum(s)];}
	public int getResist(String s){return resist[getSchoolNum(s)];}
	public int getAccuracy(String s){return accuracy[getSchoolNum(s)];}
	public int getCritical(String s){return critical[getSchoolNum(s)];}
	public int getBlock(String s){return block[getSchoolNum(s)];}
	public int getPierce(String s){return pierce[getSchoolNum(s)];}
	public int getStunChance(){return stun;}
	public int getPipConversion(String s){return conversion[getSchoolNum(s)];}
	public int getIncoming(){return incoming;}
	public int getOutgoing(){return outgoing;}
	public int getPowerPipChance(){return pip;}
	public int getShadowPipChance(){return shadow;}
	private int getSchoolNum(String s){
		if(s.equals("fire")){return 0;}
		else if(s.equals("ice")){return 1;}
		else if(s.equals("storm")){return 2;}
		else if(s.equals("myth")){return 3;}
		else if(s.equals("life")){return 4;}
		else if(s.equals("death")){return 5;}
		else if(s.equals("balance")){return 6;}
		else if(s.equals("shadow")){return 7;}
		return -1;
	}
	public Card[] getHand(){return hand;}
	public Card getHand(int pos){return hand[pos];}
	public ArrayList<Charm> getCharms(){return charms;}
	public Charm getCharm(int pos){return charms.get(pos);}
	public ArrayList<Ward> getWards(){return wards;}
	public DamageOverTime getDoT(int pos){return dots.get(pos);}
	public ArrayList<DamageOverTime> getDoTs(){return dots;}
	public Ward getWard(int pos){return wards.get(pos);}
	public int getPips(){
		int p=0;
		for(int i=pips.size()-1;i>=0;i--){
			if(pips.get(i)==true){
				p+=2;
			}else{p+=1;}
		}
		return p;
	}
	public int getCountedPips(){
		return pips.size();
	}
	public void toggleEnemy(){enemy=!enemy;}
	public boolean isEnemy(){return enemy;}
	public void toggleCPU(){cpu=!cpu;}
	public boolean isCPU(){return cpu;}
	public void setHand(Card c, int pos){hand[pos]=c;}
	public void addCharm(Charm c){charms.add(c);}
	public void addWard(Ward w){wards.add(w);}
	public void addDoT(DamageOverTime d){dots.add(d);}
	public void removePips(int p, boolean pp){
		if(pp==true){
			p=(p/2)+(p%2);
		}
		int size=pips.size();
		for(int i=size-1;i>size-p-1;i--){
			pips.remove(i);
		}
	}
	public String toString(){return name;}
	public void clearEffects(){
		charms.clear();
		wards.clear();
		dots.clear();
	}
}
