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
 * Name: Information.aspx.cs
 * Authors: Nick McRae
 *              Chris McManus
 *              Steve Pickering
 * Desc:    Information page client-server go-between code.
 */ 

namespace HealthyEatingLifePlanner
{
    public partial class Information : System.Web.UI.Page
    {
        //Class constants
        private static string consumerKey = "973c09549681479dbe1737ca30690f87";
        private static string consumerSecret = "117814e1ce594214b5a4f20a65b7075d";
        public const string URL_BASE = "http://platform.fatsecret.com/rest/server.api?";

        //Not using
        protected void Page_Load(object sender, EventArgs e)
        {

        }

        //Desc:     Makes an API call based on the submitted search term
        [WebMethod]
        public static string SearchTerm(string request)
        {
            string urlBase = URL_BASE + "search_expression=" + request + "&method=foods.search";

            FatSecretObj o = new FatSecretObj();

            Uri url = new Uri(urlBase);
            string normalizedUrl, normalizedRequestParameters;

            string signature = o.GenerateSignature(url, consumerKey, consumerSecret, null, null, out normalizedUrl, out normalizedRequestParameters);
            string jsonString = GetQueryResponse(normalizedUrl, normalizedRequestParameters + "&" + FatSecretObj.OAUTH_SIGNATURE + "=" + HttpUtility.UrlEncode(signature));

            return jsonString;
        }

        internal static XmlDocument LoadXMLDocument(string rawXML)
        {
            XmlDocument xmlDocument = new XmlDocument();
            xmlDocument.LoadXml(rawXML);
            return xmlDocument;
        }

        internal static string GetQueryResponse(string requestUrl, string postString)
        {
            HttpWebRequest webRequest = (HttpWebRequest)WebRequest.Create(requestUrl);

            webRequest.Method = "POST";
            webRequest.ContentType = "application/x-www-form-urlencoded";

            byte[] parameterString = Encoding.ASCII.GetBytes(postString);
            webRequest.ContentLength = parameterString.Length;

            using (Stream buffer = webRequest.GetRequestStream())
            {
                buffer.Write(parameterString, 0, parameterString.Length);
                buffer.Close();
            }

            WebResponse webResponse = webRequest.GetResponse();

            string responseData;
            using (StreamReader streamReader = new StreamReader(webResponse.GetResponseStream()))
            {
                responseData = streamReader.ReadToEnd();
            }
            return responseData;
        }
    }
}