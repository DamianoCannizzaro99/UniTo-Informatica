package Es_5_1;

public class NumberTok extends Token {
        public int num;

        /*costruttore */
        public NumberTok(int tag, int n) {
            super(tag);
            num = n;
        }
        public String toString() {
            return "<" + tag + ", " + num + ">";
        }
}

