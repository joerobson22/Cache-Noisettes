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
 * This class is the base window, containing the level information, the grid and the inputs
 * @author Joe Robson
 */
public class Window extends JFrame implements ActionListener, KeyListener
{
    //Color constants
    final Color BROWN = new Color(132, 60, 12);
    final Color DARKBROWN = new Color(84, 39, 8);
    final Color RED = new Color(217, 17, 17);
    final Color YELLOW = new Color(250, 213, 27);
    final Color GREEN = new Color(38, 217, 22);
    final Color BLUE = new Color(22, 139, 217);
    final Color BLACK = new Color(0, 0, 0);
    final Color WHITE = new Color(255, 255, 255);

    //direction information for input buttons
    final int numDirections = 4;
    final int[][] moveDirections = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
    final int[] rotation = {0, 180, 0, 180};
    final String[] filenames = {"ArrowY.png", "ArrowY.png", "ArrowX.png", "ArrowX.png"};
    final Color[] directionColours = {YELLOW, GREEN, BLUE, RED};

    //hole coordinate and filled information
    final int[][] holeCoordinates = {{2, 0}, {0, 1}, {1, 2}, {3, 3}};
    boolean[] holesFilled = {false, false, false, false};

    //predetermined width and height of window
    final int width = 400;
    final int height = 600;

    //panels
    JPanel mainPanel;
    JPanel gridPanel;
    JPanel infoPanel;
    JPanel inputPanel;

    //arrays of buttons
    JButton[] inputButtons;
    JButton[][] gridButtons;

    //the piece that is currently selected
    Piece selectedPiece;

    //2d array of tiles, stores information on the state of the 2d puzzle grid
    Tile[][] tileGrid;

    //what level we're on
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
        
        //load the gamestate of the .bmp file using static class FileReader
        //requires try catch block
        try 
        {
            tileGrid = FileReader.loadFile(filename);
        }
        catch(IOException e) 
        {
            //in case of error, print the stack trace and end the execution
            e.printStackTrace();
            System.out.println("Error while loading the file " + filename);
            System.exit(0); //terminate the running, since the file was not loaded correctly
        }

        //output success message
        System.out.println("Loaded file " + filename + " successfully"); 

        //setup the window
        setupWindow();

        //setup other game variables
        selectedPiece = null;
    }

    /**
     * sets up the window, initialising all the panels and then arranging them in the border layout
     */
    public void setupWindow()
    {
        //initialise all the panels
        initGridPanel();

        initInfoPanel();

        initInputPanel();

        initMainPanel();

        //then init all other variables- main content pane, size, visibility, and requesting focus to this window so that key inputs can be detected

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
    }

    /**
     * initialises the main panel: uses border layout to arrange the grid central, the information at the top and the input buttons at the button
     */
    public void initMainPanel()
    {
        //create the main panel: contains the grid, information and inputs, and is the main content pane that displays everything
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        //add all other panels to the main panel
        mainPanel.add("Center", gridPanel);
        mainPanel.add("North", infoPanel);
        mainPanel.add("South", inputPanel);
    }

    /**
     * initialises the grid panel: stores a 4x4 grid of buttons, and adds the window as an action listener to them all
     * uses information from tileGrid to set the pictures for each button
     */
    public void initGridPanel()
    {
        //create the grid panel: displays the grid: centre of the border layout
        gridPanel = new JPanel(new GridLayout(4, 4));
        gridButtons = new JButton[4][4];
        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                //System.out.printf("(%d,%d)\n", j, i);
                //System.out.println(tileGrid[j][i].getPicture().getFilename());
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
    }

    /**
     * initialises the info panel: stores the main menu button, the current level as a label and your username
     */
    public void initInfoPanel()
    {
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
    }

    /**
     * initialises the input panel: stores 4 buttons of 4 different colours and arrow directions, and adds action listeners to each one
     */
    public void initInputPanel()
    {
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
     * given a set of coordinates, identify if every tile in that piece can validly move in that direction
     * @param piece the piece to move
     * @param moveDirection the movement vector
     */
    public void movePiece(Piece piece, int[] moveDirection)
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
     * function used to update all grid button pictures
     * uses the 'setIcon()' function, passing in each respective tile's Picture, since Picture extends ImageIcon
     */
    public void updateGridVisuals()
    {
        //loop through all tiles
        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                //for each button, update its icons to be the respective tile's current icon
                Tile t = tileGrid[i][j];
                gridButtons[i][j].setIcon(t.getPicture());
                //if the tile is part of a piece that is currently selected, highlight it!
                if(t.getPiece() != null && t.getPiece() == selectedPiece) 
                    gridButtons[i][j].setBorder(BorderFactory.createLineBorder(RED));
                else 
                    gridButtons[i][j].setBorder(BorderFactory.createLineBorder(DARKBROWN));
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
        boolean found = false;

        //check if the button press came from the input buttons
        for(int i = 0; i < inputButtons.length; i++) 
        {
            //check if the input button matches the source of the actionEvent
            if(inputButtons[i] == e.getSource())
            {
                found = true;
                //System.out.println("Input button " + Integer.toString(i) + " pressed");
                if(selectedPiece != null)
                {
                    found = true;
                    movePiece(selectedPiece, moveDirections[i]);
                }
                break;
            }
        }

        //not from input buttons, check if the button press came from the grid buttons
        if(!found) 
        {
            //loop through all grid buttons
            for(int i = 0; i < 4; i++) 
            {
                for(int j = 0; j < 4; j++)
                {
                    //check if the grid button matches the source of the actionEvent
                    if(gridButtons[i][j] == e.getSource())
                    {
                        //System.out.println("Grid button (" + Integer.toString(i) + "," + Integer.toString(j) + ") pressed");
                        Tile t = tileGrid[i][j];
                        //if the tile is movable, set the selected piece to be the movable tile's piece
                        if(t.isMovable())
                        {
                            selectedPiece = tileGrid[i][j].getPiece();
                            //System.out.println("Movable piece");
                        }
                        found = true;
                        break;
                    }
                }
            }
        }
        
        //not grid or input buttons, must be the main menu button
        if(!found)
            System.out.println("Main Menu pressed");
        
        //update all visuals
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
