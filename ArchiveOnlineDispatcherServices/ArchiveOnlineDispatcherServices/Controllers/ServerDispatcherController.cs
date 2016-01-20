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

        [Route("api/getFormats")]
        public HttpResponseMessage GetFormatsList(uint type)
        {
            try
            {
                if (CheckType(type))
                {
                    List<Format> formats = ServerCollection.getAvailableFormatsByType(type);
                    return Request.CreateResponse(HttpStatusCode.OK, formats);
                }
                else
                {
                    return Request.CreateErrorResponse(HttpStatusCode.BadRequest, "Неверныйтип сервера сервера, допустимые значения 0 - сжатие, 1 - расжатие.");
                }

            }
            catch (Exception ex)
            {
                return Request.CreateErrorResponse(HttpStatusCode.BadRequest, ex.Message);
            }
            
            
        }

        public bool CheckType(uint type)
        {
            return ((int)type == (int)Server.ServerType.COMPRESSOR || (int)type == (int)Server.ServerType.DEPRESSOR);
        }

    }
}
