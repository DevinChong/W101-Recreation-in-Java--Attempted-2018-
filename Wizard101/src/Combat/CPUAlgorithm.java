package Combat;

import Cards.Card;
import Player.Player;

public class CPUAlgorithm extends Tools {
	public CPUAlgorithm(Arena a){
		super(a);			
	}
	private int promptHand(Player p){
		boolean pCheck=false;
		int c=-1;
		while(pCheck==false){
			try{
				c=r.nextInt(8);
				if(c==7){
					pass=true;
					break;
				}else{
					pCheck=pipCheck(p.getHand(c),p);
				}
			}catch(NullPointerException e){}
		}return c;
	}
	private int promptPosition(Card c, Player p){
		int pos=0;
		while(positionChecker(c,p,pos)==false){
			pos=r.nextInt(8);
		}
		return pos;
	}
	public void cpuAlgorithmRun(int i){
		Player p=arena.getPlayer(i);
		displayMessage(p.getName()+"\'s turn!");
		int c=promptHand(p);
		if(pass==false){
			Card card=p.getHand(c);
			int pos=-1;
			if(card.getChooseable()==true){
				pos=promptPosition(card,p);
				useCard(c,p,pos);
			}else{useCard(c,p,pos);}
		}else{
			displayMessage(p.getName()+" passed!");
		}
		arena.deathChecker();
		redrawHand(p);
		pass=false;
	}
}
