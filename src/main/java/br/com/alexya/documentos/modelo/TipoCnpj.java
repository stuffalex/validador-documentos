package br.com.alexya.documentos.modelo;

/**
 * Representa a classificação do formato da base de um CNPJ.
 */
public enum TipoCnpj {

    /**
     * CNPJ tradicional composto exclusivamente por dígitos numéricos.
     */
    NUMERICO,

    /**
     * CNPJ novo com caracteres alfabéticos na base (IN RFB nº 2.229/2024).
     */
    ALFANUMERICO
}
