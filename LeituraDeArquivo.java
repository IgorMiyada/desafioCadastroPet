import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LeituraDeArquivo {
    static File FILE = new File("C:\\Igor\\Java\\DesafioSistemaDeCadastro\\desafioCadastro\\formulario.txt");

    public void cadastroPet(){
        Scanner scanner = new Scanner(System.in);
        List<String> questions = new ArrayList<>();
        List<String> answers = new ArrayList<>();
        Pet pet = new Pet();
        try(FileReader fileReader = new FileReader(FILE); BufferedReader bufferedReader = new BufferedReader(fileReader)){
            String linhaDeTexto;
            while((linhaDeTexto=bufferedReader.readLine())!=null){
                System.out.println(linhaDeTexto);
            }
        }catch(IOException error){
            System.out.println("Erro ao ler o arquivo " + error.getMessage());
        }
    }

}
