public class Tile
{
    protected Picture picture;
    protected boolean canMoveHere;
    protected boolean isMovable;
    protected int maxNuts;
    protected int nuts;

    protected int x;
    protected int y;

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

    public Picture getPicture()
    {
        return picture;
    }

    public void setPicture(Picture p)
    {
        picture = p;
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

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public void move(int movX, int movY)
    {

    }

    public boolean getHoldingNut()
    {
        return false;
    }

    public void setHoldingNut(boolean val)
    {

    }

    public int getRotation()
    {
        return 0;
    }
}