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
            int x = (i * 4) + 1; //I IS X
            for(int j = 0; j < 4; j++)
            {
                int y = (j * 4) + 1; //J IS Y
                Color color = colourArray[x][y];
                Tile t = null;
                //System.out.printf("(%d,%d)\n", x, y);
                //System.out.printf("Pixel R: %d, G: %d, B: %d\n", color.getRed(), color.getGreen(), color.getBlue());
                //square == EMPTY: square is empty
                if(isSameColor(color, EMPTY))
                {
                    //create new Tile with max nuts of 0
                    t = new Tile("icons/Empty.png", 0, 0, 0, i, j);
                }
                //square == WHITE: square is an empty hole
                else if(isSameColor(color, WHITE))
                {
                    //create new Tile with max nuts of 1
                    t = new Tile("icons/Hole.png", 0, 0, 1, i, j);
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
                    //System.out.println("FLOWER!");
                    //create a new StationaryTile object
                    t = new StationaryTile("icons/Flower.png", 0, 0, i, j);
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
        for(int x = 0; x < 15; x++)
        {
            //System.out.println("y: " + Integer.toString(y));
            for(int y = 0; y < 15; y++)
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
        Tile t = new MovableTile(piece, "icons/RedSquirrel1Nut.png", rotation, i, j, true);
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
        int bodyX = -1;
        int bodyY = -1;
        int bodyI = -1;
        int bodyJ = -1;
        //i == y, j == x
        int[][] directions = {{0, 1}, {-1, 0}, {0, -1}, {1, 0}};
        int numConnections = 1;
        //check all directions for the nut piece
        for(int[] dir : directions)
        {
            int newX = x + dir[0] * 2;
            int newY = y + dir[1] * 2;
            int newJ = j + dir[1];
            int newI = i + dir[0];

            //ignore if out of bounds
            if(!inBounds(newX, newY, 15) || !inBounds(newI, newJ, 4)) continue;

            //System.out.printf("Checking pixel (%d,%d)\n", newX, newY);
            //System.out.printf("Pixel R: %d, G: %d, B: %d\n", colourArray[newX][newY].getRed(), colourArray[newX][newY].getGreen(), colourArray[newX][newY].getBlue());

            //if a connecting square is non white, it must be connected to us. 
            if(!isSameColor(colourArray[newX][newY], WHITE) && tileGrid[newI][newJ] == null)
            {
                //Therefore, as long as it's not already been made: create new tile, add to piece arrayList and add to tileGrid
                //System.out.printf("(%d,%d) is empty!\n", newI, newJ);
                String filename;
                int r = 0;
                if(isSameColor(colourArray[newX][newY], GRASS))
                    filename = "icons/SquirrelFlower.png";
                else
                {
                    filename = "icons/RedSquirrel2.png";
                    r = rotation;
                    bodyX = newX + dir[0] * 2; //move two more pixels over to get the center point of the body tile
                    bodyY = newY + dir[1] * 2;
                    bodyI = newI;
                    bodyJ = newJ;
                }
                tileGrid[newI][newJ] = new MovableTile(piece, filename, r, newI, newJ, false);
            }
        }

        //now get pieces connected to the body, using the body x, y, i and j variables as start points
        //check all directions for the nut piece
        for(int[] dir : directions)
        {
            int newX = bodyX + dir[0] * 2;
            int newY = bodyY + dir[1] * 2;
            int newJ = bodyJ + dir[1];
            int newI = bodyI + dir[0];

            //ignore if out of bounds
            if(!inBounds(newX, newY, 15) || !inBounds(newI, newJ, 4)) continue;

            //System.out.printf("Checking pixel (%d,%d)\n", newX, newY);
            //System.out.printf("Pixel R: %d, G: %d, B: %d\n", colourArray[newX][newY].getRed(), colourArray[newX][newY].getGreen(), colourArray[newX][newY].getBlue());

            //if a connecting square is non white, it must be connected to us. 
            if(!isSameColor(colourArray[newX][newY], WHITE) && tileGrid[newI][newJ] == null && newI != i && newJ != j)
            {
                //Therefore, as long as it's not already been made: create new tile, add to piece arrayList and add to tileGrid
                tileGrid[newI][newJ] = new MovableTile(piece, "icons/SquirrelFlower.png", 0, newI, newJ, false);
            }
        }
    }


    public static boolean inBounds(int x, int y, int upper)
    {
        return(x >= 0 && x < upper && y >= 0 && y < upper);
    }
}