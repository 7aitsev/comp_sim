package ui;

import comp.Computer;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class TestFrame {
  public static void main(String[] args) {
    try{
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch(Exception e) {
      JOptionPane.showMessageDialog(null, "Defoult style hasn't accepted",
        "Error #1", JOptionPane.WARNING_MESSAGE);
    }
    
    // Creating the computer
    Computer sim_comp = new Computer();
    
    // Creating a panel
    JPanel bgPanel = new JPanel(new GridBagLayout());

    
    // Adding all elements on the bgPanel
    bgPanel.add(sim_comp, new GridBagConstraints(0, 0, 1, 1, 1, 1,
            GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
            new Insets(5, 5, 5, 5), 0, 0));    

    //  Creating frame
    JFrame mainFrame = new JFrame("Computer Simulator");
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainFrame.setContentPane(bgPanel);
    mainFrame.setSize(960, 480);
    mainFrame.setLocationRelativeTo(null);
    mainFrame.setVisible(true);
  }
}
