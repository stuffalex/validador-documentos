package br.com.alexya.documentos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DocumentosTest {

    @Test
    @DisplayName("Deve delegar chamadas de validação e formatação através da fachada Documentos")
    void deveDelegarChamadasPelaFachada() {
        assertThat(Documentos.ehCpfValido("529.982.247-25")).isTrue();
        assertThat(Documentos.ehCpfValido("000.000.000-00")).isFalse();

        assertThat(Documentos.ehCnpjValido("12.ABC.345/01DE-35")).isTrue();
        assertThat(Documentos.ehCnpjValido("00.000.000/0001-91")).isTrue();

        assertThat(Documentos.ehCnpjAlfanumericoValido("12.ABC.345/01DE-35")).isTrue();
        assertThat(Documentos.ehCnpjAlfanumericoValido("00.000.000/0001-91")).isFalse();

        assertThat(Documentos.ehCnpjNumericoValido("00.000.000/0001-91")).isTrue();
        assertThat(Documentos.ehCnpjNumericoValido("12.ABC.345/01DE-35")).isFalse();

        assertThat(Documentos.formatarCpf("52998224725")).isEqualTo("529.982.247-25");
        assertThat(Documentos.formatarCnpj("12ABC34501DE35")).isEqualTo("12.ABC.345/01DE-35");
        assertThat(Documentos.limpar("12.abc.345/01de-35")).isEqualTo("12ABC34501DE35");

        assertThat(Documentos.cpf()).isNotNull();
        assertThat(Documentos.cnpj()).isNotNull();
        assertThat(Documentos.cnpjNumerico()).isNotNull();
    }
}
