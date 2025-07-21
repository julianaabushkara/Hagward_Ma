package boundary;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.border.EmptyBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import control.LogInLogic;
import entity.LogInInfo;

import javax.swing.JLayeredPane;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class HomePage extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel homePagePnl;
	private RptGeneratePeriodicalAcceptance AcceptanceReportPnl;
	private RptGenerateValidityReport rpValidityReport;
	private ImportJsonExportAcceptedXml exportImport;
	private FrmInitialPeriod updateInitialStatusPnl;
	private FrmUpdateStage updateStagePnl;
	private FrmExamptCourses examptCoursesPnl;

	private FrmAddNewApplication nwApplicationPnl;
	private AttachDocumentsPnl attachDocument;
	
	private FrmFinalPeriod finalPeriodPnl;
	private FrmAfterInterview afterInterviewPnl;
	
	

	// layOut
	private JLayeredPane layeredPane;

	// LogIn
	private HashMap<String, LogInInfo> users;
	private LogInInfo currentUser;
	private Boolean LoggedIn;

	// background
	private ImageIcon homePageBackground;
	private JLabel backgroundPage;

	// Get screen size
	private Dimension screenSize;// = Toolkit.getDefaultToolkit().getScreenSize();
	private int width;
	private int height;

	// the menu bar
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmUpdate;
	private JMenuItem mntmLogOut;
	private JMenuItem mntmPeriodicalAcceptanceReport;
	private JMenuItem mntmUpdateInitialStatus;
	private JMenuItem mntmUpdateFinalStatus;
	private JMenuItem mntmUpdateAfterInterviewStatus;

	private JMenuItem mntmExemptCourses;

	private JMenuItem mntmExportImport;
	private JMenuItem mntmUpdateApplicantInfo;

	private JMenuItem mntmValidityReport;
	private JMenuItem mntmUpdateApplicantsStages;

	private JMenuItem mntmExit;
	private JMenuItem mntmAttachDocument;
	private JMenu mnReports;
	private JMenu mnHomePage;
	private JMenu mnLogo;
	private JMenu mnUser;

	
	public HomePage(LogInInfo userId) {
		this.LoggedIn = true;
		this.currentUser = userId;
		
		
		//System.out.println(" in HomePage LoggedIn = " + LoggedIn + "/n");

		fetchAndRefresh();

		initComponents();
		createEvents();

	}

	private void createEvents() {
		// TODO Auto-generated method stub

	}

	private void initComponents() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = (int) (screenSize.getWidth() * 0.8);
		height = (int) (screenSize.getHeight() * 0.8);
		setResizable(false);
		setBounds(50, 50, width, height);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		layeredPane = new JLayeredPane();
		layeredPane.setBounds(05, 05, width - 23, height - 100);
		contentPane.add(layeredPane);
		layeredPane.setLayout(new CardLayout(0, 0));

		// Initialize panel
		homePagePnl = new JPanel();

		// logInPnl = new LogIn(this);
		AcceptanceReportPnl = new RptGeneratePeriodicalAcceptance();
		rpValidityReport = new RptGenerateValidityReport();
		exportImport = new ImportJsonExportAcceptedXml();
		updateStagePnl = new FrmUpdateStage();
		updateInitialStatusPnl = new FrmInitialPeriod();
		examptCoursesPnl = new FrmExamptCourses();
		
		finalPeriodPnl= new FrmFinalPeriod();
		afterInterviewPnl=new FrmAfterInterview();
		

		if (!currentUser.getId().equals("01") && !currentUser.getId().equals("0")) {
			attachDocument = new AttachDocumentsPnl(currentUser.getId());
			nwApplicationPnl = new FrmAddNewApplication(2, currentUser.getId());

		}

		// Initialize the initial panel
		backgroundPage = new JLabel("");
		backgroundPage.setBounds(0, 0, homePagePnl.getWidth(), height);
		homePageBackground = new ImageIcon(this.getClass().getResource("/boundary/images/Hagwarts.gif"));
		backgroundPage.setIcon(homePageBackground);
		homePagePnl.add(backgroundPage);
		homePagePnl.setBackground(Color.lightGray);

		/*private JMenuItem mntmUpdateFinalStatus;
		private JMenuItem mntmUpdateAfterInterviewStatus;*/
		
		// adds the j panels
		layeredPane.add(homePagePnl);
		layeredPane.add(updateStagePnl);
		layeredPane.add(examptCoursesPnl);
		layeredPane.add(updateInitialStatusPnl);
		layeredPane.add(finalPeriodPnl);
		layeredPane.add(afterInterviewPnl);


		
		

		if (!currentUser.getId().equals("01") && !currentUser.getId().equals("0")) {
			layeredPane.add(attachDocument);
			layeredPane.add(nwApplicationPnl);

		}
		layeredPane.add(rpValidityReport);
		layeredPane.add(exportImport);

		setTitle("HomePage");
		SwitchPanels(homePagePnl);
		createMenuBar();

	}

	private void fetchAndRefresh() {
		users = LogInLogic.getInstance().getLogIn();
	}

	public void createMenuBar() {
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		mnLogo = new JMenu("");
		ImageIcon Logo = new ImageIcon(this.getClass().getResource("/boundary/images/logo.png"));
		Image image = Logo.getImage(); // transform it
		Image newimg = image.getScaledInstance(100, 50, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
		Logo = new ImageIcon(newimg); // transform it back
		mnLogo.setIcon(Logo);
		menuBar.add(mnLogo);

		mnFile = new JMenu("File");
		menuBar.add(mnFile);

		mntmExit = new JMenuItem("Exit ");
		// exits the program if pressed E
		mntmExit.setMnemonic(KeyEvent.VK_E);
		// This line sets a tooltip for the eMenuItem.
		// "Exit program": This is the text that will be displayed as the tooltip.

		mntmExit.setToolTipText("Exit program");
		mntmExit.addActionListener((ActionEvent event) -> {
			System.exit(0);
		});
		mnFile.add(mntmExit);

		mnHomePage = new JMenu("HomePage");
		mnHomePage.addMenuListener(new MenuListener() {
			public void menuCanceled(MenuEvent e) {
			}

			public void menuDeselected(MenuEvent e) {
			}

			public void menuSelected(MenuEvent e) {
				setTitle("HomePage");
				SwitchPanels(homePagePnl);

			}
		});

		menuBar.add(mnHomePage);

		mnUser = new JMenu("User");
		menuBar.add(mnUser);

		mntmUpdate = new JMenuItem("Update");
		mntmUpdate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Open FrmOrders window and close the current frame
				setTitle("Log IN Form");
				updateMenuVisibility();

			}
		});
		mnUser.add(mntmUpdate);

		// mnUser.add(mntmNewApplicantion);
		
		
		
		
		
		
		
		// Add a menu item to "attach douments "
		mntmAttachDocument = new JMenuItem("Attach Documents");
		// Add ActionListener to the menu item
		mntmAttachDocument.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setTitle("Attach Documents");
				SwitchPanels(attachDocument);
			}

		});
		mnUser.add(mntmAttachDocument);

		// Add a menu item to "Reports"
		mntmUpdateApplicantsStages = new JMenuItem("Update Applicants Stages");
		// Add ActionListener to the menu item
		mntmUpdateApplicantsStages.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setTitle("Update Applicants Stages ");
				SwitchPanels(updateStagePnl);

			}
		});
		mnUser.add(mntmUpdateApplicantsStages);

		
		//validity List
		// Add a menu item to "applicant"
		mntmUpdateInitialStatus = new JMenuItem("Update Initial Applicants Status");
		mntmUpdateInitialStatus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setTitle("Update Applicants Status ");
				SwitchPanels(updateInitialStatusPnl);

			}
		});
		mnUser.add(mntmUpdateInitialStatus);

		
		
		// mntmUpdateStatus
		// Add a menu item to "applicant"
		mntmExemptCourses = new JMenuItem("Exempt Courses");
		mntmExemptCourses.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setTitle("exampt courses ");
				SwitchPanels(examptCoursesPnl);
			}
		});
		mnUser.add(mntmExemptCourses);

		
		//mntmUpdateInitialStatus;
		mntmUpdateApplicantInfo = new JMenuItem("Update Appicant Info ");
		mntmUpdateApplicantInfo.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		  setTitle("Update Profile");
		  SwitchPanels(nwApplicationPnl);
			}
		});
		mnUser.add(mntmUpdateApplicantInfo);
		
		mntmUpdateFinalStatus = new JMenuItem("Update Final Period  ");
		mntmUpdateFinalStatus.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		  setTitle("Update Applicants Status");
		  SwitchPanels(finalPeriodPnl);
			}
		});
		mnUser.add(mntmUpdateFinalStatus);
		
		
		mntmUpdateAfterInterviewStatus = new JMenuItem("Update after Interview");
		mntmUpdateAfterInterviewStatus.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		  setTitle("Update Applicants Status");
		  SwitchPanels(afterInterviewPnl);
			}
		});
		mnUser.add(mntmUpdateAfterInterviewStatus);


	
	
		// Add a menu item to "Registration"
		mntmLogOut = new JMenuItem("Log Out");
		mntmLogOut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				LoggedIn = false;
				currentUser = null;
				LogInPage logOut = new LogInPage();
				logOut.setLoggedIn(false);
				logOut.setVisible(true);
				dispose();
			}
		});
		mnUser.add(mntmLogOut);
		
		


		
		
		mnReports = new JMenu("Reports");
		// Add a menu item to "Reports"
		mntmPeriodicalAcceptanceReport = new JMenuItem("Periodical Acceptance Report ");
		mntmPeriodicalAcceptanceReport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setTitle("Periodical Acceptance Report ");

				SwitchPanels(AcceptanceReportPnl);

			}
		});
		mnReports.add(mntmPeriodicalAcceptanceReport);

		
		
		
		
		// Add a menu item to "Reports"
		mntmExportImport = new JMenuItem("export and import applicants data ");
		mntmExportImport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setTitle("export Xml and Import Json ");
				SwitchPanels(exportImport);

			}
		});
		mnReports.add(mntmExportImport);

		// mntmValidityReport
		// Add a menu item to "Reports"
		mntmValidityReport = new JMenuItem("Validity Report ");
		mntmValidityReport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setTitle("Validity Report ");
				SwitchPanels(rpValidityReport);

			}
		});
		mnReports.add(mntmValidityReport);

		menuBar.add(mnReports);
		updateMenuVisibility();

	}

	public void SwitchPanels(JPanel panel) {
		layeredPane.removeAll();
		panel.setSize(width - 100, height - 100);
		layeredPane.add(panel);
		layeredPane.repaint();
		layeredPane.revalidate();
	}

	public String getCurrentUser() {
		if (currentUser.equals(""))
			return "";
		else
			return currentUser.getId();
	}

	public void setCurrentUser(String id) {
		fetchAndRefresh();
		currentUser = users.get(id);
		updateMenuVisibility();
	}

	public void setLoggedIn() {
		LoggedIn = true;
		updateMenuVisibility();

		//System.out.println("  =setLoggedIn= LoggedIn = " + LoggedIn + "/n");
		//System.out.println("  =setLoggedIn= currentUser = " + currentUser.getId() + "/n");

	}
		


	private void updateMenuVisibility() {
		mntmLogOut.setVisible(LoggedIn);
		mntmUpdate.setVisible(!LoggedIn);
		mntmAttachDocument.setVisible(LoggedIn);
		mnReports.setVisible(!LoggedIn);
		mntmValidityReport.setVisible(false);
		mntmPeriodicalAcceptanceReport.setVisible(false);
		mntmExportImport.setVisible(false);
		mntmUpdateApplicantsStages.setVisible(false);
		mntmUpdateInitialStatus.setVisible(false);
		mntmExemptCourses.setVisible(false);
		mntmUpdateApplicantInfo.setVisible(true);
		mntmUpdateAfterInterviewStatus.setVisible(false);
		mntmUpdateFinalStatus.setVisible(false);

		if (currentUser.getId().equals("0")) {
			mntmLogOut.setVisible(LoggedIn);
			mntmUpdate.setVisible(false);
			mntmAttachDocument.setVisible(false);
			mnReports.setVisible(LoggedIn);
			mntmValidityReport.setVisible(false);
			mntmPeriodicalAcceptanceReport.setVisible(true);
			mntmExportImport.setVisible(true);
			mntmUpdateApplicantsStages.setVisible(false);

			mntmUpdateAfterInterviewStatus.setVisible(true);

			mntmUpdateInitialStatus.setVisible(true);
			mntmExemptCourses.setVisible(true);
			mntmUpdateApplicantInfo.setVisible(false);
			mntmUpdateFinalStatus.setVisible(true);



		}

		if (currentUser.getId().equals("01")) {
			mntmUpdateFinalStatus.setVisible(false);

			mntmUpdateAfterInterviewStatus.setVisible(false);
			mntmLogOut.setVisible(LoggedIn);
			mntmUpdate.setVisible(false);
			mntmAttachDocument.setVisible(false);
			mnReports.setVisible(LoggedIn);
			mntmValidityReport.setVisible(true);
			mntmPeriodicalAcceptanceReport.setVisible(false);
			mntmExportImport.setVisible(false);
			mntmUpdateApplicantsStages.setVisible(true);
			mntmUpdateInitialStatus.setVisible(false);
			mntmExemptCourses.setVisible(false);
			mntmUpdateApplicantInfo.setVisible(false);


		}

	}

	public JPanel getHomePagePanel() {
		return homePagePnl;
	}
}
