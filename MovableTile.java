/**
 * the movable tile class, of which extends the tile class
 * @author Joe Robson
 */
public class MovableTile extends Tile
{
    //movable tile specific variables
    Piece piece;
    boolean holdingNut;
    int rotation;
    String colorString;
    
    /**
     * constructor
     * @param piece the piece this tile belongs to
     * @param imageFilename the image filename to pass into the picture constructor
     * @param rotation the image rotation to pass into the picture constructor
     * @param x the tile's x coord
     * @param y the tile's y coord
     * @param holdingNut true or false to if the squirrel is holding a nut
     * @param colorString the color string of the squirrel
     */
    public MovableTile(Piece piece, String imageFilename, int rotation, int x, int y, boolean holdingNut, String colorString)
    {
        super(imageFilename, rotation, 0, 0, x, y);
        this.piece = piece;
        canMoveHere = false;
        isMovable = true;
        this.holdingNut = holdingNut;
        this.rotation = rotation;
        this.colorString = colorString;

        //adds this tile to the piece's tile arraylist
        piece.addTile(this);
    }

    /**
     * gets the piece this tile belongs to- overrides from base
     * @return piece
     */
    public Piece getPiece()
    {
        return piece;
    }

    /**
     * identifies if this movable tile is holding a nut or not- overrides from base
     * @return holdingNut- true or false
     */
    public boolean getHoldingNut()
    {
        return holdingNut;
    }

    /**
     * sets if this tile is holding a nut or not- overrides from base
     * @param val true or false
     */
    public void setHoldingNut(boolean val)
    {
        holdingNut = val;
    }

    /**
     * moves this piece's x and y coordinates for later reference by its piece- overrides from base
     * @param movX the x movement
     * @param movY the y movement
     */
    public void move(int movX, int movY)
    {
        this.x += movX;
        this.y += movY;
    }

    /**
     * gets the rotation of the tile's image
     * @return rotation
     */
    public int getRotation()
    {
        return rotation;
    }

    /**
     * gets the color string of the squirrel this movable tile belongs to
     * @return colorString
     */
    public String getColor()
    {
        return colorString;
    }   
}