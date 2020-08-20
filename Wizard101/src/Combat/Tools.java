package Combat;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import Cards.Card;
import HangingEffects.Aura;
import HangingEffects.Bubble;
import HangingEffects.Charm;
import HangingEffects.Damage;
import HangingEffects.Effect;
import HangingEffects.Heal;
import HangingEffects.Sun;
import HangingEffects.Ward;
import HangingEffects.DamageOverTime;
import Player.Player;

public class Tools{
	protected Random r = new Random();
	protected Arena arena;
	protected Bubble bubble=new Bubble("none","blank",100);
	protected boolean pass=false;
	public Tools(Arena a){
		arena=a;
		for(int i=0;i<8;i++){
			try{
				for(int j=0;j<7;j++){a.getPlayer(i).setHand(draw(a.getPlayer(i)),j);}
			}catch(NullPointerException e){}
		}
	}
	
	private Card draw(Player p){
		ArrayList<Card> deck = p.getDeck();
		if(deck.size()<=0){return null;}
		else{
			int num=r.nextInt(deck.size());
			Card c=deck.get(num);
			deck.remove(num);
			return c;
		}
	}
	public void redrawHand(Player p){
		for(int i=0;i<7;i++){
			if(p.getHand(i)==null){
				try{
					p.setHand(draw(p),i);
				}catch(Exception e){}
			}
		}
	}
	protected boolean positionChecker(Card card,Player p,int pos){
		try{arena.getPlayer(pos).getName();}
		catch(NullPointerException e){return false;}
		if(card.getClickType().equals("ally")){
			if(!p.isEnemy()){
				if(pos<4){return true;}
				else{
					if(!p.isCPU()){
						displayMessage("Select type: ally (Positions 0-3)");
					}return false;
				}
			}
			else if(p.isEnemy()){
				if(pos>3){return true;}
				else{
					if(!p.isCPU()){
						displayMessage("Select type: ally (Positions 4-7)");
					}return false;
				}
			}
		}else if(card.getClickType().equals("enemy")){
			if(!p.isEnemy()){
				if(pos>3){return true;}
				else{
					if(!p.isCPU()){
						displayMessage("Select type: enemy (Positions 4-7)");
					}return false;
				}
			}
			else if(p.isEnemy()){
				if(pos<4){return true;}
				else{
					if(!p.isCPU()){
						displayMessage("Select type: enemy (Positions 0-3)");
					}return false;
				}
			}
		}
		
		if(!p.isCPU()){displayMessage("No player there");}
		return false;
	}
	/**
	 * Can be overridden for GUI
	 * @param s -Message
	 */
	protected void displayMessage(String s){
		System.out.println(s);
		try {
			TimeUnit.SECONDS.sleep(0);
		} catch (InterruptedException e) {}
	}
	private void useEffect(Player p,Effect e,int pos){
		if(e.getEffectType().equals("damage")){
			int damage=e.getValue();
			damage=(int)(damage*(1+(double)(p.getDamage(e.getSchool()))/100));
			damage=useCharms(damage,e, p.getCharms());
			damage=useBubble(damage,e);
			damage=useWards(damage,e, arena.getPlayer(pos).getWards());
			damage=(int)(damage*(1-(double)arena.getPlayer(pos).getResist(e.getSchool())/100));
			displayMessage(damage+" damage!");
			arena.subHealth(pos,damage);
		}else if(e.getEffectType().equals("heal")){
			int heal=e.getValue();
			heal=(int)(heal*(1+(double)(p.getOutgoing())/100));
			heal=useCharms(heal,e, p.getCharms());
			heal=useBubble(heal,e);
			heal=useWards(heal,e, arena.getPlayer(pos).getWards());
			heal=(int)(heal*(1+(double)(arena.getPlayer(pos).getIncoming())/100));
			displayMessage(heal+" heal!");
			arena.addHealth(pos,heal);
		}else if(e.getEffectType().equalsIgnoreCase("DoT")){
			DamageOverTime dot=(DamageOverTime)e;
			int initial=dot.getInitialValue();
			initial=(int)(initial*(1+(double)(p.getDamage(dot.getSchool()))/100));
			int[] charmCalculation=useCharmsForDoTs(dot, p.getCharms());
			initial=charmCalculation[0];
			dot.setCalculatedTotalOverTime(charmCalculation[1]);
			if(initial!=0){
				initial=useBubble(initial,e);
				initial=useWards(initial,e, arena.getPlayer(pos).getWards());
				initial=(int)(initial*(1-(double)arena.getPlayer(pos).getResist(e.getSchool())/100));
				displayMessage(initial+" damage! Added "+e.getSchool()+" damage over time to "+arena.getPlayer(pos)+"!");
				arena.subHealth(pos,initial);
			}
			arena.getPlayer(pos).addDoT((DamageOverTime)e);
		}else if(e.getEffectType().equalsIgnoreCase("HoT")){
			DamageOverTime hot=(DamageOverTime)e;
			int initial=hot.getInitialValue();
			initial=(int)(initial*(1+(double)(p.getOutgoing())/100));
			int[] charmCalculation=useCharmsForDoTs(hot, p.getCharms());
			initial=charmCalculation[0];
			hot.setCalculatedTotalOverTime(charmCalculation[1]);
			if(initial!=0){
				initial=useBubble(initial,e);
				initial=useWards(initial,e, arena.getPlayer(pos).getWards());
				initial=(int)(initial*(1+(double)(arena.getPlayer(pos).getIncoming())/100));
				displayMessage(initial+" heal! Added "+e.getSchool()+" heal over time to "+arena.getPlayer(pos)+"!");
				arena.addHealth(pos,initial);
			}
			arena.getPlayer(pos).addDoT((DamageOverTime)e);
		}else if(e.getEffectType().equals("charm")){
			arena.getPlayer(pos).addCharm((Charm)e);
		}else if(e.getEffectType().equals("ward")){
			arena.getPlayer(pos).addWard((Ward)e);
		}//else if(e.getEffectType().equals("manipulation")){}
		//+Summons
	}
	private int calculateAccuracy(Player p, Card c){
		int acc=c.getAccuracy()+p.getAccuracy(p.getSchool());
		for(int i=0;i<p.getCharms().size();i++){//accuracy charms
			if(p.getCharm(i).getType().equals("acc")
			||p.getCharm(i).getType().equals("accuracy")){
					acc+=p.getCharm(i).getValue();
			}
		}
		if(0>acc){acc=0;}
		return acc;
	}
	private int useCharms(int value, Effect effect, ArrayList<Charm> pCharms){
		ArrayList<Charm> antistack = new ArrayList<Charm>();
		for(int i=0;i<pCharms.size();i++){
			if((effect.getSchool().equals(pCharms.get(i).getSchool())
			||pCharms.get(i).getSchool().equals("any"))
			&&(pCharms.get(i).getType().equals(effect.getEffectType())
			||effect.getEffectType().equalsIgnoreCase(pCharms.get(i).getType().charAt(0)+"ot"))){
				boolean stop=false;
				for(int j=0;j<antistack.size();j++){
					if(antistack.get(j)==pCharms.get(i)){stop=true;}
				}
				if(stop==false){
					antistack.add(pCharms.get(i));
					value+=value*pCharms.get(i).getValue()/100;
					displayMessage("   "+pCharms.get(i).toString()+" used!");
					pCharms.remove(i);
				}
			}
		}return value;
	}
	//int[0]=initial value; int[1]=total overtime value
	private int[] useCharmsForDoTs(DamageOverTime effect, ArrayList<Charm> pCharms){
		ArrayList<Charm> antistack = new ArrayList<Charm>();
		int initial=effect.getInitialValue();
		int overtime=effect.getOverTimeTotalValue();
		for(int i=0;i<pCharms.size();i++){
			if((effect.getSchool().equals(pCharms.get(i).getSchool())
			||pCharms.get(i).getSchool().equals("any"))
			&&(pCharms.get(i).getType().equals(effect.getEffectType())
			||effect.getEffectType().equalsIgnoreCase(pCharms.get(i).getType().charAt(0)+"ot"))){
				boolean stop=false;
				for(int j=0;j<antistack.size();j++){
					if(antistack.get(j)==pCharms.get(i)){stop=true;}
				}
				if(stop==false){
					antistack.add(pCharms.get(i));
					initial+=initial*pCharms.get(i).getValue()/100;
					overtime+=overtime*pCharms.get(i).getValue()/100;
					displayMessage("   "+pCharms.get(i).toString()+" used!");
					pCharms.remove(i);
				}
			}
		}return new int[]{initial,overtime};
	}	
	//Add pierce
	private int useWards(int value, Effect effect, ArrayList<Ward> eWards){
		ArrayList<Ward> antistack = new ArrayList<Ward>();
		for(int i=0;i<eWards.size();i++){
			if((effect.getSchool().equals(eWards.get(i).getSchool())
					||eWards.get(i).getSchool().equals("any"))
					&&(eWards.get(i).getType().equals(effect.getEffectType())
					||effect.getEffectType().equalsIgnoreCase(eWards.get(i).getType().charAt(0)+"ot"))){
				boolean stop=false;
				for(int j=0;j<antistack.size();j++){
					if(antistack.get(j)==eWards.get(i)){stop=true;}
				}
				if(stop==false){
					antistack.add(eWards.get(i));
					value+=value*eWards.get(i).getValue()/100;
					displayMessage("   "+eWards.get(i).toString()+" used!");
					eWards.remove(i);
				}
			}
		}return value;
	}
	private int useBubble(int value, Effect effect){
		if((effect.getSchool().equals(bubble.getSchool())
				||bubble.getSchool().equals("any"))
				&&bubble.getType().equals(effect.getEffectType())){
					value+=value*bubble.getValue()/100;
				}
		return value;
	}
	public void useDoTRound(int pos){
		Player p = arena.getPlayer(pos);
		for(int i=0;i<p.getDoTs().size();i++){
			DamageOverTime dot=p.getDoT(i);
			if(dot.getEffectType().equalsIgnoreCase("dot")){
				int damage=dot.getDamageForRound();
				damage=useBubble(damage,dot);
				damage=useWards(damage,dot, arena.getPlayer(pos).getWards());
				damage=(int)(damage*(1-(double)arena.getPlayer(pos).getResist(p.getSchool())/100));
				arena.subHealth(pos, damage);
				displayMessage(dot.getSchool().toUpperCase().charAt(0)+dot.getSchool().substring(1)+" DoT "+damage+" damage to "+p+"!");
			}else if(dot.getEffectType().equalsIgnoreCase("hot")){
				int heal=dot.getDamageForRound();
				heal=useBubble(heal,dot);
				heal=useWards(heal,dot, arena.getPlayer(pos).getWards());
				heal=(int)(heal*(1+(double)(arena.getPlayer(pos).getIncoming())/100));
				arena.addHealth(pos, heal);
				displayMessage("HoT "+heal+" heal!");
			}
			dot.decreaseRound();
			//Remove Dot
			if(dot.getRounds()==0){
				displayMessage(dot.getSchool().toUpperCase().charAt(0)+dot.getSchool().substring(1)+" Over Time removed!");
				p.getDoTs().remove(i);
				i--;
			}
		}arena.deathChecker();
	}
	public void useEnchant(Player p,int selectedPos,int enchantPos){
		Card card=p.getHand(enchantPos);
		ArrayList<Integer> splitLocations = new ArrayList<Integer>();
		for(int i=0;i<p.getHand(selectedPos-1).getEffect().size();i++){
			if(card.getEffect().get(0).getEffectType().equals(p.getHand(selectedPos-1).getEffect().get(i).getEffectType())){
				splitLocations.add(i);
			}
		}
		Card replace=new Card("Enchant",
				p.getHand(selectedPos-1).getSchool(),
				p.getHand(selectedPos-1).getPip(),
				p.getHand(selectedPos-1).getAccuracy(),
				p.getHand(selectedPos-1).getClickType()
		);
		for(Effect e:p.getHand(selectedPos-1).getEffect()){
			replace.addEffect(e);
		}
		for(int i=0;i<splitLocations.size();i++){
			Damage reference=(Damage)replace.getEffect().get(splitLocations.get(i));
			//replace.getEffect().get(splitLocations.get(i)).setValue(
			//		replace.getEffect().get(splitLocations.get(i)).getValue() 
			//		+card.getEffect().get(0).getValue()/splitLocations.size()
			//);
			int v=replace.getEffect().get(splitLocations.get(i)).getValue() 
					+card.getEffect().get(0).getValue()/splitLocations.size();
			replace.getEffect().set(splitLocations.get(i),new Damage(reference.getSchool(),v,reference.getBounds(),reference.getPlacement()));
		}
		p.setHand(replace, selectedPos-1);
		p.setHand(null, enchantPos);
	}
	private Effect copyEffect(Effect effect,int newValue){
		if(effect.getEffectType().equals("damage")){Damage e=(Damage)effect;return new Damage(e.getSchool(), newValue,e.getBounds(),e.getPlacement());}
		else if(effect.getEffectType().equals("heal")){Heal e=(Heal)effect;return new Heal(newValue,e.getBounds());}
		else if(effect.getEffectType().equals("+charm")){return new Charm(list.next(),list.next(),list.nextInt(),list.next());}
		else if(effect.getEffectType().equals("-charm")){return new Charm(list.next(),list.next(),-1*list.nextInt(),list.next());}
		else if(effect.getEffectType().equals("+ward")){return new Ward(list.next(),list.next(),list.nextInt(),list.next());}
		else if(effect.getEffectType().equals("-ward")){return new Ward(list.next(),list.next(),-1*list.nextInt(),list.next());}
		else if(effect.getEffectType().equals("bubble")){return new Bubble(list.next(),list.next(),list.nextInt()));}
		else if(effect.getEffectType().equals("dot")){return new DamageOverTime(list.next(), "D", list.nextInt(),list.nextInt(),list.nextInt(),list.next());}
		else if(effect.getEffectType().equals("hot")){return new DamageOverTime(list.next(), "H", list.nextInt(),list.nextInt(),list.nextInt(),list.next());}
		else if(effect.getEffectType().equals("sun")){return new Sun(list.next(),list.nextInt()));}
		else if(effect.getEffectType().equals("aura")){
			Aura aura=new Aura(list.next());
			while(!type.equals("end")){
				if(type.equals("+charm")){aura.addEffect(new Charm(list.next(),list.next(),list.nextInt(),list.next()));}
				else if(type.equals("-charm")){aura.addEffect(new Charm(list.next(),list.next(),-1*list.nextInt(),list.next()));}
				else if(type.equals("+ward")){aura.addEffect(new Ward(list.next(),list.next(),list.nextInt(),list.next()));}
				else if(type.equals("-ward")){aura.addEffect(new Ward(list.next(),list.next(),-1*list.nextInt(),list.next()));}
				type=list.next().toLowerCase();
			}c.addEffect(aura);
		}
	}
	protected boolean pipCheck(Card card, Player p){
		int pip;
		pip=p.countPPips(card.getSchool());
		if(card.getPip()<=pip){
			return true;
		}
		if(p.isCPU()==false){displayMessage("Not enough pips!");}
		return false;
	}
	public void useCard(int handPos, Player p, int pos){
		if(handPos!=-1){
			Card card=p.getHand(handPos);
			//Accuracy calculation
			if(calculateAccuracy(p,card)>r.nextInt(100)){
				//Display card used
				String playerHit="everyone";
				if(pos!=-1){
					playerHit=arena.getPlayer(pos).getName();
				}
				displayMessage(p.getName()+" used "+card+" on "+playerHit);
				//Use effects
				for(int i=0;i<card.getEffect().size();i++){
					Effect e=card.getEffect().get(i);
					if(e.getPlacement().equals("self")){pos=arena.getPlayerPosition(p);}//ally and enemy are okay
					if(card.getChooseable()==true){
						useEffect(p,e,pos);
					}else if(e.getPlacement().equals("ally")||e.getPlacement().equals("enemy")){
						int jVal=0;
						if(arena.getPlayerPosition(p)<4&&e.getPlacement().equals("enemy")
						||arena.getPlayerPosition(p)>3&&e.getPlacement().equals("ally")){jVal=4;}
						try{
							for(int j=jVal;j<jVal+4;j++){useEffect(p,e,j);}
						}catch(Exception h){}
					}//else if(card.getClickType().equals("summon")){}				
				}
				//Remove pips and card from hand
				boolean pp=false;
				if(p.getSchool().equals(card.getSchool())){pp=true;}
				p.removePips(card.getPip(), pp);
				p.getDeck().remove(card);
				p.setHand(null, handPos);
			}else{
				displayMessage(p.getName()+" fizzled "+card.getSchool()+" spell!");
			}
		}
	}
	
	public void endText(){
		if(arena.getPlayer(0)==null
		&&arena.getPlayer(1)==null
		&&arena.getPlayer(2)==null
		&&arena.getPlayer(3)==null){
			displayMessage("You've lost!");
		}else{displayMessage("You've won!");}
	}
	public void clearEffects(){
		for(int i=0;i<8;i++){
			if(arena.getPlayer(i)!=null){
				arena.getPlayer(i).clearEffects();
			}
		}
	}
	public Bubble getBubble(){return bubble;}
	public void setBubble(Bubble b){bubble=b;}
}
