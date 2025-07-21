package boundary;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
public class SignUp extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private FrmAddNewApplication nwApplicationPnl;
	private Dimension screenSize;// = Toolkit.getDefaultToolkit().getScreenSize();
	private int width;
	private int height;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SignUp frame = new SignUp();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SignUp() {
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = (int) (screenSize.getWidth() * 0.8);
		height = (int) (screenSize.getHeight() * 0.8);
		setResizable(false);
		setBounds(50, 50, width, height);		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		nwApplicationPnl=new FrmAddNewApplication(1,"");
		nwApplicationPnl.setSize(width, height);		

		Boolean signedUp=nwApplicationPnl.getSucessInSignUp();
		contentPane.add(nwApplicationPnl);

		setContentPane(contentPane);
		
		
	}

}
