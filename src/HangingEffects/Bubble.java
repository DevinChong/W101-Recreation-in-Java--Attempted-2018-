package HangingEffects;

public class Bubble extends Effect{
	private String type;//damage, accuracy, heal, etc.
	private int value;
	public Bubble(String t, String s, int v){
		super(s,"bubble",v,"all");
		type=t;
		value=v;
	}
	public int getValue(){return value;}
	public String getType(){return type;}
	public void setType(String t){type=t;}
	public void setValue(int v){value=v;}
}
