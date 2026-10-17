package br.com.alexya.documentos.validador;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ValidadorCnpjTest {

    private final ValidadorCnpj validador = ValidadorCnpj.instancia();

    @Test
    @DisplayName("Deve validar o CNPJ alfanumérico oficial do exemplo da Receita Federal (12.ABC.345/01DE-35)")
    void deveValidarCnpjAlfanumericoOficial() {
        assertThat(validador.ehValido("12.ABC.345/01DE-35")).isTrue();
        assertThat(validador.ehValido("12ABC34501DE35")).isTrue();
        assertThat(validador.ehValido("12.abc.345/01de-35")).isTrue();
        assertThat(validador.ehAlfanumerico("12.ABC.345/01DE-35")).isTrue();
        assertThat(validador.ehNumerico("12.ABC.345/01DE-35")).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "00.000.000/0001-91", // Banco do Brasil
            "00000000000191",
            "33.000.167/0001-01", // Petrobras
            "33000167000101",
            "11.222.333/0001-81",
            "04.252.011/0001-10"
    })
    @DisplayName("Deve validar CNPJs puramente numéricos válidos com ou sem pontuação")
    void deveValidarCnpjsNumericosValidos(String cnpj) {
        assertThat(validador.ehValido(cnpj)).isTrue();
        assertThat(validador.ehNumerico(cnpj)).isTrue();
        assertThat(validador.ehAlfanumerico(cnpj)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "12.ABC.345/01DE-34", // DV1 incorreto
            "12.ABC.345/01DE-36", // DV2 incorreto
            "12.ABC.345/01DE-AB", // DV não numérico (DVs devem ser numéricos)
            "00.000.000/0000-00", // Repetidos
            "11.111.111/1111-11",
            "AAAAAAAAAAAAAA",
            "12.ABC.345/01D@-35", // Caractere inválido
            "12.ABC.345/01DE-3",  // Tamanho menor
            "12.ABC.345/01DE-350",// Tamanho maior
            "",
            "   "
    })
    @DisplayName("Deve rejeitar CNPJs inválidos por DV, tamanho, formato ou repetição")
    void deveRejeitarCnpjsInvalidos(String cnpj) {
        assertThat(validador.ehValido(cnpj)).isFalse();
    }

    @Test
    @DisplayName("Deve retornar false para documento nulo")
    void deveRetornarFalseParaNulo() {
        assertThat(validador.ehValido(null)).isFalse();
        assertThat(validador.ehAlfanumerico(null)).isFalse();
        assertThat(validador.ehNumerico(null)).isFalse();
    }

    @Test
    @DisplayName("Deve formatar CNPJ alfanumérico e numérico")
    void deveFormatarCnpj() {
        assertThat(validador.formatar("12ABC34501DE35")).isEqualTo("12.ABC.345/01DE-35");
        assertThat(validador.formatar("00000000000191")).isEqualTo("00.000.000/0001-91");
    }

    @Test
    @DisplayName("Deve lançar exceção ao formatar com tamanho incompatível")
    void deveLancarExcecaoAoFormatarTamanhoInvalido() {
        assertThatThrownBy(() -> validador.formatar("12345"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
