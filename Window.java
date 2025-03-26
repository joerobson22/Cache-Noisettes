import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.event.*;
import java.time.chrono.JapaneseChronology;

/**
 * This class is the base window, containing the board
 */
public class Window extends JFrame implements ActionListener, KeyListener
{
    JPanel mainPanel;
    JPanel gridPanel;
    JPanel infoPanel;
    JPanel inputPanel;

    JButton[] inputButtons;

    final int numDirections = 4;
    final int[] rotation = {0, 180, 0, 180};
    final String[] filenames = {"ArrowY.png", "ArrowY.png", "ArrowX.png", "ArrowX.png"};

    final int width = 600;
    final int height = 600;

    int levelNum;

    public Window(int levelNum)
    {
        this.levelNum = levelNum;
        //create the main panel: contains the grid, information and inputs, and is the main content pane that displays everything
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        //create the grid panel: displays the grid: centre of the border layout
        gridPanel = new JPanel();

        //create the info panel: contains current level, main menu button and your information
        infoPanel = new JPanel(new BorderLayout());

        Picture mainMenuPicture = new Picture("icons/MainMenu.png", 0);
        JButton mainMenuButton = new JButton(mainMenuPicture);
        mainMenuButton.addActionListener(this);
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
    }

    /**
     * Listens for button presses
     */
    public void actionPerformed(ActionEvent e)
    {
        System.out.println("Button pressed");
    }

    /**
     * Only important key listener, listens for arrow keys
     */
    public void keyReleased(KeyEvent e)
    {
        System.out.println("Key pressed");
    }


    public void keyTyped(KeyEvent e){}
    public void keyPressed(KeyEvent e){}
}
