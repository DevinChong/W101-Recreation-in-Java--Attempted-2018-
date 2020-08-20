package HangingEffects;

import java.util.Random;

public class Damage extends Effect{
	private int bounds;
	private boolean firstCall=true;
	public Damage(String s, int v, int b, String p){
		super(s,"damage",v, p);
		bounds=b;
	}
	@Override
	public int getValue(){
		if(firstCall==true){
			generateValue();
			firstCall=false;
		}
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

	public void setBounds(int b){bounds=b;}
}