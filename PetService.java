import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LeituraDeArquivo {
    static File FILE = new File("C:\\Igor\\Java\\DesafioSistemaDeCadastro\\desafioCadastro\\formulario.txt");
    Scanner scanner = new Scanner(System.in);


    public void leituraDeFormulario(){
        List<String> questions = new ArrayList<>();
        Pet pet = new Pet();
        String answer=null;
        try(FileReader fileReader = new FileReader(FILE); BufferedReader bufferedReader = new BufferedReader(fileReader)){
            String linhaDeTexto;
            while((linhaDeTexto=bufferedReader.readLine())!=null){
                if(linhaDeTexto.isEmpty()){
                    continue;
                }
                questions.add(linhaDeTexto);
            }
            for(String question : questions){
                Pattern pattern = Pattern.compile("(^\\d)(.+)$");
                Matcher matcher = pattern.matcher(question);
                if(matcher.matches()){
                    int questionNumber = Integer.parseInt(matcher.group(1));
                    boolean respostaValida = false;
                    while(!respostaValida){
                        System.out.println(question);
                        if(questionNumber!=4){
                            System.out.println("Digite sua resposta : ");
                            answer = scanner.nextLine();
                        }
                        try{
                            respostaValida =  armazenaResposta(questionNumber,answer,pet);
                        }catch(RuntimeException error){
                            System.out.println(error.getMessage());
                        }
                    }
                }else{
                    System.out.println("Linha fora do padrão");
                }
            }
            RepositorioPet.adicionarPet(pet);
            savePetInFile(pet);
        }catch(IOException error){
            System.out.println("Erro ao ler o arquivo " + error.getMessage());
        }
    }

    public boolean armazenaResposta(int questionNumber, String resposta, Pet pet){
        boolean validaResposta = false;
        boolean respostaVazia = resposta.trim().isEmpty();


        switch(questionNumber){
            case 1:
                if(respostaVazia){
                    pet.setNome(Pet.NAO_INFORMADO);validaResposta = true;break;
                }
                String[] verificaNome = resposta.split("\\s+");
                if(verificaNome.length>=2 && !isThereAnySpecialChar(resposta)){
                    pet.setNome(resposta);
                }
                else{
                    throw new RuntimeException("Por favor verifique o nome. Ou está faltando sobrenome ou há caracteres inválidos");
                }
                validaResposta = true;
                break;
            case 2:
                try{
                    pet.setTipoDePet(TipoDePet.valueOf(resposta.toUpperCase()));
                    validaResposta = true;
                } catch (IllegalArgumentException e) {
                    System.out.println("Insira Cachorro ou Gato");
                }
                break;
            case 3:
                try{
                    pet.setSexoPet(SexoPet.valueOf(resposta.toUpperCase()));
                    validaResposta = true;
                } catch (IllegalArgumentException e) {
                    System.out.println("Insira Macho ou Femea");
                }
                break;
            case 4:
                System.out.println("Digite o número da casa : ");
                String houseNumber = scanner.nextLine();
                if(houseNumber.trim().isEmpty()){
                    houseNumber = Pet.NAO_INFORMADO;
                }
                else if(!isThereAnyNumber(houseNumber)){
                    throw new IllegalArgumentException("Insira apenas números ");
                }
                System.out.println("Digite o nome da cidade : ");
                String cityName = scanner.nextLine();
                if(isThereAnyNumber(cityName)){
                    throw new IllegalArgumentException("Não é permitido números ");
                }
                System.out.println("Digite o nome da rua : ");
                String streetName = scanner.nextLine();
                if(isThereAnyNumber(streetName)){
                    throw new IllegalArgumentException("Não é permitido números ");
                }
                Endereco endereco = new Endereco(houseNumber,cityName,streetName);
                pet.setEndereco(endereco);
                validaResposta = true;
                break;
            case 5:
                resposta = resposta.replace(',','.');
                if(respostaVazia){
                    pet.setIdade(Pet.NAO_INFORMADO);validaResposta = true;break;
                } else if(!isThereAnyNumber(resposta)){
                    throw new IllegalArgumentException("Insira apenas números");
                }else if(Double.parseDouble(resposta) > 20){
                    throw new IllegalArgumentException("Não é permitido colocar mais de 20 anos");
                }
                pet.setIdade(resposta);
                validaResposta = true;
                break;
            case 6:
                resposta = resposta.replace(',','.');
                if(respostaVazia){
                    pet.setPeso(Pet.NAO_INFORMADO);
                    validaResposta = true;
                    break;
                }else if(Double.parseDouble(resposta) < 0.5 || Double.parseDouble(resposta) > 60){
                    throw new RuntimeException("O peso não pode ser menor que 0.5kg ou maior que 60kg");
                }
                pet.setPeso(resposta);
                validaResposta = true;
                break;
            case 7:
                if(respostaVazia){
                    pet.setRaca(Pet.NAO_INFORMADO);
                    validaResposta = true;
                    break;
                }
                else if(isThereAnyNumber(resposta) || isThereAnySpecialChar(resposta)){
                    throw new IllegalArgumentException("Não pode ser colocado números ou caracteres especiais");
                }
                pet.setRaca(resposta);
                validaResposta = true;
                break;
        }
        return validaResposta;
    }

    public void savePetInFile(Pet pet){
        LocalDateTime date = LocalDateTime.now();
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        int hour = date.getHour();
        int minute = date.getMinute();
        String petAddress = String.format("%s,%s,%s",pet.getEndereco().getCity(),pet.getEndereco().getRua(),pet.getEndereco().getHouseNumber());
        String petName = pet.getNome().replace(" ","");
        String fileName = String.format("%d%d%dT%d%d-%S",year,month,day,hour,minute,petName);
        File file = new File("petsCadastrados\\"+fileName+".txt");
        try(FileWriter fileWriter = new FileWriter(file);BufferedWriter bufferedWriter= new BufferedWriter(fileWriter); ){
            bufferedWriter.write("1 - "+pet.getNome());
            bufferedWriter.newLine();
            bufferedWriter.write("2 - " + pet.getTipoDePet().toString());
            bufferedWriter.newLine();
            bufferedWriter.write("3 - " + pet.getSexoPet().toString());
            bufferedWriter.newLine();
            bufferedWriter.write("4 - " + petAddress);
            bufferedWriter.newLine();
            bufferedWriter.write("5 - " + pet.getIdade());
            bufferedWriter.newLine();
            bufferedWriter.write("6 - " + pet.getPeso() + "kg");
            bufferedWriter.newLine();
            bufferedWriter.write("7 - " +pet.getRaca());
            bufferedWriter.flush();
        }catch(IOException error){
            System.out.println("Erro ao tentar salvar o arquivo :" + error.getMessage());
        }
    }

    public boolean isThereAnyNumber(String text){
        Pattern patternOnlyNumbers = Pattern.compile("^(\\d)+([\\.,])?(\\d)?$");
        Matcher matcherNumber = patternOnlyNumbers.matcher(text);
        return matcherNumber.find();
    }
    public boolean isThereAnySpecialChar(String text){
        Pattern patternOnlyLetters = Pattern.compile("[^a-zA-Z $]");
        Matcher matcherOnlyLetters =  patternOnlyLetters.matcher(text);
        return matcherOnlyLetters.find();
    }
}
