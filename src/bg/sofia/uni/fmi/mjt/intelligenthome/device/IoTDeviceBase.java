package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public abstract class IoTDeviceBase implements IoTDevice {

    public static int uniqueNumberDevice = 0;
    protected String id;
    protected DeviceType type;
    private String name;
    private double powerConsumption;
    private LocalDateTime installationDateTime;
    private LocalDateTime registration;

    public IoTDeviceBase(String name, double powerConsumption, LocalDateTime installationDateTime) {
        this.name = name;
        this.powerConsumption = powerConsumption;
        this.installationDateTime = installationDateTime;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public double getPowerConsumption() {
        return powerConsumption;
    }

    @Override
    public LocalDateTime getInstallationDateTime() {
        return this.installationDateTime;
    }

    @Override
    public DeviceType getType() {
        return this.type;
    }

    @Override
    public long getRegistration() {
        return Duration.between(this.registration, LocalDateTime.now()).toHours();
    }

    @Override
    public void setRegistration(LocalDateTime registration) {
        this.registration = registration;
    }

    @Override
    public long getPowerConsumptionKWh() {
        long duration = Duration.between(getInstallationDateTime(), LocalDateTime.now()).toHours();
        return (long) (duration * this.powerConsumption);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        IoTDeviceBase that = (IoTDeviceBase) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

