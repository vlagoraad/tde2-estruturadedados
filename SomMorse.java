import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/** Toca o código Morse: bipe curto para ponto e bipe longo para traço. */
public class SomMorse {
    private static final float AMOSTRAS_POR_SEGUNDO = 44100f;
    private static final int FREQUENCIA_HZ = 700;
    private static final int VOLUME = 100;
    private static final int DURACAO_PONTO_MS = 100;
    private static final int DURACAO_TRACO_MS = 300;
    private static final int PAUSA_ENTRE_LETRAS_MS = 200;

    /** Recebe o Morse com os códigos separados por espaço, ex.: "... --- ...". */
    public void tocar(String morse) {
        AudioFormat formato = new AudioFormat(AMOSTRAS_POR_SEGUNDO, 8, 1, true, false);

        try (SourceDataLine saida = AudioSystem.getSourceDataLine(formato)) {
            saida.open(formato);
            saida.start();
            for (char simbolo : morse.toCharArray()) {
                tocarSimbolo(saida, simbolo);
            }
            saida.drain();
        } catch (LineUnavailableException | IllegalArgumentException e) {
            System.out.println("Não foi possível tocar o som.");
        }
    }

    private void tocarSimbolo(SourceDataLine saida, char simbolo) {
        if (simbolo == '.') {
            tocarBipe(saida, DURACAO_PONTO_MS);
            tocarSilencio(saida, DURACAO_PONTO_MS);
        } else if (simbolo == '-') {
            tocarBipe(saida, DURACAO_TRACO_MS);
            tocarSilencio(saida, DURACAO_PONTO_MS);
        } else if (simbolo == ' ') {
            tocarSilencio(saida, PAUSA_ENTRE_LETRAS_MS);
        }
    }

    private void tocarBipe(SourceDataLine saida, int duracaoMs) {
        byte[] amostras = new byte[calcularQuantidadeAmostras(duracaoMs)];
        for (int i = 0; i < amostras.length; i++) {
            double tempo = i / AMOSTRAS_POR_SEGUNDO;
            amostras[i] = (byte) (Math.sin(2 * Math.PI * FREQUENCIA_HZ * tempo) * VOLUME);
        }
        saida.write(amostras, 0, amostras.length);
    }

    private void tocarSilencio(SourceDataLine saida, int duracaoMs) {
        byte[] amostras = new byte[calcularQuantidadeAmostras(duracaoMs)];
        saida.write(amostras, 0, amostras.length);
    }

    private int calcularQuantidadeAmostras(int duracaoMs) {
        return (int) (AMOSTRAS_POR_SEGUNDO * duracaoMs / 1000);
    }
}
