package com.company;

public class Node {
    private int xCord;
    private int yCord;
    private float distanceToStart;
    private float distanceToEnd;
    private float totalDistance = distanceToEnd + distanceToStart;
    private Boolean isSolid;
    private Node origin;


    public Node(int xCord, int yCord, Boolean isSolid, Node origin) {
        this.xCord = xCord;
        this.yCord = yCord;
        this.isSolid = isSolid;
        this.origin = origin;
    }


    public int getxCord() {
        return xCord;
    }

    public void setxCord(int xCord) {
        this.xCord = xCord;
    }

    public int getyCord() {
        return yCord;
    }

    public void setyCord(int yCord) {
        this.yCord = yCord;
    }

    public Boolean getSolid() {
        return isSolid;
    }

    public void setSolid(Boolean solid) {
        isSolid = solid;
    }

    public Node getOrigin() {
        return origin;
    }

    public void setOrigin(Node origin) {
        this.origin = origin;
    }

    public float getDistanceToStart() {
        return distanceToStart;
    }

    public void setDistanceToStart(float distanceToStart) {
        this.distanceToStart = distanceToStart;
    }

    public float getDistanceToEnd() {
        return distanceToEnd;
    }

    public void setDistanceToEnd(float distanceToEnd) {
        this.distanceToEnd = distanceToEnd;
    }

    public float getTotalDistance() {
        return distanceToEnd + distanceToStart;
    }

    public void setTotalDistance(float totalDistance) {
        this.totalDistance = totalDistance;
    }

}
