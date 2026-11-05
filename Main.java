import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ArvoreMorse arvore = new ArvoreMorse();
        SomMorse som = new SomMorse();
        Scanner entrada = new Scanner(System.in);
        String opcao;

        do {
            mostrarMenu();
            opcao = entrada.nextLine().trim();

            switch (opcao) {
                case "1":
                    System.out.print("Morse (letras separadas por espaço): ");
                    String morseDigitado = entrada.nextLine();
                    System.out.println("Palavra: " + arvore.decodificar(morseDigitado));
                    som.tocar(morseDigitado);
                    break;
                case "2":
                    System.out.print("Palavra: ");
                    String morseGerado = arvore.codificar(entrada.nextLine());
                    System.out.println("Morse: " + morseGerado);
                    som.tocar(morseGerado);
                    break;
                case "3":
                    arvore.imprimir();
                    break;
                case "0":
                    System.out.println("Até mais!");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (!opcao.equals("0"));

        entrada.close();
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("1 - Decodificar (Morse para texto)");
        System.out.println("2 - Codificar (texto para Morse)");
        System.out.println("3 - Imprimir árvore");
        System.out.println("0 - Sair");
        System.out.print("Opção: ");
    }
}