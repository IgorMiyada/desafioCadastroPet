package Service;

import Model.Pet;
import Enum.TipoDePet;
import Enum.SexoPet;
import Model.Endereco;
import Repository.RepositorioPet;
import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PetService {
    static File FILE = new File("C:\\Igor\\Java\\DesafioSistemaDeCadastro\\desafioCadastro\\formulario.txt");
    Scanner scanner = new Scanner(System.in);


    public List<String> leituraDeFormulario(File file){
        List<String> linhasDoArquivo = new ArrayList<>();
        try(FileReader fileReader = new FileReader(file); BufferedReader bufferedReader = new BufferedReader(fileReader)){
            String linhaDeTexto;
            while((linhaDeTexto=bufferedReader.readLine())!=null){
                if(linhaDeTexto.isEmpty()){
                    continue;
                }
                linhasDoArquivo.add(linhaDeTexto);
            }
        }catch(IOException error){
            System.out.println("Erro ao ler o arquivo " + error.getMessage());
        }
        return linhasDoArquivo;
    }

    public void cadastroDePet(){
        Pet pet = new Pet();
        String answer = null;
        List<String> linhasDoArquivo = leituraDeFormulario(FILE);
        for(String lineText : linhasDoArquivo){
            Pattern pattern = Pattern.compile("(^\\d)(.+)$");
            Matcher matcher = pattern.matcher(lineText);
            if(matcher.matches()){
                int questionNumber = Integer.parseInt(matcher.group(1));
                boolean respostaValida = false;
                while(!respostaValida){
                    System.out.println(lineText);
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
    }

    public void alteracaoPet(int tipo1,String criterio1,int tipo2,String criterio2){
        File file = new File("");
        if(criterio2.isEmpty()){
            file = buscarPorCriterios(tipo1,criterio1);
        }
        else{
            file = buscarPorCriterios(tipo1,criterio1,tipo2,criterio2);
        }
        if(file.exists()){
            Pet pet = new Pet();
            String answer = null;
            List<String> linhasDoArquivo = leituraDeFormulario(file);
            for(String lineText : linhasDoArquivo){
                Pattern pattern = Pattern.compile("(^\\d)(\\s*-\\s*)(.+)$");
                Matcher matcher = pattern.matcher(lineText);
                if(matcher.matches()){
                    int questionNumber = Integer.parseInt(matcher.group(1));
                    boolean respostaValida = false;
                    while(!respostaValida){
                        if(questionNumber==2 || questionNumber==3){
                            answer = matcher.group(3);
                        }
                        else if(questionNumber!=4){
                            System.out.println("Dado atual : " + lineText);
                            System.out.println("Digite o dado para ser atualizado : ");
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
            updatePetData(pet,file);
        }
        else return;

    }

    public void deletaPet(int tipo1,String criterio1,int tipo2,String criterio2){
        Scanner scanner = new Scanner(System.in);
        File file = new File("");
        if(criterio2.isEmpty()) {
            file = buscarPorCriterios(tipo1, criterio1);
        }else{
            file = buscarPorCriterios(tipo1,criterio1,tipo2,criterio2);
        }
        if(file.exists()){
            while(true){
                System.out.println("Deseja mesmo excluir o pet ? (s/n)");
                String confirmacao = scanner.next();
                if(!confirmacao.equalsIgnoreCase("s") && !confirmacao.equalsIgnoreCase("n")){
                    System.out.println("Resposta fora do escopo");
                }
                else{
                    try{
                        file.delete();
                        System.out.println("Arquivo deletado com sucesso");
                        Thread.sleep(2000);
                        break;
                    }catch(RuntimeException | InterruptedException error){
                        System.out.println(error.getMessage());
                    }
                }
            }

        }
    }

    public boolean armazenaResposta(int questionNumber, String resposta, Pet pet){
        boolean validaResposta = false;

        switch(questionNumber){
            case 1:
                if(isTheAnswerEmpty(resposta)){
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
                if(isTheAnswerEmpty(houseNumber)){
                    houseNumber = Pet.NAO_INFORMADO;
                }
                else if(!isThereAnyNumber(houseNumber)){
                    throw new IllegalArgumentException("Insira apenas números ");
                }
                System.out.println("Digite o nome da rua : ");
                String streetName = scanner.nextLine();
                if(isThereAnyNumber(streetName)){
                    throw new IllegalArgumentException("Não é permitido números ");
                }
                System.out.println("Digite o nome da cidade : ");
                String cityName = scanner.nextLine();
                if(isThereAnyNumber(cityName)){
                    throw new IllegalArgumentException("Não é permitido números ");
                }
                Endereco endereco = new Endereco(houseNumber,cityName,streetName);
                pet.setEndereco(endereco);
                validaResposta = true;
                break;
            case 5:
                resposta = resposta.replace(',','.');
                if(isTheAnswerEmpty(resposta)){
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
                if(isTheAnswerEmpty(resposta)){
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
                if(isTheAnswerEmpty(resposta)){
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
            bufferedWriter.write("5 - " + pet.getIdade() + " anos");
            bufferedWriter.newLine();
            String peso = pet.getPeso().equals(Pet.NAO_INFORMADO) ? pet.getPeso() : (pet.getPeso() + " kg");
            bufferedWriter.write("6 - " + peso);
            bufferedWriter.newLine();
            bufferedWriter.write("7 - " +pet.getRaca());
            bufferedWriter.flush();
            System.out.println("Arquivo pet Salvo");
            Thread.sleep(2000);
        }catch(IOException | InterruptedException error){
            System.out.println("Erro ao tentar salvar o arquivo :" + error.getMessage());
        }
    }

    public void updatePetData(Pet pet, File file){
        File newFileName = null;
        String oldFileName = file.toString();
        Pattern pattern = Pattern.compile("(.+?)\\\\(.+)-(.+)(\\.txt)");
        Matcher matcher = pattern.matcher(oldFileName);
        if(matcher.matches()){
            String petName = pet.getNome().replace(" ","");
            newFileName = new File("petsCadastrados\\"+matcher.group(2)+"-"+petName+".txt");
        }
        String petAddress = String.format("%s,%s,%s",pet.getEndereco().getCity(),pet.getEndereco().getRua(),pet.getEndereco().getHouseNumber());
        try(FileWriter fileWriter = new FileWriter(file);BufferedWriter bufferedWriter= new BufferedWriter(fileWriter); ){
            bufferedWriter.write("1 - "+pet.getNome());
            bufferedWriter.newLine();
            bufferedWriter.write("2 - " + pet.getTipoDePet().toString());
            bufferedWriter.newLine();
            bufferedWriter.write("3 - " + pet.getSexoPet().toString());
            bufferedWriter.newLine();
            bufferedWriter.write("4 - " + petAddress);
            bufferedWriter.newLine();
            bufferedWriter.write("5 - " + pet.getIdade() + " anos");
            bufferedWriter.newLine();
            String peso = pet.getPeso().equals(Pet.NAO_INFORMADO) ? pet.getPeso() : (pet.getPeso() + " kg");
            bufferedWriter.write("6 - " + peso);
            bufferedWriter.newLine();
            bufferedWriter.write("7 - " +pet.getRaca());
            bufferedWriter.flush();
            System.out.println("Arquivo pet atualizado");
            Thread.sleep(2000);
        }catch(IOException | InterruptedException error){
            System.out.println("Erro ao tentar salvar o arquivo :" + error.getMessage());
        }
        file.renameTo(newFileName);
    }

    public File[] listaPets(){
        File petFolder = new File("petsCadastrados\\");

        FileFilter fileFilter = new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().endsWith(".txt");
            }
        };

        File [] arquivosTxt = petFolder.listFiles(fileFilter);
        return arquivosTxt;
    }

    public File buscarPorCriterios(int tipo1,String criterio1){
        List<File> files = new ArrayList<>();
        File[] arquivosTxt = this.listaPets();
        File selectedFile = new File("");
        try{
            for(File file : arquivosTxt){
                List<String> textoArquivo = Files.readAllLines(file.toPath());
                if(textoArquivo.get(tipo1-1).toUpperCase().contains(criterio1.toUpperCase())){
                    files.add(file);
                }
            }

            if(files.isEmpty()){
                System.out.println();
                System.out.println("Nenhum pet encontrado com os critérios informados.");
                System.out.println();
                Thread.sleep(2000);
            }else{
                int count = 1;
                for(File file : files){
                    String textoFormatado = Files.readString(file.toPath());
                    System.out.println("Pet " + count +" : " );
                    System.out.println(textoFormatado);
                    System.out.println("------------------------------------");
                    count++;
                }
                System.out.println("Digite o número do pet desejado : ");
                int petToBeChanged = scanner.nextInt();
                scanner.nextLine();
                selectedFile = files.get(petToBeChanged-1);
            }

        }catch (IOException | InterruptedException error){
            System.out.println(error.getMessage());
        }
        return selectedFile;
    }
    public File buscarPorCriterios(int tipo1,String criterio1,int tipo2,String criterio2) {
        List<File> files = new ArrayList<>();
        File[] arquivosTxt = this.listaPets();
        File selectedFile = new File("");
        try{
            for(File file : arquivosTxt){
                boolean valida1 = false, valida2 = false;
                List<String> textoArquivo = Files.readAllLines(file.toPath());
                if (textoArquivo.get(tipo1-1).toUpperCase().contains(criterio1.toUpperCase())) {
                    valida1 = true;
                }
                if (textoArquivo.get(tipo2-1).toUpperCase().contains(criterio2.toUpperCase())) {
                    valida2 = true;
                }
                if (valida1 && valida2) {
                    files.add(file);
                    break;
                }
            }

            if(files.isEmpty()){
                System.out.println();
                System.out.println("Nenhum pet encontrado com os critérios informados.");
                System.out.println();
                Thread.sleep(2000);
            }else{
                int count = 1;
                for(File file : files){
                    String textoFormatado = Files.readString(file.toPath());
                    System.out.println("Pet " + count +" : " );
                    System.out.println(textoFormatado);
                    System.out.println("------------------------------------");
                    count++;
                }
                System.out.println("Digite o número do pet desejado : ");
                int petToBeChanged = scanner.nextInt();
                scanner.nextLine();
                selectedFile = files.get(petToBeChanged-1);
            }

        }catch (IOException | InterruptedException error){
            System.out.println(error.getMessage());
        }
        return selectedFile;
    }

    private boolean isThereAnyNumber(String text){
        Pattern patternOnlyNumbers = Pattern.compile("^(\\d)+([\\.,])?(\\d)?$");
        Matcher matcherNumber = patternOnlyNumbers.matcher(text);
        return matcherNumber.find();
    }
    private boolean isThereAnySpecialChar(String text){
        Pattern patternOnlyLetters = Pattern.compile("[^a-zA-Z $]");
        Matcher matcherOnlyLetters =  patternOnlyLetters.matcher(text);
        return matcherOnlyLetters.find();
    }

    private boolean isTheAnswerEmpty(String text){
        return text.trim().isEmpty();
    }


}
