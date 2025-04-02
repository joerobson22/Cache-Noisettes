import javax.swing.*;

import java.awt.*;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.event.*;
import java.io.*;

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

    int numLevels = 10;

    /**
     * constructor for main menu
     */
    public MainMenu()
    {
        setupWindow();
    }

    /**
     * sets up all the panels and initialises the main menu as a visible window
     */
    public void setupWindow()
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
     */
    public void initMainPanel()
    {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(DARKBROWN);

        mainPanel.add("North", titlePanel);
        mainPanel.add("Center", levelsPanel);
    }

    /**
     * initialises the title panel, with a central 'main menu' title label
     */
    public void initTitlePanel()
    {
        titlePanel = new JPanel(new GridLayout(1, 5));
        titlePanel.add(new Label());
        titlePanel.add(new Label());

        titlePanel.add(new Label("Main Menu"));

        titlePanel.add(new Label());
        titlePanel.add(new Label());
    }

    /**
     * initialises the level panel, creating all possible buttons with action listeners in a 3 by X grid layout
     */
    public void initLevelsPanel()
    {
        levelsPanel = new JPanel(new GridLayout(4, 3));
        levelButtons = new JButton[numLevels];
        for(int i = 0; i < numLevels; i++)
        {
            JButton b = new JButton(Integer.toString(i + 1));
            //JButton b = new JButton(new Picture("levels/level" + Integer.toString(i + 1) + ".bmp", 0));

            b.addActionListener(this);
            b.setBackground(BROWN);
            b.setForeground(BLACK);
            b.setBorder(BorderFactory.createLineBorder(WHITE));
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
        for(int i = 0; i < levelButtons.length; i++)
        {
            if(levelButtons[i] == e.getSource())
            {
                Window gameWindow = new Window(i + 1, numLevels);
                this.setVisible(false);
            }
        }
    }
}