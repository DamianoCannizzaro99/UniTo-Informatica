package Es_5_1;

import java.io.*;

public class Translator {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count=0;

    public Translator(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }
    /**legge il token ritornato dal lexer */
    void move() {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);

    }
    void error(String s) {
        throw new Error("near line " + lex.line + ": " + s);
    }
    void match(int t) {
        if (look.tag == t) {
            if (look.tag !=Tag.EOF) move();
        } else error("syntax error match");
    }

    public void prog() {
        if ( look.tag == Tag.ASSIGN || look.tag == Tag.PRINT || look.tag == Tag.READ || look.tag == Tag.COND ||
                look.tag == Tag.WHILE || look.tag == '{') {
            int lnext_prog = code.newLabel(); /* L0 */
            statlist(lnext_prog);
            code.emitLabel(lnext_prog); /* L0: */
            match(Tag.EOF);
            try {
                code.toJasmin();
            }
            catch(java.io.IOException e) {
                System.out.println("IO error\n");
            };
        } else {
            error("Syntax error prog");
        }
    }
    // guida SL -> S SL' {assing,print,read,while,conditional,'{'}
    private void statlist(int lnext_prog) {
        if (look.tag == Tag.ASSIGN || look.tag == Tag.PRINT || look.tag == Tag.READ || look.tag == Tag.COND ||
                look.tag == Tag.WHILE || look.tag == '{') {
            int lnext_stat = code.newLabel(); /* L1 */
            stat(lnext_stat);
            code.emitLabel(lnext_stat); /* L1: */
            statlistp();
            code.emit(OpCode.GOto,lnext_prog);
        } else {
            error("Syntax error statlist");
        }
    }

    //guida(SL' -> ; S SL') - ;
    //guida(SL' -> eps) - }
    private void statlistp() {
        switch (look.tag) {
            case ';':
                match(';');
                int lnext_stat = code.newLabel(); /* L2 */
                stat(lnext_stat);
                code.emitLabel(lnext_stat);
                statlistp();
                break;
            case '}':
            case Tag.EOF: // E'->ℇ
                break;
            default:
                error("Syntax error statlistp");
        }
    }

    /**
     * guida(S -> assign E to IL) - assign
     * guida(S -> print [EL]) - print
     * guida(S -> read [IL]) - read
     * guida(S -> while (B) S) - while
     * guida(S -> conditional [OL] S') - cond
     * guida(S -> {SL}) - {
     * */
    private void stat(int lnext_stat) {
        switch (look.tag) {
            case Tag.ASSIGN:
                match(Tag.ASSIGN);
                expr();
                match(Tag.TO);
                idlist(true);
                code.emit(OpCode.GOto,lnext_stat); // goto L2
                break;
            case Tag.PRINT:
                match(Tag.PRINT);
                match('[');
                exprlist(OpCode.invokestatic);
                match(']');
                code.emit(OpCode.invokestatic,1);
                code.emit(OpCode.GOto, lnext_stat);
                break;
            case Tag.READ:
                match(Tag.READ);
                match('[');
                idlist(false);
                match(']');
                code.emit(OpCode.GOto,lnext_stat); // goto L2
                break;
            case Tag.COND:
                match(Tag.COND);
                match('[');
                optlist(lnext_stat);
                match(']');
                statp(lnext_stat);
                break;
            case Tag.WHILE:
                match(Tag.WHILE);
                match('(');
                int while_true = code.newLabel();
                int stat_next = code.newLabel();
                code.emitLabel(stat_next);
                bexpr(while_true,lnext_stat);
                match(')');
                code.emitLabel(while_true);
                stat(stat_next);
                break;
            case '{':
                match('{');
                statlist(lnext_stat);
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
    private void statp(int lnext_stat){
        switch(look.tag){
            case Tag.END:
                match(Tag.END);
                break;
            case Tag.ELSE:
                match(Tag.ELSE);
                stat(lnext_stat);
                match(Tag.END);
                break;
            default:
                error("Syntax error statp");

        }
    }
    /**
     * guida(IL -> id IL') - id
     * */
    private void idlist(boolean writeTrue) {
        if (writeTrue) {            //assign                                    //salvataggio in memoria assign
            int id_addr = st.lookupAddress(((Word)look).lexeme);
            if (id_addr == -1) {
                id_addr = count;
                st.insert(((Word) look).lexeme, count++);
            }
            match(Tag.ID);
            if(look.tag == ',') code.emit(OpCode.dup);
            code.emit(OpCode.istore, id_addr);

        }else if (!writeTrue){        //read
            if(look.tag==Tag.ID){
                int id_addr = st.lookupAddress(((Word)look).lexeme);
                if (id_addr==-1) {
                    id_addr = count;
                    st.insert(((Word)look).lexeme,count++);
                }
                match(Tag.ID);
               
                code.emit(OpCode.invokestatic,0);
                code.emit(OpCode.istore,id_addr); // istore
            }
        }
        idlistp(writeTrue);
    }
    /**
     * guida(IL' -> , id IL') - ,
     * guida(IL' -> eps) - ";","}","end",option,"]", EOF
     * */

    private void idlistp(boolean writeTrue){
        switch(look.tag){
            case ',':
                match(',');
                int id_addr = st.lookupAddress(((Word) look).lexeme);
                    if (id_addr == -1) {
                        id_addr = count;
                        st.insert(((Word) look).lexeme, count++);
                    }
                    match(Tag.ID);
                    if(look.tag == ',') code.emit(OpCode.dup);
                  else if(!writeTrue)   code.emit(OpCode.invokestatic,0);

                    code.emit(OpCode.istore, id_addr);
                idlistp(writeTrue);
                break;
            case ';':

            case '}':
            case ']':
            case Tag.OPTION:
            case Tag.END:
            case Tag.EOF:
                break;
            default:
                error("Syntax error idlistp");
        }
    }

    /**
     * guida(B -> relop E E) - relop
     * */
    private void bexpr(int lnext_iftrue,int lnext_iffalse) {
        if (look.tag == Tag.RELOP) {
            switch (look.tag) {
                case Tag.RELOP:
                    String relop = ((Word) look).lexeme;
                    match(Tag.RELOP);
                    expr();
                    expr();
                    switch (relop) {
                        case ">":
                            code.emit(OpCode.if_icmpgt, lnext_iftrue);
                            break;
                        case "<":
                            code.emit(OpCode.if_icmplt, lnext_iftrue);
                            break;
                        case "==":
                            code.emit(OpCode.if_icmpeq, lnext_iftrue);
                            break;
                        case ">=":
                            code.emit(OpCode.if_icmpge, lnext_iftrue);
                            break;
                        case "<=":
                            code.emit(OpCode.if_icmple, lnext_iftrue);
                            break;
                        case "<>":
                            code.emit(OpCode.if_icmpne, lnext_iftrue);
                            break;
                    }
                    break;
            }
            code.emit(OpCode.GOto, lnext_iffalse);
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
    private void expr() {
        switch (look.tag) {
            case '+':
                match('+');
                match('(');
                exprlist(OpCode.iadd);
                match(')');
                break;
            case '-':
                match('-');
                expr();
                expr();
                code.emit(OpCode.isub);
                break;
            case '*':
                match('*');
                match('(');
                exprlist(OpCode.imul);
                match(')');
                break;
            case '/':
                match('/');
                expr();
                expr();
                code.emit(OpCode.idiv);
                break;
            case Tag.NUM:
                code.emit(OpCode.ldc, ((NumberTok)look).num);
                match(Tag.NUM);
                break;
            case Tag.ID:
                int id_addr = st.lookupAddress(((Word)look).lexeme);
                if (id_addr==-1) {
                    error("Identifier used but never created: " + look);


                }
                code.emit(OpCode.iload, id_addr);
                match(Tag.ID);
                break;
            default:
                error("Syntax error expr");
        }
    }

    /**
     * guida(EL -> E EL') - {+,-,*,/, NUM, ID}
     * */
    private void exprlist(OpCode op) {
        if (look.tag == '+' || look.tag == '-' || look.tag == '*' || look.tag == '/' || look.tag == Tag.NUM ||
                look.tag == Tag.ID) {
            expr();
            if(op == OpCode.invokestatic)
            exprlistp(op);
        }else {
            error("Syntax error exprlist");
        }
    }


    /**
     * guida(EL' -> , E EL') - {+,-,*,/, NUM, ID}
     * guida(EL' -> eps) - "]",")"
     * */
    private void exprlistp(OpCode op) {
        if (look.tag == ',') {
            match(',');
            expr();
            if(op == OpCode.invokestatic){
                code.emit(OpCode.invokestatic,1);
            }else{
                code.emit(op);
            }
            exprlistp(op);
        } else if (look.tag != ')' && look.tag != ']') {
            error("Syntax error exprlistp");
        }
    }
   
    //guida(OL -> OI OL') - option
    private void optlist(int lnext_stat){
        if(look.tag == Tag.OPTION){

            optitem(lnext_stat);
            optlistp(lnext_stat);
        }else {error("Syntax error optlist");}
    }

    /**
     * guida(OL' -> OI OL') - option
     * guida(OL' -> eps) - "]"
     * */
    private void optlistp(int lnext_stat){
        switch(look.tag){
            case Tag.OPTION:
                optitem(lnext_stat);
                optlistp(lnext_stat);
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
    private void optitem(int lnext_stat){
        if (look.tag == Tag.OPTION){
            int lnext_iftrue = code.newLabel();
            int lnext_iffalse = code.newLabel();
            match(Tag.OPTION);
            match('(');
            bexpr(lnext_iftrue,lnext_iffalse);
            match(')');
            match(Tag.DO);
            code.emitLabel(lnext_iftrue);
            stat(lnext_stat);
            code.emitLabel(lnext_iffalse);
        }else{
            error("Syntax error optitem");
        }
    }
}