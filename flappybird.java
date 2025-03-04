package javaflappybird;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import javaflappybird.soundeffects;
import java.util.Iterator;

public class flappybird extends JPanel implements ActionListener, KeyListener {
	Image background;
	Image bird;
	Image toppipe;
	Image bottompipe;
	Image coin1;
	int birdx=360/8;//width=360
	int birdy=640/2;//height=640
	int birdwidth=50;
	int birdheight=40;
	class Bird{
		int x=birdx;
		int y=birdy;
		int width=birdwidth;
		int height=birdheight;
		Image img;
	
		Bird(Image img)
		{
			this.img=img;
		}
	}
	
	private soundeffects jumpSound;
	private soundeffects collisionSound;
	private soundeffects coinSound;
	
	int pipex=360;
	int pipey=0;
	int pipewidth=64;
	int pipeheight=512;
	class pipe{
		int x=pipex;
		int y=pipey;
		int width=pipewidth;
		int height=pipeheight;
		Image img;
		boolean passed=false;
		pipe(Image img){
		this.img=img;
		}
	}
	
	//game logic
	Bird b;
	int velocityx=-4;
	int velocityy=0;
	int gravity=1;
	
	int coinx=200;
	int coiny=400;
	int coinwidth=25;
	int coinheight=25;
	class coin{
		int x=coinx;
		int y=coiny;
		int width=coinwidth;
		int height=coinheight;
		Image img;
		boolean passed=false;
		coin(Image img){
		this.img=img;
	}
	}
	
	ArrayList<pipe> pipes;
	Random random=new Random();
	
	ArrayList<coin> coins;
	Random r=new Random();
	
	
	Timer gameloop;
	Timer placepipestimer;
	Timer placecoinstimer;
	
	boolean gameover=false;
	double score=0;
	int coinCount=0;
	
	flappybird(){
		setPreferredSize(new Dimension(360,640));
		setFocusable(true);
		addKeyListener(this);
		//setBackground(Color.blue);
		background=new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
		bird=new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
		toppipe=new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
		bottompipe=new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();
		coin1=new ImageIcon(getClass().getResource("./coin.png")).getImage();
		
		jumpSound = new soundeffects("jumpsound.wav");
		collisionSound= new soundeffects("collisionsound.wav");
		coinSound= new soundeffects("coinsound.wav");
		
		b=new Bird(bird);
		pipes=new ArrayList<pipe>();
		
		placepipestimer =new Timer(2000,new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				placepipes();
			}
		});
		placepipestimer.start();
		
		
        coins=new ArrayList<coin>();
		placecoinstimer =new Timer(1000,new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				placecoins();
			}
		});
		placecoinstimer.start();
		
		gameloop=new Timer(1000/60,this);
		gameloop.start();
	}
	int openingspace;
	int randompipey;
	public void placepipes() {
		randompipey=(int)(pipey-pipeheight/4-Math.random()*(pipeheight/2));
		openingspace=640/4;
		pipe topPipe=new pipe(toppipe);
		topPipe.y=randompipey;
		pipes.add(topPipe); 
		
		pipe bottomPipe=new pipe(bottompipe);
		bottomPipe.y=topPipe.y+pipeheight+openingspace;
		pipes.add(bottomPipe);
		
	}
	public void placecoins() {
		int randomcoiny=(int)(coiny-coinheight/4-Math.random()*(coinheight/2));
		int randomcoinx = (int) (Math.random() * 300); 
		coin c=new coin(coin1);
		 c.x = pipex + 50; // Set the coin's x position to be ahead of the pipe
		    c.y = randompipey + pipeheight + openingspace / 2 - coinheight / 2; // Generate coin in the gap
		coins.add(c);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	public void draw(Graphics g) {
		//System.out.println("draw");
		g.drawImage(background,0,0,360,640,null);
		g.drawImage(b.img,b.x,b.y,b.width,b.height,null);
		
		for(int i=0;i<pipes.size();i++)
		{
			pipe p=pipes.get(i);
			g.drawImage(p.img, p.x, p.y, p.width, p.height, null);
		}
		
		for(int i=0;i<coins.size();i++)
		{
			coin c=coins.get(i);
			g.drawImage(c.img, c.x, c.y, c.width, c.height, null);
		}
		
		
		
		g.setColor(Color.white);
		g.setFont(new Font("Arial",Font.PLAIN,32));
		if (gameover) { 
			g.drawString("GAME OVER",90, 310 );
	        g.drawString("Score:" + String.valueOf((int) score) + "          Coins:" + String.valueOf(coinCount), 10, 35);
	    } else {
	        g.drawString("Score- " + String.valueOf((int) score) + "           Coins- " + String.valueOf(coinCount), 10, 35);
	    }
		
	}
	public boolean collision(Bird a, coin b) {
	    return a.x < b.x + b.width && a.x + a.width > b.x && a.y < b.y + b.height && a.y + a.height > b.y;
	}

	public void move() {
		velocityy+=gravity;
		b.y+=velocityy;
		b.y=Math.max(b.y, 0);
		for(int i=0;i<pipes.size();i++) {
			pipe p=pipes.get(i);
			p.x+=velocityx;
			if(!p.passed && b.x>p.x+p.width) {
				p.passed=true;
				score+=0.5;
			}
			
			
			if(collision(b,p)) {
				collisionSound.playSound();
				gameover=true;
			}
			Iterator<coin> iterator = coins.iterator();
		    while (iterator.hasNext()) {
		        coin c = iterator.next();
		        c.x += velocityx;
		        if (collision(b, c)) {
		        	coinSound.playSound();
		            iterator.remove();
		            score++;
		            coinCount++; // Increment the coin count
		        }
		    }
		}
		if(b.y>640) {
			gameover=true;
		}
		
	}
	
	public boolean collision(Bird a,pipe b) {
		return a.x<b.x+b.width &&
			   a.x+a.width>b.x &&
			   a.y<b.y+b.height &&
			   a.y+a.height>b.y;
	}
	
	public void actionPerformed(ActionEvent e) {
		move();
		repaint();
		if(gameover) {
			placepipestimer.stop();
			placecoinstimer.stop();
			gameloop.stop();
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_SPACE) {
			velocityy=-9;
			jumpSound.playSound();
			if(gameover) {
				b.y=birdy;
				velocityy=0;
				coins.clear();
				pipes.clear();
				score=0;
				gameover=false;
				gameloop.start();
				placepipestimer.start();
				placecoinstimer.start();
			}
		}
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		
		
	}
}
