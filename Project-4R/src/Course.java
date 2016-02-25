import javax.swing.*;

import com.google.appengine.api.utils.SystemProperty;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.*;

public class Course {
	//Global variables
	private JFrame frmCourseCatelog;
	private JTextField txtCourseCatelog, enrollmentTextField;
	JTextArea prerequisiteTextArea;
	private JComboBox<String> classNameComboBox;	
	public String url;
	/*
	 * Create the application.
	 */
	
	/**
	 * @param userName 
	 * @wbp.parser.entryPoint
	 */
	public void CourseInterface(String userNAme) {
		initialize(userNAme);
	}

	/*
	 * Initialize the contents of the frame.
	 */
	private void initialize(String usernAme) {		
		//Name and set title of the frame
		frmCourseCatelog = new JFrame();
		frmCourseCatelog.getContentPane().setBackground(Color.ORANGE);
		
		//Set text field: Prompts user to choose a class
		txtCourseCatelog = new JTextField();
		txtCourseCatelog.setBounds(239, 88, 234, 20);
		txtCourseCatelog.setHorizontalAlignment(SwingConstants.CENTER);
		txtCourseCatelog.setText("Note: choose a class to see details.");
		txtCourseCatelog.setColumns(10);
		
		//Get user choice from combobox
		//Set a combo box to display classes		
		ComboBoxListener();
		
		//Name and set "Course Catalog" label
		JLabel lblCourseCatelog = new JLabel("Course Catelog");
		lblCourseCatelog.setBounds(304, 58, 113, 19);
		lblCourseCatelog.setFont(new Font("Tahoma", Font.BOLD, 15));
		frmCourseCatelog.getContentPane().add(lblCourseCatelog);
		
		//Name and set "Details" label
		JLabel lblNewLabel = new JLabel("Details");
		lblNewLabel.setBounds(0, 201, 78, 16);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		frmCourseCatelog.getContentPane().add(lblNewLabel);
		
		//Name and set "Enrollment Max" label
		JLabel lblEnrollmentLimit = new JLabel("Enrollment Max:");
		lblEnrollmentLimit.setBounds(10, 238, 83, 14);
		frmCourseCatelog.getContentPane().add(lblEnrollmentLimit);
		
		//Set text field to show course enrollment maximum
		enrollmentTextField = new JTextField();
		enrollmentTextField.setBounds(111, 235, 86, 20);
		enrollmentTextField.setColumns(10);
		/*Get course enrollment from database (based on student choice from combo box)*/
		
		//Name and set "Prerequisite" label
		JLabel lblPrerequisite = new JLabel("Prerequisite:");
		lblPrerequisite.setBounds(10, 274, 61, 14);
		frmCourseCatelog.getContentPane().add(lblPrerequisite);
		/*Get course prerequisite from database (based on student choice from combo box)*/
		
		//Set Back To Menu button
		JButton btnBackToMenu = new JButton("Back to Menu");
		btnBackToMenu.setBounds(0, 8, 119, 23);
		btnBackToMenu.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent l){
				Student m = new Student();
				m.StudentInterface(usernAme);	
				//exit frame
				frmCourseCatelog.dispose();
			}			
		});
		frmCourseCatelog.getContentPane().add(btnBackToMenu);
		
		//Set text area to display course prerequisites
		prerequisiteTextArea = new JTextArea();
		prerequisiteTextArea.setBounds(111, 274, 218, 109);
		
		//Resets the Enrollment and Prerequisite text area
		JButton btnRefreshButton = new JButton("Refresh");		
		btnRefreshButton.setBounds(384, 384, 89, 23);
		btnRefreshButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent redo){
				enrollmentTextField.setText("");
				prerequisiteTextArea.setText("");
			}
		});
		
		//Set Log Out button
		JButton btnLogOut = new JButton("Log Out");
		btnLogOut.setBounds(700, 8, 95, 23);
		btnLogOut.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent r){
				LogOut s = new LogOut();
				s.LogOutInterface();	
				//Exit frame
				frmCourseCatelog.dispose();
			}			
		});
		//Adds all contents of the frame to the pane
		frmCourseCatelog.getContentPane().add(btnRefreshButton);
		frmCourseCatelog.getContentPane().setLayout(null);
		frmCourseCatelog.getContentPane().add(lblCourseCatelog);
		frmCourseCatelog.getContentPane().add(lblNewLabel);
		frmCourseCatelog.getContentPane().add(lblEnrollmentLimit);
		frmCourseCatelog.getContentPane().add(lblPrerequisite);
		frmCourseCatelog.getContentPane().add(btnBackToMenu);
		frmCourseCatelog.getContentPane().add(enrollmentTextField);
		frmCourseCatelog.getContentPane().add(btnLogOut);
		//frmCourseCatelog.getContentPane().add(comboBox);
		frmCourseCatelog.getContentPane().add(txtCourseCatelog);		
		frmCourseCatelog.getContentPane().add(prerequisiteTextArea);
		frmCourseCatelog.setTitle("Course Catelog & Resources");
		frmCourseCatelog.setBounds(100, 100, 811, 457);
		frmCourseCatelog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Sets frame visible to user
		frmCourseCatelog.setVisible(true);
	}

	private void getCourseDetails(String courseName) {
		/*
		 * gets course chosen by user and displays
		 * enrollment max in the text box and displays
		 * prerequisites for the course in the text
		 * box
		*/			
		String username = " root";
		String password = "1982";
		Connection connection = null;
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
			
			connection = DriverManager.getConnection(url, username, password);
			Statement stmt = connection.createStatement();
			String getCourseID = "SELECT ID FROM Course WHERE CourseName = '"+ courseName+"'";
			String query2 = "SELECT EnrollmentMax From Course WHERE CourseName = '"+ courseName +"'" ;
			String query3 = "SELECT Prerequisite FROM CourseRequisites WHERE CourseID = '" + getCourseID +"'";
			ResultSet rs2 = stmt.executeQuery(query2);
			ResultSet rs3 = stmt.executeQuery(query3);
			while(rs2.equals(courseName)){
				String enrollMax = rs2.getObject(1).toString();
				String coursePrerequisite = rs3.getObject(1).toString();
				enrollmentTextField.setText(enrollMax);
				prerequisiteTextArea.setText(coursePrerequisite);
			}
		}catch(SQLException e){
			e.printStackTrace();
		} catch (Exception d) {			
			d.printStackTrace();
		}
	}

	private void ComboBoxListener() {
		/*
		 * Sets up combo box for user to choose a course name.
		 * Connects to the SQL database to receive a list of 
		 * courses.
		 * Reads user choice from the combo box to display 
		 * course details. */
	
		Statement state1 = null;		
		ResultSet rs= null;
		String username = " root";
		String password = "1982";
		classNameComboBox = new JComboBox<String>();
		classNameComboBox.setBounds(260, 126, 195, 20);	
		
		//Connects SQL			
		//Reads courses from database to combo box
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
			state1 = connect.createStatement();
			String str = "SELECT * FROM Course";
			rs = state1.executeQuery(str);
				while(rs.next()){
					classNameComboBox.addItem(rs.getString(1));
				}
			
		} catch (SQLException e2) {
			e2.printStackTrace();
		} catch (Exception e3) {		
			e3.printStackTrace();
		}finally{//close the SQL connection to database
			try {
				rs.close();
				state1.close();
				connect.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}	
		}
		
		//listens to the element the user selects
		classNameComboBox.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e){
				if((e.getStateChange()==ItemEvent.SELECTED)){
					String course = classNameComboBox.getSelectedItem().toString();
					getCourseDetails(course);
				}
			}
		});
		frmCourseCatelog.getContentPane().add(classNameComboBox);
		classNameComboBox.setMaximumRowCount(5000);
		classNameComboBox.setEditable(true);
		
	}
}
