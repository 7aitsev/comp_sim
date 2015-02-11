package comp.assembler;

import comp.GenSettings;
import comp.random_access_memory.RandAccMemory;
import java.util.ArrayList;

public class Loader {
  private RandAccMemory pRam;

  public Loader(RandAccMemory ram) {
    pRam = ram;
  }

  public String load(ArrayList<Integer> bp) {
    if(bp.size() > GenSettings.ramAmount) {
      return "Not enough RAM amount for inputted program.\n"
              + "Required: " + bp.size() + ". RAM amount: " +
              GenSettings.ramAmount + ".";
    }
    for(int i = 0; i < bp.size(); ++i) {
      pRam.put(i, bp.get(i));
    }
    return "Program has loaded successfully!";
  }
}
