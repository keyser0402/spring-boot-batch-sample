# Spring Boot Batch Sample

## Comportement
   * Lire les données du fichier importé
   * Écrire des données dans la base de données
   * Lire les données de la base de données
   * Envoyer un mail à l'adresse email de données
## Vue d'ensemble
   * Utilisation de Velocity comme modèle du corps du courrier
    ** Le modèle est lu à partir de la base de données
    ** Pour les données initiales, reportez-vous au fichier de migration de flyway (package db.migration).

## Exécution
   * La préparation
   * Créer un fichier capturé
   * src / resources / sample-data.csv

tarou, yamada, yamada @ exemple.com
Hanako, Yamada, Yamada @ exemple.com
Changer le paramètre SMTP
Le paramètre SMTP est src / resources / application.yml
FakeSMTP est utile pour les tests.

Compiler et tester
Exécutez les opérations suivantes dans le répertoire de base du projet

Gradlew
Activation
Exécutez les opérations suivantes dans le répertoire de base du projet

gradlew bootRun - Pargs = "- job sendMailJob"
Ou appelez directement build / libs / spring-boot-batch-sample.jar

java - jar spring - démarrage - lot - échantillon.jar - travail sendMailJob
Erreur
Si un redémarrage est requis en raison d’une erreur ou d’une erreur similaire, la réexécution est effectuée à partir de l’endroit où la dernière erreur s’est produite en exécutant les opérations suivantes:

gradlew bootRun - Pargs = "- job sendMailJob - restart"
java - jar spring - démarrage - lot - échantillon.jar - travail sendMailJob - redémarrage
Lancement de la ligne de commande Spring-Boot-Batch
Exécution en ligne de commande par défaut
Dans cet exemple, CommandLineRunner est implémenté par lui-même et l'exécution en ligne de commande est exécutée.

Dans SpringBoot - Batch, une méthode (JobLauncherCommandLineRunner) doit être exécutée à partir de la ligne de commande par défaut.

Procédure d'exécution
Définissez spring.batch.job.enabled dans application.yml sur true
Comment out @ Composant de sample.CommandLineBatch (non soumis à DI)
Construire avec "test gradlew -x" (test sautant car il échoue)
Lorsque vous exécutez les opérations suivantes, tous les travaux enregistrés par @EnableBatchProcessing (sendMailJob, conditionalJob) sont exécutés.

java - jar spring - démarrage - lot - échantillon.jar temps (long) = 1
Le nom du travail peut également être spécifié et exécuté individuellement ci-dessous.

java - jar spring - boot - batch - sample.jar - spring.batch.job.names = sendMailJob time (long) = 1
Spring.batch.job.names peut spécifier plusieurs travaux séparés par des virgules.

java - jar spring - boot - batch - sample.jar - spring.batch.job.names = sendMailJob, conditionnelJob time (long) = 1
Paramètres d'argument
La propriété (dans l'exemple "partie" time (long) = 1 ") peut être saisie ci-dessous.

(long)
(chaîne)
(date)
(double)
Le travail exécuté par JobLauncherCommandLineRunner est ajouté avec run.id en tant que paramètre.

Étant donné que run.id est incrémenté chaque fois que l'exécution d'un lot est exécutée, le SpringBatch "Le travail du même paramètre n'est pas réexécuté" est évité.

Configuration et exécution d'un travail

Ré-exécuter le job d'erreur
Si le résultat de l'exécution précédente est une erreur (STOP ou FAILED) et que les paramètres sont identiques, il sera réexécuté.

Par exemple, si "time (long) = 1" est démarré et qu’une erreur se produit dans insertDataStep

taskletlStep -> insertDataStep (erreur) -> sendMailStep
Réexécutez avec le paramètre "time (long) = 1"
Run.id est identique et reprend où l'erreur insertDataStep s'est produite
Exécuter avec le paramètre "time (long) = 2"
Run.id est le même, mais un nouveau départ à partir de taskletlStep
