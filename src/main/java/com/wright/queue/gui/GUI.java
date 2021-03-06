package com.wright.queue.gui;

import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import com.wright.queue.exceptions.EmptyQueueException;
import com.wright.queue.exceptions.QueueIsFullException;
import com.wright.queue.linkedlist.Queue;

/**
 * 
 * @author christopherwright
 *
 */
public class GUI {
    
    private static final Logger MS_LOG = Logger.getLogger(GUI.class.getName());
    
    private JFrame mainFrame;
    private JButton createButton;
    private JButton processButton;
    private List<JLabel> labels;
    private JTextArea textArea;
    private JScrollPane jsp;
    
    private Queue<Integer> queue = new Queue<>();
    
    private static final String ENQUEUE = "enqueue";
    private static final String DEQUEUE = "dequeue";
    
    public void init() {
        javax.swing.SwingUtilities.invokeLater( () -> {
                MS_LOG.info("Initializing GUI...");
                initializeGUI();
        });
    }
    
    private void initializeGUI() {
        
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            MS_LOG.error(e.getMessage());
        }
        
        mainFrame = new JFrame("Queue Application");
        mainFrame.setSize(600, 400);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        labels = Labels.getLabels();
        
        initializeButtons();
        addComponentsToFrame(mainFrame.getContentPane());
        
        mainFrame.setVisible(true);
    }
    
    private void addComponentsToFrame(Container pane) {
        pane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        pane.setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        
        c.insets = new Insets(5, 5, 5, 5);
        c.ipady = 30;
        c.ipadx = 30;
        
        for (int i = 0; i < labels.size(); i++) {
            c.gridx = i;
            c.gridy = 0;
            pane.add(labels.get(i), c);
        }
        
        c.ipadx = 0;
        c.ipady = 0;
        
        c.gridx = 0;
        c.gridy = 1;
        pane.add(createButton, c);
        
        c.gridx = 1;
        c.gridy = 1;
        pane.add(processButton, c);
        
        c.gridwidth = 3;
        c.gridx = 2;
        c.gridy = 1;
        pane.add(jsp, c);
    }
    
    private void initializeButtons() {
        createButton = new JButton("Create");
        processButton = new JButton("Process");
        
        mainFrame.add(createButton);
        mainFrame.add(processButton);
        
        createButton.addActionListener(e -> {
            MS_LOG.info("Create Button clicked...");
            create();
        });
        
        processButton.addActionListener(e -> {
            MS_LOG.info("Process Button clicked...");
            process();
        });
        
        textArea = new JTextArea(12,19);
        textArea.setEditable(false);
        jsp = new JScrollPane(textArea, 
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }
    
    private void create() {
        try {
            queue.enqueue(getRandomNumber());
            updateUI(ENQUEUE);
        }
        catch (QueueIsFullException e) {
            MS_LOG.error("The queue is full", e);
        }
    }
    
    private void process() {
        try {
            queue.dequeue();
            updateUI(DEQUEUE);
        }
        catch (EmptyQueueException e) {
            MS_LOG.error("Queue is empty", e);
        }
    }
    
    private void updateUI(String type) {
        
        int index = type.equals(ENQUEUE) ? queue.getSize() - 1 : queue.getSize();
        
        switch(type) {
            case ENQUEUE:
                updateCells(index);
                break;
            case DEQUEUE: 
                updateTextArea(labels.get(index).getText());
                labels.get(index).setText(null);
                break;
            default:
                break;
        }
    }
    
    private void updateCells(int index) {
        List<Integer> tempList = queue.getQueueAsList();
        
        /**
         * 
         if (queue.getSize() == 1) {
            labels.get(index).setText(Integer.toString(queue.peek()));
            
        }
        
        while (index > 0) {
            int prevCellValue = Integer.parseInt(labels.get(index - 1).getText());
            if (prevCellValue > queue.peek())
                return;
            labels.get(index).setText(Integer.toString(prevCellValue));
            labels.get(index - 1).setText(Integer.toString(queue.peek()));
            index--;
        }
        */
        
        for (int i = 0; i < tempList.size(); i++) {
            labels.get(i).setText(Integer.toString((tempList.get(i))));
        }
        
    }
    
    private void updateTextArea(String text) {
        if (textArea.getText() == "" || textArea.getText() == null) {
            textArea.setText(text + "\n");
            return;
        }
        
        textArea.append(text + "\n");
    }
    
    private int getRandomNumber() {
        Random rand = new Random();
        return rand.nextInt(200);
    }
}
