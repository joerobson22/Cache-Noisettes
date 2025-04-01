public class MovableTile extends Tile
{
    Piece piece;
    boolean holdingNut;
    int rotation;
    
    public MovableTile(Piece piece, String imageFilename, int rotation, int x, int y, boolean holdingNut)
    {
        super(imageFilename, rotation, 0, 0, x, y);
        this.piece = piece;
        canMoveHere = false;
        isMovable = true;
        this.holdingNut = holdingNut;
        this.rotation = rotation;

        piece.addTile(this);
    }

    public Piece getPiece()
    {
        return piece;
    }

    public boolean getHoldingNut()
    {
        return holdingNut;
    }

    public void setHoldingNut(boolean val)
    {
        holdingNut = val;
    }


    public void move(int movX, int movY)
    {
        this.x += movX;
        this.y += movY;
    }

    public int getRotation()
    {
        return rotation;
    }
}