# Define as variáveis
CLIENT_SOURCES := Cliente.java InterfaceServidor.java StatusJogo.java Jogador.java
CLIENT_CLASSES := $(CLIENT_SOURCES:.java=.class)
CLIENT_JAR_FILE := Cliente.jar
MANIFEST_FILE := Manifest.txt

# Compila todas as classes Java do cliente
cliente: $(CLIENT_CLASSES)

# Regra para compilar cada classe Java do cliente
%.class: %.java
	javac $<

# Cria o arquivo JAR do cliente com o manifesto
Cliente.jar: $(CLIENT_CLASSES) $(MANIFEST_FILE)
	jar cfm $(CLIENT_JAR_FILE) $(MANIFEST_FILE) $(CLIENT_CLASSES)

# Remove os arquivos .class do cliente
clean-cliente:
	rm -f $(CLIENT_CLASSES)

# Remove o arquivo .jar do cliente
distclean-cliente: clean-cliente
	rm -f $(CLIENT_JAR_FILE)
