using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using FactualDriver;
using FactualDriver.Filters;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Web.Services;

namespace MappingProjectOne
{
    
    public class Program : System.Web.Services.WebService
    {
        const string MY_KEY = "QaDSplLnh2MWyjmvxpfpYlNEoTxhg9apQUQXBikL";
        const string MY_SECRET = "dJOr8ZqqnUNlJ79MEmpgEdkw7tLO3NExzTtE2FSu";

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
            public string region { get; set; }
            public string status { get; set; }
            public string website { get; set; }
            public string tel { get; set; }
            public string[] neighborhood { get; set; }
            public string postcode { get; set; }
            public string[][] category_labels { get; set; }
            public string country { get; set; }
            public int[] category_ids { get; set; }
            public string address { get; set; }
            public string name { get; set; }
            public string locality { get; set; }
            public double longitude { get; set; }
            public double latitude { get; set; }
            public string address_extended { get; set; }
            public string factual_id { get; set; }
            public float distance { get; set; }
        }

        [WebMethod]
        public String fetchFromFactual(string lat, string lon)
        {
            Factual factual = new Factual(MY_KEY, MY_SECRET);

            // Set the request timeouts back to default 100000 and 300000 respectively
            factual.ConnectionTimeout = null;
            factual.ReadTimeout = null;

            double dLat = double.Parse(lat);
            double dLon = double.Parse(lon);

            string factualJson = factual.Fetch("places", new Query().WithIn(new Circle(dLat, dLon, 2000)).Limit(5));
            Rootobject factualData = JsonConvert.DeserializeObject<Rootobject>(factualJson);

            return factualJson;
        }
    }
}