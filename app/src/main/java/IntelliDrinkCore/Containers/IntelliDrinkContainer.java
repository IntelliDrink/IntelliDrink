package IntelliDrinkCore.Containers;

/**
 * Created by Terryn-Fredrickson on 4/8/15.
 */
public interface IntelliDrinkContainer {

    /**
     * Updates the DB with the info on the class
     */
    public void transfer();

    /**
     * Updates the Container from the DB
     */
    public void update();
}
