using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Data;
using System.Data.OleDb;
using System.Windows;
using System.Text.RegularExpressions;
using System.Windows.Forms;

namespace Stoichiometry
{   
    
    public interface IMolecule
    {
        string Formula { get; set; }
        bool Valid();
        double MolecularWeight { get; }
        void Normalize();
        bool Save();
        string[] FormulasList { get; }        
    }
    
    /******************************************************
     * Author: Nick McRae
     * Date: January. 25th/2014
     * Purpose: Component class with interface used for different clients
     * Revision History
     * Jan. 25th - Initial Implementation
     * Jan. 26th - Final touches 
     ******************************************************/

    public class Molecule : IMolecule
    {
        // Data-related member variables 
        private DataSet _ds;
        private OleDbDataAdapter _adElements, _adMolecules;

        // Container storage for database information
        Dictionary<string, double> elementData = new Dictionary<string,double>();
        List<string> moleculeData = new List<string>();
        
        public string formula;
        public double molecularWeight;
        public string[] formulasList;

        //We get information we need from the database upon class construction
        public Molecule()
        {
            initializeElements();
        }
        
        //Pass the combo-box formula to the object that's in context and allow it to be set as well
        public string Formula
        {
            get { return formula; }
            set
            {
                if (formula != value)
                {
                    formula = value;                    
                }
            }
        }

        //Validation function that checks to see if the formula is in a proper format, and only contains valid elements
        public bool Valid()
        {
            String formula = Formula;
            Regex rgx = new Regex(@"^[A-Z][a-z]?\d*");
            List<String> elements = separateElements(formula);
            Boolean matchFound;

            if (rgx.IsMatch(formula))
            {
                foreach (String element in elements)
                {
                    matchFound = false;
                    
                    foreach (String symbol in elementData.Keys)
                    {
                        if (symbol == element)
                        {
                            matchFound = true;
                        }
                    }

                    if (matchFound == false)
                    {
                        return false;
                    }
                }

                return true;
            }

            return false;
        }

        //Calculates and returns the molecular weight of the input formula
        public double MolecularWeight
        {   
            get 
            {
                molecularWeight = 0.0;
                
                if(!Valid())
                {
                    MessageBox.Show("Validation Error!");
                    return molecularWeight = 0.0;
                }
                
                List<Tuple<String, int>> mappedFormula = mapFormula(Formula);

                for (int i = 0; i < mappedFormula.Count; ++i )
                {
                    foreach (String st in elementData.Keys)
                    {
                        if (mappedFormula[i].Item1 == st)
                        {
                            molecularWeight += (mappedFormula[i].Item2 * elementData[st]);
                        }
                    }
                }
                
                return molecularWeight; 
            }
        }

        //Normalizes the formula
        public void Normalize()
        {
            List<Tuple<String, int>> mappedFormula = mapFormula(Formula);
            formula = "";

            for (int i = 0; i < mappedFormula.Count; ++i)
            {
                String element = mappedFormula[i].Item1;
                int coefficient1 = mappedFormula[i].Item2;
                int newCoefficient = 0;
                List<int> toRemove = new List<int>();
                newCoefficient += coefficient1;

                for (int j = i+1; j < mappedFormula.Count; ++j)
                {
                    if(mappedFormula[i].Item1 == mappedFormula[j].Item1)
                    {                           
                        int coefficient2 = mappedFormula[j].Item2;
                        newCoefficient += coefficient2;
                        toRemove.Add(j);
                    }
                }
                mappedFormula[i] = new Tuple<String, int>(element, newCoefficient);

                for (int k = 0; k < toRemove.Count; ++k )
                {
                    mappedFormula.RemoveAt(toRemove[k]);

                    for (int l = 0; l < toRemove.Count; ++l )
                    {
                        toRemove[l] -= 1;
                    }
                }
            }

            for (int i = 0; i < mappedFormula.Count; ++i )
            {
                formula += mappedFormula[i].Item1;

                if (mappedFormula[i].Item2 != 1)
                {
                    formula += Convert.ToString(mappedFormula[i].Item2);
                }
            }
            
        }

