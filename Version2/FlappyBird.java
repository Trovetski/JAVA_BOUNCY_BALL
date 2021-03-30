package Version2;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.border.BevelBorder;

//stores the game state
class GameState{
	public Point p1;
	public int size = 20;
	public int line[];
	public int gaps[];
	public int high[];
	public Color c = Color.RED;
	public double v = 0;
	public int points = 0;
	public int highscore = 0;
}

//gain access to paint component
//acts as render engine
class Panel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		
		g.drawLine(0, 45, 457, 45);
		g.drawLine(0, 330, 457, 330);
		
		Render(FlappyBird.game,g);
	}
	
	@Override
	public void update(Graphics g){
		super.update(g);
		
		Render(FlappyBird.game,g);
	}

	public void Render(GameState g, Graphics g0){
		Graphics2D g2d = (Graphics2D)g0;
		
		g2d.setColor(g.c);
		g2d.fillOval(g.p1.x, g.p1.y, g.size, g.size);
		
		g2d.setColor(Color.BLACK);
		g2d.drawOval(g.p1.x, g.p1.y, g.size, g.size);
		
		Shape circle = new Ellipse2D.Double(g.p1.x, g.p1.y, g.size, g.size);
		Rectangle rect1, rect2;
		
		for(int i=0;i<4;i++){
			rect1 = new Rectangle(g.line[i], 45, 20, g.high[i]);
			rect2 = new Rectangle(g.line[i], g.high[i]+g.gaps[i]+45, 20, 285-g.high[i]-g.gaps[i]);
			g2d.draw(rect1);
			g2d.draw(rect2);
			
			if(g2d.hit(rect1, circle, true)||g2d.hit(rect2, circle, true)){FlappyBird.gameState=0;FlappyBird.Reset();}
		}
		g2d.drawString("Points: "+g.points, 10, 345);
		g2d.drawString("High Score: "+g.highscore, 300, 345);
		g2d.dispose();
	}
}

public class FlappyBird {
	
	//declare everything
	private JFrame frame;
	private JLayeredPane pane;
	private static Panel panel;
	private JSlider slider, slider_1;
	
	private JRadioButton rdbtnRed, rdbtnBlue, rdbtnYellow;//all the settings stuff
	private JLabel lblSpeed, lblColor, lblSize;
	
	public static GameState game;//actual game state
	public static boolean running = true;//is running
	public static int gameState = 0;//check if paused, 0 is paused, 1 is running
	public static int speed = 1;
	
