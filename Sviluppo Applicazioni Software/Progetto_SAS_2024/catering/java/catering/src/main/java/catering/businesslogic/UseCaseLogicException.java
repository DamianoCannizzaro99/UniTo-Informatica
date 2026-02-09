package catering.businesslogic;



public class UseCaseLogicException extends Exception {
    private String fileName;
    private int lineNumber;
    private String errorType;

    public UseCaseLogicException(String message) {
        super(message);
        extractErrorDetails(); // Estrae i dettagli dell'errore
    }

    private void extractErrorDetails() {
        // Prende il primo elemento dello stack trace
        StackTraceElement element = getStackTrace()[0];
        this.fileName = element.getFileName(); // Nome del file
        this.lineNumber = element.getLineNumber(); // Numero di riga
        this.errorType = this.getClass().getSimpleName(); // Tipo di errore (nome della classe)
    }

    // Metodo per ottenere i dettagli dell'errore
    public String getErrorDetails() {
        return String.format(
                "Errore: %s\nTipo: %s\nFile: %s\nLinea: %d",
                super.getMessage(), errorType, fileName, lineNumber
        );
    }

}
