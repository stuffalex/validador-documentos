package br.com.alexya.documentos.calculo;

/**
 * Utilitário responsável pelo cálculo dos dígitos verificadores utilizando o algoritmo Módulo 11.
 * Implementa as especificações oficiais para CPF e CNPJ (numérico e alfanumérico conforme IN RFB nº 2.229/2024).
 */
public final class Modulo11 {

    private static final int BASE_ASCII = 48;
    private static final int DIVISOR = 11;
    private static final int RESTO_LIMITE = 2;

    private static final int[] PESOS_CNPJ_PRIMEIRO_DIGITO = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] PESOS_CNPJ_SEGUNDO_DIGITO = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    private Modulo11() {
    }

    /**
     * Calcula um dígito verificador de CPF a partir de uma base numérica e do peso inicial decrescente.
     *
     * @param base sequência numérica de entrada (9 dígitos para o 1º DV, 10 para o 2º DV).
     * @param pesoInicial peso atribuído ao primeiro dígito da sequência (10 para o 1º DV, 11 para o 2º DV).
     * @return dígito verificador calculado entre 0 e 9.
     */
    public static int calcularDigitoCpf(String base, int pesoInicial) {
        int soma = 0;
        for (int i = 0; i < base.length(); i++) {
            int digito = base.charAt(i) - BASE_ASCII;
            soma += digito * (pesoInicial - i);
        }
        int resto = soma % DIVISOR;
        return (resto < RESTO_LIMITE) ? 0 : (DIVISOR - resto);
    }

    /**
     * Converte um caractere alfanumérico para seu valor correspondente na regra do CNPJ (ASCII - 48).
     *
     * @param caractere caractere válido ('0'-'9' ou 'A'-'Z').
     * @return valor decimal para a soma ponderada.
     * @throws IllegalArgumentException se o caractere não for um dígito ou letra maiúscula válida.
     */
    public static int obterValorCaractereCnpj(char caractere) {
        if ((caractere >= '0' && caractere <= '9') || (caractere >= 'A' && caractere <= 'Z')) {
            return (int) caractere - BASE_ASCII;
        }
        throw new IllegalArgumentException("Caractere inválido para CNPJ: " + caractere);
    }

    /**
     * Calcula o primeiro dígito verificador do CNPJ (numérico ou alfanumérico).
     *
     * @param base12 os 12 primeiros caracteres do CNPJ (raiz + filial).
     * @return primeiro dígito verificador calculado (0 a 9).
     */
    public static int calcularPrimeiroDigitoCnpj(String base12) {
        int soma = 0;
        for (int i = 0; i < PESOS_CNPJ_PRIMEIRO_DIGITO.length; i++) {
            soma += obterValorCaractereCnpj(base12.charAt(i)) * PESOS_CNPJ_PRIMEIRO_DIGITO[i];
        }
        int resto = soma % DIVISOR;
        return (resto < RESTO_LIMITE) ? 0 : (DIVISOR - resto);
    }

    /**
     * Calcula o segundo dígito verificador do CNPJ (numérico ou alfanumérico).
     *
     * @param base12 os 12 primeiros caracteres do CNPJ.
     * @param primeiroDigito primeiro dígito verificador previamente calculado.
     * @return segundo dígito verificador calculado (0 a 9).
     */
    public static int calcularSegundoDigitoCnpj(String base12, int primeiroDigito) {
        int soma = 0;
        for (int i = 0; i < PESOS_CNPJ_PRIMEIRO_DIGITO.length; i++) {
            soma += obterValorCaractereCnpj(base12.charAt(i)) * PESOS_CNPJ_SEGUNDO_DIGITO[i];
        }
        soma += primeiroDigito * PESOS_CNPJ_SEGUNDO_DIGITO[12];
        int resto = soma % DIVISOR;
        return (resto < RESTO_LIMITE) ? 0 : (DIVISOR - resto);
    }
}
