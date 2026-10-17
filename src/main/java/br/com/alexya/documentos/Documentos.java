package br.com.alexya.documentos;

import br.com.alexya.documentos.formatador.FormatadorDocumento;
import br.com.alexya.documentos.validador.ValidadorCnpj;
import br.com.alexya.documentos.validador.ValidadorCnpjNumerico;
import br.com.alexya.documentos.validador.ValidadorCpf;

/**
 * Fachada unificada (Facade) para acesso simplificado aos validadores e formatadores de documentos brasileiros.
 */
public final class Documentos {

    private Documentos() {
    }

    /**
     * Fornece o validador de CPF.
     *
     * @return instância de {@link ValidadorCpf}.
     */
    public static ValidadorCpf cpf() {
        return ValidadorCpf.instancia();
    }

    /**
     * Fornece o validador geral de CNPJ (com suporte a alfanumérico e numérico).
     *
     * @return instância de {@link ValidadorCnpj}.
     */
    public static ValidadorCnpj cnpj() {
        return ValidadorCnpj.instancia();
    }

    /**
     * Fornece o validador estrito para CNPJ tradicional exclusivamente numérico.
     *
     * @return instância de {@link ValidadorCnpjNumerico}.
     */
    public static ValidadorCnpjNumerico cnpjNumerico() {
        return ValidadorCnpjNumerico.instancia();
    }

    /**
     * Verifica se o CPF informado é válido.
     *
     * @param cpf CPF com ou sem máscara.
     * @return {@code true} se for válido.
     */
    public static boolean ehCpfValido(String cpf) {
        return ValidadorCpf.instancia().ehValido(cpf);
    }

    /**
     * Verifica se o CNPJ (numérico ou alfanumérico) informado é válido.
     *
     * @param cnpj CNPJ com ou sem máscara.
     * @return {@code true} se for válido.
     */
    public static boolean ehCnpjValido(String cnpj) {
        return ValidadorCnpj.instancia().ehValido(cnpj);
    }

    /**
     * Verifica se o CNPJ informado é válido e puramente numérico.
     *
     * @param cnpj CNPJ com ou sem máscara.
     * @return {@code true} se for válido e exclusivamente numérico.
     */
    public static boolean ehCnpjNumericoValido(String cnpj) {
        return ValidadorCnpjNumerico.instancia().ehValido(cnpj);
    }

    /**
     * Verifica se o CNPJ informado é válido e possui base alfanumérica.
     *
     * @param cnpj CNPJ com ou sem máscara.
     * @return {@code true} se for válido e alfanumérico.
     */
    public static boolean ehCnpjAlfanumericoValido(String cnpj) {
        return ValidadorCnpj.instancia().ehAlfanumerico(cnpj);
    }

    /**
     * Formata uma sequência de 11 dígitos no padrão de CPF (000.000.000-00).
     *
     * @param cpf valor a ser formatado.
     * @return CPF formatado.
     */
    public static String formatarCpf(String cpf) {
        return FormatadorDocumento.formatarCpf(cpf);
    }

    /**
     * Formata uma sequência de 14 caracteres no padrão de CNPJ (XX.XXX.XXX/XXXX-00).
     *
     * @param cnpj valor a ser formatado.
     * @return CNPJ formatado.
     */
    public static String formatarCnpj(String cnpj) {
        return FormatadorDocumento.formatarCnpj(cnpj);
    }

    /**
     * Higieniza qualquer documento, removendo caracteres não alfanuméricos e convertendo para maiúsculo.
     *
     * @param documento valor original.
     * @return texto alfanumérico limpo.
     */
    public static String limpar(String documento) {
        return FormatadorDocumento.limpar(documento);
    }
}
