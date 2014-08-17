
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class Standings_Controller extends JFrame
{
	// constants
	private final static long serialVersionUID = 1L;	
	
	private JFrame standingsFrame = new JFrame();
	private JTable standingsTable;
	private JComboBox cmbDivision;
	private JButton btnRefresh, stndClose;
	private JLabel stndLabel;
	
	JPanel standingsPanel = new JPanel();
	JPanel standingBtnPanel = new JPanel();		
	BorderLayout standingsBorder = new BorderLayout();
	GridLayout standingsGrid = new GridLayout(1,4,15,15);
	
	private ButtonListener listener;
	
	public Standings_Controller() throws HeadlessException
	{	
		listener = new ButtonListener();
				
		//standingsPanel.setBorder( BorderFactory.createTitledBorder("Game Information"));
		standingsPanel.setLayout(standingsBorder);
		standingBtnPanel.setLayout(standingsGrid);		
		
		
		cmbDivision = new JComboBox();
		cmbDivision.addItem("All");
		cmbDivision.addItem("Atlantic");
		cmbDivision.addItem("Northeast");
		cmbDivision.addItem("Southeast");
		cmbDivision.addItem("Central");
		cmbDivision.addItem("Northwest");
		cmbDivision.addItem("Pacific");		
		
		cmbDivision.addActionListener(listener);
		btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(listener);
		stndClose = new JButton("Close");
		stndClose.addActionListener(listener);
		stndLabel = new JLabel("Select Division: ");
		standingsTable = new JTable();
				
		standingBtnPanel.add(stndLabel);
		standingBtnPanel.add(cmbDivision);
		standingBtnPanel.add(btnRefresh);
		standingBtnPanel.add(stndClose);
				
		standingsTable = initializeTable(standingsTable);
		
		JScrollPane tablePane = new JScrollPane(standingsTable);		
		standingsPanel.add(tablePane, BorderLayout.CENTER);
		standingsPanel.add(standingBtnPanel, BorderLayout.SOUTH);
		
		standingsFrame.add(standingsPanel);			
		
		standingsFrame.setTitle("--ALL-- Division Standings");
		standingsFrame.setSize(500,500);
		standingsFrame.setLocationRelativeTo(null);
				
		CenterScreen(standingsFrame);
		
		standingsFrame.setVisible(true);
	}
	
	public static void CenterScreen(JFrame jf)
	{
		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension screenSize = toolkit.getScreenSize();
		final int x = (screenSize.width - jf.getWidth()) / 2;
		final int y = (screenSize.height - jf.getHeight()) / 2;
		jf.setLocation(x, y);
	}
	
	public static JTable refreshTable(JTable table, JComboBox divBox)
	{
		Connection conn = null;
		Statement stmt = null;
		ResultSet results = null;
		
		Vector colNames = new Vector();
		Vector dataVector = new Vector();
		
		try
		{
			//establish connection to db
			conn = DriverManager.getConnection("jdbc:odbc:NHL2013db");
						
			//create statement
			stmt = conn.createStatement();
			
			String division = (String)divBox.getSelectedItem();
			
			if(division == "All")
			{
				results = stmt.executeQuery("SELECT * FROM teams ORDER BY Points DESC");
			}
			else if(division == "Atlantic")
			{
				results = stmt.executeQuery("SELECT * FROM teams WHERE TeamID IN (1,2,3,4,5) ORDER BY Points DESC;");
			}
			else if(division == "Northeast")
			{
				results = stmt.executeQuery("SELECT * FROM teams WHERE TeamID IN (6,7,8,9,10) ORDER BY Points DESC;");
			}
			else if(division == "Southeast")
			{
				results = stmt.executeQuery("SELECT * FROM teams WHERE TeamID IN (11,12,13,14,15) ORDER BY Points DESC;");
			}
			else if(division == "Central")
			{
				results = stmt.executeQuery("SELECT * FROM teams WHERE TeamID IN (16,17,18,19,20) ORDER BY Points DESC;");
			}
			else if(division == "Northwest")
			{
				results = stmt.executeQuery("SELECT * FROM teams WHERE TeamID IN (21,22,23,24,25) ORDER BY Points DESC;");
			}
			else if(division == "Pacific")
			{
				results = stmt.executeQuery("SELECT * FROM teams WHERE TeamID IN (26,27,28,29,30) ORDER BY Points DESC;");
			}
	
			ResultSetMetaData metaData = results.getMetaData();
	
			int numberOfColumns = metaData.getColumnCount();
			
			for(int i = 1; i <= numberOfColumns; i++)
			{
				colNames.add( metaData.getColumnName(i) );
			}
			
			while(results.next() )
			{
				Vector row = new Vector();
				for(int i = 1; i <= numberOfColumns; i++)
				{
					row.add(results.getString(i) );
				}
				dataVector.add(row);			
			}		
		}
		catch(SQLException sqle)
		{
			//Pop up a message here
		}
		
		table = new JTable(dataVector,colNames);
		
		return table;
	}
	
	public static JTable initializeTable(JTable table)
	{
		Connection conn = null;
		Statement stmt = null;
		ResultSet results = null;
		
		Vector colNames = new Vector();
		Vector dataVector = new Vector();
		
		try
		{
			conn = DriverManager.getConnection("jdbc:odbc:NHL2013db");
			
			stmt = conn.createStatement();
			
			results = stmt.executeQuery("SELECT * FROM teams ORDER BY Points DESC;");
	
			ResultSetMetaData metaData = results.getMetaData();
	
			int numberOfColumns = metaData.getColumnCount();
						
			for(int i = 1; i <= numberOfColumns; i++)
			{
				colNames.add( metaData.getColumnName(i) );
			}
			
			while(results.next() )
			{
				Vector row = new Vector();
				for(int i = 1; i <= numberOfColumns; i++)
				{
					row.add(results.getString(i) );
				}
				dataVector.add(row);
			}
			
		}
		catch(SQLException sqle)
		{
			//Pop up message here
		}
		
		table = new JTable(dataVector,colNames);
		
		return table;
	}
	
	private class ButtonListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == stndClose)
			{
				
			}
			else if(e.getSource() == btnRefresh)
			{
				standingsPanel.removeAll();
				standingsTable = refreshTable(standingsTable, cmbDivision);
				JScrollPane tablePane = new JScrollPane(standingsTable);		
				standingsPanel.add(tablePane, BorderLayout.CENTER);
				standingsPanel.add(standingBtnPanel, BorderLayout.SOUTH);
				standingsPanel.revalidate();
				standingsPanel.repaint();
			}
			
		}
	}
	
}
