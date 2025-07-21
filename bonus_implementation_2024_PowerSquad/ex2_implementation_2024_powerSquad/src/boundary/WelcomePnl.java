package boundary;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class WelcomePnl extends JPanel {
	
	

	
	
	
	private static final long serialVersionUID = 1L;
	
	private SoundPlayer sound;
	private JLabel backgroundLabel;
	private ImageIcon background;
	private Dimension screenSize;// = Toolkit.getDefaultToolkit().getScreenSize();
	private int width;
	private int height;
	


	
	/**
	 * Create the panel.
	 */
	public WelcomePnl() {
		fetchAndRefresh();
		initComponents();
		createEvents();


	}

	private void createEvents() {
		// TODO Auto-generated method stub
		  
		
	}

	private void initComponents() {
		// TODO Auto-generated method stub
		//sound=new SoundPlayer(this.getClass().getResource("/boundary/sound/welcomSound.wav"));
		 setLayout(new BorderLayout());

         // Load the GIF
		 screenSize = Toolkit.getDefaultToolkit().getScreenSize();
         width = (int) ((screenSize.getWidth() * 0.8)/2);
         height = (int) (screenSize.getHeight() * 0.8);
         background = new ImageIcon(this.getClass().getResource("/boundary/images/WelcomeVid.png"));
         background.setImage(background.getImage().getScaledInstance(width, height,Image.SCALE_DEFAULT));
         backgroundLabel = new JLabel(background);
         //backgroundLabel.setPreferredSize(new Dimension(width, height));
         add(backgroundLabel, BorderLayout.CENTER);
	}

	private void fetchAndRefresh() {
		// TODO Auto-generated method stub
		
	}
	
	
}
