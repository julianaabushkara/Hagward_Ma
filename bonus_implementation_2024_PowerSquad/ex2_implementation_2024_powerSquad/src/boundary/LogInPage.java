package boundary;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import control.LogInLogic;
import entity.LogInInfo;

public class LogInPage extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel backgroundPnl;

	
	
	private JButton logIn;
	private JButton reset;
	private JLabel logInLbl;
	private JLabel passwordLbl;
	private JLabel logInTitle;
	private JLabel errMsg;
	private JLabel signUp;
	
	
	// LogIn
	private HashMap<String, LogInInfo> usersInfo;
	private LogInInfo currentUser;
	private Boolean LoggedIn;

	private JTextField txtId;
	private JPasswordField txtPassword;
	// Get screen size
	private Dimension screenSize;// = Toolkit.getDefaultToolkit().getScreenSize();
	private int width;
	private int height;
	private WelcomePnl welcome;
	SoundPlayer btnSound,sound;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LogInPage frame = new LogInPage();
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
	public LogInPage() {
		this.LoggedIn = false;
		this.currentUser = null;

		welcome=new WelcomePnl();
		fetchAndRefresh();
		initComponents();
		createEvents();	
		

		
		//System.out.println(" in HomePage LoggedIn = "+ LoggedIn+"/n");
		setContentPane(contentPane);
	}
	
	
	
	private void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = (int) (screenSize.getWidth() * 0.8);
		height = (int) (screenSize.getHeight() * 0.8);
		setResizable(false);
		setBounds(50, 50, width, height);
		sound=new SoundPlayer(this.getClass().getResource("/boundary/sound/welcomSound.wav"));


		
		btnSound=new SoundPlayer(this.getClass().getResource("/boundary/sound/click.wav"));
		playSound();

	
		this.logIn = new JButton("Log In");
		logIn.setIcon(new ImageIcon(this.getClass().getResource("/boundary/images/LogInBtn.png")));
		// logIn.setPreferredSize(new Dimension(80, 10000));

		this.reset = new JButton("Reset");
		reset.setIcon(new ImageIcon(this.getClass().getResource("/boundary/images/ResetBtn.png")));
		// reset.setBounds(242, 320);

		
		logInTitle = new JLabel("Log In To Your Account");
		logInTitle.setFont(new Font("Serif", Font.BOLD, 30));

		signUp = new JLabel("<HTML><U>not registered! Sign Up</U></HTML>");
		signUp.setFont(new Font("Serif", Font.BOLD, 18));
		signUp.setForeground(Color.RED); // Set the text color to red
		
		
		errMsg = new JLabel("");
		errMsg.setFont(new Font("Serif", Font.BOLD, 14));
		errMsg.setForeground(Color.RED);
		errMsg.setVisible(false);

		this.logInLbl = new JLabel("Enter ID:");
		logInLbl.setFont(new Font("Serif", Font.BOLD, 18));

		this.passwordLbl = new JLabel("Password:");
		passwordLbl.setFont(new Font("Serif", Font.BOLD, 18));

		this.txtId = new JTextField();
		this.txtPassword = new JPasswordField();
		txtId.setText("");
		txtId.setPreferredSize(new Dimension(200, 25));
		txtId.setText("");
		txtPassword.setPreferredSize(new Dimension(200, 25));

		//resize Pnl
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = (int) (screenSize.getWidth() * 0.8) / 2;
		height = (int) (screenSize.getHeight() * 0.8);

		// Resize the button
		logIn.setPreferredSize(new Dimension(100, 30)); // width, height
		reset.setPreferredSize(new Dimension(100, 30)); // width, height



		welcome=new WelcomePnl();
		ImageIcon background = new ImageIcon(this.getClass().getResource("/boundary/images/LogInPage.png"));
		Image image = background.getImage(); // transform it

		Image newimg = image.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way

		background = new ImageIcon(newimg); // transform it back

		// Create a JLabel with the background image
		/*JLabel backgroundLabel = new JLabel();
		backgroundLabel.setIcon(background);
		backgroundLabel.setLayout(new GridBagLayout());*/
		
		backgroundPnl=new JPanel();		
		backgroundPnl.setLayout(new GridLayout(1, 5));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);

		//JPanel panel = new JPanel(new GridBagLayout());
		JPanel panelInfo = new JPanel(new GridBagLayout());

		/*gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.PAGE_START;
		panel.add(backgroundLabel, gbc);*/

		
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		panelInfo.add(errMsg, gbc);

		// secound row
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		panelInfo.add(logInLbl, gbc);

		gbc.gridx = 2;
		gbc.anchor = GridBagConstraints.LINE_START;
		panelInfo.add(txtId, gbc);

		// third row
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.LINE_END;
		panelInfo.add(passwordLbl, gbc);

		gbc.gridx = 2;
		gbc.anchor = GridBagConstraints.LINE_START;
		panelInfo.add(txtPassword, gbc);

		// fourth row: sign up
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		panelInfo.add(signUp, gbc);

		logIn.setPreferredSize(new Dimension(130, 50));
		reset.setPreferredSize(new Dimension(120, 50));

		// fifth row: Buttons
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(20, 15, 15, 15);
		gbc.anchor = GridBagConstraints.LINE_END;
		panelInfo.add(logIn, gbc);

		gbc.gridx = 2;
		gbc.anchor = GridBagConstraints.LINE_END;
		panelInfo.add(reset, gbc);

		panelInfo.setBackground(new Color(173, 216, 230));
		
		backgroundPnl.add(panelInfo);
		backgroundPnl.add(welcome);
		contentPane.add(backgroundPnl);

	}

	private void fetchAndRefresh() {
		// TODO Auto-generated method stub
		usersInfo = LogInLogic.getInstance().getLogIn();

	}

	// checks if all Fields are entered
	public Boolean areAllFieldsFilled() {
		Boolean fieldsFilled = false;
		char[] password = txtPassword.getPassword();

		fieldsFilled = !txtId.getText().isEmpty() && !(password.length == 0);

		return fieldsFilled;
	}
	
	
	private void createEvents() {

		reset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				errMsg.setVisible(false);
				btnSound.clickSound();

				
				txtPassword.setText("");
				txtId.setText("");
				
			}
			
			
		});
		
		signUp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
				btnSound.clickSound();
				signUp.setForeground(Color.blue); // Set the text color to blue
				
				SignUp sigUp=new SignUp();
				stopSound();
		        txtPassword.setText("");
				txtId.setText("");			
				dispose();
				sigUp.setVisible(true);
				
				
            }
        });
		
		logIn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				errMsg.setVisible(false);
				btnSound.clickSound();
				
				String UserId = txtId.getText().toString();
				char[] passwordArray = txtPassword.getPassword();
				String password = new String(passwordArray);

			
				if (areAllFieldsFilled()) {
					fetchAndRefresh();
					if (usersInfo.containsKey(UserId)&& !usersInfo.get(UserId).getDeleted()) {
						LogInInfo user = usersInfo.get(UserId);
						if (!user.getDeleted()) {
							if (user.getPassword().equals(password)) {
								
								HomePage homePage=new HomePage(user);
								stopSound();
						        txtPassword.setText("");
								txtId.setText("");
								
								dispose();
								homePage.setVisible(true);
							} else {
								errMsg.setText("**incorrect password**");
								errMsg.setVisible(true);
							}
						} else {
							errMsg.setText("**application Deleted**");
							errMsg.setVisible(true);
						}

					} else {
						errMsg.setText("**no application with this ID exists**");
						errMsg.setVisible(true);
					}
				} else {
					errMsg.setText("**please Fill all fields**");
					errMsg.setVisible(true);
				}

			}
		});
	}
	
	public Boolean getLoggedIn() {
		return this.LoggedIn;
	}
	
	
	
	 public void playSound() {
	        sound.soundOn();
	        sound.loop();
	    }

	    public void stopSound() {
	        sound.stop();
	    }

	
	public void setLoggedIn(Boolean logged) {
		this.LoggedIn=logged;
	}
	
	public void setCurrentUser(LogInInfo user)
	{
		this.currentUser=user;
	}
	
	public LogInInfo getCurrentUser()
	{
		return this.currentUser;
	}

}
