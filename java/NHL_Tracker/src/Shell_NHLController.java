/**
 * Program Name:Shell_NHLController.java
 * Purpose: this is the shell code for displaying the NHLController GUI.
 * Author: Bill Pulling
 * Date: Jul 15, 2012
 */


import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/****************************************************************************************/
/*   Author					Date				Description
 * **************************************************************************************
 *   Nicholas McRae         June. 5/13          Initial implementation, set up database, implemented combo-boxes, finished rough add/delete/clear/retrieve
 *   Nicholas McRae         June. 6/13          Implemented standings and close button
 *   Nicholas McRae         June. 7/13          Refactored and decomposed code
 *   Nicholas McRae         June. 8/13          Tested and debugged code until it was fully functional
 *   Nicholas McRae         June. 16/13         Re-tested and verified app, moved some validation logic around so a user wasn't prompted if validation failed
 */

public class Shell_NHLController extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	//factors for sizing and placing GUI according to screen resolution
	private static final double FR_WIDTH_FACTOR = 0.5;
	private static final double FR_HEIGHT_FACTOR = 0.45;
	
	//class scope declarations
	private JButton addBtn, btnClear, btnDelete, btnRetrieve, btnStandings, btnClose;
	private JComboBox cmbMonth, cmbDay, cmbYear;
	private JComboBox cmbHome, cmbAway;
	private JTextField fldHome, fldAway;
	private JCheckBox chbOvertime, chbShootout;
	private ButtonListener listener;
	
	private int yearContainer;
	private String monthContainer = "";	
	
	//constructor
	public Shell_NHLController() throws HeadlessException
	{
		// Set up the basic JFrame
		this.setTitle("NHL Game Tracker");
		this.setSize((int)(getToolkit().getScreenSize().width * FR_WIDTH_FACTOR),
								(int)(getToolkit().getScreenSize().height * FR_HEIGHT_FACTOR));
		this.setLocationRelativeTo(null);
		// set default as do nothing so that user can deny exit confirmation dialog
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		//CenterScreen
		CenterScreen(this);
		
		//check when user clicks the frame's "Close" button. 
		this.addWindowListener(new WindowAdapter() 
		{
		  public void windowClosing(WindowEvent e)
		  {
			  int n = JOptionPane.showConfirmDialog(
						Shell_NHLController.this, "Are you sure you want to exit?",
						"User Confirmation", JOptionPane.YES_NO_OPTION);
				
				if ( n == JOptionPane.YES_OPTION)
				{
					//destory the frame object
					Shell_NHLController.this.dispose();					
				}			
		  }//end method
		});//end method addWindowListener() arg list
		
		//create one button listener object for all buttons
		listener = new ButtonListener();
		// GameInfoPanel
		JPanel gameInfoPanel = new JPanel();
		
		gameInfoPanel.setBorder( BorderFactory.createTitledBorder("Game Information"));
		gameInfoPanel.setLayout(new GridLayout(5, 1, 20, 20));		
		
		// Date Panel
		JPanel datePanel = new JPanel();		
		datePanel.setLayout( new GridLayout(1, 4, 20, 20) );		
		datePanel.add(new JLabel("<< Date >> (Month/Day/Year)",  JLabel.RIGHT));
		
		cmbMonth = new JComboBox();
		cmbMonth.addItem("January");
		cmbMonth.addItem("February");
		cmbMonth.addItem("March");
		cmbMonth.addItem("April");
		cmbMonth.addItem("May");
		cmbMonth.addItem("June");
		cmbMonth.addItem("July");
		cmbMonth.addItem("August");
		cmbMonth.addItem("September");
		cmbMonth.addItem("October");
		cmbMonth.addItem("November");
		cmbMonth.addItem("December");
		
		cmbMonth.addActionListener(listener);		
		
		datePanel.add(cmbMonth);
		
		cmbDay = new JComboBox();
		datePanel.add(cmbDay);
		cmbYear = new JComboBox();
		 for ( int year = 2012; year <= 2020; ++year) 
		 {
			cmbYear.addItem(year);
		 }
		datePanel.add(cmbYear);
		cmbYear.addActionListener(listener);
		
		gameInfoPanel.add(datePanel);	
		
		JPanel title = new JPanel();		
		title.setLayout( new GridLayout(1, 3, 20, 20) );		
		title.add(new JLabel(""));
		title.add(new JLabel("HOME", JLabel.CENTER));
		title.add(new JLabel("AWAY",  JLabel.CENTER));
		
		gameInfoPanel.add(title);
	
		JPanel team = new JPanel();		
		team.setLayout( new GridLayout(1, 3, 20, 20));		
		team.add(new JLabel("<< Team >> ",  JLabel.RIGHT));
		
		cmbHome = new JComboBox();
		team.add(cmbHome);
		
		cmbAway = new JComboBox();
		team.add(cmbAway);		
		gameInfoPanel.add(team);
		
		JPanel goal = new JPanel();		
		goal.setLayout( new GridLayout(1, 3, 20, 20));		
		goal.add(new JLabel("Goals", JLabel.RIGHT));
		
		fldHome = new JTextField("");
		fldHome.setHorizontalAlignment(JTextField.CENTER) ;
		goal.add(fldHome);
		
		fldAway = new JTextField("");
		fldAway.setHorizontalAlignment(JTextField.CENTER) ;
		goal.add(fldAway);		
		gameInfoPanel.add(goal);
		
		JPanel overtime = new JPanel();		
		overtime.setLayout( new GridLayout(1, 3, 20, 20));		
		overtime.add(new JLabel("Overtime?", JLabel.RIGHT));
		
		chbOvertime= new JCheckBox();
		overtime.add(chbOvertime);
		
		overtime.add(new JLabel("Shootout?", JLabel.RIGHT));
		chbShootout = new JCheckBox("");
		overtime.add(chbShootout);		
		gameInfoPanel.add(overtime);		
		this.add(gameInfoPanel, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();		
		buttonPanel.setLayout( new GridLayout(2, 3, 20, 20) );
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		addBtn = new JButton("Add");
		addBtn.addActionListener(listener);
		buttonPanel.add(addBtn);
		
		btnClear = new JButton("Clear");
		btnClear.addActionListener(listener);
		buttonPanel.add(btnClear);
		
		btnDelete = new JButton("Delete");
		btnDelete.addActionListener(listener);
		buttonPanel.add(btnDelete);
		
		btnRetrieve = new JButton("<< Retrieve >> ");
		btnRetrieve.addActionListener(listener);
		buttonPanel.add(btnRetrieve);
		
		btnStandings = new JButton("Standings");
		btnStandings.addActionListener(listener);
		buttonPanel.add(btnStandings);
		
		btnClose = new JButton("Close");
		btnClose.addActionListener(listener);
		buttonPanel.add(btnClose);
		
		this.add(buttonPanel, BorderLayout.SOUTH);
		
		this.setVisible(true);
		
		cmbMonth.setEnabled(false);
		cmbDay.setEnabled(false);
		
		loadTeamBoxes(cmbHome, cmbAway);
		
	}//end constructor
	
	
	public static void CenterScreen(JFrame jf)
	{
		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension screenSize = toolkit.getScreenSize();
		final int x = (screenSize.width - jf.getWidth()) / 2;
		final int y = (screenSize.height - jf.getHeight()) / 2;
		jf.setLocation(x, y);
	}
	
	public static void loadTeamBoxes(JComboBox home, JComboBox away)
	{			
			Connection conn = null;
			Statement stmt = null;
			Statement stmt2 = null;
			ResultSet homeRslt = null;
			ResultSet awayRslt = null;
			
			try
			{
				Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");				
				conn = DriverManager.getConnection("jdbc:odbc:NHL2013db");
				stmt = conn.createStatement();
				stmt2 = conn.createStatement();				
				homeRslt = stmt.executeQuery("SELECT TeamName FROM teams");
				awayRslt = stmt2.executeQuery("SELECT TeamName FROM teams");				
				
				while( homeRslt.next() )
				{
					home.addItem(homeRslt.getString("TeamName"));						
				}
				
				while( awayRslt.next() )
				{
					away.addItem(awayRslt.getString("TeamName"));						
				}
			}
			catch(ClassNotFoundException ex)
			{
				System.out.println(ex.getMessage());
			}
			catch(SQLException ex)
			{
				System.out.println(ex.getMessage());
			}				
			finally
			{				
				try
				{
					if( stmt != null && stmt2 != null )
					{
						stmt.close();
						stmt2.close();
					}
					if( conn != null )
						conn.close();
				}
				catch(SQLException ex)
				{
					System.out.println(ex.getMessage());
				}
			}
	}	
	
	public static void clearFields( JComboBox year, JComboBox month, JComboBox day, JComboBox home, JComboBox away,
			JTextField homeField, JTextField awayField, JCheckBox overtime, JCheckBox shootout, JButton add )
	{
		try
		{
			int dialogButton = 0;
			dialogButton = JOptionPane.showConfirmDialog((Component) null, "Would you like to save contents before clearing?","Alert", JOptionPane.YES_NO_CANCEL_OPTION);
			
	        if( dialogButton == JOptionPane.YES_OPTION )
	        {
	        	boolean validated = Validation(homeField, awayField, home, away, day);
				
				if( validated )
				{
					addGame(year, month, day, home, away, homeField, awayField, overtime, shootout);
					JOptionPane.showMessageDialog(null,"The game was added successfully!", "Information", JOptionPane.INFORMATION_MESSAGE);
				}			
				
				resettingFields( year, month, day, home, away, homeField, awayField, overtime, shootout );
	        }
	        else if( dialogButton == JOptionPane.NO_OPTION )
	        {
	        	resettingFields(year, month, day, home, away, homeField, awayField, overtime, shootout);    		
	        }
		}
		catch( Exception e )
		{
			resettingFields( year, month, day, home, away, homeField, awayField, overtime, shootout );
			
			JOptionPane.showMessageDialog( null,"Something went wrong!", "Error", JOptionPane.ERROR_MESSAGE );
		}
	}
	
	public static void resettingFields(JComboBox year, JComboBox month, JComboBox day, JComboBox home, JComboBox away,
			JTextField homeField, JTextField awayField, JCheckBox overtime, JCheckBox shootout)
	{
		year.setSelectedIndex(0);
		month.setSelectedIndex(0);
		day.setSelectedIndex(0);
		home.setSelectedIndex(0);
		away.setSelectedIndex(0);
		homeField.setText("");
		awayField.setText("");
		overtime.setSelected(false);
		shootout.setSelected(false);
	}
	
	public static boolean addGame(JComboBox year, JComboBox month, JComboBox day, JComboBox home, JComboBox away,
			JTextField homeField, JTextField awayField, JCheckBox overtime, JCheckBox shootout)
	{	
		
			Connection conn = null;
			Statement insertGame = null;
			Statement queryHome = null;
			Statement queryAway = null;
			Statement updateHome = null;
			Statement updateAway = null;
			ResultSet queriedHome = null;
			ResultSet queriedAway = null;
			
			int gameUpdated = 0;
			int homeUpdated = 0;
			int awayUpdated = 0;
			
			try
			{
				Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
				
				conn = DriverManager.getConnection("jdbc:odbc:NHL2013db");
				
				//create various query statements
				insertGame = conn.createStatement();
				queryHome = conn.createStatement();
				queryAway = conn.createStatement();
				updateHome = conn.createStatement();
				updateAway = conn.createStatement();
				
				//get game data from interface for use in query statements
				int yearContainer = (Integer)year.getSelectedItem();
				String monthContainer = (String)month.getSelectedItem();
				int dayContainer = (Integer)day.getSelectedItem();
				int awayTeam = away.getSelectedIndex() + 1;
				int homeTeam = home.getSelectedIndex() + 1;
				int homeGoals = Integer.parseInt(homeField.getText());
				int awayGoals = Integer.parseInt(awayField.getText());
				boolean isOvertime = overtime.isSelected();
				boolean isShootout = shootout.isSelected();
				String s_awayTeam = (String)away.getSelectedItem();
				String s_homeTeam = (String)home.getSelectedItem();
				
				//insert game
				gameUpdated = insertGame.executeUpdate("INSERT INTO Games (HomeTeamID,AwayTeamID,HomeTeamGoals,AwayTeamGoals,GameMonth,GameDay,GameYear,Overtime,Shootout)"
						+ "VALUES("
						+ homeTeam + ","
						+ awayTeam + ","
						+ homeGoals + ","
						+ awayGoals + ",'"
						+ monthContainer + "',"
						+ dayContainer + ","
						+ yearContainer + ","
						+ isOvertime + ","
						+ isShootout + ")"
						);	
				
				//query home and away team details for update depending on results of game
				queriedHome = queryHome.executeQuery("Select GamesPlayed, Wins, Points, RegulationLosses, OvertimeLosses, ShootoutLosses FROM teams WHERE TeamName='" + s_homeTeam + "';");
				queriedAway = queryAway.executeQuery("Select GamesPlayed, Wins, Points, RegulationLosses, OvertimeLosses, ShootoutLosses FROM teams WHERE TeamName='" + s_awayTeam + "';");
				
				
				while(queriedHome.next() && queriedAway.next())
				{
					//home details					
					int homeGamesPlayed = queriedHome.getInt("GamesPlayed");
					int homeWins = queriedHome.getInt("Wins");
					int homePoints = queriedHome.getInt("Points");
					int homeRegLoss = queriedHome.getInt("RegulationLosses");
					int homeOvrLoss = queriedHome.getInt("OvertimeLosses");
					int homeShtLoss = queriedHome.getInt("ShootoutLosses");
					
					//away details
					int awayGamesPlayed = queriedAway.getInt("GamesPlayed") ;
					int awayWins = queriedAway.getInt("Wins");
					int awayPoints = queriedAway.getInt("Points");
					int awayRegLoss = queriedAway.getInt("RegulationLosses");
					int awayOvrLoss = queriedAway.getInt("OvertimeLosses");
					int awayShtLoss = queriedAway.getInt("ShootoutLosses");
				
					if(homeGoals > awayGoals && !isOvertime && !isShootout)//home team wins in regulation
					{
						homeUpdated = updateHome.executeUpdate("Update teams SET GamesPlayed=" + (homeGamesPlayed + 1) + ",Wins=" + (homeWins + 1) + ",Points=" + (homePoints + 2) +
								" WHERE TeamID=" + homeTeam + ";");
						
						awayUpdated = updateAway.executeUpdate("Update teams SET GamesPlayed=" + (awayGamesPlayed + 1) + ",RegulationLosses=" + (awayRegLoss + 1) +
								" WHERE TeamID=" + awayTeam + ";");
					}
					else if(homeGoals < awayGoals && !isOvertime && !isShootout)//away team wins in regulation
					{
						awayUpdated = updateAway.executeUpdate("Update teams SET GamesPlayed=" + (awayGamesPlayed + 1) + ", Wins=" + (awayWins + 1) + ",Points=" + (awayPoints + 2) +
								" WHERE TeamID=" + awayTeam + ";");
						
						homeUpdated = updateHome.executeUpdate("Update teams SET GamesPlayed=" + (homeGamesPlayed + 1) + ",RegulationLosses=" + (homeRegLoss + 1) +
								" WHERE TeamID=" + homeTeam + ";");
					}
					else if(homeGoals > awayGoals && isOvertime && !isShootout)//home team wins in overtime
					{							
						homeUpdated = updateHome.executeUpdate("Update teams SET GamesPlayed=" + (homeGamesPlayed + 1) + ", Wins=" + (homeWins + 1) + ",Points=" + (homePoints + 2) +
								" WHERE TeamID=" + homeTeam + ";");
						
						awayUpdated = updateAway.executeUpdate("Update teams SET GamesPlayed=" + (awayGamesPlayed + 1) + ",OvertimeLosses=" + (awayOvrLoss + 1) + ",Points=" + (awayPoints + 1) +
								" WHERE TeamID=" + awayTeam + ";");
					}
					else if(homeGoals < awayGoals && isOvertime && !isShootout)//away team wins in overtime
					{
						awayUpdated = updateAway.executeUpdate("Update teams SET GamesPlayed=" + (awayGamesPlayed + 1) + ", Wins=" + (awayWins + 1) + ",Points=" + (awayPoints + 2) +
								" WHERE TeamID=" + awayTeam + ";");
						
						homeUpdated = updateHome.executeUpdate("Update teams SET GamesPlayed=" + (homeGamesPlayed + 1) + ",OvertimeLosses=" + (homeOvrLoss + 1) + ",Points=" + (homePoints + 1) +
								" WHERE TeamID=" + homeTeam + ";");
					}
					else if(homeGoals > awayGoals && ((!isOvertime && isShootout) || (isOvertime && isShootout)))//home team wins in shootout
					{
						homeUpdated = updateHome.executeUpdate("Update teams SET GamesPlayed=" + (homeGamesPlayed + 1) + ", Wins=" + (homeWins + 1) + ",Points=" + (homePoints + 2) +
								" WHERE TeamID=" + homeTeam + ";");
						
						awayUpdated = updateAway.executeUpdate("Update teams SET GamesPlayed=" + (awayGamesPlayed + 1) + ",ShootoutLosses=" + (awayShtLoss + 1) + ",Points=" + (awayPoints + 1) +
								" WHERE TeamID=" + awayTeam + ";");
						
					}
					else if(homeGoals < awayGoals && ((!isOvertime && isShootout) || (isOvertime && isShootout)))//away team wins in shootout
					{
						awayUpdated = updateAway.executeUpdate("Update teams SET GamesPlayed=" + (awayGamesPlayed + 1) + ", Wins=" + (awayWins + 1) + ",Points=" + (awayPoints + 2) +
								" WHERE TeamID=" + awayTeam + ";");
						
						homeUpdated = updateHome.executeUpdate("Update teams SET GamesPlayed=" + (homeGamesPlayed + 1) + ",ShootoutLosses=" + (homeShtLoss + 1) + ",Points=" + (homePoints + 1) +
								" WHERE TeamID=" + homeTeam + ";");
					}
				}
			}
			catch(ClassNotFoundException ex)
			{
				System.out.println(ex.getMessage());
			}
			catch(SQLException ex)
			{
				System.out.println(ex.getMessage());
			}	
			catch(Exception ex)
			{
				JOptionPane.showMessageDialog(null,"Y'all did something crazy!", "Error", JOptionPane.ERROR_MESSAGE);
			}
			finally
			{	
				try
				{
					if( insertGame != null && queryHome != null && queryAway != null && updateHome != null && updateAway != null )
					{
						insertGame.close();
						queryHome.close();
						queryAway.close();
						updateHome.close();
						updateAway.close();
					}
					if( conn != null )
						conn.close();
				}
				catch(SQLException ex)
				{
					System.out.println(ex.getMessage());
				}
			}
			
			if(gameUpdated > 0 && homeUpdated > 0 && awayUpdated > 0)
			{
				return true;
			}
			else
			{
				return false;
			}
	}//end add
	
	public static void retrieveGame(JComboBox year, JComboBox month, JComboBox day, JComboBox home, JComboBox away,
			JTextField homeField, JTextField awayField, JCheckBox overtime, JCheckBox shootout)
	{		
		
			Connection conn = null;
			Statement retrieveGame = null;				
			ResultSet retrievedGame = null;			
			
			try
			{
				Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
				
				conn = DriverManager.getConnection("jdbc:odbc:NHL2013db");
				
				retrieveGame = conn.createStatement();					
				
				int yearContainer = (Integer)year.getSelectedItem();
				String monthContainer = (String)month.getSelectedItem();
				int dayContainer = (Integer)day.getSelectedItem();	
				
				int awayTeam = away.getSelectedIndex() + 1;
				int homeTeam = home.getSelectedIndex() + 1;
				
				retrievedGame = retrieveGame.executeQuery("Select * FROM games WHERE GameDay=" + dayContainer + " AND GameMonth='" + monthContainer + "' AND GameYear=" + yearContainer +
						" AND HomeTeamID=" + homeTeam + " AND AwayTeamID=" + awayTeam + ";" );
				
				while(retrievedGame.next())
				{	
					String s_homeGoals = retrievedGame.getString("HomeTeamGoals");
					String s_awayGoals = retrievedGame.getString("AwayTeamGoals");						
					boolean isShootout = retrievedGame.getBoolean("Shootout");
					boolean isOvertime = retrievedGame.getBoolean("Overtime");
					
					homeField.setText(s_homeGoals);
					awayField.setText(s_awayGoals);
					shootout.setSelected(isShootout);
					overtime.setSelected(isOvertime);
				}
			}
			catch(ClassNotFoundException ex)
			{
				System.out.println(ex.getMessage());
			}
			catch(SQLException ex)
			{
				System.out.println(ex.getMessage());
			}				
			finally
			{
				
				try
				{
					if( retrieveGame != null )
						retrieveGame.close();
					if( conn != null )
						conn.close();
				}
				catch(SQLException ex)
				{
					System.out.println(ex.getMessage());
				}
			}
	}
	
	public static boolean deleteGame(JComboBox year, JComboBox month, JComboBox day, JComboBox home, JComboBox away)
	{	
			Connection conn = null;
			Statement retrieveGame = null;
			Statement deleteGame = null;
			ResultSet retrievedGame = null;
			Statement queryHome = null;
			Statement queryAway = null;
			ResultSet queriedHome = null;
			ResultSet queriedAway = null;	
			Statement updateHome = null;
			Statement updateAway = null;
			
			int gameUpdated = 0;
			int homeUpdated = 0;
			int awayUpdated = 0;
			
			try
			{				
				Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");				
				
				conn = DriverManager.getConnection("jdbc:odbc:NHL2013db");				
				
				retrieveGame = conn.createStatement();
				deleteGame = conn.createStatement();
				queryHome = conn.createStatement();
				queryAway = conn.createStatement();
				updateHome = conn.createStatement();
				updateAway = conn.createStatement();
				
				//get interface details
				int yearContainer = (Integer)year.getSelectedItem();
				String monthContainer = (String)month.getSelectedItem();
				int dayContainer = (Integer)day.getSelectedItem();	
				int awayTeam = away.getSelectedIndex() + 1;
				int homeTeam = home.getSelectedIndex() + 1;
				
				//query home and away team standing details
				queriedHome = queryHome.executeQuery("Select GamesPlayed, Wins, Points, RegulationLosses, OvertimeLosses, ShootoutLosses FROM teams WHERE TeamID=" + homeTeam + ";");
				queriedAway = queryAway.executeQuery("Select GamesPlayed, Wins, Points, RegulationLosses, OvertimeLosses, ShootoutLosses FROM teams WHERE TeamID=" + awayTeam + ";");
				
				//retrieve the interface game details
				retrievedGame = retrieveGame.executeQuery("Select * FROM games WHERE GameDay=" + dayContainer + " AND GameMonth='" + monthContainer + "' AND GameYear=" + yearContainer +
						" AND HomeTeamID=" + (home.getSelectedIndex()+1) + " AND AwayTeamID=" + awayTeam + ";" );
				
				while(retrievedGame.next() && queriedHome.next() && queriedAway.next())
				{
					//get all of the details needed for queries
					String s_homeGoals = retrievedGame.getString("HomeTeamGoals");
					String s_awayGoals = retrievedGame.getString("AwayTeamGoals");
					int homeGoals = Integer.parseInt(s_homeGoals);
					int awayGoals = Integer.parseInt(s_awayGoals);
					boolean isShootout = retrievedGame.getBoolean("Shootout");
					boolean isOvertime = retrievedGame.getBoolean("Overtime");
					
					int homeGamesPlayed = queriedHome.getInt("GamesPlayed");
					int homeWins = queriedHome.getInt("Wins");
					int homePoints = queriedHome.getInt("Points");
					int homeRegLoss = queriedHome.getInt("RegulationLosses");
					int homeOvrLoss = queriedHome.getInt("OvertimeLosses");
					int homeShtLoss = queriedHome.getInt("ShootoutLosses");
					
					int awayGamesPlayed = queriedAway.getInt("GamesPlayed") ;
					int awayWins = queriedAway.getInt("Wins");
					int awayPoints = queriedAway.getInt("Points");
					int awayRegLoss = queriedAway.getInt("RegulationLosses");
					int awayOvrLoss = queriedAway.getInt("OvertimeLosses");
					int awayShtLoss = queriedAway.getInt("ShootoutLosses");
					
					gameUpdated = deleteGame.executeUpdate("Delete FROM games WHERE GameDay=" + dayContainer + " AND GameMonth='" + monthContainer + "' AND GameYear=" + yearContainer +
							" AND HomeTeamID=" + homeTeam + " AND AwayTeamID=" + awayTeam);
				
					if(homeGoals > awayGoals && !isOvertime && !isShootout)
					{
						homeUpdated = updateHome.executeUpdate("Update teams SET GamesPlayed=" + (homeGamesPlayed - 1) + ",Wins=" + (homeWins - 1) + ",Points=" + (homePoints - 2) +
								" WHERE TeamID=" + homeTeam + ";");
						
						awayUpdated = updateAway.executeUpdate("Update teams SET GamesPlayed=" + (awayGamesPlayed - 1) + ",RegulationLosses=" + (awayRegLoss - 1) +
								" WHERE TeamID=" + awayTeam + ";");
					}
					else if(homeGoals < awayGoals && !isOvertime && !isShootout)
					{
						awayUpdated = updateAway.executeUpdate("Update teams SET GamesPlayed=" + (awayGamesPlayed - 1) + ", Wins=" + (awayWins - 1) + ",Points=" + (awayPoints - 2) +
								" WHERE TeamID=" + awayTeam + ";");
						
						homeUpdated = updateHome.executeUpdate("Update teams SET GamesPlayed=" + (homeGamesPlayed - 1) + ",RegulationLosses=" + (homeRegLoss - 1) +
								" WHERE TeamID=" + homeTeam + ";");
					}
					else if(homeGoals > awayGoals && isOvertime && !isShootout)
					{
						homeUpdated = updateHome.executeUpdate("Update teams SET GamesPlayed=" + (homeGamesPlayed - 1) + ", Wins=" + (homeWins - 1) + ",Points=" + (homePoints - 2) +
								" WHERE TeamID=" + homeTeam + ";");
						
						awayUpdated = updateAway.executeUpdate("Update teams SET GamesPlayed=" + (awayGamesPlayed - 1) + ",OvertimeLosses=" + (awayOvrLoss - 1) + ",Points=" + (awayPoints - 1) +
								" WHERE TeamID=" + awayTeam + ";");
						
					}
					else if(homeGoals < awayGoals && isOvertime && !isShootout)
					{
						awayUpdated = updateAway.executeUpdate("Update teams SET GamesPlayed=" + (awayGamesPlayed - 1) + ", Wins=" + (awayWins - 1) + ",Points=" + (awayPoints - 2) +
								" WHERE TeamID=" + awayTeam + ";");
						
						homeUpdated = updateHome.executeUpdate("Update teams SET GamesPlayed=" + (homeGamesPlayed - 1) + ",OvertimeLosses=" + (homeOvrLoss - 1) + ",Points=" + (homePoints - 1) +
								" WHERE TeamID=" + homeTeam + ";");
					}
					else if(homeGoals > awayGoals && ((!isOvertime && isShootout) || (isOvertime && isShootout)))
					{
						homeUpdated = updateHome.executeUpdate("Update teams SET GamesPlayed=" + (homeGamesPlayed - 1) + ", Wins=" + (homeWins - 1) + ",Points=" + (homePoints - 2) +
								" WHERE TeamID=" + homeTeam + ";");
						
						awayUpdated = updateAway.executeUpdate("Update teams SET GamesPlayed=" + (awayGamesPlayed - 1) + ",ShootoutLosses=" + (awayShtLoss - 1) + ",Points=" + (awayPoints - 1) +
								" WHERE TeamID=" + awayTeam + ";");
						
					}
					else if(homeGoals < awayGoals && ((!isOvertime && isShootout) || (isOvertime && isShootout)))
					{
						awayUpdated = updateAway.executeUpdate("Update teams SET GamesPlayed=" + (awayGamesPlayed - 1) + ", Wins=" + (awayWins - 1) + ",Points=" + (awayPoints - 2) +
								" WHERE TeamID=" + awayTeam + ";");
						
						homeUpdated = updateHome.executeUpdate("Update teams SET GamesPlayed=" + (homeGamesPlayed - 1) + ",ShootoutLosses=" + (homeShtLoss - 1) + ",Points=" + (homePoints - 1) +
								" WHERE TeamID=" + homeTeam + ";");
					}		
				}
			}
			catch(ClassNotFoundException ex)
			{
				System.out.println(ex.getMessage());
			}
			catch(SQLException ex)
			{
				System.out.println(ex.getMessage());
			}	
			catch(Exception ex)
			{
				JOptionPane.showMessageDialog(null,"Y'all did something crazy!", "Error", JOptionPane.ERROR_MESSAGE);
			}
			finally
			{
				
				try
				{
					if( deleteGame != null && queryHome != null && queryAway != null && updateHome != null && updateAway != null )
					{
						deleteGame.close();
						queryHome.close();
						queryAway.close();
						updateHome.close();
						updateAway.close();
					}
					if( conn != null )
						conn.close();
				}
				catch(SQLException ex)
				{
					System.out.println(ex.getMessage());
				}
			}
			
			if(gameUpdated > 0 && homeUpdated > 0 && awayUpdated > 0)
			{
				return true;
			}
			else
			{
				return false;
			}			
	}	
	
	
	public static boolean isInteger(String s) {
	    try 
	    { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) 
	    {    
	    	JOptionPane.showMessageDialog(null,"Your score is in an improper format!", "Error", JOptionPane.ERROR_MESSAGE);	 
	    	return false;
	    }
	    return true;
	}
	
	public static void main(String[] args)
	{
		new Shell_NHLController();
	}

	public static Boolean Validation(JTextField homeScore, JTextField awayScore, JComboBox home, JComboBox away, JComboBox day)
	{
		int awayScoreInt;
		int homeScoreInt;
		boolean homeScoreIsInt = isInteger(homeScore.getText());
		boolean awayScoreIsInt = isInteger(awayScore.getText());
	
		if(homeScoreIsInt && awayScoreIsInt)
		{
			awayScoreInt = Integer.parseInt(awayScore.getText());
			homeScoreInt = Integer.parseInt(homeScore.getText());
		}
		else
		{
			return false;
		}
		
		if(day.getSelectedItem() == null || homeScore.getText() == null || awayScore.getText() == null)
		{
			JOptionPane.showMessageDialog(null,"Data is missing!", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		else if( awayScoreInt == homeScoreInt )
		{
			JOptionPane.showMessageDialog(null,"Score cannot be a tie!", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		else if(home.getSelectedIndex() == away.getSelectedIndex())
		{
			JOptionPane.showMessageDialog(null,"Home and away team cannot be the same!", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}	
		else if(awayScoreInt > 20 || homeScoreInt > 20)
		{
			JOptionPane.showMessageDialog(null,"The probability of an NHL team scoring more than 20 goals on another NHL" +
					" team in one game is outside the realm of possibility!", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	
		return true;
	}
	
	public static void yearClick(JComboBox year, JComboBox month, JComboBox day, int yearContainer, String monthContainer)
	{
		yearContainer = (Integer)year.getSelectedItem();					
		
		if(monthContainer == "February")
		{
			
			if((yearContainer % 4 == 0 && yearContainer % 100 !=0) || (yearContainer % 400 == 0) )
			{
				day.removeAllItems();
				
				for (int days = 1; days < 30; ++days) 
				 {							
					day.addItem(days);
				 }
			}
			else
			{
				day.removeAllItems();
				
				for (int days = 1; days < 29; ++days) 
				 {							
					day.addItem(days);
				 }
			}					
		}				
		month.setEnabled(true);
	}
	
	public static void monthClick(JComboBox month, JComboBox day, int yearContainer, String monthContainer)
	{
		monthContainer = (String)month.getSelectedItem();
		
		day.setEnabled(true);
		
		if(monthContainer == "January" || monthContainer == "March" || monthContainer == "May" || monthContainer == "July"
				|| monthContainer == "August" || monthContainer == "October" || monthContainer == "December")
		{
			day.removeAllItems();
			for (int days = 1; days < 32; ++days) 
			 {							
				day.addItem(days);
			 }
		}
		else if(monthContainer == "February")
		{
			if((yearContainer % 4 == 0 && yearContainer % 100 !=0) || (yearContainer % 400 == 0) )
			{
				day.removeAllItems();
				
				for (int days = 1; days < 30; ++days) 
				 {							
					day.addItem(days);
				 }
			}
			else
			{
				day.removeAllItems();
				
				for (int days = 1; days < 29; ++days) 
				 {							
					day.addItem(days);
				 }
			}					
		}
		else if(monthContainer == "April" || monthContainer == "June" || monthContainer == "September" || monthContainer == "November")
		{
			day.removeAllItems();
			
			for (int days = 1; days < 31; ++days) 
			 {							
				day.addItem(days);
			 }
		}
	}
	
	private class ButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == cmbYear)
			{		
				
				yearClick(cmbYear, cmbMonth, cmbDay, yearContainer, monthContainer);
				
			}
			else if(e.getSource() == cmbMonth)
			{
				
				monthClick(cmbMonth, cmbDay, yearContainer, monthContainer);
				
			}			
			else if(e.getSource() == addBtn)
			{
				
				boolean validated = Validation(fldHome, fldAway, cmbHome, cmbAway, cmbDay);
				
				if(!validated)
				{
					return;
				}
				
				int dialogButton = 0;
				dialogButton = JOptionPane.showConfirmDialog((Component) null, "Are you sure you'd like to add this game?","Alert", JOptionPane.YES_NO_OPTION);
				if(dialogButton == JOptionPane.YES_OPTION)
				{					
					boolean success = addGame(cmbYear, cmbMonth, cmbDay, cmbHome, cmbAway, fldHome, fldAway, chbOvertime, chbShootout);
					
					if(success)
						JOptionPane.showMessageDialog(null,"The game was added successfully!", "Information", JOptionPane.INFORMATION_MESSAGE);
					else
						JOptionPane.showMessageDialog(null,"Something went wrong!", "Error", JOptionPane.ERROR_MESSAGE);						
				}
			}
			else if(e.getSource() == btnClear)
			{	
				clearFields(cmbYear, cmbMonth, cmbDay, cmbHome, cmbAway, fldHome, fldAway, chbOvertime, chbShootout, btnClear);		
			}
			else if(e.getSource() == btnDelete)
			{	
				boolean validated = Validation(fldHome, fldAway, cmbHome, cmbAway, cmbDay);
				
				if(!validated)
				{
					return;
				}
				
				int dialogButton = 0;
				dialogButton = JOptionPane.showConfirmDialog((Component) null, "Are you sure you'd like to delete this game?","Alert", JOptionPane.YES_NO_OPTION);			
				
				if(dialogButton == JOptionPane.YES_OPTION)
				{										
					
					boolean success = deleteGame(cmbYear, cmbMonth, cmbDay, cmbHome, cmbAway);
					
					if(success)
						JOptionPane.showMessageDialog(null,"The game was deleted successfully!", "Information", JOptionPane.INFORMATION_MESSAGE);
					else
						JOptionPane.showMessageDialog(null,"Something went wrong!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			else if(e.getSource() == btnRetrieve)
			{	
				retrieveGame(cmbYear, cmbMonth, cmbDay, cmbHome, cmbAway, fldHome, fldAway, chbOvertime, chbShootout);
			}
			else if(e.getSource() == btnStandings)
			{	
				Standings_Controller stand = new Standings_Controller();
			}
			else if(e.getSource() == btnClose)
			{
				int dialogButton = 0;
				dialogButton = JOptionPane.showConfirmDialog((Component) null, "Are you sure you'd like to close?","Alert", JOptionPane.YES_NO_OPTION);
				
		        if( dialogButton == JOptionPane.YES_OPTION )
		           	Shell_NHLController.this.dispose();
		    }
			
				
		}//end actionPerformed()
	}//end inner class
	
}//end class Shell_NHLController