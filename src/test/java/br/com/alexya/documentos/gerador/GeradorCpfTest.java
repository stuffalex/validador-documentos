package br.com.alexya.documentos.gerador;

import br.com.alexya.documentos.validador.ValidadorCpf;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import static org.assertj.core.api.Assertions.assertThat;

class GeradorCpfTest {

    private final ValidadorCpf validador = ValidadorCpf.instancia();

    @RepeatedTest(50)
    @DisplayName("Deve gerar CPFs limpos e válidos")
    void deveGerarCpfLimpoValido() {
        String cpf = GeradorCpf.gerar();

        assertThat(cpf).hasSize(11);
        assertThat(validador.ehValido(cpf)).isTrue();
    }

    @RepeatedTest(50)
    @DisplayName("Deve gerar CPFs formatados e válidos")
    void deveGerarCpfFormatadoValido() {
        String cpfFormatado = GeradorCpf.gerarFormatado();

        assertThat(cpfFormatado).matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");
        assertThat(validador.ehValido(cpfFormatado)).isTrue();
    }
}
