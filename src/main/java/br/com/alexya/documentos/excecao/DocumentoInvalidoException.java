package br.com.alexya.documentos.excecao;

/**
 * Exceção lançada quando um documento informado não atende às regras de validação ou estrutura.
 */
public class DocumentoInvalidoException extends IllegalArgumentException {

    public DocumentoInvalidoException(String mensagem) {
        super(mensagem);
    }

    public DocumentoInvalidoException(String tipoDocumento, String valor) {
        super(String.format("O %s '%s' é inválido.", tipoDocumento, valor));
    }
}
