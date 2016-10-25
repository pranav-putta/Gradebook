package net.codealizer.thegradebook.data;

import android.util.Pair;

import net.codealizer.thegradebook.apis.ic.xml.classbook.ClassbookActivity;
import net.codealizer.thegradebook.apis.ic.xml.classbook.ClassbookTask;
import net.codealizer.thegradebook.assets.Grade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Pranav on 10/19/16.
 */

public class GradesManager {

    public static Grade calculateEBR(ArrayList<ClassbookTask> tasks) {
        ArrayList<Integer> averageProfScores = new ArrayList<>();

        for (ClassbookTask task : tasks) {
            averageProfScores.add(calculateProfScore(task));
        }

        double four = 0;
        double three = 0;
        double two = 0;
        double one = 0;

        double size = averageProfScores.size();

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
                case 0:
                    size--;
                    break;
            }
        }

        double score = ((three + four) / size);

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

    public static int calculateProfScore(ClassbookTask group) {
        int four = 0;
        int three = 0;
        int two = 0;
        int one = 0;

        for (ClassbookActivity activity : group.getAllActivities()) {
            if (activity.isActive()) {
                double score;
                try {
                    score = Double.parseDouble(activity.score);
                } catch (NumberFormatException | NullPointerException ex) {
                    score = 0;
                }

                if (score > 0) {
                    if (score == 4) {
                        four++;
                    } else if (score >= 3) {
                        three++;
                    } else if (score >= 2) {
                        two++;
                    } else if (score >= 1) {
                        one++;
                    }
                }
            }
        }

        if (four != 0 || three != 0 || two != 0 || one != 0) {
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
                return 0;
            }
        } else {
            return 0;
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


}
