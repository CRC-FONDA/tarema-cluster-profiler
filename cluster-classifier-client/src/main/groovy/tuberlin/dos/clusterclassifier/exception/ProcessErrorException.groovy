package tuberlin.dos.clusterclassifier.exception

class ProcessErrorException extends RuntimeException{

    public ProcessErrorException() {
        super("Process returned an Error");
    }
}
