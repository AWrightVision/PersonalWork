import java.awt.event.*;
import java.text.SimpleDateFormat;

import javax.swing.*;

public class LogIn {

	private JFrame frmLogIn;
	private JTextField userNameTextField;
	private JPasswordField passwordTextField;
	private JLabel lblPassword;
	private JButton btnSubmit;
	
	/*
	 * Create the application.
	 */
	void LogInInterface() {
		initialize();
	}

	/*
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//Name and Set design for title of frame
		frmLogIn = new JFrame();
		frmLogIn.setTitle("Log In");
		frmLogIn.setBounds(100, 100, 335, 208);
		frmLogIn.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLogIn.getContentPane().setLayout(null);
		
		//Name and Set design for User Type label
		JLabel lblUserType = new JLabel("User Type");
		lblUserType.setBounds(10, 29, 70, 14);
		frmLogIn.getContentPane().add(lblUserType);
		
		//Name and Set design for ComboBox
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Administrator", "Student", "Professor", "Teacher Assistant"}));
		comboBox.setBounds(109, 26, 146, 20);
		frmLogIn.getContentPane().add(comboBox);
				
		//Name and Set design for User Name label
		JLabel lblUserName = new JLabel("User Name");
		lblUserName.setBounds(10, 60, 89, 14);
		frmLogIn.getContentPane().add(lblUserName);
		
		//Set text field to receive user id
		userNameTextField = new JTextField();
		userNameTextField.setBounds(109, 57, 100, 20);
		frmLogIn.getContentPane().add(userNameTextField);
		userNameTextField.setColumns(10);
				
		//Set text field for password
		passwordTextField = new JPasswordField();
		passwordTextField.setBounds(109, 87, 100, 20);
		frmLogIn.getContentPane().add(passwordTextField);
		
		//Name and Set design for Password label
		lblPassword = new JLabel("Password");
		lblPassword.setBounds(10, 90, 89, 14);
		frmLogIn.getContentPane().add(lblPassword);
		
		//Name, set, and receive action from Submit button
		btnSubmit = new JButton("Submit");
		btnSubmit.setBounds(109, 118, 89, 23);
		btnSubmit.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent j){
				SimpleDateFormat timeStamp = new SimpleDateFormat("HH:mm:ss");
				String username = String.valueOf(userNameTextField.getText());				
				Student k = new Student();
				k.StudentInterface(username);
				Account l = new Account();
				l.AccountInterface(username, timeStamp);
				//exit frame when action is received
				frmLogIn.dispose();
			}			
		});
		//Make Frame visible to user
		frmLogIn.getContentPane().add(btnSubmit);
		frmLogIn.setVisible(true);
	}
}
