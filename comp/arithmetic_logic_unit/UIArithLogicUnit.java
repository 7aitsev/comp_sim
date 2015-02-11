package comp.arithmetic_logic_unit;

import comp.DataBus;
import comp.GenSettings;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class UIArithLogicUnit implements ActionListener {
  private JLabel opALabel, opBLabel;
  private JTextField opATField, opBTField;
  private JCheckBox cBox;
  public JPanel ALUPanel;

  public UIArithLogicUnit() {
    opALabel = new JLabel("AX:");
    opBLabel = new JLabel("BX:");
    opATField = new JTextField("00");
    opBTField = new JTextField("00");
    opATField.setFont(GenSettings.FONT);
    opBTField.setFont(GenSettings.FONT);
    opATField.setColumns(6);
    opBTField.setColumns(6);
    opATField.setEditable(false);
    opBTField.setEditable(false);
    opATField.setBackground(GenSettings.BG_COLOR);
    opBTField.setBackground(GenSettings.BG_COLOR);
    opATField.setHorizontalAlignment(JTextField.CENTER);
    opBTField.setHorizontalAlignment(JTextField.CENTER);

    cBox = new JCheckBox("Decimal");
    cBox.addActionListener(this);

    ALUPanel = new JPanel(new GridBagLayout());
    ALUPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(" ALU "));
    ALUPanel.add(opALabel, new GridBagConstraints(0, 0, 1, 1, 0, 0,
            GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets(5, 5, 5, 0), 0, 0));
    ALUPanel.add(opATField, new GridBagConstraints(1, 0, 1, 1, 0, 0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets(5, 0, 5, 5), 0, 0));
    ALUPanel.add(opBLabel, new GridBagConstraints(0, 1, 1, 1, 0, 0,
            GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets(5, 5, 5, 0), 0, 0));
    ALUPanel.add(opBTField, new GridBagConstraints(1, 1, 1, 1, 0, 0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets(5, 0, 5, 5), 0, 0));
    ALUPanel.add(cBox, new GridBagConstraints(0, 2, 2, 1, 0, 0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets(5, 2, 5, 0), 0, 0));
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
      StringBuilder out = new StringBuilder(opATField.getText());
      out = deleteLeadingZeros(out);
      int temp = Integer.parseInt(out.toString(), 16);
      opATField.setText(Integer.toString(temp));
      out.delete(0, out.length());
      out.append(opBTField.getText());
      out = deleteLeadingZeros(out);
      temp = Integer.parseInt(out.toString(), 16);
      opBTField.setText(Integer.toString(temp));
    }
    else {
      int temp = Integer.parseInt(opATField.getText(), 10);
      StringBuilder out = new StringBuilder(Integer.toHexString(temp));
      out = addLeadingZeros(out, DataBus.getCapacity()/4);
      opATField.setText(out.toString().toUpperCase());
      temp = Integer.parseInt(opBTField.getText(), 10);
      out.delete(0, out.length());
      out.append(Integer.toHexString(temp));
      out = addLeadingZeros(out, DataBus.getCapacity()/4);
      opBTField.setText(out.toString().toUpperCase());
    }
  }

  protected void updateOpA(String value) {
    if(cBox.isSelected())
      opATField.setText(value);
    else
      opATField.setText(toHex(value));
  }

  protected void updateOpB(String value) {
    if(cBox.isSelected())
      opBTField.setText(value);
    else
      opBTField.setText(toHex(value));
  }
}
