import Combat.Arena;
import Combat.GUI;
import Combat.Main;
import HangingEffects.Damage;
import Player.Player;

public class Tester {
	public static void main(String[] args){
		System.out.println("Tester used");
		Player p = new Player("Kevin Fireflame","fire", 90);
		p.add("Flare", 2);
		//p.add("Fireblade", 2);
		//p.add("Tower_Shield", 1);
		//p.add("Elemental_Blade");
		//p.add("Feint");
		//p.add("Meteor_Strike",1);
		p.add("Link",2);
		p.add("Fire_Elf", 2);
		//p.add("BladeStorm");
		//p.addCharm((Charm)p.deck().find("Fireblade").getEffect().get(0));
		//p.addWard((Ward)p.deck().find("Tower_Shield").getEffect().get(0));
		Damage card=(Damage)p.find("Firecat").getEffect().get(0);
		
		Player l = new Player("Lames Strategist","fire",1);
		l.toggleCPU();
		l.add("Flare",30);
		
		Player e = new Player("Devin Chong","fire",85);
		e.toggleCPU();
		//e.add("Firecat", 2);
		//e.add("Tower_Shield", 1);
		//e.add("Elemental_Blade");
		//e.add("Feint");
		//e.add("Meteor_Strike",1);	
		
		Player t = new Player("Test","fire",85);
		e.toggleCPU();
		//t.add("Fireblade", 10);
		//t.add("Firecat", 10);
		
		Arena a=new Arena(p,e);
		//a.addAlly(l);
		//a.addEnemy(t);
		//Main m=new Main(a);
		
		GUI g=new GUI(a);
	}
}
