package br.com.alexya.documentos.gerador;

import br.com.alexya.documentos.calculo.Modulo11;
import br.com.alexya.documentos.formatador.FormatadorDocumento;

import java.util.Random;

/**
 * Utilitário para geração de números válidos de CNPJ (numérico tradicional e alfanumérico).
 */
public final class GeradorCnpj {

    private static final Random RANDOM = new Random();
    private static final String ALFABETO_MAIUSCULO = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ALFANUMERICO_POOL = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private GeradorCnpj() {
    }

    /**
     * Gera um CNPJ válido tradicional puramente numérico (14 dígitos).
     *
     * @return CNPJ numérico desformatado.
     */
    public static String gerarNumerico() {
        StringBuilder sb = new StringBuilder(12);
        do {
            sb.setLength(0);
            for (int i = 0; i < 8; i++) {
                sb.append(RANDOM.nextInt(10));
            }
            sb.append("0001");
        } while (FormatadorDocumento.ehSequenciaRepetida(sb.toString()));

        String base12 = sb.toString();
        int dv1 = Modulo11.calcularPrimeiroDigitoCnpj(base12);
        int dv2 = Modulo11.calcularSegundoDigitoCnpj(base12, dv1);

        return base12 + dv1 + dv2;
    }

    /**
     * Gera um CNPJ puramente numérico formatado com máscara (00.000.000/0000-00).
     *
     * @return CNPJ numérico formatado.
     */
    public static String gerarNumericoFormatado() {
        return FormatadorDocumento.formatarCnpj(gerarNumerico());
    }

    /**
     * Gera um novo CNPJ alfanumérico válido conforme a IN RFB nº 2.229/2024.
     * Garante a presença de ao menos um caractere alfabético nas 12 primeiras posições.
     *
     * @return CNPJ alfanumérico desformatado (14 caracteres, sendo os 2 últimos dígitos numéricos).
     */
    public static String gerarAlfanumerico() {
        StringBuilder sb = new StringBuilder(12);
        do {
            sb.setLength(0);
            for (int i = 0; i < 12; i++) {
                sb.append(ALFANUMERICO_POOL.charAt(RANDOM.nextInt(ALFANUMERICO_POOL.length())));
            }
            if (!contemLetra(sb.toString())) {
                sb.setCharAt(RANDOM.nextInt(12), ALFABETO_MAIUSCULO.charAt(RANDOM.nextInt(ALFABETO_MAIUSCULO.length())));
            }
        } while (FormatadorDocumento.ehSequenciaRepetida(sb.toString()));

        String base12 = sb.toString();
        int dv1 = Modulo11.calcularPrimeiroDigitoCnpj(base12);
        int dv2 = Modulo11.calcularSegundoDigitoCnpj(base12, dv1);

        return base12 + dv1 + dv2;
    }

    /**
     * Gera um CNPJ alfanumérico formatado com máscara oficial (XX.XXX.XXX/XXXX-00).
     *
     * @return CNPJ alfanumérico formatado.
     */
    public static String gerarAlfanumericoFormatado() {
        return FormatadorDocumento.formatarCnpj(gerarAlfanumerico());
    }

    private static boolean contemLetra(String texto) {
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                return true;
            }
        }
        return false;
    }
}
