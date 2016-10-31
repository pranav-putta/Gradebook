package net.codealizer.thegradebook.apis.ic.curves;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Pranav on 10/23/16.
 */

public class Rubric implements Serializable {

    private int scoreGroupID;
    private String name;

    private ArrayList<RubricItem> items;

    public int getScoreGroupID() {
        return scoreGroupID;
    }

    public String getName() {
        return name;
    }

    public ArrayList<RubricItem> getItems() {
        return items;
    }

    public Rubric(Element rubric) {

        scoreGroupID = Integer.parseInt(rubric.getAttribute("scoreGroupID"));
        name = rubric.getAttribute("name");
        items = new ArrayList<>();

        NodeList list = rubric.getElementsByTagName("Item");

        for (int i = 0; i < list.getLength(); i++) {
            Element e = (Element) list.item(i);
            items.add(new RubricItem(e));
        }


    }

    private class RubricItem implements Serializable {

        private int itemID;
        private String score;
        private String name;
        private String gpaValue;
        private String bonusPoints;
        private int seq;

        public RubricItem(Element e) {

            itemID = Integer.parseInt(e.getAttribute("itemID"));
            score = e.getAttribute("score");
            name = e.getAttribute("name");
            gpaValue = e.getAttribute("gpaValue");
            bonusPoints = e.getAttribute("bonusPoints");
            seq = Integer.parseInt(e.getAttribute("seq"));

        }
    }

}
