package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import java.time.LocalDateTime;

public class AmazonAlexa extends IoTDeviceBase {

    public AmazonAlexa(String name, double powerConsumption, LocalDateTime installationDateTime) {
        super(name, powerConsumption, installationDateTime);

        this.type = DeviceType.SMART_SPEAKER;
        this.id = String.format("%s-%s-%d", this.getType().getShortName(), this.getName(), uniqueNumberDevice);
        uniqueNumberDevice++;
    }

}
