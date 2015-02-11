package comp;

public class DataBus implements IntBus {
  private static int capacity = 8;
  private static int capacityMask = 0xFF;

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
    if(size >= 1 || size <= 32) {
      capacity = size;
      this.createMask(size);
    }
  }
}
