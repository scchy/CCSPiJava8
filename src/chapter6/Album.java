package chapter6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Scc_hy
 * @version 1.0.0
 * @ClassName Album.java
 * @Description 按照长度(专辑时间长度，歌曲数量)，对迈克杰克逊的专辑进行聚类
 * @createTime 2023年03月26日 19:02:00
 */
public class Album extends DataPoint {
    private String name;
    private int year;

    public Album(String name, int year, double length, double tracks) {
        super(Arrays.asList(length, tracks));
        this.name = name;
        this.year = year;
    }

    @Override
    public String toString() {
        return "(" + name + ", " + year + ")";
    }

    public static void main(String[] args) {
        List<Album> albums = new ArrayList<>();
        albums.add(new Album("Got to Be There", 1972, 35.45, 10));
        albums.add(new Album("Ben", 1972, 31.31, 10));
        albums.add(new Album("Music & Me", 1973, 32.09, 10));
        albums.add(new Album("Forever, Michael", 1975, 33.36, 10));
        albums.add(new Album("Off the Wall", 1979, 42.28, 10));
        albums.add(new Album("Thriller", 1982, 42.19, 9));
        albums.add(new Album("Bad", 1987, 48.16, 10));
        albums.add(new Album("Dangerous", 1991, 77.03, 14));
        albums.add(new Album("HIStory: Past, Present and Future, Book I", 1995, 148.58, 30));
        albums.add(new Album("Invincible", 2001, 77.05, 16));
        KMeans<Album> kms = new KMeans<>(2, albums);
        List<KMeans<Album>.Cluster> clusters = kms.run(100);
        for (int clusterIndex = 0; clusterIndex < clusters.size(); clusterIndex++) {
            System.out.printf("Cluster %d Avg Length %f Avg Tracks %f: %s%n",
                    clusterIndex, clusters.get(clusterIndex).center.dimensions.get(0),
                    clusters.get(clusterIndex).center.dimensions.get(1),
                    clusters.get(clusterIndex).points);
        }
    }

}
