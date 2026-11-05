import java.util.ArrayList;
import java.util.List;

/** Árvore binária do código Morse: ponto vai para a esquerda e traço para a direita. */
public class ArvoreMorse {
    private static final String[][] TABELA_MORSE = {
        {"A", ".-"}, {"B", "-..."}, {"C", "-.-."}, {"D", "-.."}, {"E", "."}, {"F", "..-."},
        {"G", "--."}, {"H", "...."}, {"I", ".."}, {"J", ".---"}, {"K", "-.-"}, {"L", ".-.."},
        {"M", "--"}, {"N", "-."}, {"O", "---"}, {"P", ".--."}, {"Q", "--.-"}, {"R", ".-."},
        {"S", "..."}, {"T", "-"}, {"U", "..-"}, {"V", "...-"}, {"W", ".--"}, {"X", "-..-"},
        {"Y", "-.--"}, {"Z", "--.."},
        {"0", "-----"}, {"1", ".----"}, {"2", "..---"}, {"3", "...--"}, {"4", "....-"},
        {"5", "....."}, {"6", "-...."}, {"7", "--..."}, {"8", "---.."}, {"9", "----."}
    };

    private final No raiz = new No();

    public ArvoreMorse() {
        for (String[] linha : TABELA_MORSE) {
            inserir(linha[0].charAt(0), linha[1]);
        }
    }

    /** Percorre o código, criando os nós que faltam, e grava o caractere no nó final. */
    public void inserir(char caractere, String codigo) {
        No atual = raiz;
        for (char simbolo : codigo.toCharArray()) {
            if (simbolo == '.') {
                if (atual.esquerda == null) {
                    atual.esquerda = new No();
                }
                atual = atual.esquerda;
            } else {
                if (atual.direita == null) {
                    atual.direita = new No();
                }
                atual = atual.direita;
            }
        }
        atual.valor = caractere;
    }

    /** Recebe os códigos separados por espaço, ex.: "... --- ...". */
    public String decodificar(String morse) {
        StringBuilder palavra = new StringBuilder();
        for (String codigo : morse.trim().split("\\s+")) {
            palavra.append(decodificarCaractere(codigo));
        }
        return palavra.toString();
    }

    /** Segue o caminho do código na árvore. Devolve '?' se o código não existir. */
    private char decodificarCaractere(String codigo) {
        No atual = raiz;
        for (char simbolo : codigo.toCharArray()) {
            if (simbolo == '.') {
                atual = atual.esquerda;
            } else if (simbolo == '-') {
                atual = atual.direita;
            } else {
                return '?';
            }
            if (atual == null) {
                return '?';
            }
        }
        return atual.valor == No.VAZIO ? '?' : atual.valor;
    }

    /** Devolve a palavra em Morse, com os códigos separados por espaço. */
    public String codificar(String palavra) {
        StringBuilder morse = new StringBuilder();
        for (char caractere : palavra.trim().toUpperCase().toCharArray()) {
            String codigo = buscarCodigo(raiz, caractere, "");
            morse.append(codigo == null ? "?" : codigo).append(' ');
        }
        return morse.toString().trim();
    }

    /** Procura o caractere na árvore, montando o caminho percorrido. */
    private String buscarCodigo(No no, char caractere, String caminho) {
        if (no == null) {
            return null;
        }
        if (no.valor == caractere) {
            return caminho;
        }
        String codigo = buscarCodigo(no.esquerda, caractere, caminho + ".");
        if (codigo != null) {
            return codigo;
        }
        return buscarCodigo(no.direita, caractere, caminho + "-");
    }

    /** Imprime a árvore nível por nível. Nós sem caractere aparecem como '*'. */
    public void imprimir() {
        int altura = calcularAltura(raiz);
        List<No> nivel = new ArrayList<>();
        nivel.add(raiz);

        for (int profundidade = 0; profundidade < altura; profundidade++) {
            int largura = (int) Math.pow(2, altura - profundidade);
            StringBuilder linhaGalhos = new StringBuilder();
            StringBuilder linhaNos = new StringBuilder();
            List<No> proximoNivel = new ArrayList<>();

            for (int i = 0; i < nivel.size(); i++) {
                No no = nivel.get(i);
                char galho = i % 2 == 0 ? '/' : '\\';
                linhaGalhos.append(celula(no == null ? ' ' : galho, largura));
                linhaNos.append(celula(rotulo(no), largura));
                proximoNivel.add(no == null ? null : no.esquerda);
                proximoNivel.add(no == null ? null : no.direita);
            }

            if (profundidade > 0) {
                System.out.println(linhaGalhos);
            }
            System.out.println(linhaNos);
            nivel = proximoNivel;
        }
    }

    private String celula(char conteudo, int largura) {
        return " ".repeat(largura / 2 - 1) + conteudo + " ".repeat(largura / 2);
    }

    private char rotulo(No no) {
        if (no == null) {
            return ' ';
        }
        return no.valor == No.VAZIO ? '*' : no.valor;
    }

    private int calcularAltura(No no) {
        if (no == null) {
            return 0;
        }
        return 1 + Math.max(calcularAltura(no.esquerda), calcularAltura(no.direita));
    }
}
