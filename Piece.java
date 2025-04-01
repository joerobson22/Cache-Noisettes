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

    public boolean checkValidMove(Tile[][] tileGrid, int movX, int movY)
    {
        for(Tile t : tiles)
        {
            int startX = t.getX();
            int startY = t.getY();
            int endX = startX + movX;
            int endY = startY + movY;

            if(!inBounds(endX, endY, 4)) return false;

            //if the tile is empty OR the tile is currently occupied
            if(!(tileGrid[endX][endY].canMoveHere() || tileGrid[endX][endY].getPiece() == this))
            {
                //cannot move here
                return false;
            }
        }
        //valid for all pieces to move in this direction
        return true;
    }

    public void move(Tile[][] tileGrid, int movX, int movY)
    {
        //initialise the tiles array so that they are sorted in a way so that a tile won't mvoe into another tile before it can move and mess up the movement sequence
        initTiles(movX, movY);

        //loop through all tiles, move them in the desired direction, set old square to null
        for(Tile t : tiles)
        {
            int oldX = t.getX();
            int oldY = t.getY();
            t.move(movX, movY);

            tileGrid[oldX][oldY] = null;
            tileGrid[t.getX()][t.getY()] = t;
        }
    }

    public boolean inBounds(int x, int y, int upper)
    {
        return(x >= 0 && x < upper && y >= 0 && y < upper);
    }


    public void initTiles(int movX, int movY)
    {
        //sort tile list
        //if movX is negative, the tile is moving left, so sort the tiles in terms of x position increasing
        //if movX is positive, the tile is moving right, so sort the tiles in terms of x position decreasing
        //same goes for movY
        String axis = "y";
        String order = "increasing";

        if(movX != 0)
        {
            axis = "x";
            if(movX > 0)
                order = "decreasing";
        }
        else
        {
            if(movY > 0)
                order = "decreasing";
        }

        sortTiles(axis, order);
    }

    public void sortTiles(String axis, String order)
    {
        //bubble sort the tiles dependent on the axis and order to get them in
        boolean swapped = true;
        while(swapped)
        {
            swapped = false;
            for(int i = 1; i < tiles.size(); i++)
            {
                int m1 = -1;
                int m2 = -1;

                if(axis == "x")
                {
                    m1 = tiles.get(i - 1).getX();
                    m2 = tiles.get(i).getX();
                }
                else
                {
                    m1 = tiles.get(i - 1).getY();
                    m2 = tiles.get(i).getY();
                }

                if(order == "increasing")
                {
                    if(m1 > m2)
                    {
                        Tile temp = tiles.get(i - 1);
                        tiles.set(i - 1, tiles.get(i));
                        tiles.set(i, temp);
                        swapped = true;
                    }
                }
                else
                {
                    if(m1 < m2)
                    {
                        Tile temp = tiles.get(i - 1);
                        tiles.set(i - 1, tiles.get(i));
                        tiles.set(i, temp);
                        swapped = true;
                    }
                }
            }
        }
    }
}