package HangingEffects;

public class Charm extends Effect{
	private String type;//damage, accuracy, heal, etc.
	private double value;
	public Charm(String t, String s, int v, String p){
		super(s,"charm",v,p);
		type=t;
		value=v;
	}
	
	public boolean isPoitive(){
		if(value>=0){
			return true;
		}
		return false;
	}
	public boolean isNegative(){
		if(value<0){
			return true;
		}return false;
	}
	public String toString(){
		String s="";
		if(value>0){
			s+="+";
		}
		return s+value+" "+getSchool()+" charm";
	}
	public String getType(){return type;}
	
	public void setType(String t){type=t;}
}
