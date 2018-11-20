# Spring Boot Batch Sample

# Opération

* Lire les données du fichier capturé
* Ecrire des données dans la base de données
* Lire les données de la base de données
* Envoyer un mail à l'adresse mail

# Vue d'ensemble

* Velocity est utilisé comme modèle du corps du mail
    * Le modèle est lu à partir de la base de données.
    * Pour les données initiales, reportez-vous au fichier de migration de flyway (package db.migration).

# Run

# Préparation

* Créer un fichier d'importation

src/resources/sample-data.csv

    tarou,yamada,yamada@example.com
    hanako,yamada,yamada@example.com

* Modifier les paramètres SMTP
    * Le paramètre SMTP est src/resources/application.yml

Pour les tests, [FakeSMTP] (http://nilhcem.github.io/FakeSMTP/index.html) est utile.

## Compiler & Test

Exécutez les opérations suivantes dans le répertoire de base du projet

    Gradlew

## démarrage

Exécutez les opérations suivantes dans le répertoire de base du projet

    gradlew bootRun -Pargs="-job sendMailJob"

Ou appelez directement build / libs / spring-boot-batch-sample.jar

    java -jar spring-boot-batch-sample.jar -job sendMailJob

## erreur

Si un redémarrage est requis en raison d’une erreur ou d’une erreur similaire, la réexécution est effectuée à partir de l’endroit où la dernière erreur s’est produite en exécutant les opérations suivantes:

    gradlew bootRun -Pargs="-job sendMailJob -restart"
    java -jar spring-boot-batch-sample.jar -job sendMailJob -restart

## Lancement en ligne de commande de Spring-Boot-Batch

### Exécution par défaut de la ligne de commande

Dans cet exemple, CommandLineRunner est implémenté par lui-même et l'exécution de la ligne de commande est exécutée.

Dans SpringBoot - Batch, une méthode (JobLauncherCommandLineRunner) doit être exécutée à partir de la ligne de commande par défaut.

### Procédure d'exécution

* Changement de spring.batch.job.enabled dans application.yml en true
* Commentez @Component de Command.CommandLineBatch (non soumis à DI)
* Construire avec "gradlew -x test" (test sautant car il échoue)

Lorsque vous exécutez les opérations suivantes, tous les travaux enregistrés par @EnableBatchProcessing (sendMailJob, conditionalJob) sont exécutés.

    java -jar spring-boot-batch-sample.jar time(long)=1

Le nom du travail peut également être spécifié et exécuté individuellement ci-dessous.

    java -jar spring-boot-batch-sample.jar --spring.batch.job.names=sendMailJob time(long)=1

Spring.batch.job.names peut spécifier plusieurs travaux séparés par des virgules.

    java -jar spring-boot-batch-sample.jar --spring.batch.job.names=sendMailJob,conditionalJob time(long)=1

### Paramètres paramètres

La propriété (dans l'exemple "time(long)=1") peut être saisie ci-dessous.

* (long)
* (chaîne)
* (date)
* (double)

Le travail exécuté par JobLauncherCommandLineRunner est ajouté avec run.id en tant que paramètre.

Étant donné que run.id est incrémenté chaque fois que l'exécution d'un lot est exécutée, le SpringBatch "Le travail du même paramètre n'est pas réexécuté" est évité.

[Configuration et exécution d'un travail] (http://docs.spring.io/spring-batch/trunk/reference/html/configureJob.html#restartability)

### Error Ré-exécution du job

Si le résultat de l'exécution précédente est une erreur (STOP ou FAILED) et que les paramètres sont identiques, il sera réexécuté.

Par exemple, si "time(long)=1" est démarré et qu’une erreur se produit dans insertDataStep

    taskletlStep -> insertDataStep (error) -> sendMailStep

* Réexécutez avec le paramètre "time(long)=1"
    * run.id est identique et redémarré à partir duquel l'erreur insertDataStep s'est produite
* Exécution avec le paramètre "time(long)=2"
    * run.id est identique, mais il a été démarré depuis taskletlStep
