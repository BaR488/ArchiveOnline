using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ArchiveOnlineDispatcherServices.Models
{
    public class Server
    {

        //Тип сервера, 0 - сжатие, 1 - расжатие
        private int type;
        public int Type
        {
            get
            {
                return type;
            }

            set
            {
                type = value;
            }
        }

        //Адрес сервера
        private string address;
        public string Address
        {
            get
            {
                return address;
            }

            set
            {
                address = value;
            }
        }

        //Имя сервера
        private string name;
        public string Name
        {
            get
            {
                return name;
            }

            set
            {
                name = value;
            }
        }

        //Формат с которым работает сервер
        private string format;
        public string Format
        {
            get
            {
                return format;
            }

            set
            {
                format = value;
            }
        }

        // Количество одноверменно запускаемых потоков
        private int threadCount;
        public int ThreadCount
        {
            get
            {
                return threadCount;
            }

            set
            {
                threadCount = value;
            }
        }

        //Размер очереди
        private int queueSize;
        public int QueueSize
        {
            get
            {
                return queueSize;
            }

            set
            {
                queueSize = value;
            }
        }


        public Server(int type, string address, string name, string format, int threadCount, int queueSize)
        {
            this.type = type;
            this.address = address;
            this.name = name;
            this.format = format;
            this.threadCount = threadCount;
            this.queueSize = queueSize;
        }


    }
}