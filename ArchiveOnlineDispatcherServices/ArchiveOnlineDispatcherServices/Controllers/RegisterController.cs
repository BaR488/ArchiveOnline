using ArchiveOnlineDispatcherServices.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.ServiceModel.Channels;
using System.Web;
using System.Web.Http;

namespace ArchiveOnlineDispatcherServices.Controllers
{
    public class RegisterController : ApiController
    {

        private const string HttpContext = "MS_HttpContext";
        private const string RemoteEndpointMessage = "System.ServiceModel.Channels.RemoteEndpointMessageProperty";

        [Route("api/register")]
        [HttpGet]
        public HttpResponseMessage RegisterServer(uint port, uint type, string format, uint threadCount, uint queueSize)
        {
            try
            {
                if (CheckRegisterParametrs(port, type, format, threadCount, queueSize))
                {
                    //Создаем новый объект типа сервер
                    Server server = new Server(port, (Server.ServerType)type, GetClientIp(Request), format.ToLower(), threadCount, queueSize);

                    //Регистрируем его
                    ServerCollection.registerServer(server);

                    //Возвращаем успех
                    return Request.CreateResponse(HttpStatusCode.OK, server);

                }
                else
                {
                    return Request.CreateErrorResponse(HttpStatusCode.BadRequest, "Регистарция сервера невозможна. Неверные параметры сервера");
                }

            }
            catch (Exception ex)
            {
                return Request.CreateErrorResponse(HttpStatusCode.BadRequest, ex.Message);
            }


        }

        [Route("api/delete")]
        [HttpGet]
        public HttpResponseMessage DeleteServer(uint serverId)
        {
            try
            {
                if (ServerCollection.deleteServer(serverId))
                {
                    return Request.CreateResponse(HttpStatusCode.OK, "Server was succesfully deleted");
                } else
                {
                    return Request.CreateResponse(HttpStatusCode.OK, "There is no registred server with given id");
                }
            }
            catch (Exception ex)
            {
                return Request.CreateErrorResponse(HttpStatusCode.BadRequest, ex.Message);
            }
        }

        //Возвращает IP адрес клиента
        private string GetClientIp(HttpRequestMessage request)
        {
            //Если служба захосчена в IIS
            if (request.Properties.ContainsKey("MS_HttpContext"))
            {
                return ((HttpContextWrapper)request.Properties["MS_HttpContext"]).Request.UserHostAddress;
            }

            //Если служба само хосчена
            if (request.Properties.ContainsKey(RemoteEndpointMessageProperty.Name))
            {
                RemoteEndpointMessageProperty prop;
                prop = (RemoteEndpointMessageProperty)request.Properties[RemoteEndpointMessageProperty.Name];
                return prop.Address;
            }

            return null;
        }

        //Проверяет параметры регестрируемого сервера
        public bool CheckRegisterParametrs(uint port, uint type, string format, uint threadCount, uint queueSize)
        {
            return !string.IsNullOrWhiteSpace(format) && ((int)type == (int)Server.ServerType.COMPRESSOR || (int)type == (int)Server.ServerType.DEPRESSOR)
                && port > 0 && threadCount >= 0 && queueSize >= 0 && threadCount + queueSize > 0;
        }


    }
}
