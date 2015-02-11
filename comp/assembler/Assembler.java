package comp.assembler;

import comp.AddressBus;
import comp.control_unit.Commands;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.text.JTextComponent;

public class Assembler {
  private static final String TOKEN_COMMENT = ";";
  private static final String TOKEN_LABEL = ":";

  private ArrayList<Integer> binaryProgram;
  private ArrayList<Integer> dirLabels;
  private ArrayList<String> labels;
  private JTextComponent txt;

  public Assembler(JTextComponent jtc) {
    binaryProgram = new ArrayList();
    dirLabels = new ArrayList<>();
    labels = new ArrayList<>();
    txt = jtc;
  }

  private void fillDir(int cmdAddr, int addr) {
    int dat1 = 0, dat2 = 0; // addr = dat1 + dat2
    dat2 = addr & AddressBus.getCapacityMask()/2;
    dat1 = addr & (AddressBus.getCapacityMask()/2
                << AddressBus.getCapacity()/2);
    binaryProgram.set(cmdAddr + 1, dat1);
    binaryProgram.set(cmdAddr + 2, dat2);
  }

  private int smallFunc(String s) {
    int dir = 0;
    if(labels.contains(s)) {
      dir = dirLabels.get(labels.indexOf(s));
    } else {
      try {
        dir = Integer.parseInt(s, 16);
      } catch(NumberFormatException e) {
        return -1;
      }
    }
    return dir;
  }

  private int parseContent(String s, int cCmd) {
    int direct; // Stores the address
    if(s.contains("+")) {
      int n;
      String[] ops = s.split("\\+", 2);
      // We work whith both operand
      direct = smallFunc(ops[0]);
      n = smallFunc(ops[1]);
      if(direct == -1 || n == -1) {
        return -1;
      } else { // If there isn't errors
        direct += n; // Compute new address
        fillDir(cCmd, direct); // put address in the binaryProgram
        return 0;
      }
    }
    direct = smallFunc(s);
    if(direct == -1) 
      return -1;
    else {
      fillDir(cCmd, direct);
      return 0;
    }
  }

  public void resetAsm() {
    binaryProgram.clear();
    labels.clear();
    dirLabels.clear();
  }

