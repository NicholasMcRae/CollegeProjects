﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Security.Cryptography;
using System.Collections;
using System.IO;
using System.Net;
using System.Xml;

/*
 * Name: FatSecretObj.cs
 * Authors: Nick McRae
 *              Chris McManus
 *              Steve Pickering
 * Desc:    Csharp code that interacts with the FatSecret API
 *              Nick found the code somewhere and revised slightly to suit our purposes
 */ 

namespace FatSecret
{
    public class FatSecretObj
    {
        //Class constants
        public const string OAUTH_VERSION_NUMBER = "1.0";
        public const string OAUTH_PARAMETER_PREFIX = "oauth_";
        public const string XOAUTH_PARAMETER_PREFIX = "xoauth_";
        public const string OPEN_SOCIAL_PARAMETER_PREFIX = "opensocial_";

        public const string OAUTH_CONSUMER_KEY = "oauth_consumer_key";
        public const string OAUTH_CALLBACK = "oauth_callback";
        public const string OAUTH_VERSION = "oauth_version";
        public const string OAUTH_SIGNATURE_METHOD = "oauth_signature_method";
        public const string OAUTH_SIGNATURE = "oauth_signature";
        public const string OAUTH_TIMESTAMP = "oauth_timestamp";
        public const string OAUTH_NONCE = "oauth_nonce";
        public const string OAUTH_TOKEN = "oauth_token";
        public const string OAUTH_TOKEN_SECRET = "oauth_token_secret";

        protected string unreservedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.~";

        /// <summary>
        /// Generates a signature using the HMAC-SHA1 algorithm
        /// </summary>		
        /// <param name="url">The full url that needs to be signed including its non OAuth url parameters</param>
        /// <param name="consumerKey">The consumer key</param>
        /// <param name="consumerSecret">The consumer seceret</param>
        /// <param name="token">The token, if available. If not available pass null or an empty string</param>
        /// <param name="tokenSecret">The token secret, if available. If not available pass null or an empty string</param>
        /// <param name="httpMethod">The http method used. Must be a valid HTTP method verb (POST,GET,PUT, etc)</param>
        /// <returns>A base64 string of the hash value</returns>
        public string GenerateSignature( Uri url, string consumerKey, string consumerSecret, string token, string tokenSecret, out string normalizedUrl, out string normalizedRequestParameters )
        {
            normalizedUrl = null;
            normalizedRequestParameters = null;

            string signatureBase = GenerateSignatureBase( url, consumerKey, token, "POST", GenerateTimeStamp(), GenerateNonce(), "HMAC-SHA1", out normalizedUrl, out normalizedRequestParameters );
            HMACSHA1 hmacsha1 = new HMACSHA1();
            hmacsha1.Key = Encoding.ASCII.GetBytes( string.Format( "{0}&{1}", UrlEncode( consumerSecret ), IsNullOrEmpty( tokenSecret ) ? "" : UrlEncode( tokenSecret ) ) );
            return GenerateSignatureUsingHash( signatureBase, hmacsha1 );
        }

        protected class QueryParameter
        {
            private string name = null;
            private string value = null;

            public QueryParameter( string name, string value )
            {
                this.name = name;
                this.value = value;
            }

            public string Name
            {
                get { return name; }
            }

            public string Value
            {
                get { return value; }
            }
        }

        protected class QueryParameterComparer: IComparer
        {
            public int Compare( object a, object b )
            {
                QueryParameter x = ( QueryParameter )a;
                QueryParameter y = ( QueryParameter )b;
                if( x.Name == y.Name )
                {
                    return string.Compare( x.Value, y.Value );
                }
                else
                {
                    return string.Compare( x.Name, y.Name );
                }
            }
        }

        private static bool IsNullOrEmpty( string str )
        {
            return ( str == null || str.Length == 0 );
        }

        private string ComputeHash( HashAlgorithm hashAlgorithm, string data )
        {
            byte[] dataBuffer = System.Text.Encoding.ASCII.GetBytes( data );
            byte[] hashBytes = hashAlgorithm.ComputeHash( dataBuffer );

            return Convert.ToBase64String( hashBytes );
        }

