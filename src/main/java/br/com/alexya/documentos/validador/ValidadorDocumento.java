package br.com.alexya.documentos.validador;

/**
 * Contrato comum para validadores e formatadores de documentos fiscais e cadastrais brasileiros.
 */
public interface ValidadorDocumento {

    /**
     * Valida se o documento fornecido respeita as regras oficiais do órgão emissor.
     *
     * @param documento documento a ser validado, com ou sem formatação.
     * @return {@code true} se o documento for válido; {@code false} caso contrário.
     */
    boolean ehValido(String documento);

    /**
     * Aplica a máscara padrão do documento sobre a sequência fornecida.
     *
     * @param documento documento limpo ou pré-formatado.
     * @return documento formatado com a pontuação padrão.
     * @throws IllegalArgumentException se o documento não possuir tamanho ou caracteres adequados.
     */
    String formatar(String documento);

    /**
     * Remove caracteres de pontuação e higieniza a sequência, retornando a forma canônica.
     *
     * @param documento documento com ou sem formatação.
     * @return representação limpa do documento.
     */
    String limpar(String documento);
}
