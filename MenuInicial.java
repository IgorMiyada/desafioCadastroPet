import javax.crypto.spec.PSource;
import java.util.Scanner;

public class MenuInicial {

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
        LeituraDeArquivo leituraDeArquivo = new LeituraDeArquivo();

        switch (opcaoMenu){
            case 1:
                leituraDeArquivo.cadastroPet();
                break;
            case 2:
                leituraDeArquivo.cadastroPet();
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
}
