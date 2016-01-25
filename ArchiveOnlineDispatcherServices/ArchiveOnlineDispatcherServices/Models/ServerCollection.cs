using MySql.Data.MySqlClient;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Net;
using System.Web;
using Newtonsoft.Json;
using System.IO;

namespace ArchiveOnlineDispatcherServices.Models
{
    public class ServerCollection
    {

        //Подгружает список доступных форматов для сжатия/расжатия
        public static List<Format> getAvailableFormatsByType(uint type)
        {

            MySqlCommand command = new MySqlCommand();
            command.CommandText = "SELECT FORMAT.ID, FORMAT.NAME_FORMAT FROM FORMAT, SERVER WHERE SERVER.TYPE = @type AND SERVER.FORMAT_ID = FORMAT.ID";
            command.Parameters.AddWithValue("@type", type);

            DataTable formatsDt = QueryExecutor.ExecuteQuery(command);

            List<Format> formatList = new List<Format>();

            foreach (DataRow row in formatsDt.Rows)
            {
                formatList.Add(new Format(row[0].ToString(), row[1].ToString()));
            }

            return formatList;
        }

        //Регистрирует указанный сервер
        public static void registerServer(Server server)
        {
            if (server.isAvailable())
            {
                Server sameAddressServer = getServerByAddress(server.Address, server.Port);
                if (sameAddressServer == null)
                {
                    addServer(server);
                }
                else
                {
                    updateServer(sameAddressServer.Id, server);
                    server.Id = sameAddressServer.Id;
                }
            }
            else
            {
                throw new Exception("Регистрация невозможна, сервер не отвечает на запросы");
            }

        }



        //Возвращает самый не нагруженный сервер, выполняющий поддерживающий указанный формат
        public static Server getMostIdleServer(uint type, string format)
        {
            //Получаем список всех серверов из БД
            List<Server> servers = getServersByTypeAndFormat(type, format);

            //Удлаяем все сервера которы не доступны
            servers.RemoveAll(server => !server.isAvailable());

            //Если остались сервера
            if (servers.Count > 0)
            {
                //Сортируем сервера по нагруженности
                List<Server> ordered = servers.OrderBy(server => server.Status.filesInProgress).ToList();

                //Возвращаем самый не загруженый
                return ordered[0];
            }
            else
            {
                return null;
            }
        }

        //Удаляем сервер с указаным id
        public static bool deleteServer(uint serverId)
        {
            MySqlCommand command = new MySqlCommand();
            command.CommandText = "DELETE FROM SERVER WHERE SERVER.ID = @serverId";
            command.Parameters.AddWithValue("@serverId", serverId);
            return QueryExecutor.ExecuteNonQuery(command) > 0;
        }

        //Добавляет указанный сервер
        public static void addServer(Server server)
        {
            //Созадем подключение к БД
            using (MySqlConnection connection = new MySqlConnection(QueryExecutor.mysqlCSB.ConnectionString))
            {

                MySqlTransaction transaction = null;

                try
                {
                    //Открываем его
                    connection.Open();

                    //Начинаем транзакцию
                    transaction = connection.BeginTransaction();

                    //ID формата добавляемого сервера
                    int formatId = addRowReferencedTable(connection, transaction, "FORMAT", server.Format);


                    MySqlCommand command = new MySqlCommand("SET foreign_key_checks = 0;", connection, transaction);
                    command.ExecuteNonQuery();

                    //Выполняем запрос по вставке сервера
                    command.CommandText = "INSERT INTO SERVER VALUES(NULL, @address, @port, @type, @format_id, @thread_count, @queue_size)";
                    command.Parameters.AddWithValue("@address", server.Address);
                    command.Parameters.AddWithValue("@port", server.Port);
                    command.Parameters.AddWithValue("@type", server.Type);
                    command.Parameters.AddWithValue("@format_id", formatId);
                    command.Parameters.AddWithValue("@thread_count", server.ThreadCount);
                    command.Parameters.AddWithValue("@queue_size", server.QueueSize);
                    command.ExecuteNonQuery();

                    //Получаем Id сервера
                    command.CommandText = "SELECT LAST_INSERT_ID()";
                    server.Id = Convert.ToUInt32(command.ExecuteScalar());

                    command.CommandText = "SET foreign_key_checks = 1;";
                    command.ExecuteNonQuery();

                    //Применяем изменения
                    transaction.Commit();
                }
                catch (Exception ex)
                {
                    if (transaction != null)
                    {
                        transaction.Rollback();
                    }
                }
            }
        }

