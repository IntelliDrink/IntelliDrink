package IntelliDrinkCore;

/**
 * Created by David on 4/6/2015.
 */
public class CustomerInformation {

    private int ID;
    private String customerName;
    private double balance;
    private String customerRFID;
    private boolean coolDown;

    public CustomerInformation()
    {
        String customerName = new String();
        String customerRFID = new String();
    }

    public boolean isCoolDown() {
        return coolDown;
    }

    public void setCoolDown(boolean coolDown) {
        this.coolDown = coolDown;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCustomerRFID() {
        return customerRFID;
    }

    public void setCustomerRFID(String customerRFID) {
        this.customerRFID = customerRFID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

}
