package ui;

import comp.GenSettings;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class StatusBar extends JPanel {
  private JTextArea status;
  
  public StatusBar(String str) {
    super(new BorderLayout(10, 2));
    status = new JTextArea(str);
    status.setRows(2);
    status.setEditable(false);
    status.setFont(GenSettings.FONT);
    status.setForeground(GenSettings.STATUS_FG_COLOR);
    status.setBackground(GenSettings.STATUS_BG_COLOR);
    status.setSelectedTextColor(GenSettings.STATUS_SELTXT_COLOR);
    status.setSelectionColor(GenSettings.STATUS_SEL_COLOR);
    
    JScrollPane sp = new JScrollPane(status);
    this.add(sp, BorderLayout.CENTER);
  }
  
  public void setStatus(String msg) {
    status.setText(msg);
  }
  
  public String getStatus() {
    return status.getText();
  }
}
