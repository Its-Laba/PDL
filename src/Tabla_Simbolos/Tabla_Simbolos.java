package Tabla_Simbolos;

import Token.Token;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Tabla_Simbolos {
    public int indice = 2;
    private int registros = 0;
    private ArrayList<Object[]> Tabla ;
    private int desp = 0;

    // #TODO GETS y BORRADO
    public Tabla_Simbolos(){
        Tabla = new ArrayList<Object[]>();

    }
    public void addPR(String cad)  {
    switch (cad){
        case("function"):
            this.addToken(new Token("RES", "function"));
            this.addDire(new Token("RES", "function"), "function".length());
            this.addTipo(new Token("RES", "function"), "RES");
            break;
        case("if"):
        this.addToken(new Token("RES", "if"));
        this.addDire(new Token("RES", "if"), "if".length());
        this.addTipo(new Token("RES", "if"), "RES");
        break;
        case("else"):
        this.addToken(new Token("RES", "else"));
        this.addDire(new Token("RES", "else"), "else".length());
        this.addTipo(new Token("RES", "else"), "RES");
        break;
        case("input"):
        this.addToken(new Token("RES", "input"));
        this.addDire(new Token("RES", "input"), "input".length());
        this.addTipo(new Token("RES", "input"), "RES");
        break;
        case("let"):
        this.addToken(new Token("RES", "let"));
        this.addDire(new Token("RES", "let"), "let".length());
        this.addTipo(new Token("RES", "let"), "RES");
        break;
        case("return"):
        this.addToken(new Token("RES", "return"));
        this.addDire(new Token("RES", "return"), "return".length());
        this.addTipo(new Token("RES", "return"), "RES");
        break;
        case("print"):
        this.addToken(new Token("RES", "print"));
        this.addDire(new Token("RES", "print"), "print".length());
        this.addTipo(new Token("RES", "print"), "RES");
        break;
        case("int"):
        this.addToken(new Token("RES", "int"));
        this.addDire(new Token("RES", "int"), "int".length());
        this.addTipo(new Token("RES", "int"), "RES");
        break;
        case("string"):
        this.addToken(new Token("RES", "string"));
        this.addDire(new Token("RES", "string"), "string".length());
        this.addTipo(new Token("RES", "string"), "RES");
        break;
        case("boolean"):
        this.addToken(new Token("RES", "boolean"));
        this.addDire(new Token("RES", "boolean"), "boolean".length());
        this.addTipo(new Token("RES", "boolean"), "RES");
        break;
    }
    }

    public ArrayList<Object[]> getTabla() {
        return Tabla;
    }

    public void addToken(Token token){
        /*
         * Posicion 0 = Lexema
         * Posicion 1 = Tipo
         * Posicion 2 = Direccion
         * ----Solo funciones------
         * Posicion 3 = Nª Parametros
         * Posicion 4 = Tipo devuelto
         * Posicion 5 = Etiqueta
         * */
        boolean eslocal = false;
        if(registros > 0 && this.Tabla.get(registros-1)[0] instanceof Tabla_Simbolos){
            Tabla_Simbolos tLocal = (Tabla_Simbolos) Tabla.get(registros-1)[0];
            tLocal.addToken(token);
            eslocal = true;
        }
        if (!eslocal){
            this.Tabla.add(new Object[6]); // Incrementa posicion
            this.Tabla.get(registros)[0] = token.getAtributo();
            registros++;
        }
    }

    public void addTipo (Token token, String tipo){
        if (buscar(token.getAtributo())[0] != null){
            Integer [] cont = buscar(token.getAtributo());
            if (cont[1] == 1){
                Tabla_Simbolos tLocal = (Tabla_Simbolos) this.Tabla.get(registros-1)[0];
                tLocal.getTabla().get(cont[0])[1] = tipo;
            } else{
                this.Tabla.get(cont[0])[1] = tipo;
            }

        }
    }

    public void addPar(int par){
        // menos dos porque ya se ha creado la primera entrada de la tabla de la funcion
        this.Tabla.get(registros-2)[3] = par;
    }

    public void addEtiqueta () {
        String etiqueta = "FUNCION ";
        etiqueta +=  this.Tabla.get(registros-2)[0];
        this.Tabla.get(registros-2)[5] = etiqueta;
    }
    public void addDevolver (String tipo){

        this.Tabla.get(registros-2)[4] = tipo;
    }
    public void addDire (Token token, int size){
        if (this.buscar(token.getAtributo())[0] != null){
            Integer [] cont = buscar(token.getAtributo());
            if (cont[1] == 1){
                Tabla_Simbolos tLocal = (Tabla_Simbolos) this.Tabla.get(registros-1)[0];
                tLocal.getTabla().get(cont[0])[2] = tLocal.desp += size;
            } else{
                this.Tabla.get(cont[0])[2] = desp;
                desp += size;
            }
        }
    }


    public Integer[] buscar(String cadena){
        Integer [] local = new Integer[2];
        if(registros > 0 && this.Tabla.get(registros-1)[0] instanceof Tabla_Simbolos){
            Tabla_Simbolos tLocal = (Tabla_Simbolos) Tabla.get(registros-1)[0];
            tLocal.buscar(cadena);
            local[1] = 1;
        }
        if (local[0] == null){
            Integer[] contador = new Integer[2];
            contador[0] = 0;
            contador[1] = 0;
            boolean encontrado = false;
            while(contador[0] < this.Tabla.size() && !encontrado){
                if(this.Tabla.get(contador[0])[0].equals(cadena)){
                    encontrado = true;
                }else
                    contador[0]++;
            }
            if (!encontrado){
                contador[0] = null;
            }
            return contador;}
        return local;
    }
    public boolean palabraRes (String cadena){
        return "if".equals(cadena) || "else".equals(cadena) ||
                "function".equals(cadena) || "input".equals(cadena) ||
                "let".equals(cadena) || "return".equals(cadena) ||
                "print".equals(cadena) || "int".equals(cadena) ||
                "string".equals(cadena) || "boolean".equals(cadena);
    }

    public void printTabla(BufferedWriter wr)throws IOException {
        Tabla_Simbolos printear;
        if (this.Tabla.get(registros-1)[0] instanceof Tabla_Simbolos){
            // tabla de funcion
            printear = (Tabla_Simbolos) this.Tabla.get(registros-1)[0] ;
            wr.newLine();
            wr.newLine();
            wr.newLine();
        }else {
            // tabla pricipal
            printear = this;
            wr.write("TABLA PRINCIPAL # 1 :");
            wr.newLine();
            wr.newLine();
        }
        for (int i = 0; i<printear.Tabla.size(); i++){
            Object[] fila = printear.Tabla.get(i);

            for (int j = 0; j < fila.length && fila[j] != null; j++){

                if (j == 0) {
                    wr.write("* LEXEMA : '" + fila[j].toString() + "'");
                    wr.newLine();
                } else if (j == 1) {
                    wr.write("\tATRIBUTOS : ");
                    wr.newLine();
                    wr.write(
                            "\t+ tipo : '" + fila[j].toString() + "'");
                    wr.newLine();
                } else if (j == 2) {
                    wr.write(
                            "\t+ dirección : " + fila[j].toString());
                    wr.newLine();
                } else if (j == 3) {
                    wr.write(
                            "\t+ número de parámetros : " + fila[j].toString());
                    wr.newLine();
                }
                else if (j == 5) {
                    wr.write(
                            "\t+ tipo devuelto : '" + fila[j].toString() + "'");
                    wr.newLine();
                } else if (j == 6) {
                    wr.write(
                            "\t+ etiqueta : '" + fila[j].toString() + "'");
                    wr.newLine();
                }
                wr.flush();
            }

        }
        wr.newLine();
        wr.write("-------------------------------------------------");
        wr.newLine();
    }

    public int getDesp() {
        return desp;
    }

    public int getIndice() {
        return indice;
    }

    public int getRegistros() {
        return registros;
    }
}
