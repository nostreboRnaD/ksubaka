# Compilation

From the home directory of the project, run:

`mvn clean install`

This will create a runnable .jar in the target directory

# Running

From the target directory created during compilation, run:

`java -Dapi=<api> -Dtitle=<title> -jar query-jar-with-dependencies.jar`

Possible values for <api> are "imdb" (to search for films) and "musicbrainz" (for music).
<title> is the name of the film or album, respectively.
Both parameters are case-insensitive.