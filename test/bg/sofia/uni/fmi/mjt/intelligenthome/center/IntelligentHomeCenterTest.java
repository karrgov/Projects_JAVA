package bg.sofia.uni.fmi.mjt.intelligenthome.center;

import bg.sofia.uni.fmi.mjt.intelligenthome.center.exceptions.DeviceAlreadyRegisteredException;
import bg.sofia.uni.fmi.mjt.intelligenthome.center.exceptions.DeviceNotFoundException;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.DeviceType;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.IoTDevice;
import bg.sofia.uni.fmi.mjt.intelligenthome.storage.DeviceStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IntelligentHomeCenterTest {

    @Mock
    private DeviceStorage storageMock;

    @InjectMocks
    private IntelligentHomeCenter intelligentHomeCenter;

    @Test
    void testRegisterThrowsExceptionIfDeviceIsNull() {
        assertThrows(IllegalArgumentException.class, () -> intelligentHomeCenter.register(null),
                "Method register() should receive a value that is not null!");
    }

    @Test
    void testRegisterThrowsExceptionIfDeviceAlreadyExists() {
        IoTDevice device = mock();

        when(storageMock.exists(device.getId())).thenReturn(true);
        assertThrows(DeviceAlreadyRegisteredException.class, () -> intelligentHomeCenter.register(device),
                "Method register() should receive a device that is not already registered!");
    }

    @Test
    void testRegisterDeviceRegistersSuccessfully() {
        IoTDevice device = mock();
        when(device.getId()).thenReturn("SPKR-device-0");

        when(storageMock.exists(device.getId())).thenReturn(false);

        assertDoesNotThrow(() -> intelligentHomeCenter.register(device),
                "Method register() should not throw an exception if a unregistered device is being registered!");

        when(storageMock.exists(device.getId())).thenReturn(true);
        when(storageMock.get(device.getId())).thenReturn(device);

        assertEquals(device, intelligentHomeCenter.getDeviceById(device.getId()),
                "If method register is successful then method getDeviceById should return the device!");
    }

    @Test
    void testUnregisterThrowsExceptionIfDeviceIsNull() {
        assertThrows(IllegalArgumentException.class, () -> intelligentHomeCenter.unregister(null),
                "Method unregister() should receive a device that is not null!");
    }

    @Test
    void testUnregisterThrowsExceptionIfDeviceDoesNotExist() {
        IoTDevice device = mock();

        when(storageMock.exists(device.getId())).thenReturn(false);

        assertThrows(DeviceNotFoundException.class, () -> intelligentHomeCenter.unregister(device),
                "Method unregister() should receive a device that is already registered!");
    }

    @Test
    void testUnregisterDeviceUnregistersSuccessfully() {
        IoTDevice device = mock();

        when(device.getId()).thenReturn("SPKR-device-0");
        when(storageMock.exists(device.getId())).thenReturn(true);

        assertDoesNotThrow(() -> intelligentHomeCenter.unregister(device),
                "Method unregister should unregister the device!");
    }

    @Test
    void testGetDeviceByIdThrowsExceptionIfItIsNullOrBlank() {
        String outputMessage = "Method getDeviceById() should accept a string that is not null or blank!";

        assertThrows(IllegalArgumentException.class, () -> intelligentHomeCenter.getDeviceById(null), outputMessage);

        assertThrows(IllegalArgumentException.class, () -> intelligentHomeCenter.getDeviceById("  "), outputMessage);
    }

    @Test
    void testGetDeviceByIdThrowsExceptionIfDeviceWithIdDoesNotExist() {
        IoTDevice device = mock();

        when(device.getId()).thenReturn("SPKR-device-0");
        when(storageMock.exists(device.getId())).thenReturn(false);

        assertThrows(DeviceNotFoundException.class, () -> intelligentHomeCenter.getDeviceById(device.getId()),
                "Method getDeviceById() should receive an Id of a device that is already registered!");
    }

    @Test
    void testGetDeviceByIdReturnsTheCorrectDevice() {
        IoTDevice device = mock();

        when(device.getId()).thenReturn("SPKR-device-0");
        when(storageMock.exists(device.getId())).thenReturn(true);
        when(storageMock.get(device.getId())).thenReturn(device);

        assertEquals(device, intelligentHomeCenter.getDeviceById(device.getId()),
                "Method getDeviceById() should return the device!");

    }

    @Test
    void testGetDeviceQuantityPerTypeThrowsExceptionIfTypeIsNull() {
        assertThrows(IllegalArgumentException.class, () -> intelligentHomeCenter.getDeviceQuantityPerType(null),
                "Method getDeviceQuantityPerType() should receive a deviceType that is not null!");
    }

    @Test
    void testGetDeviceQuantityPerTypeReturnsCorrectValue() {
        IoTDevice device1 = mock();
        IoTDevice device2 = mock();
        IoTDevice device3 = mock();

        when(device1.getType()).thenReturn(DeviceType.SMART_SPEAKER);
        when(device2.getType()).thenReturn(DeviceType.BULB);
        when(device3.getType()).thenReturn(DeviceType.BULB);

        when(storageMock.listAll()).thenReturn(List.of(device1, device2, device3));

        assertEquals(0, intelligentHomeCenter.getDeviceQuantityPerType(DeviceType.THERMOSTAT));
        assertEquals(1, intelligentHomeCenter.getDeviceQuantityPerType(DeviceType.SMART_SPEAKER));
        assertEquals(2, intelligentHomeCenter.getDeviceQuantityPerType(DeviceType.BULB));
    }

    @Test
    void testGetTopNDevicesByPowerConsumptionThrowsExceptionIfNIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> intelligentHomeCenter.getTopNDevicesByPowerConsumption(-1),
                "Method getTopNDevicesByPowerConsumption() should receive a non negative number!");
    }

    @Test
    void testGetTopNDevicesByPowerConsumptionReturnsCorrectCollection() {
        IoTDevice device1 = mock();
        IoTDevice device2 = mock();
        IoTDevice device3 = mock();

        when(device1.getId()).thenReturn("BLB-bulb-0");
        when(device1.getPowerConsumptionKWh()).thenReturn(50L);

        when(device2.getId()).thenReturn("SPKR-speaker-1");
        when(device2.getPowerConsumptionKWh()).thenReturn(110L);

        when(device3.getId()).thenReturn("TMST-thermostat-2");
        when(device3.getPowerConsumptionKWh()).thenReturn(80L);

        when(storageMock.listAll()).thenReturn(List.of(device1, device2, device3));

        assertEquals(List.of(device2.getId(), device3.getId()), intelligentHomeCenter.getTopNDevicesByPowerConsumption(2));

        assertEquals(List.of(device2.getId(), device3.getId(), device1.getId()), intelligentHomeCenter.getTopNDevicesByPowerConsumption(7));
    }

    @Test
    void testGetFirstNDevicesByRegistrationThrowsExceptionIfNIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> intelligentHomeCenter.getFirstNDevicesByRegistration(-1),
                "Method getFirstNDevicesByRegistration() should receive a non negative number!");
    }

    @Test
    void testGetFirstNDevicesByRegistrationReturnsCorrectCollection() {
        IoTDevice device1 = mock();
        IoTDevice device2 = mock();
        IoTDevice device3 = mock();

        when(device1.getRegistration()).thenReturn(50L);
        when(device2.getRegistration()).thenReturn(110L);
        when(device3.getRegistration()).thenReturn(80L);

        when(storageMock.listAll()).thenReturn(List.of(device1, device2, device3));

        assertEquals(List.of(device2, device3), intelligentHomeCenter.getFirstNDevicesByRegistration(2));

        assertEquals(List.of(device2, device3, device1), intelligentHomeCenter.getFirstNDevicesByRegistration(7));
    }

}
