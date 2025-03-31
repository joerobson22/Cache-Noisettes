import java.awt.Color;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class FileReader
{
    final Color EMPTY = new Color(185, 122, 87);            //EMPTY SQUARE
    final Color WHITE = new Color(255, 255, 255);           //UNCONNECTED TILE/ HOLE
    final Color GRASS = new Color(34, 177, 76);             //GREEN TILE (could me movable or immovable)
    final Color NUT = new Color(255, 201, 14);              //NUT
    final Color FLOWER = new Color(237, 28, 36);            //FLOWER/ blocked hole

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
            //square == WHITE: square is an empty hole:
                //create new Tile with max nuts of 1
            //square == EMPTY: square is empty:
                //create new Tile with max nuts of 0
            //square == NUT: square is a nut from a squirrel
                //in this case, create a new Piece class
                //first: to get rotation, look at tiles left, right, up and down the nut. if one of them IS NOT GREEN, that's where the squirrel's hands are
                    //use this to get the rotation of the images
                //second: using where the hands are, get the body of the squirrel tile
                //third: to identify all connected pieces, look at the 8 connected lines around the nut. if any of them are green, then that piece is connected to the nut
            //square == FLOWER: square is a blocked hole
                //create a new StationaryTile class
            //square == GREEN: square is connected to a nut
                //continue until you find the nut
            //square == anything else: square is a squirrel's body
                //continue until you find the nut

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
}