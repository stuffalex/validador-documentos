package br.com.alexya.documentos.modelo;

import br.com.alexya.documentos.excecao.DocumentoInvalidoException;
import br.com.alexya.documentos.validador.ValidadorCnpj;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * Objeto de valor (Value Object) imutável que representa um CNPJ válido,
 * podendo ser do formato tradicional numérico ou do novo formato alfanumérico (IN RFB nº 2.229/2024).
 */
public record Cnpj(String valor, TipoCnpj tipo) implements Comparable<Cnpj>, Serializable {

    public Cnpj {
        Objects.requireNonNull(valor, "O valor do CNPJ não pode ser nulo.");
        String limpo = ValidadorCnpj.instancia().limpar(valor);
        if (!ValidadorCnpj.instancia().ehValido(limpo)) {
            throw new DocumentoInvalidoException("CNPJ", valor);
        }
        valor = limpo;
        if (tipo == null) {
            tipo = ValidadorCnpj.instancia().ehAlfanumerico(limpo)
                    ? TipoCnpj.ALFANUMERICO
                    : TipoCnpj.NUMERICO;
        }
    }

    /**
     * Cria uma instância de {@link Cnpj} a partir de um valor com ou sem formatação.
     * O tipo (numérico ou alfanumérico) é inferido automaticamente.
     *
     * @param valor texto com os 14 caracteres do CNPJ.
     * @return instância validada de {@link Cnpj}.
     * @throws DocumentoInvalidoException se o CNPJ informado for inválido.
     */
    public static Cnpj de(String valor) {
        return new Cnpj(valor, null);
    }

    /**
     * Tenta criar uma instância de {@link Cnpj}, retornando {@link Optional#empty()} se inválido.
     *
     * @param valor texto com os 14 caracteres do CNPJ.
     * @return {@link Optional} contendo o {@link Cnpj} se válido, ou vazio.
     */
    public static Optional<Cnpj> deOpcional(String valor) {
        if (valor == null || !ValidadorCnpj.instancia().ehValido(valor)) {
            return Optional.empty();
        }
        return Optional.of(new Cnpj(valor, null));
    }

    /**
     * Verifica rapidamente se uma string representa um CNPJ válido.
     *
     * @param valor texto a ser verificado.
     * @return {@code true} se o valor for um CNPJ válido.
     */
    public static boolean ehValido(String valor) {
        return ValidadorCnpj.instancia().ehValido(valor);
    }

    /**
     * Indica se este CNPJ possui caracteres alfabéticos em sua base.
     *
     * @return {@code true} se for alfanumérico.
     */
    public boolean ehAlfanumerico() {
        return tipo == TipoCnpj.ALFANUMERICO;
    }

    /**
     * Indica se este CNPJ é puramente numérico.
     *
     * @return {@code true} se for numérico.
     */
    public boolean ehNumerico() {
        return tipo == TipoCnpj.NUMERICO;
    }

    /**
     * Retorna o CNPJ formatado com a máscara oficial (XX.XXX.XXX/XXXX-00).
     *
     * @return CNPJ formatado.
     */
    public String obterFormatado() {
        return ValidadorCnpj.instancia().formatar(valor);
    }

    /**
     * Retorna a sequência canônica de 14 caracteres sem pontuação.
     *
     * @return texto limpo em letras maiúsculas e dígitos.
     */
    public String obterLimpo() {
        return valor;
    }

    /**
     * Retorna a raiz/base do CNPJ (os 8 primeiros caracteres).
     *
     * @return sequência de 8 caracteres representando a raiz da pessoa jurídica.
     */
    public String obterRaiz() {
        return valor.substring(0, 8);
    }

    /**
     * Retorna o identificador de ordem/estabelecimento/filial (4 caracteres intermediários).
     *
     * @return sequência de 4 caracteres representando o estabelecimento.
     */
    public String obterOrdem() {
        return valor.substring(8, 12);
    }

    /**
     * Retorna os dois dígitos verificadores numéricos finais do CNPJ.
     *
     * @return string de 2 dígitos contendo os DVs.
     */
    public String obterDigitosVerificadores() {
        return valor.substring(12, 14);
    }

    @Override
    public int compareTo(Cnpj outro) {
        return this.valor.compareTo(outro.valor);
    }

    @Override
    public String toString() {
        return obterFormatado();
    }
}
