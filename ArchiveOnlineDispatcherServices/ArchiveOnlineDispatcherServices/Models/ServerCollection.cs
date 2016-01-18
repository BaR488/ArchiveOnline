using MySql.Data.MySqlClient;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Web;

namespace ArchiveOnlineDispatcherServices.Models
{
    public class ServerCollection
    {

        //Подгружает список доступных форматов для сжатия/расжатия
        public static List<Format> getAvailableFormatsByType(int type)
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
                    int formatId= addRowReferencedTable(connection, transaction, "FORMAT", server.Format);

                    //Отключаем проверку целостности данных
                    MySqlCommand command = new MySqlCommand("SET foreign_key_checks = 0;", connection, transaction);
                    command.ExecuteNonQuery();

                    //Выполняем запрос по вставке сервера
                    command.CommandText = "INSERT INTO SERVER VALUES(NULL, @address, @name, @type, @format_id, @thread_count, @queue_size)";
                    command.Parameters.AddWithValue("@address", server.Address);
                    command.Parameters.AddWithValue("@name", server.Name);
                    command.Parameters.AddWithValue("@type", server.Type);
                    command.Parameters.AddWithValue("@format_id", formatId);
                    command.Parameters.AddWithValue("@thread_count", server.ThreadCount);
                    command.Parameters.AddWithValue("@queue_size", server.QueueSize);
                    command.ExecuteNonQuery();

                    //Включаем проверку обратно
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
                finally
                {
                    connection.Close();
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
    }
}