using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Controls;

namespace GUI_Project_3
{
    class Friend : TreeViewItem
    {
        private string first;
        private string last;
        private int birthYear;
        private string birthMonth;
        private int birthDay;
        private string bornIn;
        private string livesIn;
        private string metIn;
        private string religion;
        private string howMet;
        private string details;
        private string funnyStory;
        private double rating;

        public string First
        {
            get
            {
                return this.first;
            }
            set
            {
                this.first = value;
            }
        }

        public string Last
        {
            get
            {
                return this.last;
            }
            set
            {
                this.last = value;
            }
        }

        public int BirthYear
        {
            get
            {
                return this.birthYear;
            }
            set
            {
                this.birthYear = value;
            }
        }

        public string BirthMonth
        {
            get
            {
                return this.birthMonth;
            }
            set
            {
                this.birthMonth = value;
            }
        }

        public int BirthDay
        {
            get
            {
                return this.birthDay;
            }
            set
            {
                this.birthDay = value;
            }
        }

        public string BornIn
        {
            get
            {
                return this.bornIn;
            }
            set
            {
                this.bornIn = value;
            }
        }

        public string LivesIn
        {
            get
            {
                return this.livesIn;
            }
            set
            {
                this.livesIn = value;
            }
        }

        public string MetIn
        {
            get
            {
                return this.metIn;
            }
            set
            {
                this.metIn = value;
            }
        }

        public string Religion
        {
            get
            {
                return this.religion;
            }
            set
            {
                this.religion = value;
            }
        }

        public string HowMet
        {
            get
            {
                return this.howMet;
            }
            set
            {
                this.howMet = value;
            }
        }

        public string Details
        {
            get
            {
                return this.details;
            }
            set
            {
                this.details = value;
            }
        }

        public string FunnyStory
        {
            get
            {
                return this.funnyStory;
            }
            set
            {
                this.funnyStory = value;
            }
        }

        public double Rating
        {
            get
            {
                return this.rating;
            }
            set
            {
                this.rating = value;
            }
        }

        public override string ToString()
        {
           
            if (this.Header == "Friends")
            {
                return "Here's your list of friends!";
            }

            return "Name: " + first + " " + last + "    Born in: " + birthYear + "    Lives in: " + livesIn + "    Practices: " + religion;
        }

    }
}
