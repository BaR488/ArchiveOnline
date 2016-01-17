using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ArchiveOnlineDispatcherServices.Models
{
    public class Format
    {
        public string Name { get; set;}
        
        public Format (string _Name)
        {
            this.Name = _Name;
        }
    }
}