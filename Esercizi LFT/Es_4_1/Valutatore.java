package Es_4_1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Valutatore {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;
    public Valutatore(Lexer l, BufferedReader br) {
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
        int expr_val;
        if (look.tag == '(' || look.tag == Tag.NUM){
        expr_val = expr();
        System.out.print("Result : " + expr_val + "\n");
        match(Tag.EOF);
        } else {
            error("Syntax error start.");
        }
    }
    /**guida(expr -> <term><expr>) - {(, NUM}*/
    private int expr() {
        int term_val, exprp_val;
        if (look.tag == '(' || look.tag == Tag.NUM) {
            term_val = term();
            exprp_val =  exprp(term_val);
            return exprp_val;
        }else {
            error("Syntax error expr");
            return -1;
        }
    }

    /**
     * guida(E'->+<term><exprp>) = {+}
     * guida(E'->-<term><exprp>) = {-}
     * guida(E'->eps) = follow(E') = {$, )}
     */

    private int exprp(int exprp_i) {
        int term_val, exprp_val;
        switch (look.tag) {
            case '+':
                match('+');
                term_val = term();
                exprp_val = exprp(exprp_i + term_val);
                return exprp_val;
            case '-':
                match('-');
                term_val = term();
                exprp_val = exprp(exprp_i - term_val);
                return exprp_val;
            case ')':
            case Tag.EOF:
                return exprp_i;
            default:
                error("Syntax error exprp");
        }
        return 0;
    }

    /**
     * guida(term-> <fact><termp>) = {(, NUM}
     */
    private int term() {
        int fact_val, termp_val;
        if (look.tag == '(' || look.tag == Tag.NUM) {
            fact_val = fact();
            termp_val = termp(fact_val);
            return termp_val;
        } else {
            error("Syntax error term");
            return 0;
        }

    }

    /**
     * guida(T'->*<fact><termp>) = {*}
     * guida(T'->/<fact><termp>) = {/}
     * guida(T' -> eps) = follow(T') = {$,),+,-}
     */
    private int termp(int termp_i) {
        int fact_val, termp_val;
        switch(look.tag){
            case '*':
                match('*');
                fact_val = fact();
                termp_val = termp(termp_i * fact_val);
                return termp_val;
            case '/':
                match('/');
                fact_val = fact();
                termp_val = termp(termp_i / fact_val);
                return termp_val;
            case ')':
            case '+':
            case '-':
            case Tag.EOF:
                return termp_i;
            default:
                error("Syntax error termp");
                return 0;
        }

    }
    /**
     * guida(F -> (<expr>)) = {(}
     * guida(F->NUM) = {NUM}
     */

    private int fact() {
        int expr_val, num_val;
        switch (look.tag) {
            case '(':
                match('(');
                expr_val = expr();
                match(')');
                return expr_val;
            case Tag.NUM:
                match(Tag.NUM);
                num_val = lex.getN();
                System.out.println("numero : " + num_val);
                return num_val;
            default:
                error("Syntax error fact");
        }
        return 0;
    }




    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "ParserTest.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Valutatore valutatore = new Valutatore(lex, br);
            valutatore.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }



 }