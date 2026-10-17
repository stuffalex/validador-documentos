package br.com.alexya.documentos.gerador;

import br.com.alexya.documentos.validador.ValidadorCnpj;
import br.com.alexya.documentos.validador.ValidadorCnpjNumerico;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import static org.assertj.core.api.Assertions.assertThat;

class GeradorCnpjTest {

    private final ValidadorCnpj validadorCnpj = ValidadorCnpj.instancia();
    private final ValidadorCnpjNumerico validadorNumerico = ValidadorCnpjNumerico.instancia();

    @RepeatedTest(50)
    @DisplayName("Deve gerar CNPJs puramente numéricos válidos")
    void deveGerarCnpjNumericoValido() {
        String cnpj = GeradorCnpj.gerarNumerico();

        assertThat(cnpj).hasSize(14);
        assertThat(validadorCnpj.ehValido(cnpj)).isTrue();
        assertThat(validadorNumerico.ehValido(cnpj)).isTrue();
        assertThat(validadorCnpj.ehNumerico(cnpj)).isTrue();
    }

    @RepeatedTest(50)
    @DisplayName("Deve gerar CNPJs puramente numéricos formatados e válidos")
    void deveGerarCnpjNumericoFormatadoValido() {
        String cnpjFormatado = GeradorCnpj.gerarNumericoFormatado();

        assertThat(cnpjFormatado).matches("\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}");
        assertThat(validadorCnpj.ehValido(cnpjFormatado)).isTrue();
    }

    @RepeatedTest(50)
    @DisplayName("Deve gerar novos CNPJs alfanuméricos válidos com letras")
    void deveGerarCnpjAlfanumericoValido() {
        String cnpj = GeradorCnpj.gerarAlfanumerico();

        assertThat(cnpj).hasSize(14);
        assertThat(validadorCnpj.ehValido(cnpj)).isTrue();
        assertThat(validadorCnpj.ehAlfanumerico(cnpj)).isTrue();
        assertThat(validadorNumerico.ehValido(cnpj)).isFalse();
    }

    @RepeatedTest(50)
    @DisplayName("Deve gerar novos CNPJs alfanuméricos formatados e válidos")
    void deveGerarCnpjAlfanumericoFormatadoValido() {
        String cnpjFormatado = GeradorCnpj.gerarAlfanumericoFormatado();

        assertThat(cnpjFormatado).matches("[0-9A-Z]{2}\\.[0-9A-Z]{3}\\.[0-9A-Z]{3}/[0-9A-Z]{4}-\\d{2}");
        assertThat(validadorCnpj.ehValido(cnpjFormatado)).isTrue();
    }
}
