package comp.assembler;

import comp.GenSettings;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.Utilities;

/**
 * The class displays line numbers for a connected JTextComponent, that
 * must use the same line height for each line. LineNumbers supports
 * highlighting of the current line number and has adjustable field width
 * 
 * @author Maxim Zaitsev
 * @version 1.1_16.11
 */
class LineNumbers extends JPanel
  implements CaretListener, DocumentListener {
  private final int HEIGHT = ((int)-1 >>> 3); // a litle magic is here

  //  Text component with which LineNumbers is connected
  private JTextComponent component;

  //  Properties that can be changed
  private Color currLineHighlight;
  private int minimumDisplayDigits;

  //  Keep old data to reduce the number of times the component
  //  needs to be repainted
  private int lastDigits;
  private int lastHeight;
  private int lastLine;

  /**
   *  Create a line numbers for a text component.
   *
   *  @param component  the connected text component
   *  @param minDisplayDigits  the number of digits used to calculate
   *                           the minimum width of the LineNumers
   */
  public LineNumbers(JTextComponent component, int minDisplayDigits) {
    this.component = component;
    setFont(component.getFont());
    setBorderInner(2);
    setCurrLineHighlight(GenSettings.NMBR_COLOR);
    setMinimumDisplayDigits(minDisplayDigits);
    component.getDocument().addDocumentListener(this);
    component.addCaretListener(this);
  }

  /**
   *  Create a line numbers component for a text component. Default value is 3
   *
   *  @param component  the related text component
   */
  public LineNumbers(JTextComponent component) {
    this(component, 3);
  }

  public final void setBorderInner(int borderInner) {
    Border inner = new EmptyBorder(0, borderInner, 0, borderInner);
    Border vertSeparator=new MatteBorder(0, 0, 0, 2, GenSettings.VSEP_COLOR);
    setBorder(new CompoundBorder(vertSeparator, inner));
  }

  /**
   *  Gets the current line rendering Color
   *
   *  @return the Color used to render the current line number
   */
  public Color getCurrentLineForeground() {
    return currLineHighlight == null ? getForeground() : currLineHighlight;
  }

  /**
   *  The Color used to render the current line digits. Default is Color.RED.
   *
   *  @param currentLineForeground  the Color used to render the current line
   */
  public final void setCurrLineHighlight(Color currentLineForeground)
  {
    this.currLineHighlight = currentLineForeground;
  }

  /**
   *  Gets the minimum display digits
   *
   *  @return the minimum display digits
   */
  public int getMinimumDisplayDigits()
  {
    return minimumDisplayDigits;
  }

  /**
   *  Specify the minimum number of digits used to calculate the preferred
   *  width of the component.
   *
   *  @param minimumDisplayDigits  the number digits used in the preferred
   *                               width calculation
   */
  public final void setMinimumDisplayDigits(int minimumDisplayDigits) {
    this.minimumDisplayDigits = minimumDisplayDigits;
    setPreferredWidth();
  }

  /**
   *  Calculate the width needed to display the maximum line number
   */
  private void setPreferredWidth() {
    Element root = component.getDocument().getDefaultRootElement();
    int lines = root.getElementCount();
    int digits = Math.max(String.valueOf(lines).length(), minimumDisplayDigits);

    //  Update sizes when number of digits in the line number changes
    if (lastDigits != digits) {
      lastDigits = digits;
      FontMetrics fontMetrics = getFontMetrics(getFont());
      int width = fontMetrics.charWidth('0') * digits;
      Insets insets = getInsets();
      int preferredWidth = insets.left + insets.right + width;

      Dimension d = getPreferredSize();
      d.setSize(preferredWidth, HEIGHT);
      setPreferredSize( d );
      setSize(d);
    }
  }

  /**
   *  Draw the line numbers
   *
   * @param g
   */
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    // Determine the width of the space available to draw the line number
    FontMetrics fontMetrics = component.getFontMetrics(component.getFont());
    Insets insets = getInsets();
    int availableWidth = getSize().width - insets.left - insets.right;

    // Determine the rows to draw within the clipped bounds.
    Rectangle clip = g.getClipBounds();
    int rowStartOffset = component.viewToModel(new Point(0, clip.y));
    int endOffset = component.viewToModel(new Point(0, clip.y + clip.height));

    while (rowStartOffset <= endOffset) {
      try {
        if(isCurrentLine(rowStartOffset))
          g.setColor(getCurrentLineForeground());
        else
            g.setColor(getForeground());

          // Get the line number as a string and then determine the
          // "X" and "Y" offsets for drawing the string.
          String lineNumber = getTextLineNumber(rowStartOffset);
          int stringWidth = fontMetrics.stringWidth(lineNumber);
          int x = (availableWidth - stringWidth) + insets.left;
          int y = getOffsetY(rowStartOffset, fontMetrics);
          g.drawString(lineNumber, x, y);

          //  Move to the next row
          rowStartOffset = Utilities.getRowEnd(component, rowStartOffset) + 1;
      }
      catch(Exception e) { break; }
    }
  }

  /*
   *  We need to know if the caret is currently positioned on the line we
   *  are about to paint so the line number can be highlighted.
   */
  private boolean isCurrentLine(int rowStartOffset) {
    int caretPosition = component.getCaretPosition();
    Element root = component.getDocument().getDefaultRootElement();

    if(root.getElementIndex(rowStartOffset)
      == root.getElementIndex(caretPosition))
      return true;
    else
      return false;
  }

  /*
   *  Get the line number to be drawn. The empty string will be returned
   *  when a line of text has wrapped.
   */
  private String getTextLineNumber(int rowStartOffset) {
    Element root = component.getDocument().getDefaultRootElement();
    int index = root.getElementIndex(rowStartOffset);
    Element line = root.getElement(index);

    if (line.getStartOffset() == rowStartOffset)
      return String.valueOf(index + 1);
    else
      return "";
  }

  /*
   *  Determine the Y offset for the current row
   */
  private int getOffsetY(int rowStartOffset, FontMetrics fontMetrics)
    throws BadLocationException {
    // Get the bounding rectangle of the row

    Rectangle r = component.modelToView(rowStartOffset);
    int y = r.y + r.height;

    // The text needs to be positioned above the bottom of the bounding
    //  rectangle based on the descent of the font(s) contained on the row.
    int descent = fontMetrics.getDescent();

    return y - descent;
  }

  // Implement CaretListener interface
  @Override
  public void caretUpdate(CaretEvent e) {
    // Get the line the caret is positioned on
    int caretPosition = component.getCaretPosition();
    Element root = component.getDocument().getDefaultRootElement();
    int currentLine = root.getElementIndex(caretPosition);

    // Need to repaint so the correct line number can be highlighted
    if (lastLine != currentLine) {
      repaint();
      lastLine = currentLine;
    }
  }

  //  Implement DocumentListener interface
  @Override
  public void changedUpdate(DocumentEvent e) {
    documentChanged();
  }

  @Override
  public void insertUpdate(DocumentEvent e) {
    documentChanged();
  }

  @Override
  public void removeUpdate(DocumentEvent e) {
    documentChanged();
  }

  /**
   *  A document change may affect the number of displayed lines of text.
   *  Therefore the lines numbers will also change.
   * 
   * @param NULL
   */
  private void documentChanged()
  {
    //  View of the component has not been updated at the time
    //  the DocumentEvent is fired
    SwingUtilities.invokeLater(() -> {
      try {
        int endPos = component.getDocument().getLength();
        Rectangle rect = component.modelToView(endPos);
        
        if (rect != null && rect.y != lastHeight) {
          setPreferredWidth();
          repaint();
          lastHeight = rect.y;
        }
      }
      catch (BadLocationException ex) { /* nothing to do */ }
    });
  }
}
