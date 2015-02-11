package comp.random_access_memory;

import comp.AddressBus;
import comp.IntRegister;
import comp.Register;

public class RandAccMemory extends UIRandAccMemory
                           implements IntRegister {
  private Register[] memory;
  private int amount;
  /*
  Memory Address Register (MAR)
    Stores the address in memory for data transfer
  Memory  Data  Register  (MDR)
    Stores data to be transferred to memory 
  */
  private Register MAR = new Register();
  private Register MDR = new Register();

  public RandAccMemory(int amount) {
    this.amount = amount;
    if(amount >= 99 && amount <= (~(1 << 31)))
      this.amount &= AddressBus.getCapacityMask();

    memory = new Register[this.amount];
    for(int i=0; i != this.amount; ++i)
      memory[i] = new Register();
  }

  @Override
  public int getData() {
    MDR.setData(memory[MAR.getData()].getData());
    updateMdr(Integer.toString(MDR.getData())); // connected with UI
    return MDR.getData();
//    int addr = MAR.getData();
//    Register dat = memory[addr];
//    int val = dat.getData();
//    MDR.setData(val);
//    return MDR.getData();
  }

  @Override
  public void setData(int addr) {
    addr &= AddressBus.getCapacityMask();
    if(addr <= amount) {
      MAR.setData(addr);
      updateMar(Integer.toString(MAR.getData())); // connected with UI
    }
  }

  public void put(int addr, int value) {
    memory[addr].setData(value);
  }
  
  public void resetRam() { // connected with UI
    MAR.setData(0);
    updateMar("0");
    MDR.setData(0);
    updateMdr("0");
    for(int i=0; i != this.amount; ++i)
      memory[i] = new Register();
    // Update memory panel
  }
}
