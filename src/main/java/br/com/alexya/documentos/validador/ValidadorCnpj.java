package br.com.alexya.documentos.validador;

import br.com.alexya.documentos.calculo.Modulo11;
import br.com.alexya.documentos.formatador.FormatadorDocumento;

/**
 * Validador e formatador para Cadastro Nacional da Pessoa Jurídica (CNPJ).
 * Suporta o padrão alfanumérico regulamentado pela Instrução Normativa RFB nº 2.229/2024
 * e mantém total compatibilidade com o formato tradicional numérico.
 */
public final class ValidadorCnpj implements ValidadorDocumento {

    private static final ValidadorCnpj INSTANCIA = new ValidadorCnpj();
    private static final int TAMANHO_CNPJ = 14;
    private static final int TAMANHO_BASE = 12;

    public ValidadorCnpj() {
    }

    /**
     * Obtém a instância singleton padrão do validador de CNPJ.
     *
     * @return instância thread-safe de {@link ValidadorCnpj}.
     */
    public static ValidadorCnpj instancia() {
        return INSTANCIA;
    }

    @Override
    public boolean ehValido(String documento) {
        String limpo = limpar(documento);

        if (limpo.length() != TAMANHO_CNPJ) {
            return false;
        }

        if (!possuiCaracteresValidos(limpo)) {
            return false;
        }

        if (FormatadorDocumento.ehSequenciaRepetida(limpo)) {
            return false;
        }

        String base = limpo.substring(0, TAMANHO_BASE);

        int primeiroDigitoCalculado = Modulo11.calcularPrimeiroDigitoCnpj(base);
        int primeiroDigitoInformado = limpo.charAt(12) - '0';
        if (primeiroDigitoCalculado != primeiroDigitoInformado) {
            return false;
        }

        int segundoDigitoCalculado = Modulo11.calcularSegundoDigitoCnpj(base, primeiroDigitoCalculado);
        int segundoDigitoInformado = limpo.charAt(13) - '0';
        return segundoDigitoCalculado == segundoDigitoInformado;
    }

    /**
     * Verifica se o CNPJ é válido e contém pelo menos um caractere alfabético na base.
     *
     * @param documento CNPJ a ser verificado.
     * @return {@code true} se for um CNPJ válido com letras na base.
     */
    public boolean ehAlfanumerico(String documento) {
        if (!ehValido(documento)) {
            return false;
        }
        String limpo = limpar(documento);
        for (int i = 0; i < TAMANHO_BASE; i++) {
            char c = limpo.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica se o CNPJ é válido e puramente numérico.
     *
     * @param documento CNPJ a ser verificado.
     * @return {@code true} se for um CNPJ válido composto unicamente por dígitos numéricos.
     */
    public boolean ehNumerico(String documento) {
        if (!ehValido(documento)) {
            return false;
        }
        return FormatadorDocumento.contemApenasDigitos(limpar(documento));
    }

    @Override
    public String formatar(String documento) {
        return FormatadorDocumento.formatarCnpj(documento);
    }

    @Override
    public String limpar(String documento) {
        return FormatadorDocumento.limpar(documento);
    }

    private boolean possuiCaracteresValidos(String limpo) {
        for (int i = 0; i < TAMANHO_BASE; i++) {
            char c = limpo.charAt(i);
            boolean ehDigito = c >= '0' && c <= '9';
            boolean ehLetra = c >= 'A' && c <= 'Z';
            if (!ehDigito && !ehLetra) {
                return false;
            }
        }

        char dv1 = limpo.charAt(12);
        char dv2 = limpo.charAt(13);
        return dv1 >= '0' && dv1 <= '9' && dv2 >= '0' && dv2 <= '9';
    }
}
