using ArchiveOnlineDispatcherServices.Models;
using MySql.Data.MySqlClient;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.ServiceModel.Channels;
using System.Web;
using System.Web.Http;

namespace ArchiveOnlineDespatcherServices.Controllers
{
    public class ServerDispatcherController : ApiController
    {

        [Route("api/servers")]
        public object GetFormatsList(int type)
        {
            return ServerCollection.getAvailableFormatsByType(type);
        }

    }
}
