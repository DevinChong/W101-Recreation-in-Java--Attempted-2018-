package Combat;

import java.util.ArrayList;

import Cards.Card;
import HangingEffects.Bubble;
import Player.Player;

public class Arena {
	private Player[] position=new Player[8];
	private int[] HP = new int[8];
	
	public Arena(Player player, Player cpu){
		addAlly(player);
		addEnemy(cpu);
	}
	public void addAlly(Player p){
		for(int i=0;i<4;i++){
			if(isEmpty(i)){
				position[i]=p;
				HP[i]=p.getSetHP();
				break;
			}
		}
	}
	public void addEnemy(Player p){
		for(int i=4;i<8;i++){
			if(isEmpty(i)){
				position[i]=p;
				position[i].toggleEnemy();
				HP[i]=p.getSetHP();
				break;
			}
		}
	}
	public void removePlayer(Player p){
		int f=finder(p);
		if(f!=-1){
			position[f]=null;
		}
	}
	public Player getPlayer(int p){
		return position[p];
	}
	public void deathChecker(){
		for(int i=0;i<8;i++){
			try{
				if(HP[i]<=0){
					position[i]=null;
				}
			}catch(NullPointerException e){}
		}
	}
	public int getPlayerPosition(Player p){
		return finder(p);
	}
	public int getHealth(int pos){
		return HP[pos];
	}
	//Testing hack
	//public void setHealth(int pos, int hp){HP[pos]=hp;}
	public void addHealth(int pos, int hp){
		if(hp>=0&&(HP[pos]+hp<=position[pos].getSetHP())){
			HP[pos]+=hp;
		}
		if(hp>position[pos].getSetHP()){
			HP[pos]=position[pos].getSetHP();
			}
		}
	public void subHealth(int pos, int hp){
		if(hp>=0){
			HP[pos]-=hp;
		}
		if(HP[pos]<0){
			HP[pos]=0;
			}
		}
	public boolean endCheck(){
		for(int i=0;i<8;i+=4){
			if(isEmpty(i)
			&&isEmpty(i+1)
			&&isEmpty(i+2)
			&&isEmpty(i+3)){
				return true;
			}
		}
		return false;
	}
	private int finder(Player p){
		int e=0;
		if(p.isEnemy()){e=4;}
		for(int i=e;i<4;i++){
			if(position[i]==p){
				return i;
			}
		}
		return -1;
	}
	private boolean isEmpty(int pos){
		try{
			position[pos].getName();
		}catch(NullPointerException n){
			return true;
		}return false;
	}
}
