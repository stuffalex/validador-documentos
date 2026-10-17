package br.com.alexya.documentos.formatador;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Utilitário para higienização, verificação de padrão e formatação de documentos.
 */
public final class FormatadorDocumento {

    private static final Pattern CARACTERES_ESPECIAIS = Pattern.compile("[^0-9A-Za-z]");
    private static final int TAMANHO_CPF = 11;
    private static final int TAMANHO_CNPJ = 14;

    private FormatadorDocumento() {
    }

    /**
     * Remove caracteres especiais (pontos, traços, barras, espaços) e converte para maiúsculo.
     *
     * @param documento valor original do documento.
     * @return texto alfanumérico limpo ou vazio se a entrada for nula.
     */
    public static String limpar(String documento) {
        if (documento == null) {
            return "";
        }
        return CARACTERES_ESPECIAIS.matcher(documento)
                .replaceAll("")
                .toUpperCase(Locale.ROOT);
    }

    /**
     * Aplica a máscara padrão de CPF (000.000.000-00).
     *
     * @param cpfDigitos sequência de 11 dígitos numéricos do CPF.
     * @return CPF formatado com pontuação.
     * @throws IllegalArgumentException se o CPF não contiver exatamente 11 dígitos numéricos.
     */
    public static String formatarCpf(String cpfDigitos) {
        String limpo = limpar(cpfDigitos);
        if (limpo.length() != TAMANHO_CPF || !contemApenasDigitos(limpo)) {
            throw new IllegalArgumentException("CPF para formatação deve conter exatamente 11 dígitos numéricos.");
        }
        return String.format("%s.%s.%s-%s",
                limpo.substring(0, 3),
                limpo.substring(3, 6),
                limpo.substring(6, 9),
                limpo.substring(9, 11));
    }

    /**
     * Aplica a máscara padrão de CNPJ (XX.XXX.XXX/XXXX-00).
     * Funciona tanto para o padrão puramente numérico quanto para o alfanumérico.
     *
     * @param cnpjCaracteres sequência de 14 caracteres alfanuméricos do CNPJ.
     * @return CNPJ formatado com pontuação oficial.
     * @throws IllegalArgumentException se o CNPJ não contiver exatamente 14 caracteres.
     */
    public static String formatarCnpj(String cnpjCaracteres) {
        String limpo = limpar(cnpjCaracteres);
        if (limpo.length() != TAMANHO_CNPJ) {
            throw new IllegalArgumentException("CNPJ para formatação deve conter exatamente 14 caracteres.");
        }
        return String.format("%s.%s.%s/%s-%s",
                limpo.substring(0, 2),
                limpo.substring(2, 5),
                limpo.substring(5, 8),
                limpo.substring(8, 12),
                limpo.substring(12, 14));
    }

    /**
     * Verifica se todos os caracteres da sequência são exatamente iguais.
     *
     * @param sequencia texto a ser analisado.
     * @return {@code true} se todos os caracteres forem idênticos; {@code false} caso contrário.
     */
    public static boolean ehSequenciaRepetida(String sequencia) {
        if (sequencia == null || sequencia.isEmpty()) {
            return false;
        }
        char primeiro = sequencia.charAt(0);
        for (int i = 1; i < sequencia.length(); i++) {
            if (sequencia.charAt(i) != primeiro) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verifica se a sequência contém apenas dígitos de 0 a 9.
     *
     * @param sequencia texto a ser verificado.
     * @return {@code true} se todos os caracteres forem dígitos; {@code false} caso contrário.
     */
    public static boolean contemApenasDigitos(String sequencia) {
        if (sequencia == null || sequencia.isEmpty()) {
            return false;
        }
        for (int i = 0; i < sequencia.length(); i++) {
            char c = sequencia.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
}
