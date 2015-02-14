package comp.control_unit;

import comp.AddressBus;
import comp.IntRegister;
import comp.Register;
import comp.arithmetic_logic_unit.ArithLogicUnit;
import comp.random_access_memory.RandAccMemory;

public class ControlUnit extends UIControlUnit {
  private RandAccMemory ram;
  private ArithLogicUnit alu = new ArithLogicUnit();
  private ProgramCounter pc = new ProgramCounter();
  private InstructionRegister ir = new InstructionRegister();

  public ControlUnit(int start, RandAccMemory ram, ArithLogicUnit alu) {
    this.ram = ram;
    this.alu = alu;

    pc.setData(start);
  }

  private class ProgramCounter implements IntRegister {
    Register dat1 = new Register(),
             dat2 = new Register();

    public void inc(int n) {
      int res = this.getData();
      res += n;
      this.setData(res);
    }

    @Override
    public int getData() {
      int address;
      address = dat1.getData();
      address <<= AddressBus.getCapacity()/2;
      address += dat2.getData();
      return address;
    } 

    @Override
    public void setData(int value) {
      int mask = AddressBus.getCapacityMask() >> (AddressBus.getCapacity()/2);
      dat2.setData(value & mask);
      mask <<= AddressBus.getCapacity();
      dat1.setData(value & mask);
      updatePc(Integer.toString(this.getData())); // connected with UI
      try {
        Thread.sleep(50);
      } catch (InterruptedException ex) {}
    }
  }

  private class InstructionRegister extends ProgramCounter {
    Register cmd  = new Register();

    public int getAddress() {
      return super.getData();
    }

    @Override
    public int getData() {
      return cmd.getData();
    }

    @Override
    public void setData(int instruction) {
      cmd.setData(instruction);
      pc.inc(1); ram.setData(pc.getData()); dat1.setData(ram.getData());
      pc.inc(1); ram.setData(pc.getData()); dat2.setData(ram.getData());
      updateIr(Integer.toString(this.getData()) + " " +            // connected
                             Integer.toString(this.getAddress())); //  with UI
    }
  }

  /*
   * MAR ← PC
   * IR ← RAM[MAR], PC ← PC+1
   */
  public void fetchCycle() {
    ram.setData(pc.getData());
    ir.setData(ram.getData()); pc.inc(1);
//    int addr = pc.getData();
//    ram.setData(addr);
//    int dat = ram.getData();
//    ir.setData(dat); pc.inc(1);
  }

  public int executeInstruction() {
    int instruction, address, memVal;

    this.fetchCycle();
    instruction = ir.getData();

  switch (instruction) {
    case Commands.ADD:
      address = ir.getAddress();
      ram.setData(address);
      memVal = ram.getData();
      alu.loadToRegB(memVal);
      alu.addWithoutCarry();
      break;
    case Commands.ADC:
      address = ir.getAddress();
      ram.setData(address);
      memVal = ram.getData();
      alu.loadToRegB(memVal);
      alu.addWithCarry();
      break;
    case Commands.SUB:
      address = ir.getAddress();
      ram.setData(address);
      memVal = ram.getData();
      alu.loadToRegB(memVal);
      alu.subWithoutBorrow();
      break;
    case Commands.SBB:
      address = ir.getAddress();
      ram.setData(address);
      memVal = ram.getData();
      alu.loadToRegB(memVal);
      alu.subWithBorrow();
    case Commands.HLT:
      break;
    case Commands.LOD:
      address = ir.getAddress();
      ram.setData(address);
      memVal = ram.getData();
      alu.loadToRegA(memVal);
      break;
    case Commands.STO:
      address = ir.getAddress();
      ram.setData(address);
      memVal = alu.getRegA();
      ram.put(address, memVal);
      break;
    case Commands.JNZ:
      if(alu.getRegA() != 0) {
        address = ir.getAddress();
        pc.setData(address);
      }
      break;
    case Commands.JZ:
      if(alu.getRegA() == 0) {
        address = ir.getAddress();
        pc.setData(address);
      }
      break;
    default:
      instruction = Commands.ILLEGAL;
      break;
    }
  return instruction;
  }

  public int getAddrFromPC() {
    return pc.getData();
  }

  public void resetCu() {
    pc.setData(0);
    ir.dat1.setData(0); ir.dat2.setData(0);
    updateIr("0 0"); // connected with UI
  }
}
