package Model;
import Enum.TipoDePet;
import Enum.SexoPet;


public class Pet {
    private String nome;
    private TipoDePet tipoDePet;
    private SexoPet sexoPet;
    private Endereco endereco = new Endereco();
    private String idade;
    private String peso;
    private String raca;
    public static final String NAO_INFORMADO = "NÃO INFORMADO";

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoDePet getTipoDePet() {
        return tipoDePet;
    }

    public void setTipoDePet(TipoDePet tipoDePet) {
        this.tipoDePet = tipoDePet;
    }

    public SexoPet getSexoPet() {
        return sexoPet;
    }

    public void setSexoPet(SexoPet sexoPet) {
        this.sexoPet = sexoPet;
    }

    public Endereco getEndereco(){
        return this.endereco;
    }

    public void setEndereco(Endereco endereco){
        this.endereco = endereco;
    }

    public String getIdade() {
        return idade;
    }

    public void setIdade(String idade) {
        this.idade = idade;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }
}
