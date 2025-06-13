import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MenuInicial menu = new MenuInicial();
        int opcaoSelecionada;

        while(true){
            try{
                System.out.println(menu);
                System.out.print("Digite um número : ");
                opcaoSelecionada = scanner.nextInt();

                if(opcaoSelecionada>0 && opcaoSelecionada<7){
                    menu.opcoesMenu(opcaoSelecionada);
                    break;
                }
                else{
                    System.out.println("Opção inválida! Digite uma opção válida");
                }
            }catch(InputMismatchException error){
                System.out.println("É aceito apenas números, tente novamente");
                scanner.next();
            }
        }


    }
}
