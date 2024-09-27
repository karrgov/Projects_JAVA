package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import java.time.LocalDateTime;

public class RgbBulb extends IoTDeviceBase {

    public RgbBulb(String name, double powerConsumption, LocalDateTime installationDateTime) {
        super(name, powerConsumption, installationDateTime);

        this.type = DeviceType.BULB;
        this.id = String.format("%s-%s-%d", this.getType().getShortName(), this.getName(), uniqueNumberDevice);
        uniqueNumberDevice++;
    }

}
