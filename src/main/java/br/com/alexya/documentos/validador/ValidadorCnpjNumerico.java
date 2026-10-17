package br.com.alexya.documentos.validador;

import br.com.alexya.documentos.formatador.FormatadorDocumento;

/**
 * Validador e formatador estrito para CNPJ puramente numérico (14 dígitos).
 * Rejeita caracteres alfabéticos, sendo indicado para regras de negócio ou integrações legadas.
 */
public final class ValidadorCnpjNumerico implements ValidadorDocumento {

    private static final ValidadorCnpjNumerico INSTANCIA = new ValidadorCnpjNumerico();
    private final ValidadorCnpj validadorCnpj = ValidadorCnpj.instancia();

    public ValidadorCnpjNumerico() {
    }

    /**
     * Obtém a instância singleton padrão do validador estrito de CNPJ numérico.
     *
     * @return instância thread-safe de {@link ValidadorCnpjNumerico}.
     */
    public static ValidadorCnpjNumerico instancia() {
        return INSTANCIA;
    }

    @Override
    public boolean ehValido(String documento) {
        return validadorCnpj.ehNumerico(documento);
    }

    @Override
    public String formatar(String documento) {
        String limpo = limpar(documento);
        if (!FormatadorDocumento.contemApenasDigitos(limpo)) {
            throw new IllegalArgumentException("CNPJ numérico não deve conter caracteres alfabéticos.");
        }
        return validadorCnpj.formatar(limpo);
    }

    @Override
    public String limpar(String documento) {
        return validadorCnpj.limpar(documento);
    }
}
