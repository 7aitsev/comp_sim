package comp;

public class AddressBus implements IntBus {
  private static int capacity = 16;
  private static int capacityMask = 0xFFFF;

  @Override
  public void createMask(int n) {
    capacityMask = ~((int)-1 << n);
  }
  
  public static int getCapacity() {
    return capacity;
  }
  
  public static int getCapacityMask() {
  return capacityMask;
}
  
  @Override
  public void setCapacity(int size) {
    if(size >= DataBus.getCapacity() || size <= 32) {
      capacity = size;
      this.createMask(size);
    }
  }
}
