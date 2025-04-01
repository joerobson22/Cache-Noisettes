import javax.swing.*;

import java.awt.*;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.event.*;
import java.io.*;

/**
 * This class is the base window, containing the board
 */
public class Window extends JFrame implements ActionListener, KeyListener
{
    final Color BROWN = new Color(132, 60, 12);
    final Color DARKBROWN = new Color(84, 39, 8);
    final Color RED = new Color(217, 17, 17);
    final Color YELLOW = new Color(250, 213, 27);
    final Color GREEN = new Color(38, 217, 22);
    final Color BLUE = new Color(22, 139, 217);
    final Color BLACK = new Color(0, 0, 0);
    final Color WHITE = new Color(255, 255, 255);

    final int numDirections = 4;
    final int[][] moveDirections = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
    final int[] rotation = {0, 180, 0, 180};
    final String[] filenames = {"ArrowY.png", "ArrowY.png", "ArrowX.png", "ArrowX.png"};
    final Color[] directionColours = {YELLOW, GREEN, BLUE, RED};
    final int[][] holeCoordinates = {{2, 0}, {0, 1}, {1, 2}, {3, 3}};
    int[] holesFilled = {0, 0, 0, 0};

    final int width = 400;
    final int height = 600;

    JPanel mainPanel;
    JPanel gridPanel;
    JPanel infoPanel;
    JPanel inputPanel;

    JButton[] inputButtons;
    JButton[][] gridButtons;

    Piece selectedPiece;

    Tile[][] tileGrid;

    int levelNum;

