package Analizador_Lexico;

import Token.Token;

import java.io.*;

public class Analizador_Lexico {
    public static int puntero = 0; // posicion del fichero a leer
    public static Integer linea = 0;
    public static String cad = "";
    public static Long num = 0L;
    private File file = null;
    private FileReader reader = null;

    private char[] aux;

    // Metodo para leer el fichero

    public void readFile(File fichero) throws FileNotFoundException {
        this.file = fichero;
        if (file == null) {
            System.err.println("Error: Fichero no encontrado.");
        }
        this.reader = new FileReader(this.file);
        aux = new char[(int) file.length()];
        try {
            reader.read(aux);
        } catch (IOException e) {
            System.err.println("Error: Error en lectura de fichero.");
        }

    }

    // Metodo saber si es digito (ASCII)

    private boolean digit(char num){
        return num > 47 && num < 58;
    }

    // Metodo saber si es letra (ASCII)

    private boolean letra(char letra){
        return (letra > 64 && letra < 91) || (letra > 96 && letra < 123);
    }

    // Proceso principal
    // #TODO PALABRAS RESERVADAS [IF, IF-ELSE] , GESTION DE ERRORES
    public Token procPrincipal(char[] lectura) {
        Token token = null;
        if (puntero == lectura.length) {
            // Final del fichero
            token = new Token("EOF",null);
        } else if (lectura[puntero] == '/') {
            puntero++;
            if (lectura[puntero] == '/') {
                basura(lectura);
            }
        } else if (lectura[puntero] == '\n') {
            puntero++;
            linea++;
        } else if (lectura[puntero] == '\"') {
            puntero++;
            cad ="";
            cadena(lectura);
            token = new Token("CAD",cad);
        }else if (lectura[puntero] == ' ' || lectura[puntero] == '\t'){
            // No se genera token por un espacio
            puntero++;
        }
        else if (lectura[puntero] == ','){
            puntero++;
            token = new Token("COMA",null);
        }
        else if (lectura[puntero] == '('){
            puntero++;
            token = new Token("OPENPAR",null);
        } else if (lectura[puntero] == ')'){
            puntero++;
            token = new Token("CLOSEPAR",null);
        }else if (lectura[puntero] == '{'){
            puntero++;
            token = new Token("OPENLLAVE",null);
        }else if (lectura[puntero] == '}'){
            puntero++;
            token = new Token("CLOSELLAVE",null);
        }else if (lectura[puntero] == '+'){
            puntero++;
            token = new Token("SUM",null);
        }else if (lectura[puntero] == '-'){
            puntero++;
            dec(lectura);
            token = new Token("DEC",null);
        }
        else if (lectura[puntero] == '>'){
            puntero++;
            token = new Token("MAYOR",null);
        } else if (lectura[puntero] == '|'){
            puntero++;
            oplog(lectura);
            token = new Token("OR", null);
        } else if (lectura[puntero] == '='){
            puntero++;
            token = new Token("ASIG", null);
        } else if (lectura[puntero] == ';'){
            puntero++;
            token = new Token("SEMICOLON",null);
        }else if (digit(lectura[puntero])){
            num = 0L;
            Integer entero = Character.getNumericValue(lectura[puntero]);
            num = entero.longValue();

            puntero++;
            numeros(lectura);
            if (num < Math.pow(2,15)){
                token = new Token("ENTERO",num.toString());
            }else{
                System.err.println("Error linea: "+ linea.toString() + "El numero "+ num.toString()+ " sobrepasa el limite");
            }
        }else if (letra(lectura[puntero])){
            // letras
            cad = Character.toString(lectura[puntero]);
            puntero++;
            palabra(lectura);

            if (cad.equals("true")){
                token = new Token("BOOL",cad);
            }else if (cad.equals("false")){
                token = new Token("BOOL",cad);
            }
            //#TODO TABLA DE SIMBOLOS
        }

        else{
            System.err.println("Error linea: "+ linea.toString()+" caracter no reconocido");
        }
        return token;
    }

    // Procesos para la generacion de tokens

    public void basura(char[] lectura) {
        while (puntero < lectura.length && lectura[puntero] != '\n') {
            puntero++;
        }

    }

    public void cadena(char[] lectura) {
        if(puntero < lectura.length && lectura[puntero] != '\"' && lectura[puntero] != '\n'){
            cad += Character.toString(lectura[puntero]);
            puntero++;
            cadena(lectura);
        }
        else if(puntero < lectura.length && lectura[puntero] == '\"'){
            puntero++;
            if (cad.length() > 64)
                System.err.println("Error linea: " + linea.toString() + "La cadena supera los 64 caracteres");
        }else{
            System.err.println("Error linea: "+ linea.toString() + "Salto de linea en cadena");
        }

    }

    public void oplog(char[] lectura){
        if(lectura[puntero] == '|'){
            puntero++;
        }
        else{
            System.err.println("Error linea: "+ linea.toString() + "Se esperaba '|' ");

        }
    }
    public void dec(char[] lectura){
        if(lectura[puntero] == '-'){
            puntero++;
        }else{
            System.out.println("Error linea: "+ linea.toString() + "Se esperaba '-' ");
        }
    }

    public void numeros(char [] lectura){
        if (puntero < lectura.length && digit(lectura[puntero])){
            num = num*10 + Character.getNumericValue(lectura[puntero]);
            puntero++;
            numeros(lectura);
        }
    }

    public void palabra (char [] lectura){
        if (puntero < lectura.length && (digit(lectura[puntero]) || letra(lectura[puntero]) || lectura[puntero] == '_')){
            cad += Character.toString(lectura[puntero]);
            puntero++;
            palabra(lectura);
        }
    }

}
