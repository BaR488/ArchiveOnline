using MySql.Data.MySqlClient;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Web;

namespace ArchiveOnlineDispatcherServices.Models
{
    public static class QuerieExecutor
    {
        public static DataTable ExecutQuerie(MySqlCommand com)
        {
            MySqlConnectionStringBuilder mysqlCSB;
            mysqlCSB = new MySqlConnectionStringBuilder();

            mysqlCSB.Server = "colorcombat-4h300y13.cloudapp.net";
            mysqlCSB.Database = "ArchiveOnline";
            mysqlCSB.UserID = "root";
            mysqlCSB.Password = "CCrootpass";

            DataTable dt = new DataTable();

            using (MySqlConnection con = new MySqlConnection())
            {
                con.ConnectionString = mysqlCSB.ConnectionString;
                com.Connection = con;

                try
                {
                    con.Open();
                    using (MySqlDataReader dr = com.ExecuteReader())
                    {
                        //есть записи?
                        if (dr.HasRows)
                        {
                            //заполняем объект DataTable
                            dt.Load(dr);
                        }
                    }
                }
                catch (Exception e)
                {
                    return null;
                }
            }
            return dt;
        }
    }
}