    package Es_5_1;



    import java.io.BufferedReader;

        public class Parser {
            private Lexer lex;
            private BufferedReader pbr;
            private Token look;

            /*costruttore */
            public Parser(Lexer l, BufferedReader br) {
                lex = l;
                pbr = br;
                move();
            }

            /*avvia il riconoscimento del singolo token nel file da leggere */
            void move() {
                look = lex.lexical_scan(pbr);           //riconoscimento token e inserimento in look
                System.out.println("token = " + look);

            }

            /*generico avviso di errore */
            void error(String s) {
                throw new Error("near line " + lex.line + ": " + s);
            }

            /*verifica se il Token look è uguale al token t, in caso positivo esegue la prossima mossa
             */
            void match(int t) {
                if (look.tag == t) {
                    if (look.tag != Tag.EOF) move();
                } else error("syntax error match");
            }
        /**
         * guida(P -> SL EOF) - {assing,print,read,conditional,'{'}
         * */
        public void prog(){
            if(look.tag== Tag.ASSIGN || look.tag == Tag.PRINT || look.tag == Tag.READ ||
            look.tag == Tag.COND || look.tag == '{'){
                statlist();
                match(Tag.EOF);
            }else {error("Syntax error in prog");}
        }

        // guida SL -> S SL' {assing,print,read,conditional,'{'}
        private void statlist(){
            if(look.tag== Tag.ASSIGN || look.tag == Tag.PRINT || look.tag == Tag.READ ||
                    look.tag == Tag.COND || look.tag == '{'){
                stat();
                statlistp();
            }else {error("Syntax error in statlist");}
        }

        //guida(SL' -> ; S SL') - ;
        //guida(SL' -> eps) - }
        private void statlistp(){
            switch(look.tag){
                case ';':
                    match(';');
                    stat();
                    statlistp();
                    break;
                case '}':
                    break;
                default:
                    error("Syntax error statlistp");
            }
        }
        /**
         * guida(S -> assign E to IL) - assign
         * guida(s -> print [EL]) - print
         * guida(S -> read [IL]) - read
         * guida(S -> while (B) S) - while
         * guida(S -> conditional [OL] S') - cond
         * guida(S -> {SL}) - {
         * */
        private void stat() {
            switch (look.tag) {
                case Tag.ASSIGN:
                    match(Tag.ASSIGN);
                    expr();
                    match(Tag.TO);
                    idlist();
                    break;
                case Tag.PRINT:
                    match(Tag.PRINT);
                    match('[');
                    exprlist();
                    match(']');
                    break;
                case Tag.READ:
                    match(Tag.READ);
                    match('[');
                    idlist();
                    match(']');
                    break;
                case Tag.WHILE:
                    match(Tag.WHILE);
                    match('(');
                    bexpr();
                    match(')');
                    stat();
                    break;
                case Tag.COND:
                    match(Tag.COND);
                    match('[');
                    optlist();
                    match(']');
                    statp();
                    break;
                case '{':
                    match('{');
                    statlist();
                    match('}');
                    break;
                default:
                    error("Syntax error stat");
            }
        }


        /**
         * guida(S' -> end) - end
         * guida(S' -> else S end) - else
         * */
        private void statp(){
            switch(look.tag){
                case Tag.END:
                    match(Tag.END);
                    break;
                case Tag.ELSE:
                    match(Tag.ELSE);
                    stat();
                    match(Tag.END);
                    break;
                default:
                    error("Syntax error statp");

            }
        }

        /**
         * guida(IL -> id IL') - id
         * */

        private void idlist() {
            if(look.tag == Tag.ID){
                match(Tag.ID);
                idlistp();
            }else {
                error("Syntax error idlist");
            }
        }

        /**
         * guida(IL' -> , id IL') - ,
         * guida(IL' -> eps) - ";",},"end",option,"]"
         * */

        private void idlistp(){
            switch(look.tag){
                case ',':
                    match(',');
                    match(Tag.ID);
                    idlistp();
                    break;
                case ';':
                case '}':
                case ']':
                case Tag.OPTION:
                case Tag.END:
                    break;
                default:
                    error("Syntax error idlistp");
            }
        }

        //guida(OL -> OI OL') - option
        private void optlist(){
            if(look.tag == Tag.OPTION){
                optitem();
                optlistp();
            }else {error("Syntax error optlist");}
        }

        /**
         * guida(OL' -> OI OL') - option
         * guida(OL' -> eps) - "]"
         * */
        private void optlistp(){
            switch(look.tag){
                case Tag.OPTION:
                    optitem();
                    optlistp();
                    break;
                case ']':
                    break;
                default:
                    error("Syntax error optlistp");
            }
        }
        /**
         * guida(OI -> option ( B ) do S) - option
         * */
        private void optitem(){
            if (look.tag == Tag.OPTION){
                match(Tag.OPTION);
                match('(');
                bexpr();
                match(')');
                match(Tag.DO);
                stat();
            }else{
                error("Syntax error optitem");
            }
        }


        /**
         * guida(B -> relop E E) - relop
         * */
        private void bexpr(){
            if (look.tag == Tag.RELOP){
                match(Tag.RELOP);
                expr();
                expr();
            }else{
                error("Syntax error bexpr");
            }
        }

        /**
         * guida(E -> + (EL)) - "+"
         * guida(E -> - E E) - "-"
         * guida(E -> * (EL)) - "*"
         * guida(E -> / E E) - "/"
         * guida(E -> num) - num
         * guida(E -> id) - id
         * */
        private void expr(){
            switch(look.tag){
                case '+':
                    match('+');
                    match('(');
                    exprlist();
                    match(')');
                    break;
                case '-':
                    match('-');
                    expr();
                    expr();
                    break;
                case '*':
                    match('*');
                    match('(');
                    exprlist();
                    match(')');
                    break;
                case '/':
                    match('/');
                    expr();
                    expr();
                    break;
                case Tag.NUM:
                    match(Tag.NUM);
                    break;
                case Tag.ID:
                    match(Tag.ID);
                    break;
                default:
                    error("Syntax error expr");
            }
        }
        /**
         * guida(EL -> E EL') - {+,-,*,/, NUM, ID}
         * */
        private void exprlist(){
            switch (look.tag){
                case '+':
                case '-':
                case '*':
                case '/':
                case Tag.NUM:
                case Tag.ID:
                    expr();
                    exprlistp();
                    break;
                default:
                    error("Syntax error exprlist");
            }
        }
        /**
         * guida(EL' -> , E EL') - {+,-,*,/, NUM, ID}
         * guida(EL' -> eps) - "]",")"
         * */
        private void exprlistp(){
            switch(look.tag){
                case '+':
                case '-':
                case '*':
                case '/':
                case Tag.NUM:
                case Tag.ID:
                    match(',');
                    expr();
                    exprlistp();
                    break;
                case ']':
                case ')':
                    break;
                default:
                    error("Syntax error exprlistp");
            }

        }
    }
