/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wca;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JComponent;
import javax.swing.JFrame;


public class GUIHelper {
	
	public static void showOnFrame(JComponent component, String frameName) {
		JFrame frame = new JFrame(frameName);
		WindowAdapter wa = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		};
		frame.addWindowListener(wa);
		frame.getContentPane().add(component);
		frame.pack();
		frame.setVisible(true);
	}

}