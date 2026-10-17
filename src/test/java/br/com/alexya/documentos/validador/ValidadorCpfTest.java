package br.com.alexya.documentos.validador;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ValidadorCpfTest {

    private final ValidadorCpf validador = ValidadorCpf.instancia();

    @ParameterizedTest
    @ValueSource(strings = {
            "52998224725",
            "529.982.247-25",
            "11144477735",
            "111.444.777-35",
            "00000000191"
    })
    @DisplayName("Deve validar CPFs válidos com ou sem formatação")
    void deveValidarCpfsValidos(String cpf) {
        assertThat(validador.ehValido(cpf)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "00000000000",
            "11111111111",
            "22222222222",
            "33333333333",
            "44444444444",
            "55555555555",
            "66666666666",
            "77777777777",
            "88888888888",
            "99999999999"
    })
    @DisplayName("Deve rejeitar CPFs compostos por números repetidos")
    void deveRejeitarCpfsComNumerosRepetidos(String cpf) {
        assertThat(validador.ehValido(cpf)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "52998224726", // DV incorreto
            "12345678901",
            "123",
            "123456789012",
            "abc.def.ghi-jk",
            "",
            "   "
    })
    @DisplayName("Deve rejeitar CPFs com dígito incorreto, tamanho inválido ou letras")
    void deveRejeitarCpfsInvalidos(String cpf) {
        assertThat(validador.ehValido(cpf)).isFalse();
    }

    @Test
    @DisplayName("Deve retornar false para documento nulo")
    void deveRetornarFalseParaNulo() {
        assertThat(validador.ehValido(null)).isFalse();
    }

    @Test
    @DisplayName("Deve formatar CPF com sucesso")
    void deveFormatarCpf() {
        assertThat(validador.formatar("52998224725")).isEqualTo("529.982.247-25");
        assertThat(validador.formatar("529.982.247-25")).isEqualTo("529.982.247-25");
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar formatar CPF inválido em tamanho")
    void deveLancarExcecaoAoFormatarInvalido() {
        assertThatThrownBy(() -> validador.formatar("123"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
