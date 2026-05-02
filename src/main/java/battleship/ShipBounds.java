package battleship;

public class ShipBounds {
    private final Ship ship;

    public ShipBounds(Ship ship) {
        this.ship = ship;
    }

    /**
     * Gets top most pos.
     *
     * @return the top most pos
     */
    /*
     * (non-Javadoc)
     *
     * @see battleship.IShip#getTopMostPos()
     */
    public int getTopMostPos() {
        int top = ship.getPositions().get(Ship.BOW_POSITION_INDEX).getRow();
        for (int i = 1; i < ship.getSize(); i++)
            if (ship.getPositions().get(i).getRow() < top)
                top = ship.getPositions().get(i).getRow();
        return top;
    }

    /**
     * Gets bottom most pos.
     *
     * @return the bottom most pos
     */
    /*
     * (non-Javadoc)
     *
     * @see battleship.IShip#getBottomMostPos()
     */
    public int getBottomMostPos() {
        int bottom = ship.getPositions().get(0).getRow();
        for (int i = 1; i < ship.getSize(); i++)
            if (ship.getPositions().get(i).getRow() > bottom)
                bottom = ship.getPositions().get(i).getRow();
        return bottom;
    }

    /**
     * Gets left most pos.
     *
     * @return the left most pos
     */
    /*
     * (non-Javadoc)
     *
     * @see battleship.IShip#getLeftMostPos()
     */
    public int getLeftMostPos() {
        int left = ship.getPositions().get(0).getColumn();
        for (int i = 1; i < ship.getSize(); i++)
            if (ship.getPositions().get(i).getColumn() < left)
                left = ship.getPositions().get(i).getColumn();
        return left;
    }

    /**
     * Gets right most pos.
     *
     * @return the right most pos
     */
    /*
     * (non-Javadoc)
     *
     * @see battleship.IShip#getRightMostPos()
     */
    public int getRightMostPos() {
        int right = ship.getPositions().get(0).getColumn();
        for (int i = 1; i < ship.getSize(); i++)
            if (ship.getPositions().get(i).getColumn() > right)
                right = ship.getPositions().get(i).getColumn();
        return right;
    }
}