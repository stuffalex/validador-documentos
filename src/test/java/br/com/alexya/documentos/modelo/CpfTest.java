package br.com.alexya.documentos.modelo;

import br.com.alexya.documentos.excecao.DocumentoInvalidoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CpfTest {

    @Test
    @DisplayName("Deve instanciar Cpf válido a partir de string formatada ou desformatada")
    void deveInstanciarCpfValido() {
        Cpf cpf1 = Cpf.de("529.982.247-25");
        Cpf cpf2 = Cpf.de("52998224725");

        assertThat(cpf1).isEqualTo(cpf2);
        assertThat(cpf1.obterLimpo()).isEqualTo("52998224725");
        assertThat(cpf1.obterFormatado()).isEqualTo("529.982.247-25");
        assertThat(cpf1.toString()).isEqualTo("529.982.247-25");
    }

    @Test
    @DisplayName("Deve lançar DocumentoInvalidoException para CPF inválido")
    void deveLancarExcecaoParaCpfInvalido() {
        assertThatThrownBy(() -> Cpf.de("123.456.789-00"))
                .isInstanceOf(DocumentoInvalidoException.class)
                .hasMessageContaining("CPF");
    }

    @Test
    @DisplayName("Deve retornar Optional presente para CPF válido e vazio para inválido")
    void deveUtilizarOptional() {
        Optional<Cpf> valido = Cpf.deOpcional("529.982.247-25");
        Optional<Cpf> invalido = Cpf.deOpcional("000.000.000-00");
        Optional<Cpf> nulo = Cpf.deOpcional(null);

        assertThat(valido).isPresent();
        assertThat(invalido).isEmpty();
        assertThat(nulo).isEmpty();
    }

    @Test
    @DisplayName("Deve implementar ordenação natural (Comparable)")
    void deveCompararCpfs() {
        Cpf cpf1 = Cpf.de("111.444.777-35");
        Cpf cpf2 = Cpf.de("529.982.247-25");

        assertThat(cpf1.compareTo(cpf2)).isLessThan(0);
    }
}
