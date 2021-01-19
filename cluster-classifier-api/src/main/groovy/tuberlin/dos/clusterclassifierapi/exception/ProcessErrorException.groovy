package tuberlin.dos.clusterclassifierapi.exception

class ProcessErrorException extends RuntimeException{

    public ProcessErrorException() {
        super("Process returned an Error");
    }
}
