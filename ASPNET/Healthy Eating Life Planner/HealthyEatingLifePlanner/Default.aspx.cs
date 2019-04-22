using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using Newtonsoft.Json;
using System.Web.Services;
using File;
using FatSecret;
using System.Web.Script.Serialization;
using System.Security.Cryptography;
using System.Collections;
using System.IO;
using System.Net;
using System.Xml;
using System.Text;

/*
 * Name: Default.aspx.cs
 * Authors: Nick McRae
 *              Chris McManus
 *              Steve Pickering
 * Desc:    Default page client-server go-between code.
 */ 

namespace HealthyEatingLifePlanner
{
    public partial class Default: System.Web.UI.Page
    {
        //Not using
        protected void Page_Load( object sender, EventArgs e )
        {

        }

        //Desc:     saves client's user data to file
        [WebMethod]
        public static string SaveClientData( string request )
        {
            string response = "Successfully saved";
            FileIO file = new FileIO();

            if( !file.saveToFile( request ) )
                response = "Failed to save";

            return response;
        }

        //Desc:     reads client's user data from file
        [WebMethod]
        public static string ReadClientData( string request )
        {
            FileIO file = new FileIO();
            return file.readFromFile();
        }        
    }
}