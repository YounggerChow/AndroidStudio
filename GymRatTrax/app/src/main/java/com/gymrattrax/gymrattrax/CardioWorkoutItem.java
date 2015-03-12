package com.gymrattrax.gymrattrax;

public class CardioWorkoutItem extends WorkoutItem {
    private int ID;
    private String name;
    private double time;
    private double METSVal;
    private double distance;

    public CardioWorkoutItem() {
        super();
        this.setType(WorkoutType.CARDIO);
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
        if (time > 0)
            setMETSVal();
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
        if (distance > 0)
            setMETSVal();
    }

    public double getMETSVal() {
        return METSVal;
    }

    private void setMETSVal() {
        //miles per hour, multiplied by a factor of 1.6529
        METSVal = 1.6529*distance/(time/60);
    }
}