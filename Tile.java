public class Tile
{
    private Picture picture;
    protected boolean canMoveHere;
    protected boolean isMovable;
    protected int maxNuts;
    protected int nuts;

    public Tile(String imageFilename, int rotation, int maxNuts)
    {
        this.picture = new Picture(imageFilename, rotation);
        canMoveHere = true;
        isMovable = false;
        this.maxNuts = maxNuts;
        this.nuts = 0;
    }

    public Picture getPicture()
    {
        return picture;
    }

    public boolean canMoveHere()
    {
        return canMoveHere;
    }

    public boolean isMovable()
    {
        return isMovable;
    }

    public Piece getPiece()
    {
        return null;
    }

    public int getMaxNuts()
    {
        return maxNuts;
    }

    public int getNuts()
    {
        return nuts;
    }

    public boolean addNut()
    {
        if(nuts < maxNuts)
        {
            nuts++;
            return true;
        }
        return false;
    }
}