public class StationaryTile extends Tile
{
    public StationaryTile(String imageFilename, int rotation, int maxNuts, int x, int y)
    {
        super(imageFilename, rotation, 0, 0, x, y);
        this.picture = new Picture(imageFilename, rotation);
        this.canMoveHere = false;
        this.isMovable = false;
        this.maxNuts = 0;
    }
}