package comp;

public class Register implements IntRegister {
  private int value;
  
  public Register() {
    value = 0;
  }

  @Override
  public final void setData(int value) {
    this.value = value;
  }

  @Override
  public int getData() {
    return value;
  }
}
