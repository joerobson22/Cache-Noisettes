/**
 * the base tile class, normally represents the empty tile but can be extended to be a movable or stationary tile
 */
public class Tile
{
    //protected variables that can be inherited by children
    protected Picture picture;
    protected boolean canMoveHere;
    protected boolean isMovable;
    protected int maxNuts;
    protected int nuts;

    protected int x;
    protected int y;

    /**
     * constructor
     * @param imageFilename the image's filename
     * @param rotation the image's rotation
     * @param nuts how many nuts this tile has
     * @param maxNuts how many nuts the tile can hold
     * @param x the tile's x coordinate
     * @param y the tile's y coordinate
     */
    public Tile(String imageFilename, int rotation, int nuts, int maxNuts, int x, int y)
    {
        this.picture = new Picture(imageFilename, rotation);
        canMoveHere = true;
        isMovable = false;
        this.maxNuts = maxNuts;
        this.nuts = nuts;
        this.x = x;
        this.y = y;
    }

    /**
     * gets the tile's picture
     * @return picture
     */
    public Picture getPicture()
    {
        return picture;
    }

    /**
     * sets the tile's picture
     * @param p the new picture
     */
    public void setPicture(Picture p)
    {
        picture = p;
    }

    /**
     * returns the canMoveHere variable
     * @return canMoveHere
     */
    public boolean canMoveHere()
    {
        return canMoveHere;
    }

    /**
     * checks if the tile is movable or not
     * @return isMovable
     */
    public boolean isMovable()
    {
        return isMovable;
    }

    /**
     * returns how many nuts the tile can hold
     * @return maxNuts
     */
    public int getMaxNuts()
    {
        return maxNuts;
    }

    /**
     * gets the x coord of this tile
     * @return x
     */
    public int getX()
    {
        return x;
    }

    /**
     * gets the y coord of this tile
     * @return y
     */
    public int getY()
    {
        return y;
    }

    //overridden in child classes

    /**
     * gets the tile's parent piece (base tile has none)
     * @return null as default
     */
    public Piece getPiece()
    {
        return null;
    }

    /**
     * moves the tile, default does nothing, but overriden in MovableTile class
     * @param movX x movement
     * @param movY y movement
     */
    public void move(int movX, int movY)
    {

    }

    /**
     * returns if the squirrel is holding a nut or not
     * @return false as default
     */
    public boolean getHoldingNut()
    {
        return false;
    }

    /**
     * sets if the tile is holding a nut or not
     */
    public void dropNut()
    {

    }

    /**
     * gets the rotation of the tile's image
     * @return as default 0
     */
    public int getRotation()
    {
        return 0;
    }

    /**
     * gets the color string of the squirrel
     * @return as default null
     */
    public String getColor()
    {
        return null;
    }
}