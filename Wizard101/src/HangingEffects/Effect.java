package HangingEffects;

public class Effect {
	private String school;
	private String effect; //charm, ward, bubble, damage, etc.
	protected int value;
	private String placement;
	public Effect(String school, String effectType, int value, String placement){
		this.school=school;
		this.effect=effectType;
		this.value=value;
		this.placement=placement;
	}
	public String getSchool(){return school;}
	public String getEffectType(){return effect;}
	public int getValue(){return value;}
	public String getPlacement(){return placement;}
	
	public void setSchool(String s){school=s;}
	public void setEffectType(String t){effect=t;}
	public void setValue(int v){value=v;}
	public void setPlacement(String p){placement=p;}
}
