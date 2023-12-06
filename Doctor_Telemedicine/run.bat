@echo off
set "JAVA_HOME=C:\Program Files\Java\jdk-19"
"%JAVA_HOME%\bin\java.exe" --module-path "C:\Users\basag\Downloads\javafx-sdk-21.0.1\lib" --add-modules javafx.controls,javafx.fxml -Xms256m -Xmx512m -jar Doctor_Telemedicine.jar
echo Script ejecutado correctamente.