public class StationaryTile extends Tile
{
    public StationaryTile(String imageFilename, int rotation, int maxNuts)
    {
        super(imageFilename, rotation, maxNuts);
        this.picture = new Picture(imageFilename, rotation);
        this.canMoveHere = false;
        this.isMovable = false;
        this.maxNuts = 0;
    }
}