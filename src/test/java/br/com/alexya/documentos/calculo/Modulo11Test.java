package br.com.alexya.documentos.calculo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Modulo11Test {

    @Test
    @DisplayName("Deve calcular corretamente os DVs do caso oficial da Receita Federal (12ABC34501DE-35)")
    void deveCalcularDvsOficiaisReceitaFederal() {
        String base12 = "12ABC34501DE";

        int primeiroDv = Modulo11.calcularPrimeiroDigitoCnpj(base12);
        assertThat(primeiroDv).isEqualTo(3);

        int segundoDv = Modulo11.calcularSegundoDigitoCnpj(base12, primeiroDv);
        assertThat(segundoDv).isEqualTo(5);
    }

    @ParameterizedTest
    @CsvSource({
            "'0', 0",
            "'9', 9",
            "'A', 17",
            "'B', 18",
            "'Z', 42"
    })
    @DisplayName("Deve converter caracteres conforme tabela ASCII - 48 da Receita Federal")
    void deveConverterCaracteresConformeRegra(char caractere, int valorEsperado) {
        assertThat(Modulo11.obterValorCaractereCnpj(caractere)).isEqualTo(valorEsperado);
    }

    @Test
    @DisplayName("Deve lançar exceção para caracteres inválidos no CNPJ")
    void deveLancarExcecaoParaCaractereInvalido() {
        assertThatThrownBy(() -> Modulo11.obterValorCaractereCnpj('@'))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Deve calcular dígito verificador do CPF com peso decrescente")
    void deveCalcularDigitoCpf() {
        String base9 = "529982247";
        int dv1 = Modulo11.calcularDigitoCpf(base9, 10);
        assertThat(dv1).isEqualTo(2);

        int dv2 = Modulo11.calcularDigitoCpf(base9 + dv1, 11);
        assertThat(dv2).isEqualTo(5);
    }
}
