package bg.sofia.uni.fmi.mjt.gym.member;

import java.util.Comparator;

public class MemberCompareByProximityToGym implements Comparator<GymMember> {

    private final Address gymAddress;

    public MemberCompareByProximityToGym(Address gymAddress) {
        this.gymAddress = gymAddress;
    }

    @Override
    public int compare(GymMember o1, GymMember o2) {
        return Double.compare(o1.getAddress().getDistanceTo(this.gymAddress), o2.getAddress().getDistanceTo(this.gymAddress));
    }

}