        public static void updateServer(uint serverId, Server server)
        {
            //Созадем подключение к БД
            using (MySqlConnection connection = new MySqlConnection(QueryExecutor.mysqlCSB.ConnectionString))
            {

                MySqlTransaction transaction = null;

                try
                {
                    //Открываем его
                    connection.Open();

                    //Начинаем транзакцию
                    transaction = connection.BeginTransaction();

                    //ID формата добавляемого сервера
                    int formatId = addRowReferencedTable(connection, transaction, "FORMAT", server.Format);


                    MySqlCommand command = new MySqlCommand("SET foreign_key_checks = 0;", connection, transaction);
                    command.ExecuteNonQuery();

                    //Выполняем запрос по изменению
                    command.CommandText = "UPDATE SERVER SET ADDRESS=@address, PORT=@port, TYPE=@type, "
                        + "FORMAT_ID=@formatId, THREAD_COUNT=@threadCount, QUEUE_SIZE=@queueSize WHERE SERVER.ID = @serverId";
                    command.Parameters.AddWithValue("@serverId", serverId);
                    command.Parameters.AddWithValue("@address", server.Address);
                    command.Parameters.AddWithValue("@port", server.Port);
                    command.Parameters.AddWithValue("@type", server.Type);
                    command.Parameters.AddWithValue("@formatId", formatId);
                    command.Parameters.AddWithValue("@threadCount", server.ThreadCount);
                    command.Parameters.AddWithValue("@queueSize", server.QueueSize);
                    command.ExecuteNonQuery();


                    command.CommandText = "SET foreign_key_checks = 1;";
                    command.ExecuteNonQuery();

                    //Применяем изменения
                    transaction.Commit();
                }
                catch (Exception ex)
                {
                    if (transaction != null)
                    {
                        transaction.Rollback();
                    }
                }
            }
        }

        //Проверяет если такой формат уже есть, то возвращает его ID
        //иначе вставляет его, при этом возвращает его ID
        private static int addRowReferencedTable(MySqlConnection connection, MySqlTransaction transaction, string table_name, string value)
        {
            int insert_id = -1;
            MySqlCommand command = new MySqlCommand(string.Format("SELECT ID FROM {0} WHERE {1} = @value ", table_name, "NAME_" + table_name), connection, transaction);
            command.Parameters.AddWithValue("@value", value);

            MySqlDataReader DataReader = command.ExecuteReader();
            if (!DataReader.HasRows)
            {
                DataReader.Close();
                command.CommandText = String.Format("INSERT INTO {0} VALUES (NULL, @value);", table_name);
                command.ExecuteNonQuery();
                command.CommandText = "SELECT LAST_INSERT_ID()";
                insert_id = Convert.ToInt32(command.ExecuteScalar());
            }
            else
            {
                DataReader.Read();
                insert_id = Convert.ToInt32(DataReader[0]);
                DataReader.Close();
            }

            return insert_id;
        }

        //Возвращает список серверов по типу и формату
        public static List<Server> getServersByTypeAndFormat(uint type, string format)
        {
            MySqlCommand command = new MySqlCommand();
            command.CommandText = "SELECT * FROM SERVER, FORMAT WHERE FORMAT.NAME_FORMAT=@format AND SERVER.TYPE=@type AND SERVER.FORMAT_ID=FORMAT.ID";
            command.Parameters.AddWithValue("@type", type);
            command.Parameters.AddWithValue("@format", format);

            DataTable serversDt = QueryExecutor.ExecuteQuery(command);

            List<Server> serversList = new List<Server>();

            foreach (DataRow row in serversDt.Rows)
            {
                uint id = uint.Parse(row[0].ToString());
                uint port = uint.Parse(row[2].ToString());
                uint threadCount = uint.Parse(row[5].ToString());
                uint queueSize = uint.Parse(row[6].ToString());
                Server newServer = new Server(id, port, (Server.ServerType)type, row[1].ToString(), format, threadCount, queueSize);
                serversList.Add(newServer);
            }

            return serversList;
        }

        //Возвращает сервер с таким же адрессом, если таковой есть
        public static Server getServerByAddress(string address, uint port)
        {
            MySqlCommand command = new MySqlCommand();
            command.CommandText = "SELECT * FROM SERVER WHERE SERVER.ADDRESS=@address AND SERVER.PORT=@port";
            command.Parameters.AddWithValue("@address", address);
            command.Parameters.AddWithValue("@port", port);

            DataTable serversDt = QueryExecutor.ExecuteQuery(command);

            if (serversDt != null && serversDt.Rows.Count > 0)
            {
                DataRow row = serversDt.Rows[0];
                uint id = uint.Parse(row[0].ToString());
                Server.ServerType type = (Server.ServerType)uint.Parse(row[3].ToString());
                uint threadCount = uint.Parse(row[5].ToString());
                uint queueSize = uint.Parse(row[6].ToString());
                return new Server(id, port, type, address, row[4].ToString(), threadCount, queueSize);
            }
            else
            {
                return null;
            }

        }


    }
}