package org.dragon.aries.server;

import lombok.Data;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.*;

public class TestApplication {
    public static void main(String[] args) {
        TestApplication testApplication = new TestApplication();
        List<List<String>> abaa = testApplication.partition("aab");
        System.out.println(abaa);
    }

    private List<List<String>> result = new ArrayList<>();
    private List<String> list = new ArrayList();
    private boolean[][] matrix;
    public List<List<String>> partition(String s) {
        matrix = new boolean[s.length()][s.length()];
        for (int i = 0; i < s.length(); i ++) {
            matrix[i][i] = true;
        }
        int n = s.length();
        for (int i = n - 1; i >= 0; i --) {
            for (int j = i + 1 ; j < n; j ++) {
                if (s.charAt(i) == s.charAt(j) && matrix[i + 1][j - 1]) {
                    matrix[i][j] = true;
                }
                if (j - i == 1 && s.charAt(i) == s.charAt(j)) {
                    matrix[i][j] = true;
                }
            }
        }

        for (int i = 0; i < matrix[0].length; i ++) {
            if (matrix[0][i]) {
                list.add(s.substring(0, i + 1));
                compute(i + 1,s);
                list.remove(list.size() - 1);
            }
        }

        return result;
    }

    private void compute(int i, String s) {
        if (i == s.length()) {
            result.add(new ArrayList<>(list));
            return;
        }
        for (int j = 0; j < s.length(); j ++) {
            if (matrix[i][j]) {
                list.add(s.substring(i, j + 1));
                compute(j + 1, s);
                list.remove(list.size() - 1);
            }
        }
    }
}
