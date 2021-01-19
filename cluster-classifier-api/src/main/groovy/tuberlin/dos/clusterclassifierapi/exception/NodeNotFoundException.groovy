package tuberlin.dos.clusterclassifierapi.exception;

class NodeNotFoundException extends RuntimeException {

    public NodeNotFoundException(String ip) {
        super("Could not find node with ip " + ip);
    }

}
