package br.com.alexya.documentos.gerador;

import br.com.alexya.documentos.calculo.Modulo11;
import br.com.alexya.documentos.formatador.FormatadorDocumento;

import java.util.Random;

/**
 * Utilitário para geração de números válidos de CPF para testes e simulações.
 */
public final class GeradorCpf {

    private static final Random RANDOM = new Random();
    private static final int PESO_PRIMEIRO_DIGITO = 10;
    private static final int PESO_SEGUNDO_DIGITO = 11;

    private GeradorCpf() {
    }

    /**
     * Gera um CPF válido em formato limpo (apenas 11 dígitos numéricos).
     *
     * @return CPF válido desformatado.
     */
    public static String gerar() {
        StringBuilder sb = new StringBuilder(9);
        do {
            sb.setLength(0);
            for (int i = 0; i < 9; i++) {
                sb.append(RANDOM.nextInt(10));
            }
        } while (FormatadorDocumento.ehSequenciaRepetida(sb.toString()));

        String base9 = sb.toString();
        int dv1 = Modulo11.calcularDigitoCpf(base9, PESO_PRIMEIRO_DIGITO);
        int dv2 = Modulo11.calcularDigitoCpf(base9 + dv1, PESO_SEGUNDO_DIGITO);

        return base9 + dv1 + dv2;
    }

    /**
     * Gera um CPF válido com máscara padrão (000.000.000-00).
     *
     * @return CPF válido formatado.
     */
    public static String gerarFormatado() {
        return FormatadorDocumento.formatarCpf(gerar());
    }
}