        //Saves the formula to the database if it is valid and the user opts to do so
        public bool Save()
        {
            if (molecularWeight == 0.0)
            {
                return false;
            }
            
            DialogResult dr = MessageBox.Show("Do you want to save the formula?", "", MessageBoxButtons.YesNo, MessageBoxIcon.Warning);

            if (dr == DialogResult.Yes)
            {
                try
                {
                    OleDbConnection conn = new OleDbConnection("Provider=Microsoft.ACE.OLEDB.12.0;Data Source=Stoichiometry.accdb");
                    conn.Open();

                    string insert = "INSERT INTO Molecules(Formula,MolecularWeight) VALUES ('" + formula + "', " + molecularWeight + ")";

                    OleDbCommand command = new OleDbCommand(insert, conn);

                    command.ExecuteNonQuery();
                    conn.Close();
                }
                catch (Exception ex)
                {
                    MessageBox.Show("Database Error!: " + ex.Message);
                }

                return true;
            }

            return false;
        }

        //Gets and returns all of the formulas from the database for use in an array, I think the array was a bad choice here,
        //should be a list but I guessed we were supposed to keep the interface the same so I wrote some bad code.
        public string[] FormulasList
        {
            get 
            {
                try
                {
                    // Create a connection object for the Stoichiometry database
                    OleDbConnection conn = new OleDbConnection("Provider=Microsoft.ACE.OLEDB.12.0;Data Source=Stoichiometry.accdb");

                    // Create data adapter for Molecules table
                    // This table will be used in both read and write modes
                    _adMolecules = new OleDbDataAdapter();
                    _adMolecules.SelectCommand = new OleDbCommand("SELECT Formula FROM Molecules", conn);


                    // Create and fill the data set and close the connection 
                    _ds = new DataSet();
                    conn.Open();
                    _adMolecules.Fill(_ds, "Molecules");
                    conn.Close();
                }
                catch (Exception ex)
                {
                    MessageBox.Show("Database Error!: " + ex.Message);
                }

                formulasList = new string[2000];

                int i = 0;
                
                foreach (DataRow row in _ds.Tables["Molecules"].Rows)
                {
                    formulasList[i] = (row.Field<String>("Formula"));
                    i++;
                }
                return formulasList; 
            }
        }

