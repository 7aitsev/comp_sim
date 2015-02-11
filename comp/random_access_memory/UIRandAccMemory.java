package comp.random_access_memory;

import comp.DataBus;
import comp.GenSettings;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class UIRandAccMemory implements ActionListener {
  private JTextField mdrTField, marTField;
  private JLabel mdrLabel, marLabel;
  private JCheckBox cBox;
  public JPanel ramPanel;
 // int start=0, end=16;
  
  public UIRandAccMemory() {
    mdrLabel = new JLabel("MDR:");
    marLabel = new JLabel("MAR:");
    mdrTField = new JTextField("00");
    marTField = new JTextField("00");
    mdrTField.setFont(GenSettings.FONT);
    marTField.setFont(GenSettings.FONT);
   
    cBox = new JCheckBox("Decimal");
    cBox.addActionListener(this);

    mdrTField.setColumns(6);
    marTField.setColumns(6);
    mdrTField.setEditable(false);
    marTField.setEditable(false);
    mdrTField.setBackground(GenSettings.BG_COLOR);
    marTField.setBackground(GenSettings.BG_COLOR);
    mdrTField.setHorizontalAlignment(JTextField.CENTER);
    marTField.setHorizontalAlignment(JTextField.CENTER);

    ramPanel = new JPanel(new GridBagLayout());
//    ramPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(" RAM "));
    JPanel ctrl = new JPanel(new GridBagLayout());
    ctrl.setBorder(BorderFactory.createTitledBorder(" Control Block "));
    ctrl.add(marLabel, new GridBagConstraints(0, 0, 1, 1, 0, 0,
            GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets(5, 5, 5, 0), 0, 0));
    ctrl.add(marTField, new GridBagConstraints(1, 0, 1, 1, 0, 0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets(5, 0, 5, 5), 0, 0));
    ctrl.add(mdrLabel, new GridBagConstraints(0, 1, 1, 1, 0, 0,
            GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets(5, 5, 5, 0), 0, 0));
    ctrl.add(mdrTField, new GridBagConstraints(1, 1, 1, 1, 0, 0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets(5, 0, 5, 5), 0, 0));
    ctrl.add(cBox, new GridBagConstraints(0, 2, 2, 1, 0, 0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets(5, 2, 5, 0), 0, 0));
    ramPanel.add(ctrl, new GridBagConstraints(0, 0, 1, 1, 1, 1,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0), 0, 0));
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

  private String toHex(String str) {
    int val = Integer.parseInt(str);
    StringBuilder out = new StringBuilder(Integer.toHexString(val));
    out = addLeadingZeros(out, DataBus.getCapacity()/4);
    return out.toString().toUpperCase();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if(cBox.isSelected()) {
      StringBuilder out = new StringBuilder(marTField.getText());
      out = deleteLeadingZeros(out);
      int temp = Integer.parseInt(out.toString(), 16);
      marTField.setText(Integer.toString(temp));
      out.delete(0, out.length());
      out.append(mdrTField.getText());
      out = deleteLeadingZeros(out);
      temp = Integer.parseInt(out.toString(), 16);
      mdrTField.setText(Integer.toString(temp));
    }
    else {
      int temp = Integer.parseInt(marTField.getText(), 10);
      StringBuilder out = new StringBuilder(Integer.toHexString(temp));
      out = addLeadingZeros(out, DataBus.getCapacity()/4);
      marTField.setText(out.toString().toUpperCase());
      temp = Integer.parseInt(mdrTField.getText(), 10);
      out.delete(0, out.length());
      out.append(Integer.toHexString(temp));
      out = addLeadingZeros(out, DataBus.getCapacity()/4);
      mdrTField.setText(out.toString().toUpperCase());
    }
  }

  protected void updateMar(String value) {
    if(cBox.isSelected())
      marTField.setText(value);
    else
      marTField.setText(toHex(value));
  }

  protected void updateMdr(String value) {
    if(cBox.isSelected())
      mdrTField.setText(value);
    else
      mdrTField.setText(toHex(value));
  }
}
