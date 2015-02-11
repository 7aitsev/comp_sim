package comp.assembler;

import comp.GenSettings;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.JTextComponent;

public class TextEditorPanel extends JPanel {
  private JTextComponent textComponent;
  
  public TextEditorPanel() {
    super(new BorderLayout());

    //  Creating a text component
    JTextPane paneEditor = new JTextPane();
    paneEditor.setPreferredSize(new Dimension(210,200));
    paneEditor.setFont(GenSettings.FONT);
    
    //  Connecting line numbers for the text component with which is related
    LineNumbers linesNumber = new LineNumbers(paneEditor, 2);
    
    //  Setting text component to the scroll panel
    JScrollPane scrollPaneEditor = new JScrollPane(paneEditor);
    scrollPaneEditor.setRowHeaderView(linesNumber);
    this.add(scrollPaneEditor, BorderLayout.CENTER);
    
    textComponent = paneEditor;
  }
  
  /**
   *  Gets the text component of TextEditorPanel class for possible following
   *  manipulations
   * 
   * @return textComponent the text component of TextEditorPanel
   */
  public JTextComponent getTextComponent() {
    return this.textComponent;
  }
}
