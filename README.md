# Validador de Documentos Brasil 🇧🇷

Biblioteca Java de alta performance, moderna e com **zero dependências em runtime**, desenvolvida para validação, formatação, extração e geração de **CPF** e **CNPJ** — incluindo suporte total ao **novo padrão de CNPJ Alfanumérico** regulamentado pela Receita Federal do Brasil através da **Instrução Normativa RFB nº 2.229/2024**.

---

## 📌 Sumário
- [Destaques](#-destaques)
- [O Novo Padrão de CNPJ Alfanumérico](#-o-novo-padrão-de-cnpj-alfanumérico-in-rfb-nº-22292024)
- [Arquitetura e Boas Práticas (SOLID & Clean Code)](#-arquitetura-e-boas-práticas)
- [Como Usar](#-como-usar)
  - [1. Validação Rápida via Fachada (`Documentos`)](#1-validação-rápida-via-fachada-documentos)
  - [2. Modelos Ricos / Value Objects (`Cpf` e `Cnpj`)](#2-modelos-ricos--value-objects-cpf-e-cnpj)
  - [3. Validação Injetável via Contrato (`ValidadorDocumento`)](#3-validação-injetável-via-contrato-validadordocumento)
  - [4. Geração de Documentos Válidos para Testes](#4-geração-de-documentos-válidos-para-testes)
- [Compilação e Testes Locais](#-compilação-e-testes-locais)
- [Como Usar o `.jar` Diretamente no seu Projeto](#-como-usar-o-jar-diretamente-no-seu-projeto)
- [Como Publicar e Baixar via Registry Maven](#-como-publicar-e-baixar-via-registry-maven)
  - [Opção 1: JitPack (Recomendado pela simplicidade)](#opção-1-jitpack-publicação-instantânea-via-github)
  - [Opção 2: GitHub Packages](#opção-2-github-packages)
  - [Opção 3: Maven Central](#opção-3-maven-central)
- [Publicando no GitHub](#-passo-a-passo-para-subir-ao-github)

---

## ✨ Destaques

- **Padrão RFB 2026 Ready**: Algoritmo Módulo 11 com tabela ASCII - 48 oficial para CNPJs com letras e números.
- **Retrocompatibilidade Total**: Valida CNPJs numéricos tradicionais e oferece validador estrito numérico (`ValidadorCnpjNumerico`).
- **Zero Runtime Dependencies**: Sem bibliotecas de terceiros em produção. Apenas Java 17 padrão.
- **Modelagem Expressiva**: Implementado com Java 17 `record`, imutabilidade, thread-safety e tipagem forte.
- **100% Testado**: Suíte abrangente de testes unitários com JUnit 5 e AssertJ cobrindo vetores oficiais do SERPRO / RFB.
- **Javadoc Completo**: Toda a API pública documentada. Código limpo, sem comentários poluentes ou código duplicado.

---

## 🏛️ O Novo Padrão de CNPJ Alfanumérico (IN RFB nº 2.229/2024)

A partir da nova regulamentação federal:
1. **Estrutura (14 caracteres)**:
   - **Posições 1 a 8**: Raiz da empresa (alfanumérico: `0-9` e `A-Z`).
   - **Posições 9 a 12**: Ordem/Estabelecimento/Filial (alfanumérico: `0-9` e `A-Z`).
   - **Posições 13 e 14**: Dígitos Verificadores (obrigatoriamente numéricos: `0-9`).
2. **Máscara Oficial**: `XX.XXX.XXX/XXXX-00`.
3. **Cálculo do Dígito Verificador**:
   - Cada caractere é convertido para o valor `ASCII - 48` (`0` = 0, `9` = 9, `A` = 17, `Z` = 42).
   - Multiplica-se pelos pesos do Módulo 11 (`5,4,3,2,9,8,7,6,5,4,3,2`).
   - `resto = soma % 11`. Se `resto < 2`, DV = `0`; senão DV = `11 - resto`.
   - O mesmo cálculo é aplicado para o 2º DV, incorporando o 1º DV encontrado à base.

---

## 📐 Arquitetura e Boas Práticas

O projeto foi construído respeitando os princípios de **Clean Code** e **SOLID**:

- **S (Single Responsibility)**: Separação de cálculo matemático (`Modulo11`), formatação/limpeza (`FormatadorDocumento`), regras de negócio (`ValidadorCpf`, `ValidadorCnpj`), e domínio imutável (`Cpf`, `Cnpj`).
- **O (Open/Closed)**: Contrato unificado `ValidadorDocumento` que permite extensão para outros documentos sem modificar os consumidores existentes.
- **L (Liskov Substitution)**: Todos os validadores implementam `ValidadorDocumento` e podem ser usados de forma intercambiável.
- **I (Interface Segregation)**: Interface enxuta focada em `ehValido`, `formatar` e `limpar`.
- **D (Dependency Inversion)**: Facilita injeção de dependência em frameworks como Spring Boot, Quarkus ou Micronaut.

---

## 🚀 Como Usar

### 1. Validação Rápida via Fachada (`Documentos`)

A classe `Documentos` oferece métodos utilitários estáticos para verificações diretas:

```java
import br.com.alexya.documentos.Documentos;

// Validação de CPF
boolean cpfValido = Documentos.ehCpfValido("529.982.247-25"); // true
boolean cpfInvalido = Documentos.ehCpfValido("000.000.000-00"); // false

// Validação de CNPJ (aceita alfanumérico novo e numérico tradicional)
boolean cnpjAlfaValido = Documentos.ehCnpjValido("12.ABC.345/01DE-35"); // true (Caso Oficial RFB)
boolean cnpjNumValido  = Documentos.ehCnpjValido("00.000.000/0001-91"); // true (Banco do Brasil)

// Validação estrita de CNPJ Numérico (rejeita letras)
boolean apenasNumeros = Documentos.ehCnpjNumericoValido("00.000.000/0001-91"); // true
boolean rejeitaLetras = Documentos.ehCnpjNumericoValido("12.ABC.345/01DE-35"); // false

// Identificação de formato
boolean ehAlfanumerico = Documentos.ehCnpjAlfanumericoValido("12.ABC.345/01DE-35"); // true

// Formatação e higienização
String cpfFormatado = Documentos.formatarCpf("52998224725"); // "529.982.247-25"
String cnpjFormatado = Documentos.formatarCnpj("12ABC34501DE35"); // "12.ABC.345/01DE-35"
String limpo = Documentos.limpar("12.abc.345/01de-35"); // "12ABC34501DE35"
```

---

### 2. Modelos Ricos / Value Objects (`Cpf` e `Cnpj`)

Para arquiteturas baseadas em **DDD (Domain-Driven Design)**, utilize os registros imutáveis com validação automática:

```java
import br.com.alexya.documentos.modelo.Cpf;
import br.com.alexya.documentos.modelo.Cnpj;
import br.com.alexya.documentos.modelo.TipoCnpj;
import java.util.Optional;

// Criação com validação imediata (lança DocumentoInvalidoException se inválido)
Cpf cpf = Cpf.de("529.982.247-25");
System.out.println(cpf.obterLimpo());     // "52998224725"
System.out.println(cpf.obterFormatado()); // "529.982.247-25"

// Criação segura com Optional
Optional<Cpf> possivelCpf = Cpf.deOpcional("123.456.789-00");
possivelCpf.ifPresent(c -> System.out.println("Válido: " + c));

// CNPJ com extração estrutural (raiz, filial e DV)
Cnpj cnpj = Cnpj.de("12.ABC.345/01DE-35");
System.out.println(cnpj.tipo());                       // TipoCnpj.ALFANUMERICO
System.out.println(cnpj.obterRaiz());                  // "12ABC345"
System.out.println(cnpj.obterOrdem());                 // "01DE"
System.out.println(cnpj.obterDigitosVerificadores());  // "35"
System.out.println(cnpj.ehAlfanumerico());             // true
```

---

### 3. Validação Injetável via Contrato (`ValidadorDocumento`)

Ideal para injeção de dependência no **Spring**, **Quarkus** ou **Jakarta EE**:

```java
import br.com.alexya.documentos.validador.ValidadorDocumento;
import br.com.alexya.documentos.validador.ValidadorCnpj;
import br.com.alexya.documentos.validador.ValidadorCpf;

public class CadastroService {

    private final ValidadorDocumento validadorCpf = ValidadorCpf.instancia();
    private final ValidadorDocumento validadorCnpj = ValidadorCnpj.instancia();

    public void cadastrarPessoaFisica(String cpf) {
        if (!validadorCpf.ehValido(cpf)) {
            throw new IllegalArgumentException("CPF inválido!");
        }
        // ...
    }
}
```

---

### 4. Geração de Documentos Válidos para Testes

Útil para testes unitários, testes de integração, migrações e mocks:

```java
import br.com.alexya.documentos.gerador.GeradorCpf;
import br.com.alexya.documentos.gerador.GeradorCnpj;

// Geração de CPF
String cpfLimpo = GeradorCpf.gerar();                     // ex: "52998224725"
String cpfFormatado = GeradorCpf.gerarFormatado();         // ex: "529.982.247-25"

// Geração de CNPJ tradicional (apenas números)
String cnpjNumLimpo = GeradorCnpj.gerarNumerico();
String cnpjNumFormatado = GeradorCnpj.gerarNumericoFormatado();

// Geração de novo CNPJ alfanumérico (IN RFB 2.229/2024)
String cnpjAlfaLimpo = GeradorCnpj.gerarAlfanumerico();
String cnpjAlfaFormatado = GeradorCnpj.gerarAlfanumericoFormatado(); // ex: "A1.B2C.3D4/E5F6-08"
```

---

## 🛠️ Compilação e Testes Locais

### Pré-requisitos
- **Java JDK 17** ou superior
- **Apache Maven 3.8+**

### Comandos de Build

Clone o repositório e execute no terminal:

```bash
# Executa todos os testes unitários (376 testes)
mvn test

# Compila, roda os testes e empacota (.jar, sources e javadoc)
mvn clean verify
```

Após o término da compilação, os artefatos estarão disponíveis na pasta `target/`:
- `validador-documentos-1.0.0.jar` (Biblioteca binária pronta para uso)
- `validador-documentos-1.0.0-sources.jar` (Código-fonte)
- `validador-documentos-1.0.0-javadoc.jar` (Documentação Javadoc)

---

## 📦 Como Usar o `.jar` Diretamente no seu Projeto

Se você não quiser ou não puder publicar em um repositório remoto, pode utilizar o arquivo `.jar` gerado de três formas simples:

### Opção A: Instalando no seu Repositório Maven Local (`~/.m2`)

Gere o `.jar` e execute o comando:

```bash
mvn install:install-file \
  -Dfile=target/validador-documentos-1.0.0.jar \
  -DgroupId=br.com.alexya \
  -DartifactId=validador-documentos \
  -Dversion=1.0.0 \
  -Dpackaging=jar \
  -DgeneratePom=true
```

Em seguida, basta adicionar a dependência no `pom.xml` de qualquer projeto em sua máquina:

```xml
<dependency>
    <groupId>br.com.alexya</groupId>
    <artifactId>validador-documentos</artifactId>
    <version>1.0.0</version>
</dependency>
```

---

### Opção B: Referência Direta via Pasta `lib/` no `pom.xml`

Copie o `validador-documentos-1.0.0.jar` para a pasta `libs/` do seu projeto consumidor e adicione no `pom.xml`:

```xml
<dependency>
    <groupId>br.com.alexya</groupId>
    <artifactId>validador-documentos</artifactId>
    <version>1.0.0</version>
    <scope>system</scope>
    <systemPath>${project.basedir}/libs/validador-documentos-1.0.0.jar</systemPath>
</dependency>
```

---

### Opção C: Adicionando à IDE (IntelliJ, Eclipse ou VS Code)

1. Crie uma pasta `lib/` no seu projeto.
2. Cole o arquivo `validador-documentos-1.0.0.jar`.
3. No IntelliJ: Botão direito no arquivo `.jar` > **Add as Library...**.
4. No Eclipse: Botão direito no projeto > **Build Path** > **Add External Archives...**.
5. No VS Code: Adicione a referência ao classpath em `.vscode/settings.json`.

---

## 🌐 Como Publicar e Baixar via Registry Maven

### Opção 1: JitPack (Publicação Instantânea via GitHub)

O [JitPack](https://jitpack.io) é a maneira mais rápida de distribuir sua biblioteca diretamente do GitHub sem nenhuma aprovação burocrática.

#### 1. No repositório GitHub
Basta criar uma **Release** ou **Tag** no GitHub (ex: `v1.0.0`).

#### 2. No projeto consumidor (`pom.xml`):
Adicione o repositório do JitPack:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

E declare a dependência:

```xml
<dependency>
    <groupId>com.github.alexyamoraes</groupId>
    <artifactId>validador-documentos</artifactId>
    <version>v1.0.0</version>
</dependency>
```

---

### Opção 2: GitHub Packages

O projeto já vem pré-configurado para o **GitHub Packages** no `pom.xml`.

#### 1. Configurar credenciais no seu `~/.m2/settings.xml`:

Crie ou edite o arquivo `~/.m2/settings.xml`:

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                              http://maven.apache.org/xsd/settings-1.0.0.xsd">
  <servers>
    <server>
      <id>github</id>
      <username>alexyamoraes</username>
      <password>SEU_GITHUB_PERSONAL_ACCESS_TOKEN</password>
    </server>
  </servers>
</settings>
```
> O token pessoal (PAT) precisa ter as permissões `write:packages` e `read:packages`.

#### 2. Publicar o pacote:

```bash
mvn clean deploy
```

#### 3. No projeto consumidor:

Adicione o repositório no `pom.xml`:

```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/alexyamoraes/validador-documentos</url>
    </repository>
</repositories>
```

E a dependência:

```xml
<dependency>
    <groupId>br.com.alexya</groupId>
    <artifactId>validador-documentos</artifactId>
    <version>1.0.0</version>
</dependency>
```

---

### Opção 3: Maven Central

Para publicar no **Maven Central**:
1. Cadastre-se no [Central Portal Sonatype](https://central.sonatype.com).
2. Valide o domínio ou namespace `com.github.alexyamoraes`.
3. Configure o plugin `maven-gpg-plugin` para assinar os artefatos com sua chave GPG.
4. Execute `mvn clean deploy` apontando para o portal Central.

---

## 🐙 Passo a Passo para Subir ao GitHub

Para subir este projeto para a sua conta no GitHub:

### 1. Criar o repositório no GitHub
Acesse [github.com/new](https://github.com/new) e crie um repositório com o nome:
`validador-documentos` (público ou privado, sem inicializar com README pois já temos um completo).

### 2. Conectar e Enviar o Código

Execute no terminal dentro da pasta do projeto:

```bash
cd /home/alexya/Documentos/Projetos/validador-documentos

# Adicionar o endereço remoto do seu repositório no GitHub
git remote add origin https://github.com/alexyamoraes/validador-documentos.git

# Definir a branch principal como main
git branch -M main

# Enviar o código para o GitHub
git push -u origin main
```

---

## 📄 Licença

Distribuído sob a licença **MIT**. Consulte `LICENSE` para obter mais informações.
