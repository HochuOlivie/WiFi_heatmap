package wifi;

import sample.PVector;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Transmitter {

    // receivedPower = transmitterOutputPower + transmitterAntennaGain - pathLoss - interferenceMargin - penetrationLosses
    // MAPL = transmitterOutputPower + transmitterAntennaGain - interferenceMargin - penetrationLoss - RxSens
    // RxSens = thermalNoise + noiseFigure + SINR
    // ThermalNoise = -174 + log10(bandwidth)

    private double transmitterOutputPower;
    private double transmitterAntennaGain;

    private double xCoord;
    private double yCoord;
    private double height;

    private double frequency;
    private double interferenceMargin;
    private double penetrationLosses;

    private double bandwidth;
    private double noiseFigure;
    private double SINR;

    private ArrayList<Double> distance;

    public Transmitter(double xCoord, double yCoord, double height, double transmitterOutputPower,
                       double transmitterAntennaGain, double frequency, double interferenceMargin,
                       double penetrationLosses, double bandwidth, double noiseFigure, double SINR) {

        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.height = height;
        this.transmitterAntennaGain = transmitterAntennaGain;
        this.transmitterOutputPower = transmitterOutputPower;
        this.frequency = frequency;
        this.interferenceMargin = interferenceMargin;
        this.penetrationLosses = penetrationLosses;
        this.bandwidth = bandwidth;
        this.noiseFigure = noiseFigure;
        this.SINR = SINR;

        this.distance = maxDistance();
    }

    private ArrayList<Double> maxDistance() {

        ArrayList<Double> distance = new ArrayList<>();

        double cur_dist = 1;
        double cur_signal = 0;

        do {
            cur_signal = receivedPower(cur_dist);
            cur_dist += 10;
        } while (cur_signal > -60);

        distance.add(cur_dist);

        do {
            cur_signal = receivedPower(cur_dist);
            cur_dist += 10;
        } while (cur_signal > -75);

        distance.add(cur_dist);

        do {
            cur_signal = receivedPower(cur_dist);
            cur_dist += 10;
        } while (cur_signal > -90);

        distance.add(cur_dist);

        return distance;
    }

    private double MAPL() {
        return transmitterOutputPower + transmitterAntennaGain - penetrationLosses - interferenceMargin;
    }

    private double thermalNoise() {
        return -174 + Math.log10(this.bandwidth * Math.pow(10, 6));
    }

    private double RxSens() {
        return thermalNoise() - noiseFigure + SINR;
    }

    public double getPower(double x, double y) {

        PVector vector = new PVector(x, y);
        double radius = vector.dist(new PVector(this.xCoord, this.yCoord));

//        System.out.println(radius);

        double distance = Math.sqrt(Math.pow(radius, 2) + Math.pow(this.height, 2));

        return receivedPower(distance);
    }

    private double receivedPower(double distance) {
        return transmitterOutputPower + transmitterAntennaGain - pathLoss(distance) - interferenceMargin - penetrationLosses;
    }

    private double pathLoss(double distance) {
        return -27 + 26 * Math.log10(frequency) + 20 * Math.log10(distance);
    }


    public void setHeight(double height) {
        this.height = height;
    }

    public void setyCoord(double yCoord) {
        this.yCoord = yCoord;
    }

    public void setxCoord(double xCoord) {
        this.xCoord = xCoord;
    }

    public double getxCoord() {
        return xCoord;
    }

    public double getyCoord() {
        return yCoord;
    }

    public ArrayList<Double> getDistance() {
        return distance;
    }

    public double getHeight() {
        return height;
    }
}
