import javax.swing.*;

import com.google.appengine.api.utils.SystemProperty;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DesiredCourses {
	//Global variable
	private JFrame frmDesiredCourse;
	private JTextField coursePriorityTextField;
	private JTextField enterCourseNameTextField;
	private JTextField archiveDesiredCoureTextField;
	public String url;
	/*
	 * Create the application.	
	 */
	/**
	 * @param uSername 
	 * @wbp.parser.entryPoint
	 */
	public void DCInterface(String uSername) {
		initialize(uSername);
	}

	/*
	 * Initialize the contents of the frame.
	 */
	private void initialize(String username) {
		//Name and set title of Frame
		frmDesiredCourse = new JFrame();
		frmDesiredCourse.getContentPane().setBackground(Color.ORANGE);
		frmDesiredCourse.setTitle("Desired Courses");
		frmDesiredCourse.setBounds(100, 100, 858, 386);
		frmDesiredCourse.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmDesiredCourse.getContentPane().setLayout(null);
		
		//Name and set "Desired Courses" label
		JLabel lblDesiredCourses = new JLabel("Desired Courses");
		lblDesiredCourses.setHorizontalAlignment(SwingConstants.CENTER);
		lblDesiredCourses.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblDesiredCourses.setBounds(310, 34, 143, 14);
		frmDesiredCourse.getContentPane().add(lblDesiredCourses);
		
		//Name and set "Priority Level (1 - 5)" label
		JLabel lblPriorityLevel = new JLabel("Priority Level(1 - 5)");
		lblPriorityLevel.setBounds(248, 59, 112, 14);
		frmDesiredCourse.getContentPane().add(lblPriorityLevel);
		
		//Set text field for "Priority Level"
		coursePriorityTextField = new JTextField();
		coursePriorityTextField.setBounds(405, 59, 86, 20);
		frmDesiredCourse.getContentPane().add(coursePriorityTextField);
		coursePriorityTextField.setColumns(10);
				
		//Name and set "Enter course name" label
		JLabel lblEnterCourseName = new JLabel("Enter course name");
		lblEnterCourseName.setBounds(248, 98, 112, 14);
		frmDesiredCourse.getContentPane().add(lblEnterCourseName);
		
		//Set text field for "Enter course name"
		enterCourseNameTextField = new JTextField();
		enterCourseNameTextField.setBounds(405, 95, 162, 30);
		frmDesiredCourse.getContentPane().add(enterCourseNameTextField);
		enterCourseNameTextField.setColumns(10);
				
		//Name and set "Archive Desired Courses" label
		JLabel lblArchive = new JLabel("Archive Desired Courses");
		lblArchive.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 12));
		lblArchive.setBounds(35, 135, 162, 14);
		frmDesiredCourse.getContentPane().add(lblArchive);
		
		//Set text field to display desired courses 
		archiveDesiredCoureTextField = new JTextField();
		archiveDesiredCoureTextField.setBounds(35, 181, 220, 121);
		frmDesiredCourse.getContentPane().add(archiveDesiredCoureTextField);
		archiveDesiredCoureTextField.setColumns(10);
		
		//Get archive desired courses 
		getArchiveCourse(username);
		
		//Name and set Back to Menu button
		JButton btnBackToMenu = new JButton("Back to Menu");
		btnBackToMenu.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent l1){
				Student m1 = new Student();
				m1.StudentInterface(username);
				//exit frame
				frmDesiredCourse.dispose();
			}			
		});
		btnBackToMenu.setBounds(0, 0, 130, 23);
		frmDesiredCourse.getContentPane().add(btnBackToMenu);
		
		//Name and set Log Out button
		JButton btnLogOut = new JButton("Log Out");
		btnLogOut.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent e1){
				LogOut d1 = new LogOut();
				d1.LogOutInterface();
				//exit frame
				frmDesiredCourse.dispose();
			}			
		});
		
		btnLogOut.setBounds(737, 0, 105, 23);
		frmDesiredCourse.getContentPane().add(btnLogOut);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.setBounds(478, 149, 89, 23);
		btnSubmit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent j1){
				insertDesiredCourse(username);
			}
		});
		frmDesiredCourse.getContentPane().add(btnSubmit);
		
		JButton btnClear = new JButton("Clear");
		btnClear.setBounds(371, 149, 89, 23);
		btnClear.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent t3){
				enterCourseNameTextField.setText("");
				coursePriorityTextField.setText("");
			}
		});
		frmDesiredCourse.getContentPane().add(btnClear);
		
		//Set frame visible
		frmDesiredCourse.setVisible(true);
	}
	
	/*
	 * Enters desired course and priority level in 
	 * by user when the Submit button is clicked
	 * */
	private void insertDesiredCourse(String useRName){
		//declared statements
		Statement state = null;
		ResultSet rs8 = null;
		ResultSet rs9 = null;
		Connection connect = null;
		String username = " root";
		String password = "1982";
		String enteredCourse = enterCourseNameTextField.getText().toString();
		String enteredLevel = coursePriorityTextField.getText().toString();	
		
		//connects to SQL						
		//executes the query 
		try{
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
			state = connect.createStatement();			
			String query8 = "INSERT INTO StudentCourseDesired VALUES = '" +enteredCourse+"'WHERE StudentID = '"+useRName+"'" ;
			String query9 = "INSERT INTO StudentCourseDesired VALUES = '" +enteredLevel+"' WHERE StudentID = '"+useRName+"'";			
			rs8 = state.executeQuery(query8);
			rs9 = state.executeQuery(query9);					

		}catch(SQLException l){
			l.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				rs8.close();
				rs9.close();
				state.close();
				connect.close();
			}catch(SQLException f1){
				f1.printStackTrace();
			}
		}
	
	}

	private void getArchiveCourse(String username) {
		
		Statement stat = null;
		ResultSet rs7 = null;
		Connection connect = null;
		String username1 = " root";
		String password1 = "1982";
		
		//Connects SQL						
		//Reads courses from database to combo box		
		//executes query7
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
				connect = DriverManager.getConnection(url, username1, password1);
				stat = connect.createStatement();
				String query7 = "SELECT * CourseID From StudentCourseDesiredArchive WHERE StudentID = '" + username +"'";
				rs7 = stat.executeQuery(query7);
					while(rs7.next()){
						String recommendCourse = rs7.getObject(1).toString();
						archiveDesiredCoureTextField.setText(recommendCourse);
					}					
			} catch (SQLException e3) {
				e3.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}finally{//close the SQL connection to database
				try {
					rs7.close();
					stat.close();
					connect.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}	
			}	
		
	}
}
