package Cards;

import java.util.ArrayList;
import HangingEffects.Effect;

public class Card {
	private String name="";
	private String school;
	private int pip;
	private int accuracy;
	private String clickType;//ally or enemy
	private ArrayList<Effect> effects=new ArrayList<Effect>();
	private boolean chooseable=true;//false means click type is self-only or AoE

	public Card(String name, String school, int pip, int acc, String click){
		this.name=name;
		this.school=school;
		this.pip=pip;
		accuracy=acc;
		clickType=click;
		if(click.equals("false")){chooseable=false;}
	}

	public void addEffect(Effect h){effects.add(h);}
	
	public String toString(){return name;}
	public String getSchool(){return school;}
	public int getPip(){return pip;}
	public int getAccuracy(){return accuracy;}
	public String getClickType(){return clickType;}
	public boolean getChooseable(){return chooseable;}
	public ArrayList<Effect> getEffect(){return effects;}
	
	
	public void setSchool(String s){school=s;}
	public void setPip(int p){pip=p;}
	public void setAccuracy(int a){accuracy=a;}
	public void setClickType(String c){clickType=c;}
	public void setChooseable(boolean c){chooseable=c;}
	public void setEffect(ArrayList<Effect> h){effects=h;}
}
