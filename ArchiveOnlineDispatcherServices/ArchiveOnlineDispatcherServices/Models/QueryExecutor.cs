using MySql.Data.MySqlClient;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Web;

namespace ArchiveOnlineDispatcherServices.Models
{
    public static class QueryExecutor
    {

        public static MySqlConnectionStringBuilder mysqlCSB = new MySqlConnectionStringBuilder();

        static QueryExecutor()
        {
            mysqlCSB.Server = "colorcombat-4h300y13.cloudapp.net";
            mysqlCSB.Database = "ArchiveOnline";
            mysqlCSB.UserID = "root";
            mysqlCSB.Password = "CCrootpass";
            //mysqlCSB.Pooling = true;
            //mysqlCSB.MinimumPoolSize = 5;
            //mysqlCSB.MaximumPoolSize = 10;
        }

        //Метод выполняет переданную SQL команду и возвращает результат
        public static DataTable ExecuteQuery(MySqlCommand cmd)
        {
            DataTable resultDt = new DataTable();
            using (MySqlConnection connection = new MySqlConnection(mysqlCSB.ConnectionString))
            {
                try
                {
                    cmd.Connection = connection; 
                    connection.Open();

                    using (MySqlDataReader dr = cmd.ExecuteReader())
                    {
                        if (dr.HasRows)
                        {
                            resultDt.Load(dr);
                        }
                    }
                }
                catch (Exception e)
                {
                    System.Console.WriteLine(e.Message);
                    return null;
                }
            }
            return resultDt;
        }

        public static int ExecuteNonQuery(MySqlCommand cmd)
        {
            using (MySqlConnection connection = new MySqlConnection(mysqlCSB.ConnectionString))
            {
                try
                {
                    cmd.Connection = connection;
                    connection.Open();
                    return cmd.ExecuteNonQuery();
                }
                catch (Exception e)
                {
                    System.Console.WriteLine(e.Message);
                    return -1;
                }
            }
        }

    }
}