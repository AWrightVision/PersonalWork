import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.google.appengine.api.utils.SystemProperty;

public class RecommendedCourses {
	//Global variables
	private JFrame frmRecommendations;
	private JTextField timeStampTextField;
	private JTextField courseIdTextField;
	private JTextField studentIdTextField;
	private JTextArea recommendTextArea;
	public String url;
	private JTextArea pastRecommendTextArea;
	/*
	 * Create the application.
	 */
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public void RCInterface(String userName, SimpleDateFormat timeStamp) {
		initialize(userName, timeStamp);
	}

	/*
	 * Initialize the contents of the frame.
	 */
	private void initialize(String userName, SimpleDateFormat timeStamp) {
		//Name and set title for frame
		frmRecommendations = new JFrame();
		frmRecommendations.getContentPane().setBackground(Color.ORANGE);
		frmRecommendations.setTitle("Recommendations");
		frmRecommendations.setBounds(100, 100, 833, 386);
		frmRecommendations.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmRecommendations.getContentPane().setLayout(null);
		
		//Name and set "Course ID" label
		JLabel lblNewLabel = new JLabel("Course ID");
		lblNewLabel.setBounds(275, 119, 76, 14);
		frmRecommendations.getContentPane().add(lblNewLabel);
		
		//Name and set "Time" label
		JLabel lblTime = new JLabel("Time");
		lblTime.setBounds(10, 49, 46, 14);
		frmRecommendations.getContentPane().add(lblTime);
		
		//Set text field to view time stamp
		timeStampTextField = new JTextField();
		timeStampTextField.setBounds(44, 46, 86, 20);
		frmRecommendations.getContentPane().add(timeStampTextField);
		timeStampTextField.setColumns(10);
		
		//Get time stamp when user press Recommendation Course button		 
		Calendar currentCalendar = Calendar.getInstance();		
		timeStampTextField.setText(timeStamp.format(currentCalendar));
		
		//Set text field to receive course Id
		courseIdTextField = new JTextField();
		courseIdTextField.setBounds(374, 116, 86, 20);
		frmRecommendations.getContentPane().add(courseIdTextField);
		courseIdTextField.setColumns(10);
		/* Read in course id from user to*/
		
		//Name and set "Student ID" label
		JLabel lblStudentId = new JLabel("Student ID");
		lblStudentId.setBounds(272, 77, 79, 14);
		frmRecommendations.getContentPane().add(lblStudentId);
		
		//Set text field to receive student Id
		studentIdTextField = new JTextField();
		studentIdTextField.setBounds(374, 74, 86, 20);
		frmRecommendations.getContentPane().add(studentIdTextField);
		studentIdTextField.setColumns(10);
		/* Read in student Id from user */
		
		//Name and set "Course Recommendations" label
		JLabel lblCourseRecommendations = new JLabel("Course Recommendations");
		lblCourseRecommendations.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblCourseRecommendations.setHorizontalAlignment(SwingConstants.CENTER);
		lblCourseRecommendations.setBounds(272, 47, 188, 14);
		frmRecommendations.getContentPane().add(lblCourseRecommendations);
		
		//Name and set "Recommendation" label
		JLabel lblRecommendation = new JLabel("Recommendation");
		lblRecommendation.setBounds(275, 162, 111, 14);
		frmRecommendations.getContentPane().add(lblRecommendation);
		
		//Set recommend text area to view course recommendation from database
		recommendTextArea = new JTextArea();
		recommendTextArea.setBounds(358, 194, 221, 102);
		frmRecommendations.getContentPane().add(recommendTextArea);
		
		//Get course recommend from database		 
		getRecommendedCourse(userName);
		
		//Name and set "Archive Recommendations" label
		JLabel lblArchiveRecommendations = new JLabel("Archive Recommendations");
		lblArchiveRecommendations.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblArchiveRecommendations.setBounds(10, 145, 188, 14);
		frmRecommendations.getContentPane().add(lblArchiveRecommendations);
		
		//Set text area to view past recommended courses
		pastRecommendTextArea = new JTextArea();
		pastRecommendTextArea.setBounds(10, 194, 214, 102);
		frmRecommendations.getContentPane().add(pastRecommendTextArea);
		
		//Set Back To Menu button
		JButton btnBackToMenu = new JButton("Back To Menu");
		btnBackToMenu.setBounds(0, 0, 130, 23);
		btnBackToMenu.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent u){
				Student v = new Student();
				v.StudentInterface(userName);	
				//exit frame
				frmRecommendations.dispose();
			}			
		});
		frmRecommendations.getContentPane().add(btnBackToMenu);
		
		//Set Log Out button
		JButton btnLogOut = new JButton("Log Out");
		btnLogOut.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent g1){
				LogOut h1 = new LogOut();
				h1.LogOutInterface();	
				//exit frame
				frmRecommendations.dispose();
			}			
		});
		btnLogOut.setBounds(728, 0, 89, 23);
		frmRecommendations.getContentPane().add(btnLogOut);
		
		//Set frame visible to user
		frmRecommendations.setVisible(true);
	}

	private void getRecommendedCourse(String userName) {
		Statement stamt = null;
		ResultSet rs6 = null;
		ResultSet rs7 = null;
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
				stamt = connect.createStatement();
				String query6 = "SELECT * From StudentCourseRecommended WHERE StudentID = '" + userName +"'";
				String query7 = "SELECT * From StudentCourseRecommendedArchive WHERE StudentID = '" + userName +"'";
				rs6 = stamt.executeQuery(query6);
				rs7 = stamt.executeQuery(query7);
					while(rs6.next()){
						String recommendCourse = rs6.getObject(1).toString();
						recommendTextArea.setText(recommendCourse);
					}			
					while (rs7.next()){
						String archiveRecommendCourse = rs7.getObject(1).toString();
						pastRecommendTextArea.setText(archiveRecommendCourse);
					}
			} catch (SQLException e3) {
				e3.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}finally{//close the SQL connection to database
				try {
					rs6.close();
					stamt.close();
					connect.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}	
			}
		
	}

}
