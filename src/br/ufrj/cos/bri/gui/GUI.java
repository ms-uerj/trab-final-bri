package br.ufrj.cos.bri.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import br.ufrj.cos.bri.report.AuthorCoAuthor;
import br.ufrj.cos.bri.report.JournalByPerson;
import br.ufrj.cos.bri.report.Person;
import br.ufrj.cos.bri.report.ProceedingsByPerson;

public class GUI {
	private static DefaultListModel listModel;
	private static DefaultListModel proceedingsModel;
	private static DefaultListModel journalsModel;
	private static DefaultListModel coauthorsModel;
	private static Person person = new Person();
	private static ProceedingsByPerson procPerson = new ProceedingsByPerson();
	private static JournalByPerson journalPerson = new JournalByPerson();
	private static AuthorCoAuthor authorCoauthor = new AuthorCoAuthor();
	private static JTextField searchField=null;
	private static JList list=null;
	private static JScrollPane listScroller=null;
	private static JTabbedPane reportTab=null;
	private static ListSelectionListener listener=null;
	
	private static final int PERSON_BY_PROC = 0;
	private static final int PERSON_BY_JOURNAL = 1;
	
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
        listener = new ListSelectionListener() {
        	public void valueChanged(ListSelectionEvent e) {
        		if (e.getValueIsAdjusting() == false) {
        			showProceedings((String)list.getSelectedValue());
        			showJournals((String)list.getSelectedValue());
        			showCoauthors((String)list.getSelectedValue());
        		}
        	}
        };
        
        list.addListSelectionListener(listener);
        listScroller = new JScrollPane(list);
        listScroller.setPreferredSize(new Dimension(250, 600));
        
        c.fill = GridBagConstraints.VERTICAL;
        c.gridx = 0;
        c.gridy = 1;
        
        frame.getContentPane().add(listScroller, c);
        
        reportTab = new JTabbedPane();
        
        JPanel personByProceedingsPanel = new JPanel();  
        proceedingsModel = new DefaultListModel();
        JList proceedings = new JList(proceedingsModel);
        proceedings.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        proceedings.setLayoutOrientation(JList.VERTICAL);
        proceedings.setVisibleRowCount(-1);
        JScrollPane procScroll = new JScrollPane(proceedings);
        procScroll.setPreferredSize(new Dimension(600, 600));
        personByProceedingsPanel.add(procScroll);
        reportTab.addTab("Publicações em Conferências", null, personByProceedingsPanel, "Conferências onde o autor publicou");
        
        JPanel personByJournalsPanel = new JPanel();  
        journalsModel = new DefaultListModel();
        JList journals = new JList(journalsModel);
        journals.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        journals.setLayoutOrientation(JList.VERTICAL);
        journals.setVisibleRowCount(-1);
        JScrollPane journalScroll = new JScrollPane(journals);
        journalScroll.setPreferredSize(new Dimension(600, 600));
        personByJournalsPanel.add(journalScroll);
        reportTab.addTab("Publicações em Periódicos", null, personByJournalsPanel, "Periódicos onde o autor publicou");
        
        JPanel personByCoauthorPanel = new JPanel();  
        coauthorsModel = new DefaultListModel();
        JList coauthors = new JList(coauthorsModel);
        coauthors.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        coauthors.setLayoutOrientation(JList.VERTICAL);
        coauthors.setVisibleRowCount(-1);
        JScrollPane coauthorScroll = new JScrollPane(coauthors);
        coauthorScroll.setPreferredSize(new Dimension(600, 600));
        personByCoauthorPanel.add(coauthorScroll);
        reportTab.addTab("Co-autores relacionados", null, personByCoauthorPanel, "Lista de Co-autores e Frequencia");
        
        
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 1;
        frame.getContentPane().add(reportTab, c);

        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
	
	private static void populateModel() {
		
		if(searchField.getText().length()==0) {
			System.out.println("None!");
			list.removeSelectionInterval(list.getSelectedIndex(), list.getSelectedIndex());
			return;
		}
		
		listModel.removeAllElements();
		Vector<String> authors = person.getAll(searchField.getText()+"%");
		
		for(String author : authors) {
			listModel.addElement(author);
		}
	}
	
	private static void showProceedings(String author) {
		Vector<String> procs = procPerson.listProceedings(author);
		
		proceedingsModel.removeAllElements();
		
		for(String proc : procs) {
			proceedingsModel.addElement(proc);
		}
	}
	
	private static void showJournals(String author) {
		Vector<String> journals = journalPerson.listJournals(author);
		
		journalsModel.removeAllElements();
		
		for(String proc : journals) {
			journalsModel.addElement(proc);
		}
	}
	
	private static void showCoauthors(String author) {
		Map<String,Integer> coauthors = authorCoauthor.listCoAuthors(author);
		
		coauthorsModel.removeAllElements();
		
		Iterator<String> c = coauthors.keySet().iterator();
		
		while(c.hasNext()) {
			String coauthor = c.next();
			coauthorsModel.addElement("("+coauthors.get(coauthor)+") "+coauthor);
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
