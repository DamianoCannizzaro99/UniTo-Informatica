

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
public class Parser{
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;
    public Parser(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }
    void move() {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);

    }
    void error(String s) {
        throw new Error("near line " + lex.line + ": " + s);
    }
    void match(int t) {
        if (look.tag == t) {
            if (look.tag != Tag.EOF) move();
        } else error("syntax error");
    }
    /**
     * guida(START -> <expr> EOF) - {(, NUM}
     */
    public void start() {
        if (look.tag == '(' || look.tag == Tag.NUM){
        expr();
        match(Tag.EOF);
        } else {
            error("Syntax error start.");
        }
    }
    /**guida(expr -> <term><expr>) - {(, NUM}*/
    private void expr() {
        if (look.tag == '(' || look.tag == Tag.NUM) {
            term();
            exprp();
        }else {
            error("Syntax error expr");
        }
    }

    /**
     * guida(E'->+<term><exprp>) = {+}
     * guida(E'->-<term><exprp>) = {-}
     * guida(E'->eps) = follow(E') = {$, )}
     */

    private void exprp() {
        switch (look.tag) {
            case '+':
                match('+');
                term();
                exprp();
                break;
            case '-':
                match('-');
                term();
                exprp();
                break;
            case ')':
            case Tag.EOF:
                break;
            default:
                error("Syntax error exprp");

        }
    }

    /**
     * guida(term-> <fact><termp>) = {(, NUM}
     */
    private void term() {
        if (look.tag == '(' || look.tag == Tag.NUM) {
            fact();
            termp();
        } else {
            error("Syntax error term");
        }
    }

    /**
     * guida(T'->*<fact><termp>) = {*}
     * guida(T'->/<fact><termp>) = {/}
     * guida(T' -> eps) = follow(T') = {$,),+,-}
     */
    private void termp() {
        switch(look.tag){
            case '*':
                match('*');
                fact();
                termp();
                break;
            case '/':
                match('/');
                fact();
                termp();
                break;
            case ')':
            case '+':
            case '-':
            case Tag.EOF:
                break;
            default:
                error("Syntax error termp");
        }
    }
    /**
     * guida(F -> (<expr>)) = {(}
     * guida(F->NUM) = {NUM}
     */

    private void fact() {
        switch (look.tag) {
            case '(':
                match('(');
                expr();
                match(')');
                break;
            case Tag.NUM:
                match(Tag.NUM);
                break;
            default:
                error("Syntax error fact");
        }
    }




    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "ParserTest.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }



 }