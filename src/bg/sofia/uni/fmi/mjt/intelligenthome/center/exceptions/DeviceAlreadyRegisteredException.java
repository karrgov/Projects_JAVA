package bg.sofia.uni.fmi.mjt.intelligenthome.center.exceptions;

public class DeviceAlreadyRegisteredException extends RuntimeException {

    public DeviceAlreadyRegisteredException(String message) {
        super(message);
    }

    public DeviceAlreadyRegisteredException(String message, Throwable cause) {
      super(message, cause);
    }

}
