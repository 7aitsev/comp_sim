package comp.control_unit;

//import java.util.ArrayList;
//import java.util.Arrays;

public class Commands {
  public static final int LOD = 0b0001; // load
  public static final int ADD = 0b0010; // add
  public static final int SUB = 0b0011; // substract
  public static final int ADC = 0b0100; // add with carry
  public static final int SBB = 0b0101; // substract with borrow
  public static final int JMP = 0b0110; // jump
  public static final int HLT = 0b0111; // halt
  public static final int STO = 0b1000; // store form the accumulator
  public static final int JNZ = 0b1001; // jump if the AC doesn't store zero
  //  JZ - jump if the zero flag is setted in 1. In other words, jump if the
  //  most recent calculation gave a zero result.
  public static final int JZ  = 0b1010; 
  public static final int ILLEGAL = 0 ; // an instruction is illegal
  
//  private static final Integer[] insts = {
//    LOD, ADD, SUB, ADC, SBB, JMP, HLT
//  };
//  private static final ArrayList codes = new ArrayList<>(Arrays.asList(insts));
//
//  public static boolean isIlligal(int instruction) {
//    return !codes.contains((Integer)instruction);
//  }
  
  public static String decode(String codeStr) {
    int code = Integer.parseInt(codeStr);
    switch(code) {
      case LOD: return "LOD";
      case ADD: return "ADD";
      case SUB: return "SUB";
      case ADC: return "ADC";
      case SBB: return "SBB";
      case JMP: return "JMP";
      case HLT: return "HLT";
      case STO: return "STO";
      case JNZ: return "JNZ";
      case JZ : return "JZ" ;
      default : return "ILLEGAL";
    }
  }
  
  public static String encode(String codeStr) {
    switch(codeStr) {
      case "LOD": return Integer.toHexString(LOD).toUpperCase();
      case "ADD": return Integer.toHexString(ADD).toUpperCase();
      case "SUB": return Integer.toHexString(SUB).toUpperCase();
      case "ADC": return Integer.toHexString(ADC).toUpperCase();
      case "SBB": return Integer.toHexString(SBB).toUpperCase();
      case "JMP": return Integer.toHexString(JMP).toUpperCase();
      case "HLT": return Integer.toHexString(HLT).toUpperCase();
      case "STO": return Integer.toHexString(STO).toUpperCase();
      case "JNZ": return Integer.toHexString(JNZ).toUpperCase();
      case "JZ" : return Integer.toHexString(JZ).toUpperCase( );
      default   : return Integer.toHexString(     ILLEGAL     );
    }
  }
  
  public static boolean isACode(String codeStr) {
    switch(codeStr) {
      case "LOD": return true;
      case "ADD": return true;
      case "SUB": return true;
      case "ADC": return true;
      case "SBB": return true;
      case "JMP": return true;
      case "HLT": return true;
      case "STO": return true;
      case "JNZ": return true;
      case "JZ" : return true;
      default   : return false;
    }
  }
  
  public static int getOpCode(String codeStr) {
    switch(codeStr) {
      case "LOD": return LOD;
      case "ADD": return ADD;
      case "SUB": return SUB;
      case "ADC": return ADC;
      case "SBB": return SBB;
      case "JMP": return JMP;
      case "HLT": return HLT;
      case "STO": return STO;
      case "JNZ": return JNZ;
      case "JZ" : return JZ;
      default   : return 0;
    }
  }
}
