import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.*;

import com.google.appengine.api.utils.SystemProperty;

public class Account {
	//Global variables
	private JFrame frame, frmAccount;
	private JTextField txtUsageTime, txtUserName, txtAccountPassword;
	private final JTextField txtUserid = new JTextField();
	private JLabel lblAboutUser, lblUserName, lblUserID, lblPassword, lblUsageTime, lblUsageText;	
	private JButton btnLogOut;
	private JTextArea usageTextArea;
	public String url;
	/*
	 * Create the Application
	 */

	public void AccountInterface(String usernaMe, SimpleDateFormat timeStamp2) {
		initialize(usernaMe, timeStamp2);
	}
	
	/*
	 * Initialize the contents of the frame.
	 */
	private void initialize(String usernamE, SimpleDateFormat timeStamp) {
		//Name and set design for title of the frame
		frmAccount = new JFrame ();
		frmAccount.getContentPane().setBackground(Color.ORANGE);
		frmAccount.setTitle("Account");
		frmAccount.setBounds(100,100,754,449);
		frmAccount.getContentPane().setLayout(null);
		
		//Name and set text field for user name
		txtUserName = new JTextField();
		txtUserName.setBounds(118, 89, 137, 20);
		frmAccount.getContentPane().add(txtUserName);
		txtUserName.setColumns(10);
		
		//Name and set text field for user id
		txtUserid.setBounds(118, 151, 137, 20);
		frmAccount.getContentPane().add(txtUserid);
		txtUserid.setColumns(10);
				
		//Name and set text field for user password
		txtAccountPassword = new JTextField();
		txtAccountPassword.setBounds(118, 212, 137, 20);
		frmAccount.getContentPane().add(txtAccountPassword);
		txtAccountPassword.setColumns(10);
		
		
		//Get User ID, Password, Usage Text from Database		 
		getAccountInfo(usernamE);
		
		//Name, Set and Display time when user presses Account button
		txtUsageTime = new JTextField();
		txtUsageTime.setBounds(118, 277, 137, 20);
		frmAccount.getContentPane().add(txtUsageTime);
		txtUsageTime.setColumns(10);
		
		//Get time stamp when user press Recommendation Course button		 
		Calendar currentCalendar = Calendar.getInstance();		
		txtUsageTime.setText(timeStamp.format(currentCalendar));
		
		//Name and Set text field for "Usage Text"
		usageTextArea = new JTextArea();
		usageTextArea.setBounds(461, 182, 170, 98);
		frmAccount.getContentPane().add(usageTextArea);
		frmAccount.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Name and set design for "About Student" label
		lblAboutUser = new JLabel("About Student");
		lblAboutUser.setHorizontalAlignment(SwingConstants.CENTER);
		lblAboutUser.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		lblAboutUser.setBounds(277, 11, 121, 14);
		frmAccount.getContentPane().add(lblAboutUser);
		
		//Name and set design for "User Name" label
		lblUserName = new JLabel("User Name");
		lblUserName.setBounds(10, 92, 68, 14);
		frmAccount.getContentPane().add(lblUserName);
		
		//Name and set design for "User ID" label
		lblUserID = new JLabel("User ID");
		lblUserID.setBounds(10, 154, 46, 14);
		frmAccount.getContentPane().add(lblUserID);
		
		//Name and set design for "Password" label
		lblPassword = new JLabel("Password");
		lblPassword.setBounds(10, 215, 68, 14);
		frmAccount.getContentPane().add(lblPassword);
		
		//Name and set design for "Usage Time" label
		lblUsageTime = new JLabel("Usage Time");
		lblUsageTime.setBounds(10, 280, 68, 14);
		frmAccount.getContentPane().add(lblUsageTime);
				
		//Name and set "Usage Text" label
		lblUsageText = new JLabel("Usage Text");
		lblUsageText.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblUsageText.setHorizontalAlignment(SwingConstants.CENTER);
		lblUsageText.setBounds(500, 153, 83, 18);
		frmAccount.getContentPane().add(lblUsageText);
		
		//Name and set Log Out button
		btnLogOut = new JButton("Log Out");
		btnLogOut.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent b1){
				LogOut c1 = new LogOut();
				c1.LogOutInterface();
				//exit frame 
				frmAccount.dispose();
			}			
		});
		btnLogOut.setBounds(624, 336, 89, 23);
		frmAccount.getContentPane().add(btnLogOut);
		
		//Designs the black menu bar
		JMenuBar blackMenuBar = new JMenuBar();
		blackMenuBar.setForeground(Color.WHITE);
		blackMenuBar.setOpaque(true);
		blackMenuBar.setBackground(new Color(0,0,0));
		blackMenuBar.setPreferredSize(new Dimension(400, 40));
	
		//Sets gold color to panel
		JLabel goldLabel = new JLabel();
		goldLabel.setOpaque(true);
		goldLabel.setBackground(new Color(255, 215, 0));
		
		//Sets black color to menu bar
		frmAccount.setJMenuBar(blackMenuBar);
		
		//Name and sets "Back To Menu" button
		JButton btnBackToMenu = new JButton("Back to Menu");
		blackMenuBar.add(btnBackToMenu);
		btnBackToMenu.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent j){
				Student k = new Student();
				k.StudentInterface(usernamE);	
				//exits frame
				frmAccount.dispose();
			}			
		});
		//Sets frame visible to user
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frmAccount.setVisible(true);
	}

	/* 
	 * Displays user name, user id, user password, 
	 * and usage text to the student
	 * */
	private void getAccountInfo(String userNAme) {
		//declares statements
		Statement st = null;
		ResultSet rs10 = null;
		ResultSet rs11 = null; 
		ResultSet rs12 = null;
		ResultSet rs13 = null;
		Connection connect = null;
		String username = " root";
		String password = "1982";
		
		//Connects SQL				
		//Reads courses from database to combo box	
		
		try {
			if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
				// Load the class that provides the new "jdbc:google:mysql://" prefix.
				Class.forName("com.mysql.jdbc.GoogleDriver");
				url = "jdbc:google:mysql://inbound-isotope-113404:inbound-isotope-113404:sqldata/studentCourseAlloc";
			} else {
				// Local MySQL instance to use during development.
				Class.forName("com.mysql.jdbc.Driver");
				url = "jdbc:mysql://173.194.226.51:3306/studentCourseAlloc?user=root";
			}
			connect = DriverManager.getConnection(url, username, password);
			st = connect.createStatement();
			String query10 = "SELECT UserName FROM Account WHERE UserName = '"+ userNAme+"'";
			String query11 = "SELECT UserID FROM Account WHERE UserName = '"+ userNAme+"'" ;		
			String query12 = "SELECT Password FROM Accont WHERE UserName = '"+ userNAme+"'";
			String query13 = "SELECT UsageText FROM AccounLog WHERE Username = '"+ userNAme+"'";
			
			rs10 = st.executeQuery(query10);
			rs11 = st.executeQuery(query11);
			rs12 = st.executeQuery(query12);
			rs13 = st.executeQuery(query13);
				while(rs10.next()){
					String studentUserName = rs10.getObject(1).toString();
					txtUserName.setText(studentUserName);
				}
				while(rs11.next()){
					String studentUserID = rs11.getObject(1).toString();
					txtUserid.setText(studentUserID);
					CourseCompleted cp = new CourseCompleted();
					cp.CompleteInterface(userNAme,studentUserID);
				}
				while(rs12.next()){
					String studentPassword = rs12.getObject(1).toString();
					txtAccountPassword.setText(studentPassword);
				}
				while(rs13.next()){
					String usageText = rs13.getObject(1).toString();
					usageTextArea.setText(usageText);
				}
			
		} catch (SQLException e2) {
			e2.printStackTrace();
		} catch (Exception e) {			
			e.printStackTrace();
		}finally{//close the SQL connection to database
			try {
				rs10.close();
				st.close();
				connect.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}	
		}
		
	}
}