	public static int minh = 120;
	public static int maxh = 130;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {//did't write this don't know what it does
			public void run() {
				try {
					FlappyBird window = new FlappyBird();
					window.frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		while(running){
			if(gameState == 1){
				GameEngine();
				panel.repaint();
			}
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void GameEngine(){
		game.p1.y -= (int)game.v;
		if(game.p1.y > 330-game.size){ game.p1.y = 330-game.size; game.v = (-game.v)*0.77f;}
		else if(game.p1.y < 45){ game.p1.y = 45; game.v = (-game.v)*0.77f;}
		else{game.v -= 0.24f;}
		
		for(int i=0;i<4;i++){
			game.line[i] -= speed;
			if(game.line[i] < 5){
				game.line[i] = 480;
				game.points++;
				
				game.high[i] = minh + (int)((maxh-minh)*Math.random());
				game.gaps[i]--;
				minh--;
				maxh++;
			}
		}
	}
	
	public static void Reset(){
		game.p1 = new Point(50,250);
		game.v = 0;
		game.line = new int[]{120, 240, 360, 480};
		game.gaps = new int[]{90, 80, 85, 80};
		game.high = new int[]{145, 120, 155, 160};
		if(game.points > game.highscore){game.highscore = game.points;}
		game.points = 0;
		
		panel.repaint();
	}

	/**
	 * Create the application.
	 */
	public FlappyBird() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		game = new GameState();
		game.p1 = new Point(50,250);
		game.line = new int[]{120, 240, 360, 480};
		game.gaps = new int[]{90, 80, 85, 80};
		game.high = new int[]{145, 120, 155, 160};
		
		frame = new JFrame("FlappyBird_2.0");
		frame.setBounds(100, 100, 488, 487);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		
		//layered pane
		pane = new JLayeredPane();
		pane.setLocation(0, 0);
		pane.setSize(480,453);
		
		slider = new JSlider();
		slider.setValue(1);
		slider.setMaximum(10);
		slider.setMinimum(1);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				speed = slider.getValue();
			}
		});
		slider.setBounds(150, 150, 200, 25);
		pane.add(slider, JLayeredPane.POPUP_LAYER);
		
		//main game screen panel
		panel = new Panel();
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				if(gameState == 0){
					rdbtnRed.setVisible(false);
					rdbtnBlue.setVisible(false);
					rdbtnYellow.setVisible(false);
					
					lblSpeed.setVisible(false);
					lblColor.setVisible(false);
					lblSize.setVisible(false);
					
					slider.setVisible(false);
					slider_1.setVisible(false);
					
					gameState = 1;
					game.v = 4.53f;
				}else{
					gameState = 1;
					game.v = 4.53f;
				}
			}
		});
		panel.setBackground(Color.WHITE);
		panel.setBounds(12, 13, 458, 375);
		pane.add(panel, JLayeredPane.DEFAULT_LAYER);
		
		//pause button
		JButton btnPause = new JButton("Pause");
		btnPause.setBounds(22, 401, 99, 39);
		btnPause.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent arg0){
				gameState = 0;
			}
		});
		pane.add(btnPause, JLayeredPane.DEFAULT_LAYER);
		
		//settings button
		JButton btnSettings = new JButton("Settings");
		btnSettings.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				rdbtnRed.setVisible(true);
				rdbtnBlue.setVisible(true);
				rdbtnYellow.setVisible(true);
				
				lblSpeed.setVisible(true);
				lblColor.setVisible(true);
				lblSize.setVisible(true);
				
				slider.setVisible(true);
				slider_1.setVisible(true);
				
				gameState = 0;
			}
		});
		btnSettings.setBounds(244, 401, 99, 39);
		pane.add(btnSettings, JLayeredPane.DEFAULT_LAYER);
		
		//reset button
		JButton btnReset = new JButton("Reset");
		btnReset.setBounds(133, 401, 99, 39);
		btnReset.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent arg0){
				gameState = 0;
				Reset();
			}
		});
		pane.add(btnReset, JLayeredPane.DEFAULT_LAYER);
		
		//quit button
		JButton btnQuit = new JButton("Quit");
		btnQuit.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				System.exit(0);
			}
		});
		btnQuit.setBounds(355, 401, 99, 39);
		pane.add(btnQuit, JLayeredPane.DEFAULT_LAYER);
		
		//change ball color to red
		rdbtnRed = new JRadioButton("RED");
		rdbtnRed.setSelected(true);
		rdbtnRed.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if(rdbtnRed.isSelected()){
					game.c = Color.RED;
					panel.repaint();
					
					rdbtnBlue.setSelected(false);
					rdbtnYellow.setSelected(false);
				}
			}
		});
		rdbtnRed.setBounds(150, 175, 200, 25);
		pane.add(rdbtnRed, JLayeredPane.POPUP_LAYER);
		
		//change ball to blue
		rdbtnBlue = new JRadioButton("BLUE");
		rdbtnBlue.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if(rdbtnBlue.isSelected()){
					game.c = Color.BLUE;
					panel.repaint();
					
					rdbtnRed.setSelected(false);
					rdbtnYellow.setSelected(false);
				}
			}
		});
		rdbtnBlue.setBounds(150, 200, 200, 25);
		pane.add(rdbtnBlue, JLayeredPane.POPUP_LAYER);
		
		//change ball to yellow
		rdbtnYellow = new JRadioButton("YELLOW");
		rdbtnYellow.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if(rdbtnYellow.isSelected()){
					game.c = Color.YELLOW;
					panel.repaint();
					
					rdbtnBlue.setSelected(false);
					rdbtnRed.setSelected(false);
				}
			}
		});
		rdbtnYellow.setBounds(150, 225, 200, 25);
		pane.add(rdbtnYellow, JLayeredPane.POPUP_LAYER);
		
		//visual labels
		lblSpeed = new JLabel("SPEED");
		lblSpeed.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		lblSpeed.setOpaque(true);
		lblSpeed.setBackground(Color.LIGHT_GRAY);
		lblSpeed.setHorizontalAlignment(SwingConstants.CENTER);
		lblSpeed.setBounds(75, 150, 75, 25);
		pane.add(lblSpeed,JLayeredPane.POPUP_LAYER);
		
		lblColor = new JLabel("COLOR");
		lblColor.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		lblColor.setOpaque(true);
		lblColor.setBackground(Color.LIGHT_GRAY);
		lblColor.setHorizontalAlignment(SwingConstants.CENTER);
		lblColor.setBounds(75, 175, 75, 75);
		pane.add(lblColor,JLayeredPane.POPUP_LAYER);
		
		slider_1 = new JSlider();
		slider_1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				game.size = slider_1.getValue()/5 + 10;
				panel.repaint();
			}
		});
		slider_1.setBounds(150, 125, 200, 25);
		pane.add(slider_1, JLayeredPane.POPUP_LAYER);
		
		lblSize = new JLabel("SIZE");
		lblSize.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		lblSize.setBackground(Color.LIGHT_GRAY);
		lblSize.setHorizontalAlignment(SwingConstants.CENTER);
		lblSize.setOpaque(true);
		lblSize.setBounds(75, 125, 75, 25);
		pane.add(lblSize, JLayeredPane.POPUP_LAYER);
		
		//make everything invisible
		rdbtnRed.setVisible(false);
		rdbtnBlue.setVisible(false);
		rdbtnYellow.setVisible(false);
		
		lblSpeed.setVisible(false);
		lblColor.setVisible(false);
		lblSize.setVisible(false);
		
		slider.setVisible(false);
		slider_1.setVisible(false);
		
		frame.getContentPane().add(pane);
	}
}
