public class StationaryTile extends Tile
{
    public StationaryTile(String imageFilename, int rotation)
    {
        super(imageFilename, rotation, 0);
        canMoveHere = false;
        isMovable = false;
    }
}