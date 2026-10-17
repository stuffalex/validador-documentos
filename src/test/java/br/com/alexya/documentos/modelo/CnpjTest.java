package br.com.alexya.documentos.modelo;

import br.com.alexya.documentos.excecao.DocumentoInvalidoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CnpjTest {

    @Test
    @DisplayName("Deve instanciar Cnpj alfanumérico e identificar propriedades corretamente")
    void deveInstanciarCnpjAlfanumerico() {
        Cnpj cnpj = Cnpj.de("12.ABC.345/01DE-35");

        assertThat(cnpj.ehAlfanumerico()).isTrue();
        assertThat(cnpj.ehNumerico()).isFalse();
        assertThat(cnpj.tipo()).isEqualTo(TipoCnpj.ALFANUMERICO);
        assertThat(cnpj.obterLimpo()).isEqualTo("12ABC34501DE35");
        assertThat(cnpj.obterFormatado()).isEqualTo("12.ABC.345/01DE-35");
        assertThat(cnpj.obterRaiz()).isEqualTo("12ABC345");
        assertThat(cnpj.obterOrdem()).isEqualTo("01DE");
        assertThat(cnpj.obterDigitosVerificadores()).isEqualTo("35");
    }

    @Test
    @DisplayName("Deve instanciar Cnpj numérico e identificar propriedades")
    void deveInstanciarCnpjNumerico() {
        Cnpj cnpj = Cnpj.de("00.000.000/0001-91");

        assertThat(cnpj.ehNumerico()).isTrue();
        assertThat(cnpj.ehAlfanumerico()).isFalse();
        assertThat(cnpj.tipo()).isEqualTo(TipoCnpj.NUMERICO);
        assertThat(cnpj.obterLimpo()).isEqualTo("00000000000191");
        assertThat(cnpj.obterFormatado()).isEqualTo("00.000.000/0001-91");
        assertThat(cnpj.obterRaiz()).isEqualTo("00000000");
        assertThat(cnpj.obterOrdem()).isEqualTo("0001");
        assertThat(cnpj.obterDigitosVerificadores()).isEqualTo("91");
    }

    @Test
    @DisplayName("Deve lançar DocumentoInvalidoException para CNPJ inválido")
    void deveLancarExcecaoParaCnpjInvalido() {
        assertThatThrownBy(() -> Cnpj.de("12.ABC.345/01DE-99"))
                .isInstanceOf(DocumentoInvalidoException.class)
                .hasMessageContaining("CNPJ");
    }

    @Test
    @DisplayName("Deve retornar Optional presente para CNPJ válido e vazio para inválido")
    void deveUtilizarOptional() {
        Optional<Cnpj> valido = Cnpj.deOpcional("12.ABC.345/01DE-35");
        Optional<Cnpj> invalido = Cnpj.deOpcional("11.111.111/1111-11");
        Optional<Cnpj> nulo = Cnpj.deOpcional(null);

        assertThat(valido).isPresent();
        assertThat(invalido).isEmpty();
        assertThat(nulo).isEmpty();
    }

    @Test
    @DisplayName("Deve implementar ordenação natural (Comparable)")
    void deveCompararCnpjs() {
        Cnpj cnpj1 = Cnpj.de("00.000.000/0001-91");
        Cnpj cnpj2 = Cnpj.de("12.ABC.345/01DE-35");

        assertThat(cnpj1.compareTo(cnpj2)).isLessThan(0);
    }
}
