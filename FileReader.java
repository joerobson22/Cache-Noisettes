import java.awt.Color;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class FileReader
{
    final static Color EMPTY = new Color(185, 122, 87);            //EMPTY SQUARE
    final static Color WHITE = new Color(255, 255, 255);           //UNCONNECTED TILE/ HOLE
    final static Color GRASS = new Color(34, 177, 76);             //GREEN TILE (could me movable or immovable)
    final static Color NUT = new Color(255, 201, 14);              //NUT
    final static Color FLOWER = new Color(237, 28, 36);            //FLOWER/ blocked hole

    /**
     * loads a file/ .bmp image given the .bmp filename
     * @param filename the filename to load
     */
    public static Tile[][] loadFile(String filename) throws IOException
    {
        Tile[][] tileGrid = new Tile[4][4];
        //get path and setup ImageIO reader
        String path = "levels/" + filename;
        BufferedImage image = ImageIO.read(new File(path));

        //then read all colour pixel data from .bmp file into 15x15 colour array
        Color[][] colourArray = getColourArray(image);

        //once all this data is in the colour array:
        //look at all central squares (1,1), (5,1), (9,1)... (each square is 4 pixels apart)
        for(int i = 0; i < 4; i++)
        {
            int y = (i * 4) + 1;
            for(int j = 0; j < 4; j++)
            {
                int x = (j * 4) + 1;
                Color color = colourArray[x][y];
                Tile t = null;
                //square == EMPTY: square is empty
                if(isSameColor(color, EMPTY))
                {
                    //create new Tile with max nuts of 0
                    t = new Tile("icons/Empty.png", 0, 0);
                }
                //square == WHITE: square is an empty hole
                else if(isSameColor(color, WHITE))
                {
                    //create new Tile with max nuts of 1
                    t = new Tile("icons/Hole.png", 0, 1);
                }
                //square == NUT: square is a nut from a squirrel
                else if(isSameColor(color, NUT))
                {
                    //in this case, create a new Piece class
                    Piece piece = new Piece();
                    
                    t = getNutTile(tileGrid, i, j, piece, colourArray, x, y);
                    
                    //to identify all connected pieces, look at the 8 connected lines around the nut. if any of them are green, then that piece is connected to the nut
                    //looking at connected lines: 
                    completePiece(piece, tileGrid, colourArray, i, j, x, y, getRotation(colourArray, x, y));
                }
                //square == FLOWER: square is a blocked hole
                else if(isSameColor(color, FLOWER))
                {
                    //create a new StationaryTile object
                    t = new StationaryTile("icons/Flower.png", 0, 0);
                }
                //square == anything else: square is a squirrel's body or connected to a nut, continue
                else continue;

                //add the new tile to the grid
                tileGrid[i][j] = t;
            }
        }
        return tileGrid;     
    }

    public static Color[][] getColourArray(BufferedImage image)
    {
        Color[][] colourArray = new Color[15][15];
        for(int y = 0; y < 15; y++)
        {
            //System.out.println("y: " + Integer.toString(y));
            for(int x = 0; x < 15; x++)
            {
                colourArray[x][y] = new Color(image.getRGB(x, y));
                //System.out.println(colourArray[x][y]);
            }
        }
        return colourArray;
    }

    /*
    public static Tile[][] loadFile(String filename)
    {
        tileGrid = new Tile[4][4];
        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                Tile t = new Tile("icons/Empty.png", 0, 0);
                if(coordIsHole(new int[] {i, j}))
                {
                    t = new Tile("icons/Hole.png", 0, 1);
                }
                tileGrid[i][j] = t;
            }
        }
    } 
    */

    public static boolean isSameColor(Color color1, Color color2)
    {
        return(color1.getRed() == color2.getRed() && color1.getGreen() == color2.getGreen() && color1.getBlue() == color2.getBlue());
    }

    public static Tile getNutTile(Tile[][] tileGrid, int i, int j, Piece piece, Color[][] colourArray, int x, int y)
    {
        int rotation = getRotation(colourArray, x, y);
        //use this to get the rotation of the images
        Tile t = new MovableTile(piece, "icons/RedSquirrel1Nut.png", rotation);
        return t;
    }

    public static int getRotation(Color[][] colourArray, int x, int y)
    {
        //to get rotation, look at tiles left, right, up and down the nut. if one of them IS NOT GREEN, that's where the squirrel's hands are
        int[][] directions = {{0, 1}, {-1, 0}, {0, -1}, {1, 0}};
        int rotation = 0;
        for(int[] dir : directions)
        {
            if(!isSameColor(colourArray[x + dir[0]][y + dir[1]], GRASS))
            {
                break;
            }
            rotation += 90;
        }
        return rotation;
    }


    public static void completePiece(Piece piece, Tile[][] tileGrid, Color[][] colourArray, int i, int j, int x, int y, int rotation)
    {
        int originX = x;
        int originY = y;
        int[][] directions = {{0, 1}, {-1, 0}, {0, -1}, {1, 0}};
        int numConnections = 1;
        while(numConnections > 0)
        {
            numConnections = 0;
            //check all directions for the nut piece
            for(int[] dir : directions)
            {
                int newX = x + dir[0] * 2;
                int newY = y + dir[1] * 2;
                //if a connecting square is non white, it must be connected to us. 
                if(!isSameColor(colourArray[x][y], WHITE))
                {
                    //Therefore, as long as it's not already been made: create new tile, add to piece arrayList and add to tileGrid
                    if(tileGrid[i + dir[0]][j + dir[1]] == null)
                    {
                        String filename;
                        int r = 0;
                        if(isSameColor(colourArray[newX][newY], GRASS))
                            filename = "icons/SquirrelFlower.png";
                        else
                        {
                            filename = "icons/RedSquirrel2.png";
                            r = rotation;
                        }
                        MovableTile m = new MovableTile(piece, filename, r);
                        tileGrid[i + dir[0]][j + dir[1]] = m;
                        numConnections++;
                    }
                }
            }
            //then repeat for all tiles in the piece's arrayList
        }
    }


    public static boolean inBounds(int x, int y)
    {
        return(x >= 0 && x < 4 && y >= 0 && y < 4);
    }
}