import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;

/*
 * the main menu, a window to select which of the x levels to play
 * dynamically calculates how many levels exist in the levels folder
 */
public class MainMenu extends JFrame implements ActionListener
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

    //panels
    JPanel mainPanel;
    JPanel titlePanel;
    JPanel levelsPanel;

    //buttons
    JButton[] levelButtons;

    //dimensions
    int width = 400;
    int height = 400;

    int numLevels;

    /**
     * constructor for main menu, takes no parameters
     */
    public MainMenu()
    {
        //starting from 1, while that .bmp file exists, continue to look through
        boolean cont = true;
        numLevels = 1;
        while(cont)
        {
            File f = new File("levels/level" + Integer.toString(numLevels++) + ".bmp");
            cont = f.exists();
        }
        //to save checking every time if the file doesn't exist, we take 2 off the total since numLevels increments prior to setting the value of cont every loop
        //meaning it will overshoot by 2 every time
        numLevels -= 2;

        //now set up the main menu window
        setupWindow();
    }

    /**
     * sets up all the panels and initialises the main menu as a visible window
     */
    private void setupWindow()
    {
        initTitlePanel();

        initLevelsPanel();

        initMainPanel();

        //set main content pain
        this.setContentPane(mainPanel);
        //set size, visibility, title and close operation
        this.setSize(width, height);
        this.setVisible(true);
        this.setTitle("Cache Noisettes- Main Menu");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
    }

    /**
     * initialises the main panel in a border layout, containing the title and level panels
     * border layout allows you to put the title all the way at the top and leave the buttons to fill the rest of the screen
     */
    private void initMainPanel()
    {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(DARKBROWN);

        mainPanel.add("North", titlePanel);
        mainPanel.add("Center", levelsPanel);
    }

    /**
     * initialises the title panel, with a central 'main menu' title label
     */
    private void initTitlePanel()
    {
        titlePanel = new JPanel(new GridLayout(1, 5));
        //add 2 empty labels to push the 'Main Menu' label central
        titlePanel.add(new Label());
        titlePanel.add(new Label());

        titlePanel.add(new Label("Main Menu"));

        //add 2 more empty labels to pad out the rest of the grid
        titlePanel.add(new Label());
        titlePanel.add(new Label());
    }

    /**
     * initialises the level panel, creating all possible buttons with action listeners in a 3 by X grid layout
     */
    private void initLevelsPanel()
    {
        levelsPanel = new JPanel(new GridLayout(numLevels / 4, 4));
        levelButtons = new JButton[numLevels];
        for(int i = 0; i < numLevels; i++)
        {
            //create a new button displaying its respective level number
            JButton b = new JButton(Integer.toString(i + 1));

            //add action listener as this window
            b.addActionListener(this);

            //set its background and text colour, and give it a border using BorderFactory
            b.setBackground(BROWN);
            b.setForeground(BLACK);
            b.setBorder(BorderFactory.createLineBorder(WHITE));

            //add it to the levelButtons array to track later, and then add it to the panel
            levelButtons[i] = b;
            levelsPanel.add(b);
        }
    }

    /**
     * Listens for button presses
     * @param e the action event
     */
    @Override public void actionPerformed(ActionEvent e)
    {
        //loop through all buttons in the levelButtons array
        for(int i = 0; i < levelButtons.length; i++)
        {
            //if that button is the source of the click...
            if(levelButtons[i] == e.getSource())
            {
                //create a new game window, loading that respective level's .bmp file, and hide this current window
                Window gameWindow = new Window(Integer.valueOf(levelButtons[i].getText()), numLevels);
                this.setVisible(false);
            }
        }
    }
}