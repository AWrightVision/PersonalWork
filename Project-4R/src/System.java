import java.awt.*;

public class System {
	
	public static void main (String[] args){
		//invoke System
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				try {
					UserGUI window = new UserGUI();
					window.UserInterface();
				} catch (Exception e) {
					e.printStackTrace();
				}
							
			}
		} );
		
	}

}