        private IList GetQueryParameters( string parameters, IList result )
        {
            if( parameters.StartsWith( "?" ) )
            {
                parameters = parameters.Remove( 0, 1 );
            }

            if( !IsNullOrEmpty( parameters ) )
            {
                string[] p = parameters.Split( '&' );
                foreach( string s in p )
                {
                    if( !IsNullOrEmpty( s ) && !s.StartsWith( OAUTH_PARAMETER_PREFIX ) && !s.StartsWith( XOAUTH_PARAMETER_PREFIX ) && !s.StartsWith( OPEN_SOCIAL_PARAMETER_PREFIX ) )
                    {
                        if( s.IndexOf( '=' ) > -1 )
                        {
                            string[] temp = s.Split( '=' );
                            result.Add( new QueryParameter( temp[0], UrlEncode( temp[1] ) ) );
                        }
                        else
                        {
                            result.Add( new QueryParameter( s, string.Empty ) );
                        }
                    }
                }
            }

            return result;
        }

        protected string UrlEncode( string value )
        {
            StringBuilder result = new StringBuilder();

            foreach( char symbol in value )
            {
                if( unreservedChars.IndexOf( symbol ) != -1 )
                {
                    result.Append( symbol );
                }
                else
                {
                    result.Append( '%' + String.Format( "{0:X2}", ( int )symbol ) );
                }
            }

            return result.ToString();
        }

        protected string NormalizeRequestParameters( IList parameters )
        {
            StringBuilder sb = new StringBuilder();
            QueryParameter p = null;
            for( int i = 0; i < parameters.Count; i++ )
            {
                p = ( QueryParameter )parameters[i];
                sb.AppendFormat( "{0}={1}", p.Name, p.Value );

                if( i < parameters.Count - 1 )
                {
                    sb.Append( "&" );
                }
            }

            return sb.ToString();
        }

        private string GenerateSignatureBase( Uri url, string consumerKey, string token, string httpMethod, string timeStamp, string nonce, string signatureType, out string normalizedUrl, out string normalizedRequestParameters )
        {
            normalizedUrl = null;
            normalizedRequestParameters = null;

            IList parameters = new ArrayList();

            GetQueryParameters( url.Query, parameters );

            parameters.Add( new QueryParameter( OAUTH_VERSION, OAUTH_VERSION_NUMBER ) );
            parameters.Add( new QueryParameter( OAUTH_NONCE, nonce ) );
            parameters.Add( new QueryParameter( OAUTH_TIMESTAMP, timeStamp ) );
            parameters.Add( new QueryParameter( OAUTH_SIGNATURE_METHOD, signatureType ) );
            parameters.Add( new QueryParameter( OAUTH_CONSUMER_KEY, consumerKey ) );
            //Additional parameter for JSON return data
            parameters.Add( new QueryParameter( "format", "json" ) );

            if( !IsNullOrEmpty( token ) )
            {
                parameters.Add( new QueryParameter( OAUTH_TOKEN, token ) );
            }

            ( ( ArrayList )parameters ).Sort( new QueryParameterComparer() );

            normalizedUrl = string.Format( "{0}://{1}", url.Scheme, url.Host );
            if( !( ( url.Scheme == "http" && url.Port == 80 ) || ( url.Scheme == "https" && url.Port == 443 ) ) )
            {
                normalizedUrl += ":" + url.Port;
            }

            normalizedUrl += url.AbsolutePath;
            normalizedRequestParameters = NormalizeRequestParameters( parameters );

            StringBuilder signatureBase = new StringBuilder();
            signatureBase.AppendFormat( "{0}&", httpMethod );
            signatureBase.AppendFormat( "{0}&", UrlEncode( normalizedUrl ) );
            signatureBase.AppendFormat( "{0}", UrlEncode( normalizedRequestParameters ) );

            return signatureBase.ToString();
        }

        private string GenerateSignatureUsingHash( string signatureBase, HashAlgorithm hash )
        {
            return ComputeHash( hash, signatureBase );
        }

        public string GenerateTimeStamp()
        {
            TimeSpan ts = DateTime.UtcNow - new DateTime( 1970, 1, 1, 0, 0, 0, 0 );
            return Convert.ToInt64( ts.TotalSeconds ).ToString();
        }

        public string GenerateNonce()
        {
            return Guid.NewGuid().ToString().Replace( "-", "" );
        }

    }
}

/// <summary>
/// Exception class used to present data to us so we were aware of errors in the API call
/// </summary>
public class FatSecretException: Exception
{
    private int errorCode;
    private string errorMessage;

    public FatSecretException( int errorCode, string errorMsg )
    {
        this.errorCode = errorCode;
        this.errorMessage = errorMsg;
    }

    public int ErrorCode
    {
        get
        {
            return errorCode;
        }
    }

    public string ErrorMessage
    {
        get
        {
            return errorMessage;
        }
        set
        {
            errorMessage = value;
        }

    }
}