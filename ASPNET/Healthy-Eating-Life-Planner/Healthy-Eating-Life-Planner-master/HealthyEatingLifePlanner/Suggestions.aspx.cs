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
 * Name: Suggestions.aspx.cs
 * Authors: Nick McRae
 *              Chris McManus
 *              Steve Pickering
 * Desc:    Suggestions page client-server go-between code.
 */ 
namespace HealthyEatingLifePlanner
{
    public partial class Suggestions : System.Web.UI.Page
    {
        //Not using
        protected void Page_Load(object sender, EventArgs e)
        {

        }

        //Desc:     reads choice data from file
        [WebMethod]
        public static string ReadChoiceData( string request )
        {
            FileIO file = new FileIO();
            return file.readChoicesFromFile();
        }

        //Desc:     reads client data from file
        [WebMethod]
        public static string ReadClientData( string request )
        {
            FileIO file = new FileIO();
            return file.readFromFile();
        }  
    }
}