        //Gets data from the database and fills a few class properties 
        private void initializeElements()
        {
            try
            {
                // Create a connection object for the Stoichiometry database
                OleDbConnection conn = new OleDbConnection("Provider=Microsoft.ACE.OLEDB.12.0;Data Source=Stoichiometry.accdb");
                // Create data adapter for Elements table
                // This table will be used in read-only mode 
                _adElements = new OleDbDataAdapter();
                _adElements.SelectCommand = new OleDbCommand("SELECT * FROM Elements", conn);

                // Create data adapter for Molecules table
                // This table will be used in both read and write modes
                _adMolecules = new OleDbDataAdapter();
                _adMolecules.SelectCommand = new OleDbCommand("SELECT Formula FROM Molecules", conn);
                _adMolecules.InsertCommand = new OleDbCommand("INSERT INTO Molecules (Formula) VALUES(@Formula)", conn);
                // Create a parameter variable for the insert command (note that this code could easily
                // be modified to initialize the table's MolecularWeight column as well)
                _adMolecules.InsertCommand.Parameters.Add("@Formula", OleDbType.VarChar, -1, "Formula");

                // Create and fill the data set and close the connection 
                _ds = new DataSet();
                conn.Open();
                _adElements.Fill(_ds, "Elements");
                _adMolecules.Fill(_ds, "Molecules");
                conn.Close();

                // Populate the Elements list
                foreach (DataRow row in _ds.Tables["Elements"].Rows)
                {
                    elementData.Add(row.Field<string>("Symbol"), row.Field<double>("AtomicWeight"));
                }

                // Populate the Molecules list
                foreach (DataRow row in _ds.Tables["Molecules"].Rows)
                {
                    moleculeData.Add(row.Field<String>("Formula"));
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Database Error!: " + ex.Message);
            }
        }

        //Separates and organizes formula elements into a list
        public List<String> separateElements(String formula)
        {
            List<int> capitals = new List<int>();
            List<string> elements = new List<String>();
            String withoutCoefficients = formula;

            withoutCoefficients = Regex.Replace(formula, @"\d", "");

            for (int i = 0; i < withoutCoefficients.Length; i++)
            {
                if (char.IsUpper(withoutCoefficients[i]))
                {
                    capitals.Add(i); 
                }
            }

            for (int i = 0; i < capitals.Count; i++)
            {
                String element;

                if (i == capitals.Count - 1)
                {
                    element = withoutCoefficients.Substring(capitals[i], withoutCoefficients.Length - capitals[i]);
                    elements.Add(element);
                }
                else
                {
                    element = withoutCoefficients.Substring(capitals[i], (capitals[i+1] - capitals[i]));
                    elements.Add(element);
                }
            }

            return elements;
        }

        //separates and organizes coefficients into a list
        public List<int> getCoefficients(string formula)
        {
            List<int> coefficients = new List<int>();
            
            for (int i = 0; i < formula.Length; ++i)
            {
                if (i-1 == -1)
                {
                    continue;
                }
                
                if(Char.IsUpper(formula[i-1]) && Char.IsUpper(formula[i]))
                {
                    coefficients.Add(1);
                }
                else if(Char.IsUpper(formula[i-1]) && Char.IsLower(formula[i]))
                {
                    continue;
                }
                else if (Char.IsLower(formula[i-1]) && Char.IsLower(formula[i]))
                {
                    continue;
                }
                else if (Char.IsLower(formula[i - 1]) && Char.IsUpper(formula[i]))
                {
                    coefficients.Add(1);
                }
                else if (Char.IsLower(formula[i - 1]) && Char.IsDigit(formula[i]))
                {
                    int count = 0;

                    for (int j = i; j < formula.Length; ++j)
                    {
                        if (Char.IsLetter(formula[j]))
                        {
                            coefficients.Add(Convert.ToInt32(formula.Substring(i, count)));
                            break;
                        }
                        else if (j + 1 == formula.Length)
                        {
                            count++;
                            coefficients.Add(Convert.ToInt32(formula.Substring(i, count)));
                        }
                        count++;
                    }
                }
                else if (Char.IsUpper(formula[i - 1]) && Char.IsDigit(formula[i]))
                {
                    int count = 0;

                    for (int j = i; j < formula.Length; ++j)
                    {
                        if (Char.IsLetter(formula[j]))
                        {
                            coefficients.Add(Convert.ToInt32(formula.Substring(i, count)));
                            break;
                        }
                        else if (j + 1 == formula.Length)
                        {
                            count++;
                            coefficients.Add(Convert.ToInt32(formula.Substring(i, count)));
                        }

                        if ((j + 1) != formula.Length)
                            count++;
                    }
                }
                else if (i == formula.Length - 1)
                {
                    if (Char.IsUpper(formula[i]))
                    {
                        coefficients.Add(1);
                    }
                    else
                    {
                        coefficients.Add(Convert.ToInt32(formula.Substring(i, 1)));
                    }
                }
            }

            if (Char.IsLetter(formula[formula.Length - 1]))
            {
                coefficients.Add(1);
            }
            else if (Char.IsDigit(formula[formula.Length - 1]))
            {
                coefficients.Add(Convert.ToInt32(formula.Substring((formula.Length - 1), 1)));
            }

            return coefficients;
        }

        //places the elements and coefficients into a list of tuples to be used for molecular weight calculation
        public List<Tuple<string, int>> mapFormula(String moleculeFormula)
        {
            List<String> elements = separateElements(moleculeFormula);
            List<int> numbers = getCoefficients(moleculeFormula);
            List<Tuple<string, int>> mappedFormula = new List<Tuple<string,int>>();

            for (int i = 0; i < elements.Count; ++i)
            {
                Tuple<string, int> tup = new Tuple<string, int>(elements[i], numbers[i]);
                mappedFormula.Add(tup);
            }

            return mappedFormula;
        }

    }
}
