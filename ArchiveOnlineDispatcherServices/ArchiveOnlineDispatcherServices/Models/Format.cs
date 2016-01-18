using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ArchiveOnlineDispatcherServices.Models
{
    public class Format
    {
        public string Name { get; set;}
        public string Id { get; set; }
        
        public Format (string _Id, string _Name)
        {
            this.Id = _Id;
            this.Name = _Name;
        }
    }
}