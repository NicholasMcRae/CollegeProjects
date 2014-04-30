using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.IO;

/********************************************
 * Author: Nick McRae
 * Date: August 5th, 2013
 * Purpose: The logic contained in this file allows a user to keep track of their friends 
 ********************************************/


namespace GUI_Project_3
{    
    public partial class MainWindow : Window
    {
        //hard-coded information arrays
        int[] yearContainer;
        string[] monthContainer;
        int[] monthDayContainer;        

        //birthday variables used for carrying out combobox logic
        string monthSelection;
        string yearSelection;
        int yearInteger;

        Friend friendsTreeItem = new Friend(); 
        
        public MainWindow()
        {
            InitializeComponent();

            CenterWindowOnScreen();

            //loading the year combobox
            yearContainer = new int[110];
            for (int i = 0; i < 110; ++i)
            {
                yearContainer[i] = (DateTime.Now.Year - i);
            }

            year.ItemsSource = yearContainer;

            //loading the month combo box
            monthContainer = new string[] {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", 
                                               "Sep", "Oct", "Nov", "Dec"};
            month.ItemsSource = monthContainer;

            //birth day combo boxes will be enabled sequentially due to internal logic
            day.IsEnabled = false;
            month.IsEnabled = false;

            loadTreeView( "last" );            
        }

