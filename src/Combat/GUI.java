package Combat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Cards.Card;
import HangingEffects.Charm;
import HangingEffects.Damage;
import HangingEffects.Ward;
import Player.Player;

public class GUI extends JFrame{
	private ArrayList<JPanel> hand=new ArrayList<JPanel>(8);
	private Main main;
	private Arena arena;
	private int position=0;
	final int width=800;
	final int height=600;
	private Color brown=new Color(160,82,45);
	private Player player,enemy;
	//method booleans
	private Rectangle[] cards = new Rectangle[7];//For promptHand
	private Rectangle[] statbox = new Rectangle[8];//For promptPosition
	private Ellipse2D[] playerPosition = new Ellipse2D[8];
	public Stack<JComponent> components = new Stack<JComponent>();
	private MouseEvent me;
	//Toggle Booleans
	private boolean first=true;
	
	public GUI(Arena a){
		arena=a;
		for(int i=0;i<8;i++){hand.add(null);}
		setLayout(null);
		setBounds(0, 0, width, height);
		contentPane();
	}
	private void contentPane(){
		JComponent mainSetup = new JComponent(){
			public void paintComponent(Graphics g){
				mainSetup(g);
			}
		};
		add(mainSetup);
		components.push(mainSetup);
	}
	public void paintComponent(Graphics g){
		if(first==true){
			initializeMain(arena, g);
			System.out.println("called");
			first=false;
		}
	}
	private void initializeMain(Arena arena, Graphics g){
		main=new Main(arena){
			@Override
			public void display(){
				String name;
				g.setColor(Color.BLACK);
				for(int i=0;i<ch(506);i+=ch(505)){//Draws Player Stat Boxes
					for(int j=cw(25);j<cw(626);j+=cw(200)){
						g.drawRect(j, i, cw(125), ch(55));
						statbox[(j-cw(25))/cw(200)]=new Rectangle(j, i, cw(125), ch(55));
					}
					if(i==0){//Prints name on stat boxes (top 4)
						for(int k=4,j=cw(25);k<8;k++,j+=cw(200)){
							try{name=arena.getPlayer(k).getName();
							if(name.length()>cw(17)){name=name.substring(0,16)+"...";}
							g.drawString(name, j+cw(5), i+ch(15));
							g.drawString("HP:"+arena.getHealth(k), j+cw(5), i+ch(30));
							g.drawString("     /"+arena.getPlayer(k).getSetHP(), j+cw(5), i+ch(45));
							}catch(NullPointerException e){}
						}
					}else{//Prints name on stat boxes (bottom 4)
						for(int k=3,j=cw(25);k>=0;k--,j+=cw(200)){
							try{name=arena.getPlayer(k).getName();
							if(name.length()>cw(17)){name=name.substring(0,16)+"...";}
							g.drawString(name, j+cw(5), i+ch(15));
							g.drawString("HP:"+arena.getHealth(k), j+cw(5), i+ch(30));
							g.drawString("     /"+arena.getPlayer(k).getSetHP(), j+cw(5), i+ch(45));
							}catch(NullPointerException e){}
						}	
					}
				};
				printHand();		
			}
			@Override
			public void printHand(){
				g.setColor(brown);
				g.fillRect(cw(100), ch(225), cw(50), ch(80));
				g.setColor(Color.YELLOW);
				g.drawString(arena.getPlayer(0).getDeck().size()+" out ", cw(102) ,ch(260));
				g.drawString("of "+arena.getPlayer(0).getDeckSize(), cw(102) ,ch(275));
				for(int i=1;i<8;i++){
					int a=i;
					if(hand.get(a)==null){
						drawCard(a,g);
						hand.set(a, new JPanel(){
							public void paintComponent(Graphics g){
								drawCard(a,g);
							}
						});
					}
				};
			}
			@Override
			public int promptHand(){
				//Add pass button
				int c=-1;
				boolean valid=false;
				while(!valid){
					System.out.println("Started");
					for(int i=0;i<7;i++){
						System.out.println(i);
						if(cards[i].contains(me.getPoint())){
							valid=true;
							System.out.println(valid);
							c=i;
						}
					}
				}
				System.out.println("working");
				return c;
				
			}
			@Override
			public int promptPosition(){
				
			}

		};
	}
	public void mainSetup(Graphics g){
		g.drawOval(cw(37), ch(125), cw(700), ch(300));
		g.drawOval(cw(60), ch(143), cw(650), ch(265));
		g.drawOval(cw(200), ch(180), cw(390), ch(175));
		
		//Positions 0-7 in order
		g.setColor(Color.GREEN);
			g.fillOval(cw(637),ch(340),cw(130),ch(91));
			g.setColor(Color.BLACK);
			g.fillOval(cw(647),ch(345),cw(108),ch(75));
			g.setColor(brown);
			g.fillOval(cw(657), ch(355), cw(89), ch(55));
			playerPosition[0]=new Ellipse2D.Double(cw(637),ch(340),cw(130),ch(91));
		g.setColor(Color.CYAN);
			g.fillOval(cw(437),ch(400),cw(130),ch(98));
			g.setColor(Color.BLACK);
			g.fillOval(cw(447),ch(406),cw(108),ch(80));
			g.setColor(brown);
			g.fillOval(cw(457), ch(416), cw(90), ch(60));
			playerPosition[1]=new Ellipse2D.Double(cw(437),ch(400),cw(130),ch(98));
		g.setColor(Color.BLUE);
			g.fillOval(cw(237),ch(400),cw(130),ch(98));
			g.setColor(Color.BLACK);
			g.fillOval(cw(247),ch(406),cw(108),ch(80));
			g.setColor(brown);
			g.fillOval(cw(257), ch(416), cw(90), ch(60));
			playerPosition[2]=new Ellipse2D.Double(cw(237),ch(400),cw(130),ch(98));
		g.setColor(Color.MAGENTA);
			g.fillOval(cw(12),ch(340),cw(130),ch(91));
			g.setColor(Color.BLACK);
			g.fillOval(cw(23),ch(345),cw(108),ch(75));
			g.setColor(brown);
			g.fillOval(cw(33), ch(355), cw(89), ch(55));
			playerPosition[3]=new Ellipse2D.Double(cw(12),ch(340),cw(130),ch(91));
		g.setColor(Color.PINK);
			g.fillOval(cw(12), ch(140), cw(120), ch(72));
			g.setColor(Color.BLACK);
			g.fillOval(cw(23),ch(145),cw(100),ch(60));
			g.setColor(brown);
			g.fillOval(cw(33), ch(150), cw(80), ch(50));
			playerPosition[4]=new Ellipse2D.Double(cw(12), ch(140), cw(120), ch(72));
		g.setColor(Color.RED);
			g.fillOval(cw(237),ch(90),cw(120),ch(60));
			g.setColor(Color.BLACK);
			g.fillOval(cw(247),ch(95),cw(100),ch(48));
			g.setColor(brown);
			g.fillOval(cw(257), ch(100), cw(80), ch(38));
			playerPosition[4]=new Ellipse2D.Double(cw(237),ch(90),cw(120),ch(60));
		g.setColor(Color.ORANGE);
			g.fillOval(cw(437),ch(90),cw(120),ch(60));
			g.setColor(Color.BLACK);
			g.fillOval(cw(447),ch(95),cw(100),ch(48));
			g.setColor(brown);
			g.fillOval(cw(457), ch(100), cw(80), ch(38));
			playerPosition[5]=new Ellipse2D.Double(cw(437),ch(90),cw(120),ch(60));
		g.setColor(Color.YELLOW);
			g.fillOval(cw(637),ch(140),cw(120),ch(72));
			g.setColor(Color.BLACK);
			g.fillOval(cw(647),ch(145),cw(100),ch(60));
			g.setColor(brown);
			g.fillOval(cw(657), ch(150), cw(80), ch(50));
			playerPosition[7]=new Ellipse2D.Double(cw(637),ch(140),cw(120),ch(72));
		
	}
	/**
	 * Convert Width
	 * @param i
	 * @param isWidth
	 * @return
	 */
	private int cw(int i){
		return (int)((double)i/800*width);
	}
	private int ch(int i){
		return (int)((double)i/600*height);
	}
	private void pointer(int pos, Graphics g){
		switch(pos){
		case 0: 
		case 1:
		case 2: 
		case 3: 
		case 4: 
		case 5: 
		case 6:
		case 7: 
		}
	}
	private void drawCard(int a, Graphics g){
		try{
			Card c=main.getArena().getPlayer(0).getHand(a-1);
			if(main.pipCheck(c, main.getArena().getPlayer(0))==true){g.setColor(getSchoolColor(c.getSchool()));}
			else{g.setColor(Color.LIGHT_GRAY);}
			cards[a-1]=new Rectangle(cw(100+75*(a)), ch(225), cw(50), ch(80));
			g.fillRect(cw(100+75*(a)), ch(225), cw(50), ch(80));
			g.setColor(Color.BLACK);
			if(c.toString().length()<=7){g.drawString(c.toString(), cw(102+75*(a)),ch(238));}
			else{g.drawString(c.toString().substring(0,7), cw(102+75*(a)),ch(238));}
		}catch(NullPointerException e){}
	}
	private Color getSchoolColor(String s){
		if(s.equals("fire")){return Color.RED;}
		else if(s.equals("ice")){return Color.CYAN;}
		else if(s.equals("storm")){return Color.MAGENTA;}
		else if(s.equals("myth")){return Color.YELLOW;}
		else if(s.equals("life")){return Color.GREEN;}
		else if(s.equals("death")){return Color.GRAY;}
		else if(s.equals("balance")){return brown;}
		else if(s.equals("shadow")){return Color.BLACK;}
		return Color.WHITE;
	}
	
	
	public static void main(String args[]){
		Player p = new Player("Kevin Fireflame","fire", 90);
		p.add("Firecat", 2);
		p.add("Fireblade", 2);
		p.add("Tower_Shield", 1);
		p.add("Elemental_Blade");
		p.add("Feint");
		p.add("Meteor_Strike",1);
		//p.add("King_Artorius",2);
		//p.addCharm((Charm)p.deck().find("Fireblade").getEffect().get(0));
		//p.addWard((Ward)p.deck().find("Tower_Shield").getEffect().get(0));
		Damage card=(Damage)p.find("Firecat").getEffect().get(0);
		
		Player e = new Player("Devin Chong","fire",85);
		e.add("Firecat", 2);
		e.add("Fireblade", 2);
		e.add("Tower_Shield", 1);
		e.add("Elemental_Blade");
		e.add("Feint");
		//e.add("Meteor_Strike",1);	
		
		GUI window = new GUI(new Arena(p,e));
		window.setVisible(true);
		System.out.println(window.components.size());
	}
	
}
