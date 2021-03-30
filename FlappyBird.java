import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.lang.Math;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

class Panel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Point p1 = new Point(40, 160);
	float v = 0f;
	
	int l1 = 120, l2 = 240, l3 = 360, l4 = 480;
	int l1g = 50, l2g = 90, l3g = 70, l4g = 40;
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		
		g.drawLine(0, 360, 480, 360);
		
		g.setColor(Color.RED);
		g.fillOval(p1.x, p1.y, 20, 20);
		
		g.setColor(Color.BLACK);
		g.drawOval(p1.x, p1.y, 20, 20);
		
		g.drawRect(l1, 0, 18, 180-l1g);
		
		g.drawRect(l1, 180+l1g, 18, 180-l1g);
		
		g.drawRect(l2, 0, 18, 180-l2g);
		
		g.drawRect(l2, 180+l2g, 18, 180-l2g);
		
		g.drawRect(l3, 0, 18, 180-l3g);
		
		g.drawRect(l3, 180+l3g, 18, 180-l3g);
		
		g.drawRect(l4, 0, 18, 180-l4g);
		
		g.drawRect(l4, 180+l4g, 18, 180-l4g);
		
		g.drawString("Points: "+FlappyBird.points, 12, 372);
	}
	
	public float getAcc(){
		if(p1.y >= 340){ return 0f;}
		else{ return -0.24f;}
	}
	
	public int getGap(){
		double rand = Math.random();
		
		return 40 + (int)(80*rand);
	}
}


public class FlappyBird {

	private JFrame frame;
	public JLayeredPane pane;
	public final static Panel panel = new Panel();
	public static int GameState = 0;
	public static int points = 0;
	private JLabel lbl;
	
	public static boolean running = true;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
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
			if(GameState == 1){
				GameEngine();
			}
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.exit(0);
		
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
		frame = new JFrame("FlappyBird");
		pane = new JLayeredPane();
		pane.setSize(480, 430);
		frame.setBounds(100, 100, 500, 478);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		panel.setBackground(Color.WHITE);
		panel.setBounds(0, 0, 482, 380);
		//panel.setDoubleBuffered(true);
		pane.add(panel, JLayeredPane.DEFAULT_LAYER);
		
		JButton btn = new JButton("Settings");
		btn.setBounds(0, 380, 87, 51);
		pane.add(btn, JLayeredPane.DEFAULT_LAYER);
		frame.getContentPane().add(pane);
		
		final JSlider slider = new JSlider();
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
			}
		});
		slider.setBounds(150, 150, 200, 35);
		pane.add(slider, JLayeredPane.POPUP_LAYER);
		
		lbl = new JLabel("SPEED");
		lbl.setHorizontalAlignment(SwingConstants.CENTER);
		lbl.setOpaque(true);
		lbl.setForeground(Color.BLACK);
		lbl.setBackground(Color.LIGHT_GRAY);
		lbl.setBounds(90, 150, 60, 35);
		pane.add(lbl, JLayeredPane.POPUP_LAYER);
		
		JButton btnQuit = new JButton("Quit");
		btnQuit.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				running = false;
			}
		});
		btnQuit.setBounds(385, 380, 97, 51);
		pane.add(btnQuit);
		
		slider.setVisible(false);
		lbl.setVisible(false);

		btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				GameState = 0;
				slider.setVisible(true);
				lbl.setVisible(true);
			}
		});
		
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				GameState = 1;
				panel.v = 4.55f;
				slider.setVisible(false);
				lbl.setVisible(false);
			}
		});
	}
	
	public static void GameEngine(){
		panel.p1.y -= (int)(panel.v);
		panel.v += panel.getAcc();
		
		if(panel.p1.y > 340){ panel.p1.y = 340; panel.v = (-panel.v)*0.77f;}
		
		int i = 0,j = 0;
		
		panel.l1--;panel.l2--;panel.l3--;panel.l4--;
		if(panel.l1 < 5){panel.l1 = 480; panel.l1g = panel.getGap();points++;}
		else if(panel.l2 < 5){panel.l2 = 480; panel.l2g = panel.getGap();points++;}
		else if(panel.l3 < 5){panel.l3 = 480; panel.l3g = panel.getGap();points++;}
		else if(panel.l4 < 5){panel.l4 = 480; panel.l4g = panel.getGap();points++;}
		
		if(panel.l1>20&&panel.l1<120){i = panel.l1; j = panel.l1g;}
		else if(panel.l2>20&&panel.l2<120){i = panel.l2; j = panel.l2g;}
		else if(panel.l3>20&&panel.l3<120){i = panel.l3; j = panel.l3g;}
		else if(panel.l4>20&&panel.l4<120){i = panel.l4; j = panel.l4g;}
		
		if((panel.p1.x+20>i)&&(panel.p1.x<i)){
			if((panel.p1.y<180-j)||(panel.p1.y>160+j)){
				GameState = 0;
				panel.l1 = 120; panel.l2 = 240; panel.l3 = 360; panel.l4 = 480;
				panel.l1g = 50; panel.l2g = 90; panel.l3g = 70; panel.l4g = 40;
				points = 0;
			}
		}else if((panel.p1.x>i)&&(panel.p1.x<i+20)){
			if((panel.p1.y<180-j)||(panel.p1.y>160+j)){
				GameState = 0;
				panel.l1 = 120; panel.l2 = 240; panel.l3 = 360; panel.l4 = 480;
				panel.l1g = 50; panel.l2g = 90; panel.l3g = 70; panel.l4g = 40;
				points = 0;
			}
		}
		
		panel.repaint();
	}
}