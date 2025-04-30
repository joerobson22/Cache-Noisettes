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
 */
public class Window extends JFrame implements ActionListener
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
    JButton mainMenuButton;
    JButton nextlevelButton;

    //what level we're on
    int levelNum;
    JButton blankButton2;
    int numLevels;

    boolean playing = true;

    Grid grid;

    /**
     * constructor, initialises everything to do with the window
     * @param levelNum the level number
     */
    public Window(int levelNum, int numLevels)
    {
        //set the filename to load up
        String filename = null;
        this.levelNum = levelNum;
        this.numLevels = numLevels;
        if(levelNum == 0)
            filename = "color-pallete.bmp";
        else
            filename = "level" + Integer.toString(levelNum) + ".bmp";
        
        //load the gamestate of the .bmp file using static class FileReader
        //requires try catch block
        try 
        {
            System.out.printf("Loading %s...\n", filename);
            grid = new Grid(FileReader.loadFile(filename));
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
        this.setTitle("Cache Noisettes- Level " + Integer.toString(levelNum));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
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
                //run through tileGrid that has already been setup by loadFile(), making new JButtons with the pictures of each respective tileGrid
                JButton b = new JButton(grid.getTile(j, i).getPicture());

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
        //create the info panel: contains main menu button, current level num and next level button
        infoPanel = new JPanel(new GridLayout(1, 4));

        //create main menu button
        Picture mainMenuPicture = new Picture("icons/MainMenu.png", 0);
        mainMenuButton = new JButton(mainMenuPicture);
        mainMenuButton.addActionListener(this);
        mainMenuButton.setBackground(DARKBROWN);
        mainMenuButton.setBorder(BorderFactory.createLineBorder(WHITE));
        infoPanel.add(mainMenuButton);

        //add two blank buttons with no action listeners that display the level number
        JButton blankButton1 = new JButton("Level");
        blankButton2 = new JButton(Integer.toString(levelNum));
        blankButton1.setForeground(WHITE);
        blankButton2.setForeground(WHITE);
        blankButton1.setBackground(BLACK);
        blankButton2.setBackground(BLACK);
        blankButton1.setBorder(BorderFactory.createLineBorder(BLACK));
        blankButton2.setBorder(BorderFactory.createLineBorder(BLACK));
        infoPanel.add(blankButton1);
        infoPanel.add(blankButton2);
        
        //create a new button that will take the user to the next level when clicked
        nextlevelButton = new JButton(new Picture("icons/ArrowX.png", 180));
        nextlevelButton.setBackground(DARKBROWN);
        nextlevelButton.setBorder(BorderFactory.createLineBorder(WHITE));
        nextlevelButton.addActionListener(this);
        infoPanel.add(nextlevelButton);
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
                Tile t = grid.getTile(i, j);
                gridButtons[i][j].setIcon(t.getPicture());
                //if the tile is part of a piece that is currently selected, highlight it!
                if(t.getPiece() != null && t.getPiece() == grid.getSelectedPiece()) 
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
    @Override public void actionPerformed(ActionEvent e)
    {
        //System.out.println(e.getSource());
        boolean found = false;

        if(playing)
        {
            //check if the button press came from the input buttons
            for(int i = 0; i < inputButtons.length; i++) 
            {
                //check if the input button matches the source of the actionEvent
                if(inputButtons[i] == e.getSource())
                {
                    found = true;
                    grid.moveSelectedPiece(moveDirections[i]);
                    break;
                }
            }
        }
        

        //not from input buttons, check if the button press came from the grid buttons
        if(!found && playing) 
        {
            //loop through all grid buttons
            for(int i = 0; i < 4; i++) 
            {
                for(int j = 0; j < 4; j++)
                {
                    //check if the grid button matches the source of the actionEvent
                    if(gridButtons[i][j] == e.getSource())
                    {
                        grid.tileClicked(i, j);
                        found = true;
                        break;
                    }
                }
            }
        }
        
        if(e.getSource() == mainMenuButton)
        {
            //main menu
            this.setVisible(false);
            MainMenu main = new MainMenu();
        }

        if(e.getSource() == nextlevelButton)
        {
            this.setVisible(false);
            //next level
            int nextLevel = levelNum + 1;
            if(nextLevel > numLevels)
                nextLevel = 1;
            Window mainWindow = new Window(nextLevel, numLevels);
        }
        
        //update all visuals
        updateGridVisuals();

        //check if the game is won
        playing = !grid.hasWon();
        if(!playing)
        {
            //display complete status
            blankButton2.setText("Complete!");
            blankButton2.setForeground(GREEN);
        }
    }
}
