package trainSet;
/*
 * Autore: Bacigalupo Marta
 *
 * Data: 02/07/2017
 *
 * The purpose of the project is to provide a tool for speeding up and simplifying
 * the analysis of the performance of automatic classifiers
 * serializing them with different input configurations, train
 * Sets and test sets that will be specified by the user.
 * In particular this part of implementation deals with the the management of 
 * the train set that the user wishes to use and manages adding, setting, and 
 * removing them.
 * 
 */


import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;


/**
 * The Window Class manages the GUI with which the user can interact
 * Allows you to select, add, and remove a train set.
 */

public class Window {

	private JFrame frame;
	private Listener[] listener;
	private JTable[] jTable;
	private DefaultTableModel[] defaultTableModel;
	
	private FileManager fileManager;
	private JButton trainButtonRemove;
	
	/**
	 * This method allows to initialize the jTable, that is filled with
	 * the available train sets. It also initialize the table Listener
	 * and DefaultTableModel to visualize the data.

     *
	 * @param fileManager
	 * @throws Exception
	 */
	
	public Window(FileManager fileManager) throws Exception {
		this.fileManager = fileManager;

		final int modelsNumber = FileType.values().length;

		listener = new Listener[modelsNumber];
		jTable = new JTable[modelsNumber];
		defaultTableModel = new DefaultTableModel[modelsNumber];
	}
	
	/**
	 * The initialize() function creates the main frame, instantiates
	 * the graphic controls and attaches them to the frame.
	 * This function initializes all the buttons for
	 * managing train set models,  fills tables with the model data 
	 * contained in the fileManager (derived from database queries), 
	 * and starts the application GUI. 
	 * 
	 * @throws Exception
	 */

