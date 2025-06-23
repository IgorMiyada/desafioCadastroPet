import java.util.ArrayList;
import java.util.List;

public class RepositorioPet {

    private static List<Pet> listaPets = new ArrayList<>();

    public static void adicionarPet(Pet pet){
        System.out.println("Adicionando pet na lista...");
        listaPets.add(pet);
    }
}
