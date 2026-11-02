package businessLogic;
import java.util.Objects;
/** Selects a concrete Creator; presentation only supplies the desired mode. */
public final class BLFactory {
    public abstract static class Creator {
        public abstract BLFacade create();
    }
    public static final class LocalCreator extends Creator {
        @Override public BLFacade create() { return new BLFacadeImplementation(); }
    }
    public static final class RemoteCreator extends Creator {
        @Override public BLFacade create() { return RemoteFacade.connect(); }
    }
    private final Creator local;
    private final Creator remote;
    public BLFactory() { this(new LocalCreator(), new RemoteCreator()); }
    /** Tests may inject a Creator returning a Mockito mock without opening a database. */
    public BLFactory(Creator local, Creator remote) {
        this.local = Objects.requireNonNull(local, "local");
        this.remote = Objects.requireNonNull(remote, "remote");
    }
    public BLFacade getBusinessLogicFactory(boolean isLocal) {
        return (isLocal ? local : remote).create();
    }
}
