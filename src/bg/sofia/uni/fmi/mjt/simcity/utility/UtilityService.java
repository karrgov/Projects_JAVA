package bg.sofia.uni.fmi.mjt.simcity.utility;

import bg.sofia.uni.fmi.mjt.simcity.property.billable.Billable;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class UtilityService implements UtilityServiceAPI {

    private Map<UtilityType, Double> taxRates;

    public UtilityService(Map<UtilityType, Double> taxRates) {
        this.taxRates = taxRates;
    }

    @Override
    public <T extends Billable> double getUtilityCosts(UtilityType utilityType, T billable) {
        if(utilityType == null) {
            throw new IllegalArgumentException("Utility type can not be null!");
        }

        if(billable == null) {
            throw new IllegalArgumentException("Billable object can not be null!");
        }
        return switch (utilityType) {
            case WATER -> calculateWaterCostsForBuilding(billable);
            case ELECTRICITY -> calculateElectricityCostsForBuilding(billable);
            case NATURAL_GAS -> calculateNaturalGasCostsForBuilding(billable);
        };
    }

    private double calculateWaterCostsForBuilding(Billable billable) {
        return taxRates.get(UtilityType.WATER) * billable.getWaterConsumption();
    }

    private double calculateElectricityCostsForBuilding(Billable billable) {
        return taxRates.get(UtilityType.ELECTRICITY) * billable.getElectricityConsumption();
    }

    private double calculateNaturalGasCostsForBuilding(Billable billable) {
        return taxRates.get(UtilityType.NATURAL_GAS) * billable.getNaturalGasConsumption();
    }

    @Override
    public <T extends Billable> double getTotalUtilityCosts(T billable) {
        if(billable == null) {
            throw new IllegalArgumentException("Billable object can not be null!");
        }

        double total = 0.0d;
        for(UtilityType curr : UtilityType.values()) {
            total = total + getUtilityCosts(curr, billable);
        }

        return total;
    }

    @Override
    public <T extends Billable> Map<UtilityType, Double> computeCostsDifference(T firstBillable, T secondBillable) {
        if(firstBillable == null || secondBillable == null) {
            throw new IllegalArgumentException("Billable object can not be null!");
        }

        Map<UtilityType, Double> result = new EnumMap<>(UtilityType.class);

        for(UtilityType curr : UtilityType.values()) {
            double left = getUtilityCosts(curr, firstBillable);
            double right = getUtilityCosts(curr, firstBillable);

            double costDifference = Math.abs(left - right);
            result.putIfAbsent(curr, costDifference);
        }

        return Collections.unmodifiableMap(result);
    }

}
