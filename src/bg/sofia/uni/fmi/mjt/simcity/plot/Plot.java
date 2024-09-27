package bg.sofia.uni.fmi.mjt.simcity.plot;

import bg.sofia.uni.fmi.mjt.simcity.exception.BuildableAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.simcity.exception.BuildableNotFoundException;
import bg.sofia.uni.fmi.mjt.simcity.exception.InsufficientPlotAreaException;
import bg.sofia.uni.fmi.mjt.simcity.property.buildable.Buildable;

import java.util.HashMap;
import java.util.Map;

public class Plot<E extends Buildable> implements PlotAPI<E> {

    private final int buildableArea;
    private int remainingBuildableArea;
    private Map<String, E> buildings;

    public Plot(int buildableArea) {
        this.buildableArea = buildableArea;
        this.remainingBuildableArea = buildableArea;
        this.buildings = new HashMap<>();
    }

    @Override
    public void construct(String address, E buildable) {
        if(address == null || address.isBlank()) {
            throw new IllegalArgumentException("Address can not be null or blank!");
        }

        if(buildable == null) {
            throw new IllegalArgumentException("Buildable can not be null!");
        }

        if(buildings.containsKey(address)) {
            throw new BuildableAlreadyExistsException("The address is already occupied!");
        }

        if(buildable.getArea() > remainingBuildableArea) {
            throw new InsufficientPlotAreaException("Not enough area to construct the building!");
        }

        this.buildings.put(address, buildable);
        this.remainingBuildableArea = this.buildableArea - buildable.getArea();
    }

    @Override
    public void constructAll(Map<String, E> buildables) {
        if(buildables == null || buildables.isEmpty()) {
            throw new IllegalArgumentException("Buildables can not be null or empty!");
        }

        int lastRemainingBuildableArea = this.remainingBuildableArea;
        Map<String, E> lastState = new HashMap<>(this.buildings);

        for(Map.Entry<String, E> curr : buildables.entrySet()) {
            try {
                construct(curr.getKey(), curr.getValue());
            } catch (Exception e) {
                this.buildings = lastState;
                this.remainingBuildableArea = lastRemainingBuildableArea;
                throw e;
            }
        }
    }

    @Override
    public void demolish(String address) {
        if(address == null || address.isBlank()) {
            throw new IllegalArgumentException("Address can not be null or blank!");
        }

        boolean isReal = false;
        for(Map.Entry<String, E> curr : this.buildings.entrySet()) {
            if(curr.getKey().equals(address)) {
                isReal = true;
                break;
            }
        }

        if(!isReal) {
            throw new BuildableNotFoundException("The building was not found!");
        }

        Buildable demolished = this.buildings.remove(address);
        int updateArea = demolished.getArea();
        this.remainingBuildableArea = this.remainingBuildableArea + updateArea;
    }

    @Override
    public void demolishAll() {
        this.remainingBuildableArea = this.buildableArea;
        buildings.clear();
    }

    @Override
    public Map<String, E> getAllBuildables() {
        return Map.copyOf(this.buildings);
    }

    @Override
    public int getRemainingBuildableArea() {
        return remainingBuildableArea;
    }
}
