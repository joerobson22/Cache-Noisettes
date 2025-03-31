public class MovableTile extends Tile
{
    Piece piece;
    
    public MovableTile(Piece piece, String imageFilename, int rotation)
    {
        super(imageFilename, rotation, 0);
        this.piece = piece;
        canMoveHere = false;
        isMovable = true;
    }

    public Piece getPiece()
    {
        return piece;
    }
}