  public String assemble() {
    // Split in lines
    String[] lines = txt.getText().split("\n");

    // Remove comments and all empty lines
    for(int i = 0; i < lines.length; ++i) {
      String line = lines[i];
      if(line.indexOf(TOKEN_COMMENT) >= 0) {
        line = line.substring(0, line.indexOf(TOKEN_COMMENT));
      }
      if(line.replaceAll("\\s", "").length() == 0) // removing whitespaces
        lines[i] = null;
      else
        lines[i] = line;
    }

    // Analyse assembly language program and generate machine code
    if(lines.length > 0) {
      // Clear last stored program and other data
      this.resetAsm();

      /*
       *  First pass.
       *  Store all commands and their lables with addresses
       */
      boolean codeFound = false;
      int currCmd = 0;
      for(int lineNumber = 0; lineNumber < lines.length; ++lineNumber) {
        if(lines[lineNumber] != null) { // Skip possible empty lines
          String line = lines[lineNumber];
          line = line.trim(); // Remove all leading and trailing spaces
          boolean hasLabel = false;
          if(line.indexOf(TOKEN_LABEL) > 0) { // We are working with a lable
            hasLabel = true;
            labels.add(line.substring(0,
                    line.replaceAll("\\s", "").indexOf(TOKEN_LABEL)));
            line = line.substring(line.indexOf(TOKEN_LABEL)+1,line.length());
          }
          lines[lineNumber] = line; // Save finished line
          StringTokenizer tokens = new StringTokenizer(line);
          String token = tokens.nextToken();
          // Search instruction. Its code must be first. Labels or was removed,
          //  or there was no it at all
          if(Commands.isACode(token)) { // Code was found
            binaryProgram.add(Commands.getOpCode(token));
            if(!codeFound) {
              currCmd = binaryProgram.size()-1;
              codeFound = true;
            }
            // Storing the label's address
            if(hasLabel) {
              dirLabels.add(binaryProgram.size()-1);
            }
            // Allocation for storing addresses (will be added in the last pass)
            binaryProgram.add(0); binaryProgram.add(0);
          } else if(hasLabel) {
            // Check, if it is a set of addresses, separated by a comma
            String[] dirs = line.split(",");
            int flag = dirLabels.size();
            for(String dir : dirs) {
              dir = dir.replaceAll("\\s+", "");
              try {
                binaryProgram.add(Integer.parseInt(dir, 16));
              } catch(NumberFormatException e) {
                return "Error reading data in the " + (lineNumber + 1) +
                        " line.\nAddresses must be a HEX and separated by a "
                        + "comma.";
              }
              // Add (only once) the address to which the label points
              if(flag == dirLabels.size())
                dirLabels.add(binaryProgram.size()-1);
            }
            lines[lineNumber] = null; // There is nothing more in line
          } else { // Remaining cases are not processed
            return "Error reading data in the " + (lineNumber + 1) +
                    " line.\nMake shure combinations of mnemonics and syntax "
                    + "for operations and addressing modes are corrected";
          }
        }
      }

      /*
       *  Second pass.
       *  Add addresses for instruction
       */
      for(int lineNumber = 0; lineNumber < lines.length; ++lineNumber) {
        if(lines[lineNumber] != null) { // Skip possible empty lines
          String line = lines[lineNumber];
          StringTokenizer tokens = new StringTokenizer(line);
          String token = tokens.nextToken();
          
          int opCode = Commands.getOpCode(token);
          if(opCode == Commands.getOpCode("HLT")) {
            line = line.replaceFirst(token, "").replaceAll("\\s", "");
            if(line.length() > 0)
              return "Warning!\nFound extra data in the " + (lineNumber + 1) +
                      " line.";
            currCmd += 3;
          } else if(opCode == Commands.getOpCode("STO")) {
            line = line.replaceFirst(token, "").replaceAll("\\s", "");
            String content;
            // Check if there are square brackets and a space between them
            if((line.indexOf("]") - line.indexOf("[")) > 0) {
              // If everything is correct, the line must get the form "[],A"
              content = line.substring(line.indexOf("[")+1, line.indexOf("]"));
              line = line.replace(content, "");
            } else
              return "Error reading data in the line " + (lineNumber + 1) +
                      " line.\nMake shure that label or address is inputted.";
            if(!line.equals("[],A"))
              return "Error reading data in the line " + (lineNumber + 1) + 
                      " line.\nA structure of assembly language program is " +
                      "invalid";
            // Now take a content betwen brackets and analyse it.
            //  Content must be int the form of "label", "label+N", "address"
            if(parseContent(content, currCmd) == -1)
              return "Error reading label or address in the " + (lineNumber + 1)
                       + " line.\nAn address must be a HEX number while a label"
                       + " must be declared first.";
            currCmd += 3;
          } else if(opCode == Commands.getOpCode("JMP") ||
                    opCode == Commands.getOpCode("JZ")  ||
                    opCode == Commands.getOpCode("JNZ")) {
            line = line.replaceFirst(token, "").replaceAll("\\s", "");
            // After the latest operation the line must contain only an address
            //  or label in the form of "label", "label+N", "address"
            if(parseContent(line, currCmd) == -1)
              return "Error reading label or address in the " + (lineNumber + 1)
                       + " line.\nAn address must be a HEX number while a "
                       + "label must be declared first.";
            currCmd += 3;
          } else if(opCode == Commands.getOpCode("LOD") ||
                    opCode == Commands.getOpCode("ADD") ||
                    opCode == Commands.getOpCode("ADC") ||
                    opCode == Commands.getOpCode("SUB") ||
                    opCode == Commands.getOpCode("SBB")) {
            line = line.replaceFirst(token, "").replaceAll("\\s", "");
            String content;
            if((line.indexOf("]") - line.indexOf("[")) > 0) {
              // If everything is correct, the line must get the form "A,[]"
              content = line.substring(line.indexOf("[")+1, line.indexOf("]"));
              line = line.replace(content, "");
            } else
              return "Error reading data in the line " + (lineNumber + 1) +
                      " line.\nMake shure that label or address is inputted.";
            if(!line.equals("A,[]"))
              return "Error reading data in the line " + (lineNumber + 1) + 
                      " line.\nA structure of assembly language program is " +
                      "invalid";
            if(parseContent(content, currCmd) == -1)
              return "Error reading label or address in the " + (lineNumber + 1)
                      + " line.\nAn address must be a HEX number while a label "
                       + "must be declared first.";
            currCmd += 3;
          }
        }
      }
    }
    return "Code was assembled successfully.\n" +
           "You can load your program directly into the simulator.";
  }

  public ArrayList<Integer> getBinary() {
    return binaryProgram;
  }
}
