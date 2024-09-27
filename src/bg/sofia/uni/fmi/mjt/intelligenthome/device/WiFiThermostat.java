package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import java.time.LocalDateTime;

public class WiFiThermostat extends IoTDeviceBase {

    public WiFiThermostat(String name, double powerConsumption, LocalDateTime installationDateTime) {
        super(name, powerConsumption, installationDateTime);

        this.type = DeviceType.THERMOSTAT;
        this.id = String.format("%s-%s-%d", this.getType().getShortName(), this.getName(), uniqueNumberDevice);
        uniqueNumberDevice++;
    }

}
