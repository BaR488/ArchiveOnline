using MySql.Data.MySqlClient;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Web;

namespace ArchiveOnlineDispatcherServices.Models
{
    public class FormatCollections
    {
        public List<Format> formats { get; set; }

        public FormatCollections(int type)
        {
            MySqlCommand command = new MySqlCommand();
            string SQL = "select ArchiveOnline.format.NAME_FORMAT from ArchiveOnline.format, ArchiveOnline.server where ArchiveOnline.server.TYPE = @type and ArchiveOnline.server.FORMAT = ArchiveOnline.format.ID; ";
            command.CommandText = SQL;
            command.Parameters.AddWithValue("@type", type);
            DataTable formatsDt = QuerieExecutor.ExecutQuerie(command);
            formats = new List<Format>();
            foreach (DataRow row in formatsDt.Rows)
            {
                formats.Add(new Format(row[0].ToString()));
            }
        }
    }
}