package com.lu.pojo.GraphEntity;

import java.util.*;

public class ShortestPath {

    public static class MyPath {
        public List<Integer> path;
        public double weight;


        public MyPath() {
        }

        public MyPath(List<Integer> path, double weight) {
            this.path = path;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "MyPath{" +
                    "path=" + path +
                    ", weight=" + weight +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MyPath path1 = (MyPath) o;
            return path != null ? path.equals(path1.path) : path1.path == null;
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            result = path != null ? path.hashCode() : 0;
            temp = Double.doubleToLongBits(weight);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            return result;
        }
    }



    public List<MyPath> KSP_Yen(MyGraph g, int startIndex, int endIndex, int K) {
        List<MyPath> result = new ArrayList<>();
        Set<MyPath> candidatePaths = new HashSet<>();
        MyPath p1 = getSingleShortestPath_dijkstra(g, startIndex, endIndex, null, null);
        if (p1 == null)
            return new ArrayList<>();
        result.add(p1);
        int k = 1;
        List<Integer> pk = p1.path;
        while (k < K) {
                        for (int i = 0; i <= pk.size() - 2; i++) {
                double w1 = 0;
                for (int j = 0; j <= i - 1; j++) {
                    w1 += NavigationUtil.getEdgeWight(g, pk.get(j), pk.get(j + 1));
                }
                MyPath viToDestinationSP = getSingleShortestPath_dijkstra(g,
                        pk.get(i), endIndex, pk.subList(0, i), result);
                if (viToDestinationSP != null) {
                    MyPath temp = new MyPath();
                    List<Integer> tempPath = new ArrayList<>(pk.subList(0, i));
                    tempPath.addAll(viToDestinationSP.path);
                    temp.path = tempPath;
                    temp.weight = w1 + viToDestinationSP.weight;
                    if (!candidatePaths.contains(temp)) {
                        candidatePaths.add(temp);
                    }
                }
            }
            if (candidatePaths == null || candidatePaths.size() == 0) {
                break;
            } else {
                MyPath fitPath = getFitPathFromCandidate(candidatePaths);
                candidatePaths.remove(fitPath);
                result.add(fitPath);
                k++;
                pk = fitPath.path;
            }
        }
        return result;
    }

        private MyPath getFitPathFromCandidate(Set<MyPath> candidatePaths) {
        MyPath result = new MyPath(null, Double.MAX_VALUE);
        for (MyPath p : candidatePaths) {
            if (p.weight < result.weight) {
                result = p;
            }
            if (p.weight == result.weight && p.path.size() < result.path.size()) {
                result = p;
            }
        }
        return result;
    }

        public MyPath getSingleShortestPath_dijkstra(MyGraph g, int startIndex, int endIndex,
                                                 List<Integer> unavailableNodeIndexs, List<MyPath> unavailableEdges) {
        if (startIndex == -1) {
        }
        if (endIndex == -1) {
        }
        int[] set = new int[g.numPoint];        double[] dist = new double[g.numPoint];
        int[] path = new int[g.numPoint];

        set[startIndex] = 1;
        for (int i = 0; i < g.numPoint; i++) {
            if (i == startIndex) {                dist[i] = 0;
                path[i] = -1;
            } else {
                if (NavigationUtil.isConnected(g, startIndex, i)) {
                    dist[i] = NavigationUtil.getEdgeWight(g, startIndex, i);
                    path[i] = startIndex;
                } else {
                    dist[i] = Double.MAX_VALUE;
                    path[i] = -1;
                }
            }
        }

        if (unavailableEdges != null && unavailableEdges.size() != 0) {
            for (MyPath p : unavailableEdges) {
                int index = p.path.indexOf(startIndex);
                if (index >= 0 && (index + 1) >= 0) {
                    dist[p.path.get(index + 1)] = Double.MAX_VALUE;
                    path[p.path.get(index + 1)] = -1;
                }
            }
        }

        if (unavailableNodeIndexs != null && unavailableNodeIndexs.size() != 0) {
            for (Integer point : unavailableNodeIndexs) {
                set[point] = 1;
            }
        }

        for (int i = 0; i < g.numPoint - 2; i++) {
            int k = -1;
            double min = Double.MAX_VALUE;
            for (int j = 0; j < g.numPoint; j++) {
                if (set[j] == 1) {
                    continue;
                }
                if (dist[j] < min) {
                    min = dist[j];
                    k = j;
                }
            }
            if (k == -1) {
                break;
            }
            set[k] = 1;            for (int j = 0; j < g.numPoint; j++) {
                if (set[j] == 1) {
                    continue;
                }
                if (NavigationUtil.isConnected(g, k, j)) {
                    double temp = dist[k] + NavigationUtil.getEdgeWight(g, k, j);
                    if (temp < dist[j]) {
                        dist[j] = temp;
                        path[j] = k;
                    }
                }
            }
        }

        System.out.println();
        if (dist[endIndex] == Double.MAX_VALUE) {
            System.out.println("Two points are not connected");
            return null;
        } else {
            MyPath result = new MyPath();
            result.path = getMinimumPath(g, startIndex, endIndex, path);
            result.weight = dist[endIndex];
            return result;
        }
    }

        private List<Integer> getMinimumPath(MyGraph g, int sIndex, int tIndex, int[] path) {
        List<Integer> result = new ArrayList<>();
        Stack<Integer> stack = new Stack<>();
        stack.push(tIndex);
        int i = path[tIndex];
        while (i != -1) {
            stack.push(i);
            i = path[i];
        }
        while (!stack.isEmpty()) {
            result.add(g.point[stack.pop()].data);
        }
        System.out.println();
        return result;
    }

}