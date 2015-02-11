package comp.control_unit;

import comp.AddressBus;
import comp.GenSettings;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JTextField;

public class UIControlUnit implements ActionListener {
  private JLabel irLabel, pcLabel;
  private JTextField irTFieldCmd, irTFieldDir, pcTField;
  private JCheckBox cBox;
  public JPanel cuPanel;

  public UIControlUnit() {
    irLabel = new JLabel("IR:");
    pcLabel = new JLabel("PC:");
    irTFieldCmd = new JTextField("0");
    irTFieldDir = new JTextField("0000");
    pcTField = new JTextField("0000");
    irTFieldCmd.setFont(GenSettings.FONT);
    irTFieldDir.setFont(GenSettings.FONT);
    pcTField.setFont(GenSettings.FONT);
    
    cBox = new JCheckBox("Decode");
    cBox.addActionListener(this);
    
    irTFieldCmd.setColumns(4);
    irTFieldDir.setColumns(10);
    pcTField.setColumns(10);
    irTFieldCmd.setEditable(false);
    irTFieldDir.setEditable(false);
    pcTField.setEditable(false);
    irTFieldCmd.setBackground(GenSettings.BG_COLOR);
    irTFieldDir.setBackground(GenSettings.BG_COLOR);
    pcTField.setBackground(GenSettings.BG_COLOR);
    irTFieldCmd.setHorizontalAlignment(JTextField.CENTER);
    irTFieldDir.setHorizontalAlignment(JTextField.CENTER);
    pcTField.setHorizontalAlignment(JTextField.CENTER);

    cuPanel = new JPanel(new GridBagLayout());
    cuPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(" Control Unit "));

    cuPanel.add(irLabel, new GridBagConstraints(0, 0, 1, 1, 0, 0,
            GridBagConstraints.EAST, GridBagConstraints.BOTH,
            new Insets(5, 5, 5, 0), 0, 0));
    cuPanel.add(irTFieldCmd, new GridBagConstraints(1, 0, 1, 1, 0, 0,
            GridBagConstraints.WEST, GridBagConstraints.BOTH,
            new Insets(5, 0, 5, 5), 0, 0));
    cuPanel.add(irTFieldDir, new GridBagConstraints(2, 0, 1, 1, 0, 0,
            GridBagConstraints.WEST, GridBagConstraints.BOTH,
            new Insets(5, 5, 5, 5), 0, 0));
    cuPanel.add(pcLabel, new GridBagConstraints(0, 1, 1, 1, 0, 0,
            GridBagConstraints.EAST, GridBagConstraints.BOTH,
            new Insets(5, 5, 5, 0), 0, 0));
    cuPanel.add(pcTField, new GridBagConstraints(1, 1, 1, 1, 0, 0,
            GridBagConstraints.WEST, GridBagConstraints.BOTH,
            new Insets(5, 0, 5, 5), 0, 0));
    cuPanel.add(cBox, new GridBagConstraints(2, 1, 1, 1, 0, 0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets(5, 5, 5, 0), 0, 0));
  }

  protected void updateIr(String value) {
    String str = value;
    if(cBox.isSelected()) {
      irTFieldCmd.setText(Commands.decode(str.substring(0, str.indexOf(" "))));
      irTFieldDir.setText(str.substring(str.indexOf(" ")+1, str.length()));
    }
    else {
      setIrTextFields(str);
    }
  }

  protected void updatePc(String value) {
    if(cBox.isSelected()) {
      pcTField.setText(value);
    }
    else {
      setPcTextField(value);
    }
  }

  private StringBuilder addLeadingZeros(StringBuilder str, int cap) {
    while(str.length() < cap)
      str.insert(0, "0");
    return str;
  }

  private StringBuilder deleteLeadingZeros(StringBuilder str) {
    while(str.length() > 1 && str.charAt(0) == '0') {
      str.deleteCharAt(0);
    }
    return str;
  }

  private void setIrTextFields(String s) {
    int cmd = Integer.parseInt(s.substring(0, s.indexOf(" ")));
    int dir = Integer.parseInt(s.substring(s.indexOf(" ")+1, s.length()));

    StringBuilder out = new StringBuilder(Integer.toHexString(dir));
    out = addLeadingZeros(out, AddressBus.getCapacity()/4);
    
    irTFieldCmd.setText(Integer.toHexString(cmd).toUpperCase());
    irTFieldDir.setText(out.substring(0, out.length()).toUpperCase());
  }

  private void setPcTextField(String s) {
    int dir = Integer.parseInt(s);
    StringBuilder out = new StringBuilder(Integer.toHexString(dir));
    out = addLeadingZeros(out, AddressBus.getCapacity()/4);
    pcTField.setText(out.toString().toUpperCase());
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    try {
      if(cBox.isSelected()) {
        irTFieldCmd.setText(Commands.decode(irTFieldCmd.getText()));
        StringBuilder out = new StringBuilder(irTFieldDir.getText());
        out = deleteLeadingZeros(out);
        int temp = Integer.parseInt(out.toString(), 16);
        irTFieldDir.setText(Integer.toString(temp));
        out.delete(0, out.length());
        out.append(pcTField.getText());
        out = deleteLeadingZeros(out);
        temp = Integer.parseInt(out.toString(), 16);
        pcTField.setText(Integer.toString(temp));
      }
      else {
        irTFieldCmd.setText(Commands.encode(irTFieldCmd.getText()));
        int temp = Integer.parseInt(irTFieldDir.getText(), 10);
        StringBuilder out = new StringBuilder(Integer.toHexString(temp));
        out = addLeadingZeros(out, AddressBus.getCapacity()/4);
        irTFieldDir.setText(out.toString().toUpperCase());
        temp = Integer.parseInt(pcTField.getText(), 10);
        out.delete(0, out.length());
        out.append(Integer.toHexString(temp));
        out = addLeadingZeros(out, AddressBus.getCapacity()/4);
        pcTField.setText(out.toString().toUpperCase());
      }
    }
    catch (NumberFormatException ex) {
      // nothing to do
    }
  }
}
