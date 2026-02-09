package Es_1;

public class Es_1_8 {
            public static boolean scan(String s) {
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
            switch (state) {
                case 0:
                    if (ch == 'G' )
                        state = 1;
                    else if (ch != 'G' && ch >= 'A' && ch <= 'Z')
                        state = 5;
                    else
                        state = -1;
                    break;
                case 1:
                    if (ch == 'i' )
                        state = 2;
                    else if (ch != 'i' && ch >= 'a' && ch <= 'z')
                        state = 6;
                    else
                        state = -1;
                    break;
                case 2:
                    if (ch == 'n' )
                        state = 3;
                    else if (ch != 'n' && ch >= 'a' && ch <= 'z')
                        state = 7;
                    else
                        state = -1;
                    break;
                case 3:
                     if (ch >= 'a' && ch <= 'z')
                        state = 4;
                    else                   
                        state = -1;
                    break;
                case 4:
                     break;
                case 5:
                    if (ch == 'i' )
                        state = 6;
                    else
                        state = -1;
                    break;                
                case 6:
                    if (ch == 'n' )
                        state = 7;
                    else
                        state = -1;
                    break;  
                case 7:
                    if (ch == 'o' )
                        state = 4;
                    else
                        state = -1;
                    break;                                       
            }
        }
        return  state == 4;
    }

    public static void main(String[] args) {
        System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}
