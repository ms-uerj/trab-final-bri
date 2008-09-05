package br.ufrj.cos.bri.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import br.ufrj.cos.bri.report.Person;

public class GUI {
	private static DefaultListModel listModel;
	private static Person person = new Person();
	private static JTextField searchField=null;
	private static JList list=null;
	private static JScrollPane listScroller=null;
	
	private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Trabalho de BRI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        

        //Add the ubiquitous "Hello World" label.
        String[] searchStrings = { "Author", "Proceedings", "Journal" };
        JComboBox searchList = new JComboBox(searchStrings);
        searchList.setSelectedIndex(0);
        searchList.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                String petName = (String)cb.getSelectedItem();
            }
        });
        c.weightx = 0.3;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0,8,0,0);
        c.fill = GridBagConstraints.HORIZONTAL;

        frame.getContentPane().add(searchList, c);
        
        searchField = new JTextField("A%");
        searchField.addKeyListener(new KeyListener() {
        	/** Handle the key typed event from the text field. */
            public void keyTyped(KeyEvent e) {}

            /** Handle the key-pressed event from the text field. */
            public void keyPressed(KeyEvent e) {}
        	
            /** Handle the key-released event from the text field. */
            public void keyReleased(KeyEvent e) {
            	System.out.println(searchField.getText());
                populateModel();
            }
        });
        
        searchField.setColumns(50);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.7;
        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(7,8,0,0);

        
        frame.getContentPane().add(searchField, c);
        
        listModel = new DefaultListModel();
        populateModel();
        
        list = new JList(listModel); //data has type Object[]
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);
        listScroller = new JScrollPane(list);
        listScroller.setPreferredSize(new Dimension(250, 600));
        
        c.fill = GridBagConstraints.VERTICAL;
        c.gridx = 0;
        c.gridy = 1;
        
        frame.getContentPane().add(listScroller, c);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
	
	private static void populateModel() {
		
		if(searchField.getText().length()==0) {
			return;
		}
		
		listModel.removeAllElements();
		Vector<String> authors = person.getAll(searchField.getText()+"%");
		
		for(String author : authors) {
			listModel.addElement(author);
		}
		
	}
	
	public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }


}
