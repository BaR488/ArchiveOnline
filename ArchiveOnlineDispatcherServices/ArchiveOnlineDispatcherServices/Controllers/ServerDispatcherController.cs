using ArchiveOnlineDispatcherServices.Models;
using MySql.Data.MySqlClient;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace ArchiveOnlineDespatcherServices.Controllers
{
    public class ServerDispatcherController : ApiController
    {
        [Route("api/servers")]
        public object GetTypeList(int type)
        {
            FormatCollections fc = new FormatCollections(type);
            return fc.formats;
        }
    }
}
