package bg.sofia.uni.fmi.mjt.intelligenthome.center;

import bg.sofia.uni.fmi.mjt.intelligenthome.device.DeviceType;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.IoTDevice;
import bg.sofia.uni.fmi.mjt.intelligenthome.center.comparator.KWhComparator;
import bg.sofia.uni.fmi.mjt.intelligenthome.center.comparator.RegistrationComparator;
import bg.sofia.uni.fmi.mjt.intelligenthome.center.exceptions.DeviceAlreadyRegisteredException;
import bg.sofia.uni.fmi.mjt.intelligenthome.center.exceptions.DeviceNotFoundException;
import bg.sofia.uni.fmi.mjt.intelligenthome.storage.DeviceStorage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;

public class IntelligentHomeCenter {

    DeviceStorage storage;

    public IntelligentHomeCenter(DeviceStorage storage) {
        this.storage = storage;
    }

    /**
     * Adds a @device to the IntelligentHomeCenter.
     *
     * @throws IllegalArgumentException         in case @device is null.
     * @throws DeviceAlreadyRegisteredException in case the @device is already
     *                                          registered.
     */
    public void register(IoTDevice device) throws DeviceAlreadyRegisteredException {
        if (device == null) {
            throw new IllegalArgumentException("Device can not be null!");
        }

        if(this.storage.exists(device.getId())) {
            throw new DeviceAlreadyRegisteredException("The device is already registered!");
        }

        this.storage.store(device.getId(), device);
        device.setRegistration(LocalDateTime.now());
    }

    /**
     * Removes the @device from the IntelligentHomeCenter.
     *
     * @throws IllegalArgumentException in case null is passed.
     * @throws DeviceNotFoundException  in case the @device is not found.
     */
    public void unregister(IoTDevice device) throws DeviceNotFoundException {
        if (device == null) {
            throw new IllegalArgumentException("Device can not be null!");
        }

        if(!this.storage.exists(device.getId())) {
            throw new DeviceNotFoundException("The device could not be found!");
        }

        this.storage.delete(device.getId());
    }

    /**
     * Returns a IoTDevice with an ID @id if found.
     *
     * @throws IllegalArgumentException in case @id is null or blank.
     * @throws DeviceNotFoundException  in case device with ID @id is not found.
     */
    public IoTDevice getDeviceById(String id) throws DeviceNotFoundException {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Device can not be null or blank!");
        }

        if (this.storage.exists(id)) {
            return this.storage.get(id);
        }
        else {
            throw new DeviceNotFoundException("Device could not found!");
        }
    }

    /**
     * Returns the total number of devices with type @type registered in
     * SmartCityHub.
     *
     * @throws IllegalArgumentException in case @type is null.
     */
    public int getDeviceQuantityPerType(DeviceType type) {
        if(type == null) {
            throw new IllegalArgumentException("Type can not be null!");
        }
        int quantity = 0;

        for (IoTDevice device : this.storage.listAll()) {
            if (device.getType().equals(type)) {
                quantity++;
            }
        }

        return quantity;
    }

    /**
     * Returns a collection of IDs of the top @n devices which consumed
     * the most power from the time of their installation until now.
     *
     * The total power consumption of a device is calculated by the hours elapsed
     * between the two LocalDateTime-s: the installation time and the current time (now)
     * multiplied by the stated nominal hourly power consumption of the device.
     *
     * If @n exceeds the total number of devices, return all devices available sorted by the given criterion.
     * @throws IllegalArgumentException in case @n is a negative number.
     */
    public Collection<String> getTopNDevicesByPowerConsumption(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }

        List<IoTDevice> listTemp = new ArrayList<>(this.storage.listAll());

        listTemp.sort(new KWhComparator());

        //Collections.reverse(listTemp);

        List<String> arrList = new ArrayList<>();

        if (n > listTemp.size()) {
            n = listTemp.size();
        }

        for (int i = 0; i < n; i++) {
            arrList.add(listTemp.get(i).getId());
        }

        return arrList;
    }

    /**
     * Returns a collection of the first @n registered devices, i.e. the first @n that were added
     * in the IntelligentHomeCenter (registration != installation).
     *
     * If @n exceeds the total number of devices, return all devices available sorted by the given criterion.
     *
     * @throws IllegalArgumentException in case @n is a negative number.
     */
    public Collection<IoTDevice> getFirstNDevicesByRegistration(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }

        List<IoTDevice> listTemp = new ArrayList<>(this.storage.listAll());

        listTemp.sort(new RegistrationComparator());

        Collections.reverse(listTemp);

        List<IoTDevice> arrList = new ArrayList<>();

        if (n > listTemp.size()) {
            n = listTemp.size();
        }

        for (int i = 0; i < n; i++) {
            arrList.add(listTemp.get(i));
        }

        return arrList;
    }

}
