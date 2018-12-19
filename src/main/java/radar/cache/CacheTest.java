package radar.cache;

import com.google.gson.Gson;

import java.util.*;

public class CacheTest {

    private static class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    private static class TestGson {
        Map<Integer, Point> pointMap;
        List<Point> pointList;

        public TestGson(Map<Integer, Point> pointMap, List<Point> pointList) {
            this.pointMap = pointMap;
            this.pointList = pointList;
        }

        @Override
        public String toString() {
            return "TestGson{" +
                    "pointMap=" + pointMap +
                    ", pointList=" + pointList +
                    '}';
        }
    }

    public static void main(String[] args) {
        Map<Integer, Point> map = new HashMap<>();
        map.put(0, new Point(1, 2));
        map.put(1, new Point(3, 4));

        List<Point> list = new LinkedList<>();
        list.add(new Point(5, 6));
        list.add(new Point(7, 8));

        TestGson testGson = new TestGson(map, list);
        System.out.println(testGson);

        String s = new Gson().toJson(testGson);
        System.out.println(s);

        TestGson testGson1 = new Gson().fromJson(s, TestGson.class);
        System.out.println(testGson1);

        System.out.println(new Date());
        System.out.println(new Date().getTime());
    }

}
