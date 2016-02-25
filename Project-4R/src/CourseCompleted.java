import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;

import com.google.appengine.api.utils.SystemProperty;

//import com.google.appengine.api.utils.SystemProperty;

public class CourseCompleted {
	//Global variables
	private JFrame frmCpacourse;
	private JTextPane currentCourseTextPane;
	private String url;
	/*
	 * Create the application.
	 */
	
	public void CompleteInterface(String useRname, String studentid) {
		CompleteCourse(useRname, studentid);		
	}

	/*
	 * Initialize the contents of the frame.
	 */
	private void CompleteCourse(String usErname, String studentiD) {
		//Name and set title for frame
		frmCpacourse = new JFrame();
		frmCpacourse.setTitle("CourseCompleted");
		frmCpacourse.setBounds(100, 100, 450, 300);
		frmCpacourse.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCpacourse.getContentPane().setLayout(new CardLayout(0, 0));
		
		//Create a Tab pane for content panel
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		tabbedPane.setBackground(Color.WHITE);
		frmCpacourse.getContentPane().add(tabbedPane, "name_96920599441569");
		
		//Name first tab "Current Courses"
		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addTab("Current Courses", null, tabbedPane_1, null);
		
		//Set text field to display current courses
		currentCourseTextPane = new JTextPane();
		tabbedPane_1.addTab("New tab", null, currentCourseTextPane, null);
		
		/*Get student current, past, or all course from the database*/
		getCourseInfo(usErname,studentiD);
		
		//Name and set back button 
		JButton btnBackToMenu = new JButton("Back To Menu");
		btnBackToMenu.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent j){
				Student k = new Student();
				k.StudentInterface(usErname);
				//exit frame
				frmCpacourse.dispose();
			}			
		});
		tabbedPane.addTab("Back To Menu", null, btnBackToMenu, null);
		
		//Set frame visible to user
		frmCpacourse.setVisible(true);
	}
	
	/*
	 * Method gets Current, Past or All Course based
	 * on the choice the user picked at the Student Menu
	 *  */
	private void getCourseInfo(String usErname, String studentID) {
		//SQL statements declared to execute
		
		Statement stm3 = null;
		ResultSet rs3 = null;		
		String username = " root";
		String password = "1982";
		
		//Connects to SQL database		
		Connection connect = null;
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
			stm3 = connect.createStatement();	
			String query3 = "SELECT * FROM StudentCourseCompleted WHERE StudentID = '"+ studentID + "'";
			rs3 = stm3.executeQuery(query3);
				while(rs3.next()){
					String currentCourse = rs3.getObject(1).toString();
					currentCourseTextPane.setText(currentCourse);
				}				
		} catch (SQLException e2) {
			e2.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{//close the SQL connection to database
			try {
				rs3.close();
				stm3.close();
				connect.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}	
		}

	}

}
