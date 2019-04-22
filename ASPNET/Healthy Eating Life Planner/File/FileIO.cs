using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.IO;
using System.Web.Hosting;

/*
 * Name: FileIO.cs
 * Authors: Nick McRae
 *              Chris McManus
 *              Steve Pickering
 * Desc:    Class designed to interact with file system
 */ 


namespace File
{
    public class FileIO
    {
        /// <summary>
        /// Saves user's data to profile file
        /// </summary>
        /// <param name="data">The profile data to be written</param>
        /// <returns>a boolean flag indicating success or failure of the operation</returns>
        public bool saveToFile( string data )
        {
            StreamWriter sWrite = new StreamWriter( HostingEnvironment.MapPath( "~/data/profile.txt" ) );
            try
            {
                sWrite.WriteLine( data );
                sWrite.Close();
            }
            catch( Exception )
            {
                return false;
            }
            return true;
        }

        /// <summary>
        /// Reads user's profile from the file
        /// </summary>
        /// <returns>a string containing the file's data (should be in json format)</returns>
        public string readFromFile()
        {
            string ret = "";
            StreamReader sRead = null;
            try
            {
                sRead = new StreamReader( HostingEnvironment.MapPath( "~/data/profile.txt" ) );
                ret = sRead.ReadLine();
                sRead.Close();
            }
            catch( Exception )
            {
                return "";
            }
            return ret;
        }

        /// <summary>
        /// Saves user's food selections in the application to a file
        /// </summary>
        /// <param name="data">a json string containing all of the data</param>
        /// <returns>a boolean flag indicating success or failure of the operation</returns>
        public bool saveChoicesToFile(string data)
        {
            StreamWriter sWrite = new StreamWriter(HostingEnvironment.MapPath("~/data/choices.txt"));
            try
            {
                sWrite.WriteLine(data);
                sWrite.Close();
            }
            catch (Exception)
            {
                return false;
            }
            return true;
        }

        /// <summary>
        /// Reads the user's selection data from a file
        /// </summary>
        /// <returns>returns a json string containing all of the selections the user has previously made</returns>
        public string readChoicesFromFile()
        {
            string ret = "";
            StreamReader sRead = null;
            try
            {
                sRead = new StreamReader(HostingEnvironment.MapPath("~/data/choices.txt"));
                ret = sRead.ReadLine();
                sRead.Close();
            }
            catch (Exception)
            {
                return "";
            }
            return ret;
        }
    }
}