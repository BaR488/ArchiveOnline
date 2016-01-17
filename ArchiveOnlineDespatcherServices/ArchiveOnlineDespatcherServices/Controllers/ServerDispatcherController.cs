using MySql.Data.MySqlClient;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace ArchiveOnlineDespatcherServices.Controllers
{
    public class ServerDispatcherController : ApiController
    {
        [Route("api/servers")]
        public object Get()
        {
            List<string> l = new List<string>();

            MySqlConnectionStringBuilder mysqlCSB;
            mysqlCSB = new MySqlConnectionStringBuilder();

            mysqlCSB.Server = "colorcombat-4h300y13.cloudapp.net";
            mysqlCSB.Database = "ArchiveOnline";
            mysqlCSB.UserID = "root";
            mysqlCSB.Password = "CCrootpass";

            string queryString = @"SELECT *              
                        FROM   server;";

            l.Add("1");
            l.Add("2");
            l.Add("3");
            return l;
        }
    }
}
