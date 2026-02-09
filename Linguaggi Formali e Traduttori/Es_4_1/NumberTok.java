package Es_4_1;

public class NumberTok extends Token {
	// ... completare ...
        public int num;
        public NumberTok(int tag, int n) {
            super(tag);
            num = n;
        }
        public String toString() {
            return "<" + tag + ", " + num + ">";
        }
}

