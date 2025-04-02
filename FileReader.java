import java.awt.Color;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * This is a class containing static methods, and it is used to read a .bmp file and return the matching 2D array of tiles
 * @author Joe Robson
 */
public class FileReader
{
    //color constants for tiles, used to compare the RBG values of a pixel in the .bmp file to identify what the pixel means
    final static Color EMPTY = new Color(185, 122, 87);             //EMPTY SQUARE
    final static Color WHITE = new Color(255, 255, 255);            //UNCONNECTED TILE/ HOLE
    final static Color GRASS = new Color(34, 177, 76);              //GREEN TILE (could me movable or immovable)
    final static Color NUT = new Color(255, 201, 14);               //NUT
    final static Color FLOWER = new Color(237, 28, 36);             //FLOWER/ blocked hole

    final static Color REDSQUIRREL = new Color(255, 127, 39);       //red squirrel body
    final static Color GREYSQUIRREL = new Color(195, 195, 195);     //grey squirrel body
    final static Color BROWNSQUIRREL = new Color(239, 228, 176);    //brown squirrel body
    final static Color BLACKSQUIRREL = new Color(0, 0, 0);          //black squirrel body

    /**
     * loads a file/ .bmp image given the .bmp filename
     * @param filename the filename to load
     * @return returns a 2D tile grid corresponding to the .bmp file passed in
     */
    public static Tile[][] loadFile(String filename) throws IOException
    {
        //init return grid
        Tile[][] tileGrid = new Tile[4][4];

        //get path and setup ImageIO reader
        String path = "levels/" + filename;
        BufferedImage image = ImageIO.read(new File(path));

        //then read all colour pixel data from .bmp file into 15x15 colour array
        Color[][] colorArray = getColorArray(image);

        //once all this data is in the colour array:
        //look at all central squares (1,1), (5,1), (9,1)... (each square is 4 pixels apart)
        for(int i = 0; i < 4; i++)
        {
            int x = (i * 4) + 1; //I IS X
            for(int j = 0; j < 4; j++)
            {
                int y = (j * 4) + 1; //J IS Y
                Color color = colorArray[x][y];
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
                    
                    t = getNutTile(i, j, piece, colorArray, x, y);
                    
                    //to identify all connected pieces, look at the 4 connected lines around the nut. if any of them are non white, then that piece is connected to the nut
                    //looking at connected lines: 
                    completePiece(piece, tileGrid, colorArray, i, j, x, y, Integer.parseInt(getRotationAndColor(colorArray, x, y)[0]), t.getColor());
                }
                //square == FLOWER: square is a blocked hole
                else if(isSameColor(color, FLOWER))
                {
                    //create a new StationaryTile object
                    t = new StationaryTile("icons/Flower.png", 0, i, j);
                }
                //square == anything else: square is a squirrel's body or connected to a nut, continue
                else continue;

                //add the new tile to the grid
                tileGrid[i][j] = t;
            }
        }
        return tileGrid;     
    }

    /**
     * returns a 15x15 grid of Color objects from a .bmp image
     * @return returns a 2D color array
     */
    public static Color[][] getColorArray(BufferedImage image)
    {
        //init return array
        Color[][] colorArray = new Color[15][15];
        for(int x = 0; x < 15; x++)
        {
            //System.out.println("y: " + Integer.toString(y));
            for(int y = 0; y < 15; y++)
            {
                colorArray[x][y] = new Color(image.getRGB(x, y));
                //System.out.println(colourArray[x][y]);
            }
        }
        return colorArray;
    }

    /**
     * compares the RGB values of 2 colours to identify if they are the same colour
     * @param color1 the first color
     * @param color2 the second color
     * @return returns true or false
     */
    public static boolean isSameColor(Color color1, Color color2)
    {
        return(color1.getRed() == color2.getRed() && color1.getGreen() == color2.getGreen() && color1.getBlue() == color2.getBlue());
    }

    /**
     * used for debugging, quickly outputs the RGB values fo a given color
     * @param color the color to output
     */
    public static void outputColor(Color color)
    {
        System.out.printf("R: %d  G: %d  B: %d\n", color.getRed(), color.getGreen(), color.getBlue());
    }

    /**
     * gets the corresponding color of a squirrel as a string (for use in filenames later)
     * @param color the color to compare to other color constants
     * @return returns a string: "Red", "Grey", "Brown" or "Black"
     */
    public static String getColorString(Color color)
    {
        if(isSameColor(REDSQUIRREL, color))
            return "Red";
        else if (isSameColor(GREYSQUIRREL, color))
            return "Grey";
        else if(isSameColor(BROWNSQUIRREL, color))
            return "Brown";
        else
            return "Black";
    }

    /**
     * find the rotation and color of the squirrel attached to the nut, and returns that
     * @param i the tile's x coordinate
     * @param j the tile's y coordinate
     * @param piece the piece the tile belongs to
     * @param colorArray the 15x15 array of pixels
     * @param x the pixel x value of the nut
     * @param y the pixel y value of the nut
     * @return returns a nut tile with the correct rotation and squirrel color
     */
    public static Tile getNutTile(int i, int j, Piece piece, Color[][] colorArray, int x, int y)
    {
        String[] rotationAndColor = getRotationAndColor(colorArray, x, y);
        int rotation = Integer.parseInt(rotationAndColor[0]);
        String colorString = rotationAndColor[1];

        Tile t = new MovableTile(piece, "icons/" + colorString + "Squirrel1Nut.png", rotation, i, j, true, colorString);
        return t;
    }

    /**
     * find the rotation as well as the squirrel's color, given a nut tile
     * @param colorArray the 15x15 grid of pixels
     * @param x the pixel x value of the nut
     * @param y the pixel y value of the nut
     * @return String array: [0] = the rotation, [1] = the color string
     */
    public static String[] getRotationAndColor(Color[][] colorArray, int x, int y)
    {
        //to get rotation, look at tiles left, right, up and down the nut. if one of them IS NOT GREEN, that's where the squirrel's hands are
        int[][] directions = {{0, 1}, {-1, 0}, {0, -1}, {1, 0}};
        int rotation = 0;
        String colorString = "Black";
        for(int[] dir : directions)
        {
            //if this pixel is not green, then it must be the squirrel's hands
            if(!isSameColor(colorArray[x + dir[0]][y + dir[1]], GRASS))
            {
                //get the color string of the hands
                colorString = getColorString(colorArray[x + dir[0]][y + dir[1]]);
                break;
            }
            //increment the rotation by 90 degrees at a time
            rotation += 90;
        }
        //return a new array of the rotation and the color string
        return new String[] {Integer.toString(rotation), colorString};
    }

    /**
     * given a tileGrid and a start point (the nut tile), completes the movable squirrel tile, adding any random connected pieces
     * @param piece the piece that all these tiles should be added to
     * @param tileGrid the 2D 4x4 grid of tiles these tiles should be added to
     * @param colorArray the 15x15 grid of pixels
     * @param i the starting x value of the nut tile
     * @param j the starting y value of the nut tile
     * @param x the starting x coordinate of the nut pixel
     * @param y the starting y coordinate of the nut pixel
     * @param rotation the rotation of the squirrel
     * @param colorString the color of the squirrel
     */
    public static void completePiece(Piece piece, Tile[][] tileGrid, Color[][] colorArray, int i, int j, int x, int y, int rotation, String colorString)
    {
        //init all body coordinates
        int bodyX = -1;
        int bodyY = -1;
        int bodyI = -1;
        int bodyJ = -1;
        
        //init the different directions to look in
        int[][] directions = {{0, 1}, {-1, 0}, {0, -1}, {1, 0}};

        //check all directions for the nut piece
        for(int[] dir : directions)
        {
            //setup the new pixel and tile coordinates
            int newX = x + dir[0] * 2;
            int newY = y + dir[1] * 2;
            int newJ = j + dir[1];
            int newI = i + dir[0];

            //ignore if out of bounds of grid or colorArray
            if(!inBounds(newX, newY, 15) || !inBounds(newI, newJ, 4)) continue;

            //System.out.printf("Checking pixel (%d,%d)\n", newX, newY);
            //System.out.printf("Pixel R: %d, G: %d, B: %d\n", colourArray[newX][newY].getRed(), colourArray[newX][newY].getGreen(), colourArray[newX][newY].getBlue());

            //if a connecting square is non white, it must be connected to us. 
            if(!isSameColor(colorArray[newX][newY], WHITE) && tileGrid[newI][newJ] == null)
            {
                //Therefore, as long as it's not already been made: create new tile, add to piece arrayList and add to tileGrid
                //System.out.printf("(%d,%d) is empty!\n", newI, newJ);
                String filename;
                int r = 0;
                //if the tile is green, it's a grass tile
                if(isSameColor(colorArray[newX][newY], GRASS))
                    filename = "icons/SquirrelFlower.png";
                //otherwise, it's the squirrel's body
                else
                {
                    filename = "icons/" + colorString + "Squirrel2.png";
                    r = rotation;

                    //set all the body information for later
                    bodyX = newX + dir[0] * 2; //move two more pixels over to get the center point of the body tile
                    bodyY = newY + dir[1] * 2;
                    bodyI = newI;
                    bodyJ = newJ;
                }
                //add the new tile to the tileGrid
                tileGrid[newI][newJ] = new MovableTile(piece, filename, r, newI, newJ, false, colorString);
            }
        }

        //now get pieces connected to the body, using the body x, y, i and j variables as start points
        //check all directions for the nut piece
        for(int[] dir : directions)
        {
            //setup the new pixel and tile coordinates
            int newX = bodyX + dir[0] * 2;
            int newY = bodyY + dir[1] * 2;
            int newJ = bodyJ + dir[1];
            int newI = bodyI + dir[0];

            //ignore if out of bounds
            if(!inBounds(newX, newY, 15) || !inBounds(newI, newJ, 4)) continue;

            //System.out.printf("Checking pixel (%d,%d)\n", newX, newY);
            //System.out.printf("Pixel R: %d, G: %d, B: %d\n", colourArray[newX][newY].getRed(), colourArray[newX][newY].getGreen(), colourArray[newX][newY].getBlue());

            //if a connecting square is non white, it must be connected to us. 
            if(!isSameColor(colorArray[newX][newY], WHITE) && tileGrid[newI][newJ] == null && newI != i && newJ != j)
            {
                //Therefore, as long as it's not already been made: create new tile, add to piece arrayList and add to tileGrid
                //since we;ve already found the body and the nut, it's only ever going to be a flower tile
                tileGrid[newI][newJ] = new MovableTile(piece, "icons/SquirrelFlower.png", 0, newI, newJ, false, colorString);
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
    public static boolean inBounds(int x, int y, int upper)
    {
        return(x >= 0 && x < upper && y >= 0 && y < upper);
    }
}