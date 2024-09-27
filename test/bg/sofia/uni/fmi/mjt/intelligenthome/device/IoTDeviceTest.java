package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import org.junit.jupiter.api.*;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class IoTDeviceTest {

    private static IoTDevice device;

    @BeforeAll
    static void setUpTests() {
        device = new WiFiThermostat("testDevice", 150, LocalDateTime.of(2023, 11, 22, 12, 30));
    }

    @BeforeEach
    void setUp() {
        IoTDeviceBase.uniqueNumberDevice = 0;
    }

    @AfterAll
    static void tearDownTest() {
        IoTDeviceBase.uniqueNumberDevice = 0;
    }

    @Test
    void testGetIdForEveryDeviceType() {
        IoTDevice alexa = new AmazonAlexa("alexa", 100, LocalDateTime.now());
        IoTDevice thermostat = new WiFiThermostat("thermostat", 100, LocalDateTime.now());
        IoTDevice bulb = new RgbBulb("bulb", 100, LocalDateTime.now());

        String outputMessage = "Method getId() should return a string in format type-name-uniqueNumberForDevice!";
        assertEquals("SPKR-alexa-0", alexa.getId(), outputMessage);
        assertEquals("TMST-thermostat-1", thermostat.getId(), outputMessage);
        assertEquals("BLB-bulb-2", bulb.getId(), outputMessage);
    }

    @Test
    void testGetPowerConsumptionKWh() {
        assertEquals(Duration.between(LocalDateTime.of(2023, 11, 22, 12, 30),
                LocalDateTime.now()).toHours() * 150, device.getPowerConsumptionKWh(),
                "Method getPowerConsumptionKWh should should return product of powerConsumption and hours since installation!");
    }

    @Test
    void testRegistrationReturnsCorrectTime() {
        device.setRegistration(LocalDateTime.now().minusHours(13));
        assertEquals(13, device.getRegistration(), "Method getRegistration() should return amount of hours since registration!");
    }

    @Test
    void testIoTDeviceEqualsChecksOnlyForIds() {
        IoTDevice device1 = new WiFiThermostat("thermostat1", 70, LocalDateTime.now());
        IoTDevice device2 = new WiFiThermostat("thermostat2", 76, LocalDateTime.now());
        IoTDeviceBase.uniqueNumberDevice = 0;
        IoTDevice device3 = new WiFiThermostat("thermostat1", 77, LocalDateTime.now());

        assertNotEquals(device1, device2, "Method equals() should return true only if both ids are the same!");
        assertNotEquals(device2, device3, "Method equals() should return true only if both ids are the same!");
        assertEquals(device1, device3, "Method equals() should return true only if both ids are the same!");
    }

    @Test
    void testIoTDeviceEqualsIfDeviceIsNull() {
        IoTDevice device1 = new WiFiThermostat("thermostat1", 70, LocalDateTime.now());
        assertFalse(device1.equals(null));
    }

    void testIoTDeviceEqualsIfDeviceIsTheSame() {
        IoTDevice device1 = new WiFiThermostat("thermostat1", 70, LocalDateTime.now());
        assertTrue(device1.equals(device1));
    }

    @Test
    void testIoTDeviceHashCode() {
        IoTDevice device1 = new WiFiThermostat("thermostat1", 70, LocalDateTime.now());
        IoTDevice device2 = new WiFiThermostat("thermostat2", 76, LocalDateTime.now());
        IoTDeviceBase.uniqueNumberDevice = 0;
        IoTDevice device3 = new WiFiThermostat("thermostat1", 77, LocalDateTime.now());

        assertNotEquals(device1.hashCode(), device2.hashCode(), "Method equals() should return true only if both ids are the same!");
        assertNotEquals(device2.hashCode(), device3.hashCode(), "Method equals() should return true only if both ids are the same!");
        assertEquals(device1.hashCode(), device3.hashCode(), "Method equals() should return true only if both ids are the same!");
    }

}