    /**
     * constructor, initialises everything to do with the window
     * @param levelNum the level number
     */
    public Window(int levelNum)
    {
        //set the filename to load up
        String filename = null;
        this.levelNum = levelNum;
        if(levelNum == 0)
            filename = "blankwithholes.bmp";
        else
            filename = "level" + Integer.toString(levelNum) + ".bmp";
        
        //load the gamestate of the .bmp file
        int status = 1;
        try 
        {
            tileGrid = FileReader.loadFile(filename);
        }
        catch(IOException e) 
        {
            e.printStackTrace();
            status = 0;
        }

        if(status == 0) //error while loading
        {
            System.out.println("Error while loading the file " + filename);
            System.exit(0); //terminate the running, since the file was not loaded correctly
        }
        else //loaded successfully
        {
            System.out.println("Loaded file " + filename + " successfully"); 
        }

        //create the main panel: contains the grid, information and inputs, and is the main content pane that displays everything
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        //create the grid panel: displays the grid: centre of the border layout
        gridPanel = new JPanel(new GridLayout(4, 4));
        gridButtons = new JButton[4][4];
        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                //run through tileGrid that has already been setup by loadFile(), making new JButtons with the pictures of each respective tileGrid
                JButton b = new JButton(tileGrid[j][i].getPicture());

                //setup action listeners and add it to the grid button array, as well as the grid panel for displaying
                b.addActionListener(this);
                b.setBackground(BROWN);
                b.setBorder(BorderFactory.createLineBorder(DARKBROWN));
                gridButtons[j][i] = b;
                gridPanel.add(b);
            }
        }

        //create the info panel: contains current level, main menu button and your information
        infoPanel = new JPanel(new BorderLayout());

        Picture mainMenuPicture = new Picture("icons/MainMenu.png", 0);
        JButton mainMenuButton = new JButton(mainMenuPicture);
        mainMenuButton.addActionListener(this);
        mainMenuButton.setBackground(DARKBROWN);
        mainMenuButton.setBorder(BorderFactory.createLineBorder(DARKBROWN));
        infoPanel.add("West", mainMenuButton);

        infoPanel.add("Center", new Label("Level " + Integer.toString(levelNum), SwingConstants.CENTER));
        infoPanel.add("East", new Label("Username", SwingConstants.CENTER));

        //create the input panel: contains all buttons for different movement directions: bottom of the border layout
        inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(1, numDirections));
        
        //initialise and populate the input buttons array
        inputButtons = new JButton[numDirections];
        for(int i = 0; i < numDirections; i++)
        {
            Picture p = new Picture("icons/" + filenames[i], rotation[i]);
            JButton b = new JButton(p);

            b.addActionListener(this);
            b.setBackground(directionColours[i]);
            b.setBorder(BorderFactory.createLineBorder(WHITE));
            inputButtons[i] = b;
            inputPanel.add(b);
        }

        //finish setting up the main panel
        mainPanel.add("Center", gridPanel);
        mainPanel.add("North", infoPanel);
        mainPanel.add("South", inputPanel);

        //set main content pain
        this.setContentPane(mainPanel);
        //set size, visibility, title and close operation
        this.setSize(width, height);
        this.setVisible(true);
        this.setTitle("Cache Noisettes");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        //focus key inputs on this window and add this as a key listener
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this);

        //setup other game things
        selectedPiece = null;
    }


    /**
     * checks if a given coordinate on the grid is a hole, by checking every predefined hole coordinate
     * @param coords the coordinates to check
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
     * given a set of coordinates, identify if every tile in that piece can validly move in that direction
     * @param piece the piece to move
     * @param moveDirection the movement vector
     */
    public int movePiece(Piece piece, int[] moveDirection)
    {
        //check if the mvoe is valid
        if(selectedPiece.checkValidMove(tileGrid, moveDirection[0], moveDirection[1]))
        {
            //move the piece in that direction
            selectedPiece.move(tileGrid, moveDirection[0], moveDirection[1]);

            //find and tidy up any null squares
            tidyNullTiles();

            //now search through tile grid to identify if there is a nut over a hole
            nutsInHoles();
        }

        
        selectedPiece = null;
        return 0;
    }


    public void tidyNullTiles()
    {
        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                if(tileGrid[i][j] == null)
                {
                    int[] coords = {i, j};
                    int hole = coordIsHole(coords);
                    if(hole > -1)
                    {
                        if(holesFilled[hole] > 0)
                            tileGrid[i][j] = new Tile("icons/HoleNut.png", 0, 1, 1, i, j);
                        else
                            tileGrid[i][j] = new Tile("icons/Hole.png", 0, 0, 1, i, j);
                    }
                    else
                    {
                        tileGrid[i][j] = new Tile("icons/Empty.png", 0, 0, 0, i, j);
                    }
                }
            }
        }
    }


    public void nutsInHoles()
    {
        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                Tile t = tileGrid[i][j];
                int[] coords = {i, j};
                int holeInfo = coordIsHole(coords);

                //if empty hole and holding nut
                if(holeInfo > -1 && t.getHoldingNut())
                {
                    if(holesFilled[holeInfo] == 0)
                    {
                        t.setHoldingNut(false);
                        holesFilled[holeInfo] = 1;
                        tileGrid[i][j].setPicture(new Picture("icons/RedSquirrel1.png", tileGrid[i][j].getRotation()));
                    }
                }
            }
        }
    }

    /**
     * function used to update all grid button pictures
     * uses the 'setIcon()' function, passing in each respective tile's Picture, since Picture extends ImageIcon
     */
    public void updateGridVisuals()
    {
        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                Tile t = tileGrid[i][j];
                gridButtons[i][j].setIcon(t.getPicture());
                if(t.getPiece() != null && t.getPiece() == selectedPiece) gridButtons[i][j].setBorder(BorderFactory.createLineBorder(RED));
                else gridButtons[i][j].setBorder(BorderFactory.createLineBorder(DARKBROWN));
            }
        }
    }

    /**
     * Listens for button presses
     * @param e the action event
     */
    public void actionPerformed(ActionEvent e)
    {
        //System.out.println(e.getSource());
        int status = -1;

        for(int i = 0; i < inputButtons.length; i++) //check if the button press came from the input buttons
        {
            if(inputButtons[i] == e.getSource())
            {
                status = 0;
                //System.out.println("Input button " + Integer.toString(i) + " pressed");
                if(selectedPiece != null) status = movePiece(selectedPiece, moveDirections[i]);
                break;
            }
        }

        if(status == -1) //not from input buttons
        {
            for(int i = 0; i < 4; i++) //check if the button press came from the grid buttons
            {
                for(int j = 0; j < 4; j++)
                {
                    if(gridButtons[i][j] == e.getSource())
                    {
                        //System.out.println("Grid button (" + Integer.toString(i) + "," + Integer.toString(j) + ") pressed");
                        Tile t = tileGrid[i][j];
                        if(t.isMovable())
                        {
                            selectedPiece = tileGrid[i][j].getPiece();
                            //System.out.println("Movable piece");
                        }
                        status = 0;
                        break;
                    }
                }
            }
        }

        if(status == -1)
            System.out.println("Main Menu pressed");
        
        updateGridVisuals();
    }

    /**
     * Only important key listener, listens for arrow keys
     * @param e the key event
     */
    public void keyReleased(KeyEvent e)
    {
        System.out.println("Key pressed");
    }


    public void keyTyped(KeyEvent e){}
    public void keyPressed(KeyEvent e){}
}
