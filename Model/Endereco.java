package Model;

public class Endereco {
    private String houseNumber;
    private String city;
    private String rua;

    public Endereco(){

    }

    public Endereco(String houseNumber, String city, String rua) {
        this.houseNumber = houseNumber;
        this.city = city;
        this.rua = rua;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }
}
