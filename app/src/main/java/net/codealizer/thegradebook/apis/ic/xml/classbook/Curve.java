package net.codealizer.thegradebook.apis.ic.xml.classbook;

import com.google.gson.JsonObject;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.Serializable;
import java.util.ArrayList;

public class Curve implements Serializable {

    String curveID;
    String name;

    ArrayList<CurveItem> curves;

    public Curve(Element curve) {
        curves = new ArrayList<>();

        if (curve.hasAttribute("curveID"))
            this.curveID = curve.getAttribute("curveID");
        if (curve.hasAttribute("name"))
            this.name = curve.getAttribute("name");

        NodeList list = curve.getElementsByTagName("CurveListItem");
        for (int i = 0; i < list.getLength(); i++) {
            Element element = (Element) list.item(i);

            curves.add(new CurveItem(element));
        }
    }

    public String getCurveID() {
        return curveID;
    }

    public void setCurveID(String curveID) {
        this.curveID = curveID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<CurveItem> getCurves() {
        return curves;
    }

    public void setCurves(ArrayList<CurveItem> curves) {
        this.curves = curves;
    }

    private class CurveItem implements Serializable {

        private int itemID;
        private int curveID;
        private String name;
        private String score;
        private double creditCoeffecient;
        private double minPercent;
        private int seq;
        private boolean passingScore;

        public CurveItem(Element element) {
            if (element.hasAttribute("itemID"))
                itemID = Integer.parseInt(element.getAttribute("itemID"));
            if (element.hasAttribute("curveID"))
                curveID = Integer.parseInt(element.getAttribute("curveID"));
            if (element.hasAttribute("name"))
                name = element.getAttribute("name");
            if (element.hasAttribute("score"))
                score = element.getAttribute("score");
            if (element.hasAttribute("creditCoeffecient"))
                creditCoeffecient = Double.parseDouble(element.getAttribute("creditCoeffecient"));
            if (element.hasAttribute("seq"))
                seq = Integer.parseInt(element.getAttribute("seq"));
            passingScore = Boolean.valueOf(element.getAttribute("passingScore"));
        }

        public int getItemID() {
            return itemID;
        }

        public void setItemID(int itemID) {
            this.itemID = itemID;
        }

        public int getCurveID() {
            return curveID;
        }

        public void setCurveID(int curveID) {
            this.curveID = curveID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public double getCreditCoeffecient() {
            return creditCoeffecient;
        }

        public void setCreditCoeffecient(double creditCoeffecient) {
            this.creditCoeffecient = creditCoeffecient;
        }

        public double getMinPercent() {
            return minPercent;
        }

        public void setMinPercent(double minPercent) {
            this.minPercent = minPercent;
        }

        public int getSeq() {
            return seq;
        }

        public void setSeq(int seq) {
            this.seq = seq;
        }

        public boolean isPassingScore() {
            return passingScore;
        }

        public void setPassingScore(boolean passingScore) {
            this.passingScore = passingScore;
        }
    }
}
