package net.codealizer.thegradebook.data;

import android.util.Log;
import android.util.Pair;

import net.codealizer.thegradebook.apis.ic.classbook.Classbook;
import net.codealizer.thegradebook.apis.ic.classbook.ClassbookActivity;
import net.codealizer.thegradebook.apis.ic.classbook.ClassbookGroup;
import net.codealizer.thegradebook.apis.ic.classbook.ClassbookTask;
import net.codealizer.thegradebook.assets.Grade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Pranav on 10/19/16.
 */

public class GradesManager {

    public static Grade calculateEBR(ArrayList<ClassbookTask> tasks) {
        ArrayList<Integer> averageProfScores = new ArrayList<>();

        for (ClassbookTask task : tasks) {
            averageProfScores.add(calculateProfScore(task.groups));
        }

        int four = 0;
        int three = 0;
        int two = 0;
        int one = 0;

        for (Integer s : averageProfScores) {
            switch (s) {
                case 4:
                    four++;
                    break;
                case 3:
                    three++;
                    break;
                case 2:
                    two++;
                    break;
                case 1:
                    one++;
                    break;
            }
        }

        double score = ((three + four) / averageProfScores.size());

        if (one > 0) {
            return Grade.CD;
        }

        if (score == 1) {
            return Grade.A;
        } else if (score >= 0.66) {
            return Grade.AB;
        } else if (score >= 0.33) {
            return Grade.CD;
        } else {
            return Grade.D;
        }

    }

    private static int calculateProfScore(ArrayList<ClassbookGroup> groups) {

        ArrayList<Integer> profScores = new ArrayList<>();

        for (ClassbookGroup group : groups) {
            profScores.add(findGroupScore(group));
        }

        int four = 0;
        int three = 0;
        int two = 0;
        int one = 0;

        for (Integer s : profScores) {
            switch (s) {
                case 4:
                    four++;
                    break;
                case 3:
                    three++;
                    break;
                case 2:
                    two++;
                    break;
                case 1:
                    one++;
                    break;
            }
        }

        return ((int) ((four + three + two + one) / 4));

    }

    private static int findGroupScore(ClassbookGroup group) {
        int four = 0;
        int three = 0;
        int two = 0;
        int one = 0;

        for (ClassbookActivity activity : group.activities) {
            if (activity.active) {
                double score;
                try {
                    score = Double.parseDouble(activity.score);
                } catch (NumberFormatException ex) {
                    return 0;
                }
                if (score == 4) {
                    four++;
                    break;
                } else if (score >= 3) {
                    three++;
                    break;
                } else if (score >= 2) {
                    two++;
                    break;
                } else if (score >= 1) {
                    one++;
                    break;
                }
            }
        }

        ArrayList<Pair<String, Integer>> rank = rankByScore(four, three, two, one);

        int firstMostCommon = Integer.parseInt(rank.get(0).first);
        int secondMostCommon = Integer.parseInt(rank.get(1).first);

        if (firstMostCommon == 4 && secondMostCommon == 3) {
            return 4;
        } else if (firstMostCommon == 4 && secondMostCommon == 2) {
            return 4;
        } else if (firstMostCommon == 3 && secondMostCommon == 4) {
            return 4;
        } else if (firstMostCommon == 4 && secondMostCommon == 1) {
            return 3;
        } else if (firstMostCommon == 3 && secondMostCommon == 2) {
            return 3;
        } else if (firstMostCommon == 3 && secondMostCommon == 1) {
            return 3;
        } else if (firstMostCommon == 2 && secondMostCommon == 3) {
            return 2;
        } else if (firstMostCommon == 2 && secondMostCommon == 4) {
            return 2;
        } else if (firstMostCommon == 2 && secondMostCommon == 1) {
            return 2;
        } else if (firstMostCommon == 1) {
            return 1;
        } else {
            return 1;
        }


    }

    private static ArrayList<Pair<String, Integer>> rankByScore(int four, int three, int two, int one) {
        ArrayList<Pair<String, Integer>> rank = new ArrayList<>();
        rank.add(new Pair<>("4", four));
        rank.add(new Pair<>("3", three));
        rank.add(new Pair<>("2", two));
        rank.add(new Pair<>("1", one));

        Collections.sort(rank, new Comparator<Pair<String, Integer>>() {
            @Override
            public int compare(Pair<String, Integer> stringIntegerPair, Pair<String, Integer> t1) {
                return stringIntegerPair.second - t1.second;
            }
        });
        Collections.reverse(rank);

        return rank;
    }

    private static int mostCommonElement(List<Integer> list) {

        Map<Integer, Integer> map = new HashMap<Integer, Integer>();

        for (int i = 0; i < list.size(); i++) {

            Integer frequency = map.get(list.get(i));
            if (frequency == null) {
                map.put(list.get(i), 1);
            } else {
                map.put(list.get(i), frequency + 1);
            }
        }

        int mostCommonKey = 0;
        int maxValue = -1;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {

            if (entry.getValue() > maxValue) {
                mostCommonKey = entry.getKey();
                maxValue = entry.getValue();
            }
        }

        return mostCommonKey;
    }

}
