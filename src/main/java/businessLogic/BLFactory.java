package businessLogic;

/** Obtiene la fachada local o remota que solicita la presentacion. */
public class BLFactory {

    public BLFacade getBusinessLogicFactory(boolean isLocal) {
        if (isLocal) {
            return createLocalBusinessLogic();
        } else {
            return createRemoteBusinessLogic();
        }
    }

    /** Crea la implementacion local. */
    protected BLFacade createLocalBusinessLogic() {
        return new BLFacadeImplementation();
    }

    /** Obtiene la implementacion que se comunica con el servidor. */
    protected BLFacade createRemoteBusinessLogic() {
        return RemoteFacade.connect();
    }
}
