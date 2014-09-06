using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Web.Services;
using FactualDriver;
using FactualDriver.Filters;

/**************************************************************
 * Author: Nick McRae
 * Purpose: Provide factual data function to return JSON String to default.js
 * Revision History
 * Jan 26/14: Initial implementation 
 **************************************************************/

namespace MappingProjectOne
{
    public partial class _default : System.Web.UI.Page
    {
        const string MY_KEY = "QaDSplLnh2MWyjmvxpfpYlNEoTxhg9apQUQXBikL";
        const string MY_SECRET = "dJOr8ZqqnUNlJ79MEmpgEdkw7tLO3NExzTtE2FSu";
        
        protected void Page_Load(object sender, EventArgs e)
        {

        }

        public class Rootobject
        {
            public int version { get; set; }
            public string status { get; set; }
            public Response response { get; set; }
        }

        public class Response
        {
            public Datum[] data { get; set; }
            public int included_rows { get; set; }
        }

        public class Datum
        {            
            public string name { get; set; }            
            public double longitude { get; set; }
            public double latitude { get; set; }            
        }

        [WebMethod]
        public static String fetchFromFactual(string lat, string lon)
        {
            Factual factual = new Factual(MY_KEY, MY_SECRET);

            // Set the request timeouts back to default 100000 and 300000 respectively
            factual.ConnectionTimeout = null;
            factual.ReadTimeout = null;

            double dLat = double.Parse(lat);
            double dLon = double.Parse(lon);

            string factualJson = factual.Fetch("places", new Query().WithIn(new Circle(dLat, dLon, 2000)).Limit(50));

            return factualJson;
        }
    }
}