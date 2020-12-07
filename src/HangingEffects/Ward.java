package HangingEffects;

public class Ward extends Effect{
	private String type;//damage, heal, etc.
	private double value;
	public Ward(String t, String s, int v,String p){
		super(s,"ward",v,p);
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
		return s+value+" "+getSchool()+" ward";
	}
	public String getType(){return type;}
	
	public void setType(String t){type=t;}
}