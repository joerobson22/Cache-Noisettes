import java.util.ArrayList;

public class Piece
{
    ArrayList<Tile> tiles;

    public Piece()
    {
        tiles = new ArrayList<Tile>();
    }

    public void addTile(Tile t)
    {
        tiles.add(t);
    }
}