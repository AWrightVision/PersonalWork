import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;

public class LogOut {
	//Global variable
	private JFrame frmLogOut;
	/*
	 * Create the application.
	 */
	void LogOutInterface() {
		initialize();
	}
	/*
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//Name and Set design for title of frame
		frmLogOut = new JFrame();
		frmLogOut.getContentPane().setBackground(Color.ORANGE);
		frmLogOut.setTitle("Log Out");
		frmLogOut.setBounds(100, 100, 339, 238);
		frmLogOut.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLogOut.getContentPane().setLayout(null);
		
		//Name and Set design for "Log out successful" label
		JLabel lblLogOutSuccessful = new JLabel("Log out successful !");
		lblLogOutSuccessful.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 12));
		lblLogOutSuccessful.setHorizontalAlignment(SwingConstants.CENTER);
		lblLogOutSuccessful.setBounds(88, 83, 145, 14);
		frmLogOut.getContentPane().add(lblLogOutSuccessful);
		
		//Make Frame visible to user
		frmLogOut.setVisible(true);				
	}
}
