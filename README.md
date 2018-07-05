# Boletos Bancários (BankSlips)

## Objetivo:

* Criar Endpoints REST para listar, criar, atualizar e cancelar boletos bancários

## Tecnologias empregadas:

* Spring Boot
    * Spring Data
    * Spring Web
* Swagger
* Docker
* Maven
* JUnit

## Como rodar

### Pré-requisitos

* Git
* Java 8+
* Maven 3+
* Docker (Opcional)

### Rodar projeto local

```
$ git clone https://github.com/lsegala/bankslips.git
$ mvn package
$ cd rest/target
$ java -jar rest-0.0.1-SNAPSHOT.jar
```

### Rodar projeto com o docker

Edite as propriedades docker.image.prefix e docker.image.sufix do arquivo pom.xml do projeto parent com suas credenciais no docker.io

```
$ git clone https://github.com/lsegala/bankslips.git
$ mvn deploy
$ docker run -p 8080:8080 -t ${docker.image.prefix}/${docker.image.sufix}
```

### Rodar o projeto diretamento do meu reposítorio docker

```
$ docker run -p 8080:8080 -t leonardosegala/contaazul
```

### Documentação da API

http://localhost:8080/v2/api-docs

## Problemas

Se estiver rodando no windows com docker toolbox, talvez será necessário executar o comando:

```
$ VBoxManage modifyvm "default" --natpf1 "guestssh,tcp,,2375,,2376"
```


