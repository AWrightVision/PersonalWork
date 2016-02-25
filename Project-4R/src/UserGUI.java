import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class UserGUI {
	//Global variables
	private Label statusLabel;
	private Panel controlPanel;
	public JFrame frame;
	private JTextField txtWelcomeToBlackboard;
	
	//System Design
	public void systemDesign(){
		//Name and Set design for title of frame
		frame = new JFrame ("User Login");
		frame.setBounds(100,100,649,383);
		frame.getContentPane().setLayout(new GridLayout(3,1));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Design black menu bar
		JMenuBar blackMenuBar = new JMenuBar();
		blackMenuBar.setOpaque(true);
		blackMenuBar.setBackground(new Color(0,0,0));
		blackMenuBar.setPreferredSize(new Dimension(400, 40));
	
		//Design gold label
		JLabel goldLabel = new JLabel();
		goldLabel.setOpaque(true);
		goldLabel.setBackground(new Color(255, 215, 0));
		
		//Set black menu bar as JMenuBar
		frame.setJMenuBar(blackMenuBar);
		
		//Name and set "Welcome to Blackboard" title
		txtWelcomeToBlackboard = new JTextField();
		txtWelcomeToBlackboard.setForeground(Color.WHITE);
		txtWelcomeToBlackboard.setFont(new Font("Tahoma", Font.BOLD, 15));
		txtWelcomeToBlackboard.setBackground(Color.BLACK);
		txtWelcomeToBlackboard.setHorizontalAlignment(SwingConstants.CENTER);
		txtWelcomeToBlackboard.setText("Welcome to Blackboard");
		blackMenuBar.add(txtWelcomeToBlackboard);
		txtWelcomeToBlackboard.setColumns(10);
		frame.getContentPane().add(goldLabel, BorderLayout.CENTER);

	}
	/*
	 * Create the application.
	 */
	public void UserInterface() {
		initialize();
	}
	/*
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		
		//System Design
		systemDesign();
		
		//Name and set label
		statusLabel = new Label();
		statusLabel.setAlignment(Label.CENTER);
		statusLabel.setSize(350,100);
		
		//Set layout for the panel
		controlPanel = new Panel();
		controlPanel.setLayout(new FlowLayout());
		
		//Name and set Log in button
		Button loginButton = new Button ("LogIn");		
		loginButton.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent e){
				//takes users to Log in interface
				LogIn b = new LogIn();
				b.LogInInterface();	
				//close frame after button is clicked
				frame.dispose();
			}			
		});
		//Adds and sets panel and fram visible to user
		controlPanel.add(loginButton);		
		frame.getContentPane().add(controlPanel);
		frame.getContentPane().add(statusLabel);
		frame.setVisible(true);
	}
}
