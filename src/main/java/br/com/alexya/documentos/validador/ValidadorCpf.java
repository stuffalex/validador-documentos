package br.com.alexya.documentos.validador;

import br.com.alexya.documentos.calculo.Modulo11;
import br.com.alexya.documentos.formatador.FormatadorDocumento;

/**
 * Validador e formatador para Cadastro de Pessoas Físicas (CPF).
 */
public final class ValidadorCpf implements ValidadorDocumento {

    private static final ValidadorCpf INSTANCIA = new ValidadorCpf();
    private static final int TAMANHO_CPF = 11;
    private static final int PESO_PRIMEIRO_DIGITO = 10;
    private static final int PESO_SEGUNDO_DIGITO = 11;

    public ValidadorCpf() {
    }

    /**
     * Obtém a instância singleton padrão do validador de CPF.
     *
     * @return instância reutilizável e thread-safe de {@link ValidadorCpf}.
     */
    public static ValidadorCpf instancia() {
        return INSTANCIA;
    }

    @Override
    public boolean ehValido(String documento) {
        String limpo = limpar(documento);

        if (limpo.length() != TAMANHO_CPF) {
            return false;
        }

        if (!FormatadorDocumento.contemApenasDigitos(limpo)) {
            return false;
        }

        if (FormatadorDocumento.ehSequenciaRepetida(limpo)) {
            return false;
        }

        int primeiroDigito = Modulo11.calcularDigitoCpf(limpo.substring(0, 9), PESO_PRIMEIRO_DIGITO);
        if (primeiroDigito != (limpo.charAt(9) - '0')) {
            return false;
        }

        int segundoDigito = Modulo11.calcularDigitoCpf(limpo.substring(0, 10), PESO_SEGUNDO_DIGITO);
        return segundoDigito == (limpo.charAt(10) - '0');
    }

    @Override
    public String formatar(String documento) {
        return FormatadorDocumento.formatarCpf(documento);
    }

    @Override
    public String limpar(String documento) {
        return FormatadorDocumento.limpar(documento);
    }
}
