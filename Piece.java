import java.util.ArrayList;

/**
 * the piece class, keeps track of all tiles related to it
 * @author Joe Robson
 */
public class Piece
{
    //the ArrayList of tiles
    ArrayList<Tile> tiles;

    /**
     * constructor, initialises the arraylist
     */
    public Piece()
    {
        tiles = new ArrayList<Tile>();
    }

    /**
     * adds a given tile to its tile list
     * @param t the tile to add
     */
    public void addTile(Tile t)
    {
        tiles.add(t);
    }

    /**
     * checks if a move is valid, given the tilegrid, and the mvoe direction
     * @param tileGrid the tilegrid to check with
     * @param movX the x movement
     * @param movY the y movement
     */
    public boolean checkValidMove(Tile[][] tileGrid, int movX, int movY)
    {
        //loop through all tiles
        for(Tile t : tiles)
        {
            //get their start point and end point
            int startX = t.getX();
            int startY = t.getY();
            int endX = startX + movX;
            int endY = startY + movY;

            //if not in bounds of the grid, this move is invalid
            if(!inBounds(endX, endY, 4)) return false;

            //if the tile is empty OR the tile is currently occupied is NOT part of this piece, you cannot move here
            if(!(tileGrid[endX][endY].canMoveHere() || tileGrid[endX][endY].getPiece() == this))
            {
                return false;
            }
        }
        //valid for all pieces to move in this direction
        return true;
    }

    /**
     * given that the move is valid, move each tile
     * @param tileGrid the tilegrid to update
     * @param movX the x movement
     * @param movY the y movement
     */
    public void move(Tile[][] tileGrid, int movX, int movY)
    {
        //initialise the tiles array so that they are sorted in a way so that a tile won't move into another tile before it can move and mess up the movement sequence
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

    /**
     * initialises the tile list, sorting them in a given order dependent on the x and y movement
     * @param movX the x movement
     * @param movY the y movement
     */
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
        
        //sort the tiles
        sortTiles(axis, order);
    }

    /**
     * sort the tiles given an axis to check ("x" or "y") and an order ("increasing" or "decreasing")
     * @param axis axis to check
     * @param order increasing or decreasing order
     */
    public void sortTiles(String axis, String order)
    {
        //bubble sort the tiles dependent on the axis and order to get them in
        boolean swapped = true;
        while(swapped)
        {
            swapped = false;
            //loop through entire tile array
            for(int i = 1; i < tiles.size(); i++)
            {
                //two measurements to compare
                int m1 = -1;
                int m2 = -1;

                //if comparing on x axis, set the measurements to be the tiles' x axises
                if(axis == "x")
                {
                    m1 = tiles.get(i - 1).getX();
                    m2 = tiles.get(i).getX();
                }
                //otherwise set the measurements to be the tiles' y axises
                else
                {
                    m1 = tiles.get(i - 1).getY();
                    m2 = tiles.get(i).getY();
                }

                //if sorting in increasing order, swap them if the first element is greater than the second element
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
                //otherwise, swap them if the first element is smaller than the second element
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

    /**
     * checks if given coordinates are within an upper bound
     * @param x x coordinate
     * @param y y coordinate
     * @param upper upper bound
     * @return true or false
     */
    public boolean inBounds(int x, int y, int upper)
    {
        return(x >= 0 && x < upper && y >= 0 && y < upper);
    }
}