	public void initialize() throws Exception {
		
		/*
		 * Refreshes the model data
		 */
		
		fileManager.updateModelData(FileType.TRAIN);
		
		/*
		 * Creates the window main frame
		 */
		
		frame = new JFrame();
		frame.setMinimumSize(new Dimension(700, 400));
		frame.setBounds(100, 100, 1000, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
		frame.setVisible(true);
		
		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		frame.getContentPane().add(rightPanel);
		  

		/*
		 * Creates the test/train set panels, buttons, and tables
		 * and attaches them to the main frame
		 */
		
		JPanel trainSetPanel = new JPanel();
		trainSetPanel.setMinimumSize(new Dimension(100, 100));
		trainSetPanel.setLayout(new GridLayout(1, 0, 0, 0));
		rightPanel.add(trainSetPanel);

		JPanel trainSetTable = new JPanel();
		trainSetTable.setLayout(new GridLayout());
		trainSetTable.add(addScrollablePane(FileType.TRAIN));
		trainSetPanel.add(trainSetTable);

		JPanel trainSetPanelButtons = new JPanel();
		trainSetPanel.add(trainSetPanelButtons);

		JButton trainButtonAdd = new JButton("+ Train");
		trainSetPanelButtons.add(trainButtonAdd);
		trainButtonAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actionPerformedAdd(FileType.TRAIN);
			}
		});

		trainButtonRemove = new JButton("- Train");
		trainSetPanelButtons.add(trainButtonRemove);
		trainButtonRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actionPerformedRemove(FileType.TRAIN);
			}
		});
	}

    /**
     * This method allow to obtain the column name through 
     * the fileType "fileType"
     *
     * @param fileType
     * @exception: Exception
     * @return fileType
     */
	
	private String getColumnName(FileType fileType) throws Exception {
		switch (fileType) {
			case TRAIN:
				return "TrainSet";
			default:
				throw new Exception("Unrecognized file type");
		}
	}

    /**
     * This method allows to obtains the listener of the
     * given fileType "fileType".
     *
     * @param fileType
     * @exception: Exception
     * @return Listener
     */
	
	private Listener createListener(FileType fileType) throws Exception {
		switch (fileType) {
			case TRAIN:
				return new TrainSetListener(fileManager);
			default:
				throw new Exception("Unrecognized file type");
		}
	}

    /**
     * This method add a JScrollPane of the given FileType.
     *
     * @param fileType
     * @exception: Exception
     * @return JScrollPane
     */
	
	private JScrollPane addScrollablePane(FileType fileType) throws Exception {
		Object[] columnNames = { getColumnName(fileType), "Nome" };
		
		int index = fileType.ordinal();
		int size = fileManager.getArraySize(fileType);
		Object[][] data = new Object[size][2];
		for (int i = 0; i < size; i++) {
			Model tmpModel = fileManager.getElement(fileType, i);
			data[i][0] = tmpModel.getClicked();
			data[i][1] = tmpModel.getName();
		}

		defaultTableModel[index] = new DefaultTableModel(data, columnNames) {
	
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 0;
			}
		};
		
		jTable[index] = new JTable(defaultTableModel[index]) {

			private static final long serialVersionUID = 1L;

			@Override
			public Class<?> getColumnClass(int column) {
				switch (column) {
				case 0:
					return Boolean.class;
				case 1:
					return String.class;
				default:
					return String.class;
				}
			}
		};
		
		/*
		 * Creates the listener and attaches it to the table
		 * 
		 */
		
		listener[index] = createListener(fileType);
		defaultTableModel[index].addTableModelListener(listener[index]);
		jTable[index].setPreferredScrollableViewportSize(
									jTable[index].getPreferredSize());
		JScrollPane scrollPane = new JScrollPane(jTable[index], 
									 JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
									 JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		return scrollPane;
	}

    /**
     * This method menages the add of a new train set.
     * It is invoked by clicking over the button '+ Train Set'
     *
     * @param fileType
     */
	
	private void actionPerformedAdd(FileType fileType) {
		try {
			OpenFile openFile = new OpenFile(fileType);
			openFile.pickMe();

			String name = openFile.getName();
			String path = openFile.getPath();
			
			if (name.isEmpty() || path.isEmpty())
				return;
			
			fileManager.insert(name, path, fileType); // Updates file manager
			this.addRow(fileType.ordinal(), name);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null,
					"Problem adding the selected " + fileType.name() +
					"\n" + ex.getMessage());
		}
	}
	
    /**
     * This method menages the remotion of a selected TrainSet.
     * It is invoked clicking over the button "- Train Set"
     *
     * @param fileType
     */

	private void actionPerformedRemove(FileType fileType) {
		try {
						
			int dialogResult = JOptionPane.showConfirmDialog(null, 
					"Do you really want to delete the selected element?");
						
			if (dialogResult != JOptionPane.YES_OPTION)
				return;
			
			int index = fileType.ordinal();
			int rowToDelete = jTable[index].getSelectedRow();
			if (rowToDelete == -1)
				return;

			fileManager.remove(jTable[index].getModel().getValueAt(
					rowToDelete,1).toString(), fileType);
			this.removeRow(index);
			
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, 
					"Problem removing the selected " + fileType.name());
			ex.printStackTrace();
		}
	}
	
    /*
     * If the database modification succeeds, add the table line
     *  without calling the change table callback.
     *
     * @param: index
     * @param: name
     */

	private void addRow(int index, String name) {
		listener[index].setLock();
		defaultTableModel[index].addRow(new Object[] { false, name });
		listener[index].unsetLock();
	}
	
	/**
	 * Allows the elimination of a Row without firing tableChanged.
	 * 
	 * @param index
	 */
	
	private void removeRow(int index) {
		listener[index].setLock();
		defaultTableModel[index].removeRow(jTable[index].getSelectedRow());
		listener[index].unsetLock();
	}
    
    /*
     * This method allows to the test to click a train set of the table
     *
     * @exception: InterruptedException
     */
	
	public void testClickedTrainSet() throws InterruptedException{
		ArrayList<Model> trainSet = fileManager.getClicked(FileType.TRAIN);
		if(!trainSet.isEmpty()){
			int clickedIndex = -1;
			for(int i = 0; i < 
					jTable[FileType.TRAIN.ordinal()].getRowCount(); i++){
				if((boolean) defaultTableModel[FileType.TRAIN.ordinal()
				                               ].getValueAt(i, 0)){
					clickedIndex = i;
					break;
				}
			}
			int toBeClicked = ((clickedIndex + 1) > 
				defaultTableModel[FileType.TRAIN.ordinal()].getRowCount() - 1)
					? 0 : (clickedIndex + 1);
			defaultTableModel[FileType.TRAIN.ordinal()].setValueAt(true,
															toBeClicked, 0);
			defaultTableModel[FileType.TRAIN.ordinal()].fireTableChanged(
					new TableModelEvent(defaultTableModel[
					   FileType.TRAIN.ordinal()],toBeClicked,toBeClicked,0));
		}
	}
    
    /*
     * This method permit the test to add a train set to the table.
     *
     * @exception: Exception
     */
	
	public void testAddTrainSet() throws Exception{
		scheduleAutoAnswerFileChooser();
		scheduleAutoAnswerJOptionPane(JOptionPane.OK_OPTION,10000);
		actionPerformedAdd(FileType.TRAIN);
		Thread.sleep(2000);
	}
    
    /*
     * This method permit the test to remove a train set from the table.
     *
     * @exception: Exception
     */
	
	public void testRemoveTrainSet(int answer) throws Exception{
		FileType fileType = FileType.TRAIN;
		int index = fileType.ordinal();
		jTable[index].setRowSelectionInterval(0, 0);
		Thread.sleep(2000);
		int rowToDelete = jTable[index].getSelectedRow();
		if (rowToDelete == -1)
			return;

		scheduleAutoAnswerJOptionPane(answer,5000);
		trainButtonRemove.doClick();
		Thread.sleep(2000);
	}
	
	private void scheduleAutoAnswerJOptionPane(int answer,int timeToWait){
		TimerTask timerTask = new TimerTask() {
	        @Override
	        public void run() {
	        	java.awt.Window[] windows = java.awt.Window.getWindows();
	            for (java.awt.Window window : windows) {
	                if (window instanceof JDialog) {
	                    JDialog dialog = (JDialog) window;
	                    if (dialog.getContentPane().getComponentCount() == 1
	                    		&& dialog.getContentPane().getComponent(0)
	                        						instanceof JOptionPane){
	                    	JOptionPane op = (JOptionPane) 
	                    			dialog.getContentPane().getComponent(0);
	                    	op.setValue(answer);
	                    }
	                }
	            }
	        }
		};
		Timer timer = new Timer("MyTimer"); // Creates a new Timer
	    timer.schedule(timerTask, timeToWait);
	}
	
	private void scheduleAutoAnswerFileChooser(){
		TimerTask timerTask = new TimerTask() {
	        @Override
	        public void run() {
	        	java.awt.Window[] windows = java.awt.Window.getWindows();
	            for (java.awt.Window window : windows) {
	                if (window instanceof JDialog) {
	                    JDialog dialog = (JDialog) window;
	                    if (dialog.getContentPane().getComponentCount() == 1
	                        && dialog.getContentPane().getComponent(0)
	                        		instanceof JFileChooser){
	                    	JFileChooser jfc = (JFileChooser) 
	                    			dialog.getContentPane().getComponent(0);
	                    	jfc.setSelectedFile(new File("data" + 
	                    			File.separator + "train" + 
	                    			File.separator + "newTrain.txt"));
	                    	jfc.approveSelection();
	                    }
	                }
	            }
	        }
	    };
	
	    Timer timer = new Timer("MyTimer");// Creates a new Timer
	    timer.schedule(timerTask, 5000);
	}
}
