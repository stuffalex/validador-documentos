package br.com.alexya.documentos.validador;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ValidadorCnpjNumericoTest {

    private final ValidadorCnpjNumerico validador = ValidadorCnpjNumerico.instancia();

    @ParameterizedTest
    @ValueSource(strings = {
            "00.000.000/0001-91",
            "00000000000191",
            "33.000.167/0001-01",
            "11.222.333/0001-81"
    })
    @DisplayName("Deve validar CNPJs puramente numéricos")
    void deveValidarCnpjsNumericos(String cnpj) {
        assertThat(validador.ehValido(cnpj)).isTrue();
    }

    @Test
    @DisplayName("Deve rejeitar CNPJ alfanumérico no validador estrito numérico")
    void deveRejeitarCnpjAlfanumerico() {
        assertThat(validador.ehValido("12.ABC.345/01DE-35")).isFalse();
    }

    @Test
    @DisplayName("Deve formatar CNPJ numérico")
    void deveFormatarCnpjNumerico() {
        assertThat(validador.formatar("00000000000191")).isEqualTo("00.000.000/0001-91");
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar formatar CNPJ alfanumérico no validador estrito numérico")
    void deveLancarExcecaoAoFormatarAlfanumerico() {
        assertThatThrownBy(() -> validador.formatar("12ABC34501DE35"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
