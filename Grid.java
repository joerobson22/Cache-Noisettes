public class Grid
{
    //the piece that is currently selected
    Piece selectedPiece;

    //2d array of tiles, stores information on the state of the 2d puzzle grid
    Tile[][] tileGrid;

    //hole coordinate and filled information
    final int[][] holeCoordinates = {{2, 0}, {0, 1}, {1, 2}, {3, 3}};
    boolean[] holesFilled = {false, false, false, false};

    /**
     * constructor for grid
     * @param tileGrid tileGrid calculated by readFile in the Window class
     */
    public Grid(Tile[][] tileGrid)
    {
        this.tileGrid = tileGrid;
        selectedPiece = null;
    }

    /**
     * checks if a given coordinate on the grid is a hole, by checking every predefined hole coordinate
     * @param coords the coordinates to check
     * @return returns an integer that corresponds to the hole in the 'holesFilled' array
     */
    public int coordIsHole(int[] coords)
    {
        for(int i = 0; i < holeCoordinates.length; i++)
        {
            if(coords[0] == holeCoordinates[i][0] && coords[1] == holeCoordinates[i][1])
                return i;
        }
        return -1;
    }

    /**
     * after a piece has been moves, it will leave behind a few 'null' tiles in the tileGrid
     * these need to be turned back into empty squares or holes- either filled or not filled
     */
    public void tidyNullTiles()
    {
        //loop through entire array
        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                //once a tile is null, it needs to be replaced by an empty tile or a hole
                if(tileGrid[i][j] == null)
                {
                    int[] coords = {i, j};
                    int hole = coordIsHole(coords);
                    //the hole information returned is not -1, meaning it is a hole
                    if(hole > -1)
                    {
                        //if this hole is filled, show it, using the 'holenut' picture
                        if(holesFilled[hole])
                            tileGrid[i][j] = new Tile("icons/HoleNut.png", 0, 1, 1, i, j);
                        //otherwise just use the hole image
                        else
                            tileGrid[i][j] = new Tile("icons/Hole.png", 0, 0, 1, i, j);
                    }
                    //the tile is just empty
                    else
                    {
                        tileGrid[i][j] = new Tile("icons/Empty.png", 0, 0, 0, i, j);
                    }
                }
            }
        }
    }

    /**
     * once all null squares have been removed, check if there are any nuts hovering over any empty holes
     * if so, drop that nut into that hole and update the game
     */
    public void nutsInHoles()
    {
        //loop through all tiles
        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                Tile t = tileGrid[i][j];
                int[] coords = {i, j};
                //once a hole has been found (coordIsHole returns a number greater then -1)
                int holeInfo = coordIsHole(coords);

                //if the tile contains a squirrel holding a nut...
                if(holeInfo > -1 && t.getHoldingNut())
                {
                    if(!holesFilled[holeInfo])
                    {
                        //falseify the fact that the squirrel is holding a nut, drop that nut into the given hole, and change any pictures
                        t.setHoldingNut(false);
                        holesFilled[holeInfo] = true;
                        t.setPicture(new Picture("icons/" + t.getColor() + "Squirrel1.png", t.getRotation()));
                    }
                }
            }
        }
    }

    /**
     * moves the currently selected piece as long as it isn't null
     * @param moveDirection the x and y values to move the piece by
     */
    public void moveSelectedPiece(int[] moveDirection)
    {
        if(selectedPiece != null)
        {
            //check if the move is valid
            if(selectedPiece.checkValidMove(tileGrid, moveDirection[0], moveDirection[1]))
            {
                //move the piece in that direction
                selectedPiece.move(tileGrid, moveDirection[0], moveDirection[1]);

                //find and tidy up any null squares
                tidyNullTiles();

                //now search through tile grid to identify if there is a nut over a hole
                nutsInHoles();
            }
        }
    }

    /**
     * selected a given tile that has been clicked as long as it is movable
     * @param x x coord of tile
     * @param y y coord of tile
     */
    public void tileClicked(int x, int y)
    {
        //System.out.println("Grid button (" + Integer.toString(i) + "," + Integer.toString(j) + ") pressed");
        Tile t = tileGrid[x][y];
        //if the tile is movable, set the selected piece to be the movable tile's piece
        if(t.isMovable())
        {
            selectedPiece = tileGrid[x][y].getPiece();
        }
    }

    /**
     * loops through all tiles, if any of them are holding a nut then the game is not over yet
     * @return true or false
     */
    public boolean hasWon()
    {
        for(Tile[] tiles : tileGrid)
            for(Tile t : tiles)
                if(t.getHoldingNut())
                    return false;
        return true;
    }

    /**
     * gets the currently selected piece to be highlighted
     * @return selectedPiece
     */
    public Piece getSelectedPiece()
    {
        return selectedPiece;
    }

    /**
     * gets the tile from a specified position in the tileGrid
     * @param x x position
     * @param y y position
     * @return tileGrid[x][y]
     */
    public Tile getTile(int x, int y)
    {
        return tileGrid[x][y];
    }
}