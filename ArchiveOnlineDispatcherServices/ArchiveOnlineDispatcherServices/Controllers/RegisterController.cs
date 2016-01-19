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
        public List<Server> RegisterServer(int type, string format, int threadCount, int queueSize)
        {
            //Создаем новый объект типа сервер
            Server server = new Server(type, GetClientIp(Request), GetClientName(Request), format, threadCount, queueSize);
            
            //Регистрируем его
            ServerCollection.registerServer(server);

            return new List<Server>() { server };
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

        //Возвращает имя клиента
        private string GetClientName(HttpRequestMessage request)
        {
            //Если служба захосчена в IIS
            if (request.Properties.ContainsKey("MS_HttpContext"))
            {
                return ((HttpContextWrapper)request.Properties["MS_HttpContext"]).Request.UserHostName;
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

    }
}
