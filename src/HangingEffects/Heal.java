package HangingEffects;

import java.util.Random;

public class Heal extends Effect{
	private int value;
	private int bounds;
	private boolean firstCall=true;
	public Heal(int v, int b){
		super("life","heal",v,"ally");
		value=v;
		bounds=b;
	}
	@Override
	public int getValue(){
		if(firstCall==true){generateValue();}
		firstCall=false;
		return value;
	}
	private void generateValue(){
		Random r=new Random();
		int d=r.nextInt(3)*(bounds/2);
		value+=d;
	}
	public int getBounds(){
		return bounds;
	}

	public void setValue(int v){value=v;}
	public void setBounds(int b){bounds=b;}
}