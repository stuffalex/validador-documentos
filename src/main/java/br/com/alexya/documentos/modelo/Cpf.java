package br.com.alexya.documentos.modelo;

import br.com.alexya.documentos.excecao.DocumentoInvalidoException;
import br.com.alexya.documentos.validador.ValidadorCpf;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * Objeto de valor (Value Object) imutável que representa um CPF válido.
 */
public record Cpf(String valor) implements Comparable<Cpf>, Serializable {

    public Cpf {
        Objects.requireNonNull(valor, "O valor do CPF não pode ser nulo.");
        String limpo = ValidadorCpf.instancia().limpar(valor);
        if (!ValidadorCpf.instancia().ehValido(limpo)) {
            throw new DocumentoInvalidoException("CPF", valor);
        }
        valor = limpo;
    }

    /**
     * Cria uma instância de {@link Cpf} a partir de um valor com ou sem formatação.
     *
     * @param valor texto com os dígitos do CPF.
     * @return instância validada de {@link Cpf}.
     * @throws DocumentoInvalidoException se o CPF informado for inválido.
     */
    public static Cpf de(String valor) {
        return new Cpf(valor);
    }

    /**
     * Tenta criar uma instância de {@link Cpf}, retornando {@link Optional#empty()} se inválido.
     *
     * @param valor texto com os dígitos do CPF.
     * @return {@link Optional} contendo a instância de {@link Cpf} se válido, ou vazio.
     */
    public static Optional<Cpf> deOpcional(String valor) {
        if (valor == null || !ValidadorCpf.instancia().ehValido(valor)) {
            return Optional.empty();
        }
        return Optional.of(new Cpf(valor));
    }

    /**
     * Verifica rapidamente se uma string representa um CPF válido.
     *
     * @param valor texto a ser verificado.
     * @return {@code true} se o valor for um CPF válido.
     */
    public static boolean ehValido(String valor) {
        return ValidadorCpf.instancia().ehValido(valor);
    }

    /**
     * Retorna o CPF formatado com a máscara padrão (000.000.000-00).
     *
     * @return CPF formatado com pontos e traço.
     */
    public String obterFormatado() {
        return ValidadorCpf.instancia().formatar(valor);
    }

    /**
     * Retorna os 11 dígitos numéricos do CPF sem pontuação.
     *
     * @return sequência numérica limpa.
     */
    public String obterLimpo() {
        return valor;
    }

    @Override
    public int compareTo(Cpf outro) {
        return this.valor.compareTo(outro.valor);
    }

    @Override
    public String toString() {
        return obterFormatado();
    }
}
