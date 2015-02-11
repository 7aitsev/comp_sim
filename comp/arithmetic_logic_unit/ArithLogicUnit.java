package comp.arithmetic_logic_unit;

import comp.DataBus;
import comp.Register;

public class ArithLogicUnit extends UIArithLogicUnit {
  private Register operandA,
                   operandB;
  private byte flag;

  public ArithLogicUnit() {
    operandA = new Register();
    operandB = new Register();
    flag = 0;
  }

  public void loadToRegA(int value) {
    operandA.setData(value & DataBus.getCapacityMask());
    updateOpA(Integer.toString(value)); // connected with UIArithLogicUnit
  }

  public void loadToRegB(int value) {
    operandB.setData(value & DataBus.getCapacityMask());
    updateOpB(Integer.toString(value)); // connected with UIArithLogicUnit
  }

  public void addWithoutCarry() {
    int sum = operandA.getData() + operandB.getData();
    loadToRegA(sum & DataBus.getCapacityMask());
    flag = ((sum & (1 << DataBus.getCapacity()))) != 0 ? (byte)1 : (byte)0;
  }

  public void addWithCarry() {
    int sum = operandA.getData() + operandB.getData() + flag;
    sum &= DataBus.getCapacityMask();
    loadToRegA(sum);
  }

  public void subWithoutBorrow() {
    int diff = operandA.getData() - operandB.getData();
    loadToRegA(diff & DataBus.getCapacityMask());
  }

  public void subWithBorrow() {
    int diff = operandA.getData() - operandB.getData() - flag;
    if(diff < 0) {
      flag = 1;
      diff += 100;
    }
    else
      flag = 0;
    diff &= DataBus.getCapacityMask();
    loadToRegA(diff);
  }

  public int getRegA() {
    return operandA.getData();
  }
  
  public void resetAlu() {
    this.loadToRegA(0);
    this.loadToRegB(0);
    this.flag = 0;
  }
}