        private void save_Click(object sender, RoutedEventArgs e)
        {
            Friend selected = (Friend)friends.SelectedValue;
            Friend friend = new Friend();

            if (selected.Header == "Friends")
            {
                MessageBox.Show("Please select a friend in the tree view before saving.", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
                return;
            }            
            
            if (MessageBox.Show("Are you sure you'd like to update " + selected.First + " " + selected.Last + " with the data in the interface?",
                   "Confirm Save Friend", MessageBoxButton.YesNo, MessageBoxImage.Warning)
                   == MessageBoxResult.Yes)
            {                               
                friend.First = firstName.Text;
                friend.Last = lastName.Text;

                if (unknown.IsChecked == true)
                {
                    friend.BirthYear = 1800;
                    friend.BirthMonth = "Jan";
                    friend.BirthDay = 1;
                }
                else
                {
                    friend.BirthYear = Convert.ToInt32(year.Text);
                    friend.BirthMonth = month.Text;
                    friend.BirthDay = Convert.ToInt32(day.Text);
                }

                if (religion.Text != null && religion.Text != "")
                {
                    friend.Religion = religion.Text;
                }
                else
                {
                    friend.Religion = "None";
                }

                friend.Rating = ratingSlider.Value;
                friend.HowMet = howWeMet.Text;
                friend.Details = details.Text;
                friend.FunnyStory = story.Text;
                friend.BornIn = bornIn.Text;
                friend.LivesIn = livesIn.Text;
                friend.MetIn = met.Text;

                if (friend.First != "" && friend.First != null && friend.Last != "" && friend.Last != null && friend.Details != null && friend.Details != ""
                    && friend.LivesIn != "" && friend.LivesIn != null)
                {

                    friend.Header = friend.First + " " + friend.Last;
                    friendsTreeItem.Items.Add(friend);
                }
                else
                {
                    MessageBox.Show("Some of your data is not filled in", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
                    return;
                }
            }

            Friend friendContainer = (Friend)friends.SelectedValue;

            int index = -1;
            index = friendsTreeItem.Items.IndexOf(friends.SelectedValue);
            friendsTreeItem.Items.RemoveAt(index);

            MessageBox.Show(friend.First + " " + friend.Last + " has been successfully updated!", "Success!", MessageBoxButton.OK, MessageBoxImage.Information);
        }

        private void saveAll_Click(object sender, RoutedEventArgs e)
        {
            if (MessageBox.Show("Are you sure you'd like to save all?",
                  "Confirm Save to File", MessageBoxButton.YesNo, MessageBoxImage.Warning)
                  == MessageBoxResult.Yes)
            {
                writeToFile();

                friends.Items.Clear();
                friendsTreeItem.Items.Clear();

                loadTreeView("last");
            }
        }

        private void addNew_Click(object sender, RoutedEventArgs e)
        {
           Friend friend = new Friend();
            
            if (MessageBox.Show("Are you sure you'd like to add a new friend?",
                  "Confirm Exit", MessageBoxButton.YesNo, MessageBoxImage.Warning)
                  == MessageBoxResult.Yes)
            {                
                friend.First = firstName.Text;
                friend.Last = lastName.Text;

                if (unknown.IsChecked == true)
                {
                    friend.BirthYear = 1800;
                    friend.BirthMonth = "Jan";
                    friend.BirthDay = 1;
                }
                else
                {
                    friend.BirthYear = Convert.ToInt32(year.Text);
                    friend.BirthMonth = month.Text;
                    friend.BirthDay = Convert.ToInt32(day.Text);
                }

                if (religion.Text != null && religion.Text != "")
                {
                    friend.Religion = religion.Text;
                }
                else
                {
                    friend.Religion = "None Applied";
                }

                friend.Rating = ratingSlider.Value;
                friend.HowMet = howWeMet.Text;
                friend.Details = details.Text;
                friend.FunnyStory = story.Text;
                friend.BornIn = bornIn.Text;
                friend.LivesIn = livesIn.Text;
                friend.MetIn = met.Text;

                if (friend.First != "" && friend.First != null && friend.Last != "" && friend.Last != null && friend.Details != null && friend.Details != ""
                    && friend.LivesIn != "" && friend.LivesIn != null)
                {
                    friend.Header = friend.First + " " + friend.Last;
                    friendsTreeItem.Items.Add(friend);
                }
                else
                {
                    MessageBox.Show("Some of your data is not filled in", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
                    return;
                }
            }
            MessageBox.Show(friend.First + " " + friend.Last + " has been successfully added!", "Success!", MessageBoxButton.OK, MessageBoxImage.Information);
        }

        private void clear_Click(object sender, RoutedEventArgs e)
        {
            if (MessageBox.Show("Are you sure you'd like to clear the form?",
                  "Confirm Clear Form", MessageBoxButton.YesNo, MessageBoxImage.Warning)
                  == MessageBoxResult.Yes)
            {
                firstName.Text = "";
                lastName.Text = "";
                year.SelectedIndex = 0;
                month.SelectedIndex = 0;
                day.SelectedIndex = 0;
                bornIn.Text = "";
                livesIn.Text = "";
                met.Text = "";
                howWeMet.Text = "";
                details.Text = "";
                story.Text = "";
                religion.Text = "";
                ratingSlider.Value = 0.0;
            }
        }

        private void close_Click(object sender, RoutedEventArgs e)
        {
            MessageBoxResult result = MessageBox.Show("Would you like to save before closing?", "Confirm Exit", MessageBoxButton.YesNoCancel, MessageBoxImage.Warning);

            if (result == MessageBoxResult.Yes)
            {
                writeToFile();
                this.Close();
            }
            else if (result == MessageBoxResult.No)
            {
                this.Close();
                return;
            }
            
        }

        private void sortByName_Click(object sender, RoutedEventArgs e)
        {
            if (MessageBox.Show("Are you sure you'd like to sort by last name?",
                 "Sort by Last Name", MessageBoxButton.YesNo, MessageBoxImage.Warning)
                 == MessageBoxResult.Yes)
            {
                writeToFile();

                friends.Items.Clear();
                friendsTreeItem.Items.Clear();

                loadTreeView("last");
            }
        }

        private void sortByBirth_Click(object sender, RoutedEventArgs e)
        {
            if (MessageBox.Show("Are you sure you'd like to sort by birth year?",
                  "Sort by Birth Year", MessageBoxButton.YesNo, MessageBoxImage.Warning)
                  == MessageBoxResult.Yes)
            {
                writeToFile();

                friends.Items.Clear();
                friendsTreeItem.Items.Clear();

                loadTreeView("birth");
            }
        }

        private void sortByBornIn_Click(object sender, RoutedEventArgs e)
        {
            if (MessageBox.Show("Are you sure you'd like to sort by where friends were born?",
                 "Sort by Birth Location", MessageBoxButton.YesNo, MessageBoxImage.Warning)
                 == MessageBoxResult.Yes)
            {
                writeToFile();

                friends.Items.Clear();
                friendsTreeItem.Items.Clear();

                loadTreeView("bornIn");
            }
        }

        private void sortByLocation_Click(object sender, RoutedEventArgs e)
        {
            if (MessageBox.Show("Are you sure you'd like to sort by the friends location?",
                 "Sort by Lovation", MessageBoxButton.YesNo, MessageBoxImage.Warning)
                 == MessageBoxResult.Yes)
            {
                writeToFile();

                friends.Items.Clear();
                friendsTreeItem.Items.Clear();

                loadTreeView("lives");
            }
        }

        private void sortByMetLocation_Click(object sender, RoutedEventArgs e)
        {
            if (MessageBox.Show("Are you sure you'd like to sort by where you met?",
                 "Sort by Meeting Location", MessageBoxButton.YesNo, MessageBoxImage.Warning)
                 == MessageBoxResult.Yes)
            {
                writeToFile();

                friends.Items.Clear();
                friendsTreeItem.Items.Clear();

                loadTreeView("met");
            }
        }

        private void sortByReligion_Click(object sender, RoutedEventArgs e)
        {
            if (MessageBox.Show("Are you sure you'd like to sort by religion?",
                 "Sort by Religion", MessageBoxButton.YesNo, MessageBoxImage.Warning)
                 == MessageBoxResult.Yes)
            {
                writeToFile();

                friends.Items.Clear();
                friendsTreeItem.Items.Clear();

                loadTreeView("religion");
            }
        }

        private void sortByRating_Click(object sender, RoutedEventArgs e)
        {
            if (MessageBox.Show("Are you sure you'd like to sort by rating?",
                 "Sort by Rating", MessageBoxButton.YesNo, MessageBoxImage.Warning)
                 == MessageBoxResult.Yes)
            {
                writeToFile();

                friends.Items.Clear();
                friendsTreeItem.Items.Clear();

                loadTreeView("rating");
            }
        }

        private void friends_SelectedItemChanged(object sender, RoutedPropertyChangedEventArgs<object> e)
        {             
            if (friends.SelectedItem != null)
            {
                TreeViewItem item = (TreeViewItem)friends.SelectedItem;

                if (item.Header == "Friends")
                    return;

                Friend friendObj = (Friend)friends.SelectedItem;                

                firstName.Text = friendObj.First;
                lastName.Text = friendObj.Last;

                if (friendObj.BirthYear == 1800)
                {
                    year.SelectedValue = 1960;
                    month.SelectedIndex = 0;
                    day.SelectedValue = 1;
                }
                else
                {
                    year.SelectedValue = friendObj.BirthYear;
                    month.SelectedValue = friendObj.BirthMonth;
                    day.SelectedValue = friendObj.BirthDay;
                }
                bornIn.Text = friendObj.BornIn;
                livesIn.Text = friendObj.LivesIn;
                met.Text = friendObj.MetIn;
                howWeMet.Text = friendObj.HowMet;
                details.Text = friendObj.Details;
                story.Text = friendObj.FunnyStory;
                ratingSlider.Value = friendObj.Rating;
                religion.Text = friendObj.Religion;
            }//end outer if
        }

        private void month_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            monthSelection = month.SelectedItem.ToString();
            yearSelection = year.SelectedItem.ToString();
            yearInteger = Convert.ToInt32(yearSelection);

            if (monthSelection == "Jan" || monthSelection == "Mar" || monthSelection == "May" || monthSelection == "Jul"
                    || monthSelection == "Aug" || monthSelection == "Oct" || monthSelection == "Dec")
            {
                monthDayContainer = new int[31];

                for (int i = 1; i < 32; ++i)
                {
                    monthDayContainer[i - 1] = i;
                }
            }
            else if (monthSelection == "Feb")
            {
                if ((yearInteger % 4 == 0 && yearInteger % 100 != 0) || (yearInteger % 400 == 0))
                {
                    monthDayContainer = new int[29];

                    for (int i = 1; i < 30; ++i)
                    {
                        monthDayContainer[i - 1] = i;
                    }
                }
                else
                {
                    monthDayContainer = new int[28];

                    for (int i = 1; i < 29; ++i)
                    {
                        monthDayContainer[i - 1] = i;
                    }
                }

            }
            else if (monthSelection == "Apr" || monthSelection == "Jun" || monthSelection == "Sep" || monthSelection == "Nov")
            {
                monthDayContainer = new int[30];

                for (int i = 1; i < 31; ++i)
                {
                    monthDayContainer[i - 1] = i;
                }
            }

            day.IsEnabled = true;
            day.ItemsSource = monthDayContainer;
        }

        private void year_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            yearSelection = year.SelectedItem.ToString();
            yearInteger = Convert.ToInt32(yearSelection);

            if (monthSelection == "Feb")
            {
                if ((yearInteger % 4 == 0 && yearInteger % 100 != 0) || (yearInteger % 400 == 0))
                {
                    monthDayContainer = new int[29];

                    for (int i = 1; i < 30; ++i)
                    {
                        monthDayContainer[i - 1] = i;
                    }
                }
                else
                {
                    monthDayContainer = new int[28];

                    for (int i = 1; i < 29; ++i)
                    {
                        monthDayContainer[i - 1] = i;
                    }
                }

                day.ItemsSource = monthDayContainer;
            }
            month.IsEnabled = true;
        }

        //logic that loads treeview from file, highly contingent on data being in proper format
        private void loadTreeView(string sortBy)
        {
            int counter = 0;
            string line;
            List<Friend> friendList = new List<Friend>();            

            friendsTreeItem.Header = "Friends";
            friends.Items.Add(friendsTreeItem);

            // Read the file and display it line by line.
            try
            {                
                FileStream fs = new FileStream("ListOfFriends.txt", FileMode.OpenOrCreate);
                StreamReader s = new StreamReader(fs);

            //stopping variable for outer while
            bool breakWhile = false;

            //create new task object for each iteration of inner while loop
            while (!breakWhile)
            {
                Friend friend = new Friend();
                
                while ((line = s.ReadLine()) != null)
                {
                    if (counter == 0 && line == "")
                    {
                        break;
                    }
                    
                    if (counter == 0)
                    {
                        friend.First = line;
                    }
                    else if (counter == 1)
                    {
                        friend.Last = line;
                    }
                    else if (counter == 2)
                    {
                        friend.BirthYear = Convert.ToInt32(line);
                    }
                    else if (counter == 3)
                    {
                        friend.BirthMonth = line;
                    }
                    else if (counter == 4)
                    {
                        friend.BirthDay = Convert.ToInt32(line);
                    }
                    else if (counter == 5)
                    {
                        friend.BornIn = line;
                    }
                    else if (counter == 6)
                    {
                        friend.LivesIn = line;
                    }
                    else if (counter == 7)
                    {
                        friend.MetIn = line;
                    }
                    else if (counter == 8)
                    {
                        friend.HowMet = line;
                    }
                    else if (counter == 9)
                    {
                        friend.Details = line;
                    }
                    else if (counter == 10)
                    {
                        friend.FunnyStory = line;
                    }
                    else if (counter == 11)
                    {
                        friend.Rating = Convert.ToDouble(line);
                    }
                    else if (counter == 12)
                    {
                        friend.Religion = line;
                    }
                    counter++;

                    if (line == "")
                    {
                        friendList.Add(friend);
                        counter = 0;
                        break;
                    }

                }//end inner while

                if (line == null && counter == 0)
                {
                    breakWhile = true;
                }
                else  if (line == null && counter == 12)
                {
                    breakWhile = true;
                    friendList.Add(friend);
                }

            }//end outer while

            s.Close();
            fs.Close();


            if (friendList.Count != 0)
            {
                if (sortBy == "last")
                    friendList.Sort((Friend a, Friend b) => a.Last.CompareTo(b.Last));
                else if (sortBy == "birth")
                    friendList.Sort((Friend a, Friend b) => a.BirthYear.CompareTo(b.BirthYear));
                else if (sortBy == "bornIn")
                    friendList.Sort((Friend a, Friend b) => a.BornIn.CompareTo(b.BornIn));
                else if (sortBy == "lives")
                    friendList.Sort((Friend a, Friend b) => a.LivesIn.CompareTo(b.LivesIn));
                else if (sortBy == "met")
                    friendList.Sort((Friend a, Friend b) => a.MetIn.CompareTo(b.MetIn));
                else if (sortBy == "religion")
                    friendList.Sort((Friend a, Friend b) => a.Religion.CompareTo(b.Religion));
                else if (sortBy == "rating")
                    friendList.Sort((Friend a, Friend b) => a.Rating.CompareTo(b.Rating));
            }

            //placing objects in list into appropriate treeview sections
            for (int i = 0; i < friendList.Count(); i++)
            {
                friendList[i].Header = friendList[i].First + " " + friendList[i].Last;
                friendsTreeItem.Items.Add(friendList[i]);  
            }//end for loop

            
            }
            catch 
            {
                
            }

            // Suspend the screen.
            Console.ReadLine();

        }

        //void function that handles writing the Tree View to file, broke it out as it's used in two different places
        private void writeToFile()
        {          

            try
            {
                System.IO.File.WriteAllText("ListOfFriends.txt", string.Empty);
                FileStream fs = new FileStream("ListOfFriends.txt", FileMode.OpenOrCreate);
                StreamWriter s = new StreamWriter(fs);

                for (int i = 0; i < friendsTreeItem.Items.Count; i++)
                {
                    Friend friendObj = (Friend)friendsTreeItem.Items[i];

                    s.WriteLine(friendObj.First);
                    s.WriteLine(friendObj.Last);
                    s.WriteLine(friendObj.BirthYear.ToString());
                    s.WriteLine(friendObj.BirthMonth);
                    s.WriteLine(friendObj.BirthDay.ToString());
                    s.WriteLine(friendObj.BornIn);
                    s.WriteLine(friendObj.LivesIn);
                    s.WriteLine(friendObj.MetIn);
                    s.WriteLine(friendObj.HowMet);
                    s.WriteLine(friendObj.Details);
                    s.WriteLine(friendObj.FunnyStory);
                    s.WriteLine(friendObj.Rating);
                    s.WriteLine(friendObj.Religion);
                    s.WriteLine("");
                }                

                s.Close();
                fs.Close();

            }//end try
            catch
            {
                MessageBox.Show("Write to file failed", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
                return;
            }            
        }

        private void delete_Click(object sender, RoutedEventArgs e)
        {
            Friend friendContainer = (Friend)friends.SelectedValue;
            string firstName;
            string lastName;

            if (friendContainer.Header == "Friends")
            {
                MessageBox.Show("You must have a friend selected to use the delete function", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
                return;
            }
            
                if (MessageBox.Show("Are you sure you'd like to delete this friend?",
                 "Delete Friend", MessageBoxButton.YesNo, MessageBoxImage.Warning)
                 == MessageBoxResult.Yes)
                {
                    try
                    {
                        firstName = friendContainer.First;
                        lastName = friendContainer.Last;

                        int index = -1;
                        index = friendsTreeItem.Items.IndexOf(friends.SelectedValue);
                        friendsTreeItem.Items.RemoveAt(index);                        
                    }

                    catch
                    {
                        MessageBox.Show("The task was not deleted", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
                        return;
                    }            

                    MessageBox.Show(firstName + " " + lastName + " was deleted successfully!", "Success!", MessageBoxButton.OK, MessageBoxImage.Information);
                }
            }

        //void method that centers the window on the screen
        private void CenterWindowOnScreen()
        {
            double screenWidth = System.Windows.SystemParameters.PrimaryScreenWidth;
            double screenHeight = System.Windows.SystemParameters.PrimaryScreenHeight;
            double windowWidth = this.Width;
            double windowHeight = this.Height;
            this.Left = (screenWidth / 2) - (windowWidth / 2);
            this.Top = (screenHeight / 2) - (windowHeight / 2);
        }

        private void Window_Closing_1(object sender, System.ComponentModel.CancelEventArgs e)
        {
            MessageBoxResult result = MessageBox.Show("Would you like to save before closing?", "Exit Confirmation", MessageBoxButton.YesNoCancel, MessageBoxImage.Warning);

            if (result == MessageBoxResult.Yes)
            {
                writeToFile();
                return;
            }
            else if (result == MessageBoxResult.No)
            {
                return;
            }
            else
            {
                e.Cancel = true;
            }
        }
        

    }//end class
}// end namespace
