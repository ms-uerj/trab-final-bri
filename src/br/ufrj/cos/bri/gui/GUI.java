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
import java.util.List;
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
import br.ufrj.cos.bri.report.Journal;
import br.ufrj.cos.bri.report.JournalByPerson;
import br.ufrj.cos.bri.report.JournalByProceedings;
import br.ufrj.cos.bri.report.Person;
import br.ufrj.cos.bri.report.Proceedings;
import br.ufrj.cos.bri.report.ProceedingsByJournal;
import br.ufrj.cos.bri.report.ProceedingsByPerson;

public class GUI {
	private static DefaultListModel listModel;
	private static DefaultListModel proceedingsModel;
	private static DefaultListModel journalsModel;
	private static DefaultListModel coauthorsModel;
	private static Person person = new Person();
	private static Journal journal = new Journal();
	private static Proceedings proceedings = new Proceedings();
	private static ProceedingsByPerson procPerson = new ProceedingsByPerson();
	private static JournalByPerson journalPerson = new JournalByPerson();
	private static AuthorCoAuthor authorCoauthor = new AuthorCoAuthor();
	private static JournalByProceedings journalByProceedings = new JournalByProceedings();
	private static ProceedingsByJournal proceedingsByJournal = new ProceedingsByJournal();
	private static JTextField searchField=null;
	private static JList list=null;
	private static JScrollPane listScroller=null;
	private static JTabbedPane reportTab=null;
	private static ListSelectionListener listener=null;
	
	private static final int PERSON_BY_PROC = 0;
	private static final int PERSON_BY_JOURNAL = 1;
	
	private static String selectedComboboxValue = "Author";
	
    //Create and set up the window.
    private static JFrame frame = null;
	
	private static void createAndShowGUI() {
//        //Create and set up the window.
        frame = new JFrame("Trabalho de BRI");
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
                selectedComboboxValue = (String)cb.getSelectedItem();
                populateModel();
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
        
        
        reportTab = new JTabbedPane();
        
        setAuthorReportTab();
        
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 1;
        frame.getContentPane().add(reportTab, c);
        
        
        listModel = new DefaultListModel();
        populateModel();
        
        list = new JList(listModel); //data has type Object[]
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);        
        list.setVisibleRowCount(-1);
        listener = new ListSelectionListener() {
        	public void valueChanged(ListSelectionEvent e) {
        		if (e.getValueIsAdjusting() == false) {
        			if(selectedComboboxValue.equals("Author")){
	        			showProceedings((String)list.getSelectedValue());
	        			showJournals((String)list.getSelectedValue());
	        			showCoauthors((String)list.getSelectedValue());
        			}
        			else
        			if(selectedComboboxValue.equals("Proceedings")){
        				showJournalsCitedByProceedings((String)list.getSelectedValue());
        			}
        			else
    				if(selectedComboboxValue.equals("Journal")){
    					showProceedingsCitedByJournal((String)list.getSelectedValue());
    				}
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
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
	
	private static void populateModel() {

		
		if(selectedComboboxValue.equals("Author")){
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
			
			setAuthorReportTab();
			
		}
		else
		if(selectedComboboxValue.equals("Proceedings")){
			if(searchField.getText().length()==0) {
				System.out.println("None!");
				list.removeSelectionInterval(list.getSelectedIndex(), list.getSelectedIndex());
				return;
			}
			
			listModel.removeAllElements();
			Vector<String> authors = proceedings.getAll(searchField.getText()+"%");
			
			for(String author : authors) {
				listModel.addElement(author);
			}
			
			setProceedingsReportTab();
		}
		else
		if(selectedComboboxValue.equals("Journal")){
			if(searchField.getText().length()==0) {
				System.out.println("None!");
				list.removeSelectionInterval(list.getSelectedIndex(), list.getSelectedIndex());
				return;
			}
			
			listModel.removeAllElements();
			Vector<String> authors = journal.getAll(searchField.getText()+"%");
			
			for(String author : authors) {
				listModel.addElement(author);
			}
			
			setJournalReportTab();
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
	
	private static void showJournalsCitedByProceedings(String proceedings) {
//		List<br.ufrj.cos.bri.model.Journal> journals = journalByProceedings.listRankedJournalCitedByProceedings(proceedings);
//		
//		journalsModel.removeAllElements();
//		
//		Iterator<br.ufrj.cos.bri.model.Journal> c = journals.iterator();
//		
//		while(c.hasNext()) {
//			br.ufrj.cos.bri.model.Journal journal = c.next();
//			coauthorsModel.addElement(journal.getTitle());
//		}
	}
	
	private static void showProceedingsCitedByJournal(String journal) {
		List<br.ufrj.cos.bri.model.Proceedings> proceedingsList = proceedingsByJournal.listRankedProceedingsCitedByJournal(journal);
		
		proceedingsModel.removeAllElements();
		
		Iterator<br.ufrj.cos.bri.model.Proceedings> c = proceedingsList.iterator();
		
		while(c.hasNext()) {
			br.ufrj.cos.bri.model.Proceedings proceedings = c.next();
			coauthorsModel.addElement(proceedings.getTitle());
		}
	}
	
	public static void setAuthorReportTab(){
        
		reportTab.removeAll();
		
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

	}
	
	public static void setJournalReportTab(){
		
        reportTab.removeAll();
        
        JPanel ProceedingsByJournalPanel = new JPanel();  
        journalsModel = new DefaultListModel();
        JList proceedings = new JList(proceedingsModel);
        proceedings.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        proceedings.setLayoutOrientation(JList.VERTICAL);
        proceedings.setVisibleRowCount(-1);
        JScrollPane procScroll = new JScrollPane(proceedings);
        procScroll.setPreferredSize(new Dimension(600, 600));
        ProceedingsByJournalPanel.add(procScroll);
        reportTab.addTab("Conferências realcionadas à Periódico", null, ProceedingsByJournalPanel, "Conferências realcionadas à Periódico");

        
	}
	
	public static void setProceedingsReportTab(){
		
		reportTab.removeAll();
		
        JPanel JournalsByProceedingsPanel = new JPanel();  
        proceedingsModel = new DefaultListModel();
        JList proceedings = new JList(proceedingsModel);
        proceedings.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        proceedings.setLayoutOrientation(JList.VERTICAL);
        proceedings.setVisibleRowCount(-1);
        JScrollPane procScroll = new JScrollPane(proceedings);
        procScroll.setPreferredSize(new Dimension(600, 600));
        JournalsByProceedingsPanel.add(procScroll);
        reportTab.addTab("Periódicos realcionados à Conferência", null, JournalsByProceedingsPanel, "Periódicos realcionados à Conferência");
        
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
