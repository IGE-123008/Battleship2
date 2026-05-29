/**
 * 
 */
package battleship;

import java.util.ArrayList;
import java.util.List;

/**
 * The interface Fleet.
 */
public interface IFleet
{
	/**
	 * The constant FLEET_SIZE.
	 */
	Integer FLEET_SIZE = 11;

	/**
	 * Gets ships.
	 *
	 * @return the ships
	 */
	List<IShip> getShips();

	/**
	 * Add ship boolean.
	 *
	 * @param s the s
	 * @return the boolean
	 */
	boolean addShip(IShip s);

	/**
	 * Gets ships like.
	 *
	 * @param category the category
	 * @return the ships like
	 */
	List<IShip> getShipsLike(String category);

    /**
     * Gets floating ships.
     *
     * @return the floating ships
     */
    /*
* (non-Javadoc)
*
* @see battleship.IFleet#getFloatingShips()
*/
default List<IShip> getFloatingShips()
{
        List<IShip> floatingShips = new ArrayList<IShip>();
        for (IShip s : getShips())
            if (s.stillFloating())
                floatingShips.add(s);

        return floatingShips;
}

    /**
     * Gets sunk ships.
     *
     * @return the sunk ships
     */
    /*
     * (non-Javadoc)
     *
     * @see battleship.IFleet#getSunkShips()
     */
    default List<IShip> getSunkShips()
    {
        List<IShip> sunkShips = new ArrayList<IShip>();
        for (IShip s : getShips())
            if (!s.stillFloating())
                sunkShips.add(s);

        return sunkShips;
    }

	/**
	 * Ship at ship.
	 *
	 * @param pos the pos
	 * @return the ship
	 */
	IShip shipAt(IPosition pos);

	/**
	 * Print status.
	 */
	void printStatus();
}
