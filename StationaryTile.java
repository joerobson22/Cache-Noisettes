/**
 * stationary tile, cannot move and cannot be moved into
 * @author Joe Robson
 */
public class StationaryTile extends Tile
{
    /**
     * constructor
     * @param imageFilename the image's filename
     * @param rotation the image's rotation
     * @param x the tile's x coordinate
     * @param y the tile's y coordinate
     */
    public StationaryTile(String imageFilename, int rotation, int x, int y)
    {
        super(imageFilename, rotation, 0, 0, x, y);
        this.picture = new Picture(imageFilename, rotation);
        this.canMoveHere = false;
        this.isMovable = false;
        this.maxNuts = 0;
    }
}