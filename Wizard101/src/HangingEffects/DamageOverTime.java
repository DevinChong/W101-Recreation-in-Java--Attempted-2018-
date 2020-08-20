package HangingEffects;

public class DamageOverTime extends Effect{
	private int initialValue;
	private int overTimeTotalValue;
	private int rounds;
	private int initialRounds;
	private int calculatedTotalOverTime;
	public DamageOverTime(String s, String effectTypeInitial, int iv, int oTv, int r, String p){
		super(s,effectTypeInitial+"oT",iv+oTv,p);
		initialValue=iv;
		overTimeTotalValue=oTv;
		rounds=r;
		initialRounds=r;
		calculatedTotalOverTime=oTv;
	}
	public int getInitialValue(){
		return initialValue;
	}
	public int getDamageForRound(){
		return calculatedTotalOverTime/initialRounds;
	}
	public int getOverTimeTotalValue(){
		return overTimeTotalValue;
	}
	public int getRounds(){
		return rounds;
	}
	public void decreaseRound(){
		rounds--;
	}
	public void setCalculatedTotalOverTime(int c){
		calculatedTotalOverTime=c;
	}
	public void reset(){
		calculatedTotalOverTime=overTimeTotalValue;
		rounds=initialRounds;
		
	}
}
