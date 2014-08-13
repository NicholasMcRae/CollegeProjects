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


//connect to library
using Stoichiometry;

namespace WpfApplication1
{
    /******************************************************
     * Author: Nick McRae
     * Date: January. 20th/2014
     * Purpose: Component class with interface used for different clients
     * Revision History
     * Jan. 20th - Initial Implementation
     * Jan. 25th - Implemented a few of the methods
     * Jan. 26th - Implemented the save and formulaList logic
     ******************************************************/
    public partial class MainWindow : Window
    {
        private IMolecule molecule = null;
        
        public MainWindow()
        {
            molecule = new Molecule();            
            InitializeComponent();
            
            foreach(String s in molecule.FormulasList)
            {
                formulaCombo.Items.Add(s);
            }
            
        }

        private void close_Click(object sender, RoutedEventArgs e)
        {
            System.Diagnostics.Process.Start("http://www.youtube.com/watch?v=v3Lc7OgIngE");
            this.Close();
        }

        private void normalize_Click(object sender, RoutedEventArgs e)
        {
            molecule.Formula = formulaCombo.Text;
            molecule.Normalize();
            formulaCombo.Text = "";
            formulaCombo.Text = molecule.Formula;
        }

        private void calculate_Click(object sender, RoutedEventArgs e)
        {  
            molecule.Formula = formulaCombo.Text;
            weight1.Text = Convert.ToString(molecule.MolecularWeight);
            molecule.Save();
        }
    }
}
