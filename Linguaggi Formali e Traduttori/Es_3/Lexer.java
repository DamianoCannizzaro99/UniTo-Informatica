package Es_3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Lexer {

    public static int line = 1;
    private char peek = ' ';
    
    /**
     * verifica l'esistenza e legge un carattere sulla stringa
     * @param peek carattere letto da stringa
     */
    private void readch(BufferedReader br) {
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }

    public Token lexical_scan(BufferedReader br) {
        while (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r') {      //lettura dei caratteri 
            if (peek == '\n') line++;
            readch(br);                                                             
        }
        while (peek == '/'){                    //controllo commenti
            readch(br); 
            if (peek == '/') {                  // commenti "//"
                comments(br, 0);
                peek = ' ';
                readch(br);
                if (peek  == (char) -1) {
                    return new Token(Tag.EOF);
                }

            } else if (peek == '*') {           // commenti "/**/"
                comments(br, 1);
                peek = ' ';
                if (peek  == (char) -1) {
                    return new Token(Tag.EOF);
                }
            } else {
                return Token.div;
            }
            while (peek == ' ' || peek == '\t' || peek == '\n' || peek == '\r') {
                if (peek == '\n')
                    line++;
                readch(br);
            }
        }

        
        switch (peek) {
            case '!':
                peek = ' ';
                return Token.not;
            case '(':
                peek = ' ';
                return Token.lpt;
            case ')':
                peek = ' ';
                return Token.rpt;
            case '[':
                peek = ' ';
                return Token.lpq;
            case ']':
                peek = ' ';
                return Token.rpq;
            case '{':
                peek = ' ';
                return Token.lpg;
            case '}':
                peek = ' ';
                return Token.rpg;
            case '+':
                peek = ' ';
                return Token.plus;
            case '-':
                peek = ' ';
                return Token.minus;
            case '*':
                peek = ' ';
                return Token.mult;

            case ';':
                peek = ' ';
                return Token.semicolon;
            case ',':
                peek = ' ';
                return Token.comma;

            case '&':                                   // gestione operatori relazionali
                readch(br);
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {
                    System.err.println("Erroneous character"
                            + " after & : "  + peek );
                    return null;
                }
            case '|':
                readch(br);
                if (peek == '|') {
                    peek = ' ';
                    return Word.or;
                } else {
                    System.err.println("Erroneous character"
                            + " after | : "  + peek );
                    return null;
                }
            case '=':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.eq;
                } else {
                    System.err.println("Erroneous character"
                            + " after = : "  + peek );
                    return null;
                }
            case '<':
                readch(br);
                if (peek == '>') {
                    peek = ' ';
                    return Word.ne;
                } else if (peek == '='){
                    peek = ' ';
                    return Word.le;
                } else if (peek == ' '){
                    return Word.lt;
                } else {
                    System.err.println("Erroneous character"
                            + " after | : "  + peek );
                    return null;
                }
            case '>':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.ge;
                } else if(peek == ' '){
                    return Word.gt;
                } else {
                    System.err.println("Erroneous character"
                            + " after | : "  + peek );
                    return null;
                }



          
            case (char)-1:
                return new Token(Tag.EOF);

            default:                                                //gestione degli id
                if (Character.isLetter(peek) || peek == '_' ) {
                    String id = "";
                  while (Character.isLetter(peek) || Character.isDigit(peek) || peek == '_'){
                      id += Character.toString(peek);           //aggiorno il valore id
                      readch(br);
                  }
                  if(underscoreCheck(id)){System.err.println("String cannot be written only by '_'");
                  return null;}
                    return switch (id) {
                        case "assign" -> Word.assign;
                        case "to" -> Word.to;
                        case "conditional" -> Word.conditional;
                        case "option" -> Word.option;
                        case "do" -> Word.dotok;
                        case "else" -> Word.elsetok;
                        case "while" -> Word.whiletok;
                        case "begin" -> Word.begin;
                        case "end" -> Word.end;
                        case "print" -> Word.print;
                        case "read" -> Word.read;
                        default -> new Word(Tag.ID, id);
                    };

                } else if (Character.isDigit(peek)) {               // gestione riconoscimento numeri
                    int n = 0;          
                    do {
                        n = n*10 + (peek - '0');
                        readch(br);
                        if (peek == '_') {
                            System.err.println("Warning, identifier cannot start with numbers" );
                            return null;
                        }

                    }while (Character.isDigit(peek));
                    return new NumberTok(Tag.NUM, n);
                } else {
                        System.err.println("Erroneous character: " 
                                + peek );
                        return null;
                }
         }
    }
    /**
     * gestisce i commenti del file, differenziando tra commenti a riga singola e a più righe
     * @param br file da leggere
     * @param commentType tipo di commento: 0 riga singola, 1: righe multiple
     */
    private void comments(BufferedReader br,int commentType){
        boolean exit = true;
        String myKey = "";
        char prev= 'x';
        do{
            prev= peek;
            if(peek=='*'){
                myKey += peek;
            }
            readch(br);
            if(peek == (char) -1 && commentType == 1){
                System.err.println("Error while reading the comment: Comment not closed correctly");
                System.exit(-1);
            }else
            if (commentType == 0 ) {
                if (peek == '\n' || peek == (char) -1) {
                    exit = false;
                }
            } else {
                if(prev=='*' && peek=='/'){
                    exit=false;
                }
            }
        } while (exit);
        if(myKey.length()==1){
            System.err.println("Error while reading the comment: Comment not closed correctly");
            System.exit(-1);
        }
    }

    /**controlla se la stringa e' composta di soli simboli '_'
     * @param id stringa di simboli da controllare
     * @return true se la stringa è di soli '_'
     * @return false altrimenti
     */
    private boolean underscoreCheck(String id){
            for (int i = 0; i < id.length();i++){
                if (id.charAt(i)!= '_') {
                    return false;}
            }
            return true;
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "LexerTest"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;
            do {
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);
                if(tok == null) {
                    System.err.println("Error, token must be not null.");
                    br.close();
                    break;
                }
            } while (tok.tag != Tag.EOF);

            br.close();
        } catch (IOException e) {e.printStackTrace();}    
    }

}
