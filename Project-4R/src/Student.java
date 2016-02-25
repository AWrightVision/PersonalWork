import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;

import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class Student {
	//Global variables
	private JFrame frmStudent;
	private JTextField txtStudentMenu;
	public JLabel goldLabel;
	public JMenuBar blackMenuBar;

	/*
	 * Create the Application
	 */
	
	public void StudentInterface(String userNAME) {
		stuInterface(userNAME);
	}

	/*
	 * Initialize the content of the frame
	 * */
	public void stuInterface(String userName) {
		//Name and set Title of Frame
		frmStudent = new JFrame();
		frmStudent.setTitle("University Blackboard");
		frmStudent.setBounds(100, 100, 795, 425);
		frmStudent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Set black menu bar
		JMenuBar blackMenuBar = new JMenuBar();
		blackMenuBar.setOpaque(true);
		blackMenuBar.setBackground(new Color(0,0,0));
		blackMenuBar.setPreferredSize(new Dimension(400, 40));
		
		//Set gold menu bar
		JLabel goldLabel = new JLabel();
		goldLabel.setOpaque(true);
		goldLabel.setBackground(new Color(255, 215, 0));
		frmStudent.setJMenuBar(blackMenuBar);
		
		//Name and set "Log Out" button
		JButton btnLogOut = new JButton("Log Out");
		btnLogOut.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent w){
				LogOut x = new LogOut();
				x.LogOutInterface();
				//exit frame
				frmStudent.dispose();
			}			
		});
		blackMenuBar.add(btnLogOut);
		
		//Set text field for Student Menu
		txtStudentMenu = new JTextField();
		txtStudentMenu.setForeground(Color.WHITE);
		txtStudentMenu.setFont(new Font("Tahoma", Font.BOLD, 15));
		txtStudentMenu.setBackground(Color.BLACK);
		txtStudentMenu.setHorizontalAlignment(SwingConstants.CENTER);
		txtStudentMenu.setText("Student Menu");
		blackMenuBar.add(txtStudentMenu);
		txtStudentMenu.setColumns(10);
		
		//Name and set "Account Profile" label
		JLabel lblAccountProfile = new JLabel("Account Profile");
		lblAccountProfile.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 12));
		frmStudent.getContentPane().add(lblAccountProfile);
		
		//Name and set "Student Resources" label
		JLabel lblNewLabel = new JLabel("Student Resources");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 12));
		frmStudent.getContentPane().add(lblNewLabel);
		
		//Name and set "My Courses" label
		JLabel lblNewLabel_1 = new JLabel("My Courses");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 12));
		frmStudent.getContentPane().add(lblNewLabel_1);
		
		//Name and set Account button
		JButton btnAccount = new JButton("Account");		
		btnAccount.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent f){
				Account g = new Account();
				g.AccountInterface(userName, null);
				//exit frame
				frmStudent.dispose();
			}			
		});
		frmStudent.getContentPane().add(btnAccount);
		
		//Name and set Course Catalog button
		JButton btnCourseCatelog = new JButton("Course Catelog");
		btnCourseCatelog.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent h){
				Course i = new Course();
				i.CourseInterface(userName);	
				//exit frame
				frmStudent.dispose();
			}			
		});
		frmStudent.getContentPane().add(btnCourseCatelog);
		
		//Name and set Desired Courses button
		JButton btnDesiredCourses = new JButton("Desired Courses");
		btnDesiredCourses.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent n){
				DesiredCourses o = new DesiredCourses();
				o.DCInterface(userName);	
				//exit frame
				frmStudent.dispose();
			}			
		});
		frmStudent.getContentPane().add(btnDesiredCourses);
		
		//Name and set Recommended Courses button
		JButton btnRecommendedCourses = new JButton("Recommended Courses");
		btnRecommendedCourses.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent p){
				SimpleDateFormat timeStamp = new SimpleDateFormat("HH:mm:ss");
				RecommendedCourses q = new RecommendedCourses();
				q.RCInterface(userName, timeStamp);
				//exit frame
				frmStudent.dispose();
			}			
		});
		frmStudent.getContentPane().add(btnRecommendedCourses);
		
		JButton btnCompletedCourses = new JButton("Completed Courses");
		btnCompletedCourses.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent r2){
				CourseCompleted s2 = new CourseCompleted();
				s2.CompleteInterface(userName, "");
			}
		});
		
		/*
		 * Layout of the Frame
		 * */		
		GroupLayout groupLayout = new GroupLayout(frmStudent.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(goldLabel, GroupLayout.DEFAULT_SIZE, 779, Short.MAX_VALUE)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(41)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblAccountProfile, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(10)
									.addComponent(btnAccount, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)))
							.addGap(165)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(btnDesiredCourses)
								.addComponent(btnCourseCatelog)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(301)
							.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED, 172, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
							.addGap(72))
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addComponent(btnCompletedCourses)
							.addGap(52))))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(293)
					.addComponent(btnRecommendedCourses)
					.addContainerGap(341, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(goldLabel, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblNewLabel)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(11)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblAccountProfile)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(btnAccount))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblNewLabel_1)
									.addGap(18)
									.addComponent(btnCompletedCourses))))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(18)
							.addComponent(btnCourseCatelog)
							.addGap(18)
							.addComponent(btnDesiredCourses)))
					.addGap(18)
					.addComponent(btnRecommendedCourses)
					.addContainerGap(148, Short.MAX_VALUE))
		);
		frmStudent.getContentPane().setLayout(groupLayout);
		
		//Sets frame visible to user
		frmStudent.setVisible(true);
	}
}
