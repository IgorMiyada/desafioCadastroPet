package Service;

import java.util.List;
import java.util.Scanner;

public class MenuInicialService {

    @Override
    public String toString() {
        return "Selecione uma das opções para continuar\n" +
                "1.Cadastrar um pet\n" +
                "2.Alterar os dados do pet cadastrado\n" +
                "3.Deletar um pet cadastrado\n" +
                "4.Listar todos os pets cadastrados\n" +
                "5.Listar pets por algum critério(idade,nome,raça)\n" +
                "6.Sair";
    }

    public void opcoesMenu(int opcaoMenu){
        PetService petService = new PetService();

        switch (opcaoMenu){
            case 1:
                petService.leituraDeFormulario();
                break;
            case 2:
                menuBusca();
                break;
            case 3:
                System.out.println("Deletar um pet cadastrado");
                break;
            case 4:
                System.out.println("Listar odos os pets cadastrados");
                break;
            case 5:
                System.out.println("Listar pets por algum critério");
                break;
            case 6:
                System.out.println("Sair");break;
            default :
                System.out.println("Opção inválida! Digite outra opção ");
                break;
        }
    }
    public void menuBusca() {
        Scanner scanner = new Scanner(System.in);
        List<String> resultado;
        String criterio1="",criterio2 = "";
        int tipo2 = 0;
        System.out.println("=== Busca personalizada de pets ===");
        System.out.println("1-nome,2-Tipo de animal,3-Sexo,4-Endereço,5-idade,6-Peso,7-Raça");
        System.out.println("Digite qual você deseja filtrar : ");
        int tipo1 = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Digite o dado desejado : ");
        criterio1 = scanner.nextLine();
        System.out.println("Você deseja adicionar outro componente a sua busca ?(s/n)");
        if(scanner.next().equalsIgnoreCase("s")){
            System.out.println("1-nome,2-Tipo de animal,3-Sexo,4-Endereço,5-idade,6-Peso,7-Raça");
            System.out.println("Digite qual você deseja filtrar : ");
            tipo2 = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Digite o outro dado que você deseja fazer busca : ");
            criterio2 = scanner.nextLine();
        }

        if(!criterio2.isEmpty()){
            resultado = PetService.buscarPorCriterios(tipo1,criterio1.toUpperCase(),tipo2,criterio2);
        }else{
            resultado = PetService.buscarPorCriterios(tipo1,criterio1.toUpperCase());
        }

        System.out.println("\n=== Resultados encontrados ===");
        if (resultado.isEmpty()) {
            System.out.println("Nenhum pet encontrado com os critérios informados.");
        } else {
            for (String string : resultado) {
                System.out.println(string);
                System.out.println("==============================================");
            }
        }
    }

}
