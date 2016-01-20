using ArchiveOnlineDispatcherServices.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace ArchiveOnlineDispatcherServices.Controllers
{
    //Контроллер для балансировки нагрузки на сервера
    public class BalanceController : ApiController
    {
        //Возвращает список серверов по типу и формату
        [Route("api/getServers")]
        [HttpGet]
        public HttpResponseMessage GetServers(uint type, string format)
        {
            try
            {
                if (CheckParametrs(type, format))
                {
                    List<Server> servers = ServerCollection.getServersByTypeAndFormat(type, format);
                    return Request.CreateResponse(HttpStatusCode.OK, servers);
                }
                else
                {
                    return Request.CreateErrorResponse(HttpStatusCode.BadRequest, "Неверные параметры сервера");
                }
            }
            catch (Exception ex)
            {
                return Request.CreateErrorResponse(HttpStatusCode.BadRequest, ex.Message);
            }
        }

        //Возвращает самый не нагруженный сервер
        [Route("api/getIdleServer")]
        [HttpGet]
        public HttpResponseMessage GetMostIdleServer(uint type, string format)
        {
            try
            {
                if (CheckParametrs(type, format))
                {
                    Server server = ServerCollection.getMostIdleServer(type, format);
                    return Request.CreateResponse(HttpStatusCode.OK, server);
                }
                else
                {
                    return Request.CreateErrorResponse(HttpStatusCode.BadRequest, "Неверные параметры сервера");
                }

            }
            catch (Exception ex)
            {
                return Request.CreateErrorResponse(HttpStatusCode.BadRequest, ex.Message);
            }
        }

        //Проверяет параметры сервера
        public bool CheckParametrs(uint type, string format)
        {
            return !string.IsNullOrWhiteSpace(format) && ((int)type == (int)Server.ServerType.COMPRESSOR || (int)type == (int)Server.ServerType.DEPRESSOR);
        }

    }
}
