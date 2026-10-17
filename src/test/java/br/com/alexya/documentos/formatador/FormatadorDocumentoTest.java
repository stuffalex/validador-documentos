package br.com.alexya.documentos.formatador;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FormatadorDocumentoTest {

    @Test
    @DisplayName("Deve limpar documento removendo caracteres especiais e convertendo para maiúsculo")
    void deveLimparDocumento() {
        assertThat(FormatadorDocumento.limpar("12.abc.345/01de-35")).isEqualTo("12ABC34501DE35");
        assertThat(FormatadorDocumento.limpar("529.982.247-25")).isEqualTo("52998224725");
        assertThat(FormatadorDocumento.limpar(null)).isEmpty();
    }

    @Test
    @DisplayName("Deve formatar CPF com 11 dígitos")
    void deveFormatarCpf() {
        assertThat(FormatadorDocumento.formatarCpf("52998224725")).isEqualTo("529.982.247-25");
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar formatar CPF com letras")
    void deveLancarErroAoFormatarCpfComLetras() {
        assertThatThrownBy(() -> FormatadorDocumento.formatarCpf("5299822472A"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Deve formatar CNPJ alfanumérico e numérico com 14 caracteres")
    void deveFormatarCnpj() {
        assertThat(FormatadorDocumento.formatarCnpj("12ABC34501DE35")).isEqualTo("12.ABC.345/01DE-35");
        assertThat(FormatadorDocumento.formatarCnpj("00000000000191")).isEqualTo("00.000.000/0001-91");
    }

    @Test
    @DisplayName("Deve detectar sequências com todos os caracteres repetidos")
    void deveDetectarSequenciaRepetida() {
        assertThat(FormatadorDocumento.ehSequenciaRepetida("11111111111")).isTrue();
        assertThat(FormatadorDocumento.ehSequenciaRepetida("00000000000000")).isTrue();
        assertThat(FormatadorDocumento.ehSequenciaRepetida("AAAAAAAAAAAAAA")).isTrue();
        assertThat(FormatadorDocumento.ehSequenciaRepetida("12ABC34501DE35")).isFalse();
        assertThat(FormatadorDocumento.ehSequenciaRepetida("")).isFalse();
        assertThat(FormatadorDocumento.ehSequenciaRepetida(null)).isFalse();
    }
}
