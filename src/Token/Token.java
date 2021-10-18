package Token;

public class Token {
    private String codigo;
    private String atributo;

    public Token(String codigo, String atributo){
        this.codigo = codigo;
        this.atributo = atributo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getAtributo() {
        return atributo;
    }

    public void setAtributo(String atributo) {
        this.atributo = atributo;
    }

    @Override
    public String toString() {
        String ret = "< ";
        if(this.getCodigo()!=null){
            ret += this.getCodigo() + ", ";
        }
        if(this.getAtributo() != null){
           ret += this.getAtributo() + " >";
        }else{
            ret += ">";
        }
        return ret;
    }
}
