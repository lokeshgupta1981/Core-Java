package com.howtodoinjava.cloning;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.SerializationUtils;

/**
 * Prints every result shown in the article on object cloning in Java: clone(), copy constructors, shallow and
 * deep copies, arrays, collections, serialization, Jackson and records.
 */
public class CloningDemo {

  public static void main(String[] args) throws Exception {
    quickReference();
    cloneContract();
    shallowCopy();
    deepCopy();
    missingCloneable();
    copyConstructorAndInheritance();
    arrays();
    collections();
    serializationCopies();
    records();
    finalFields();
  }

  static void quickReference() {
    System.out.println("== Quick reference");
    Song original = new Song("Yellow", 10);

    Song cloned = original.clone();
    System.out.println(cloned);                          // Song[title=Yellow, plays=10]
    System.out.println(cloned == original);              // false

    Song copied = new Song(original);
    System.out.println(copied);                          // Song[title=Yellow, plays=10]
    Song factoryCopy = Song.copyOf(original);
    System.out.println(factoryCopy);                     // Song[title=Yellow, plays=10]

    cloned.setPlays(99);
    System.out.println(original.getPlays());             // 10

    ShallowPlaylist shallowOriginal = new ShallowPlaylist("Road Trip");
    shallowOriginal.add(new Song("Yellow", 10));
    ShallowPlaylist shallowCopy = shallowOriginal.clone();
    shallowCopy.getSongs().getFirst().setPlays(99);
    System.out.println(shallowOriginal.getSongs().getFirst().getPlays());   // 99

    Playlist deepOriginal = new Playlist("Road Trip");
    deepOriginal.add(new Song("Yellow", 10));
    Playlist deepCopy = deepOriginal.clone();
    deepCopy.getSongs().getFirst().setPlays(99);
    System.out.println(deepOriginal.getSongs().getFirst().getPlays());      // 10
  }

  static void cloneContract() {
    System.out.println("== clone() contract");
    Song original = new Song("Yellow", 10);
    Song cloned = original.clone();
    System.out.println(cloned != original);                         // true
    System.out.println(cloned.getClass() == original.getClass());   // true
    System.out.println(cloned.equals(original));                    // false, Song does not override equals()
  }

  static void shallowCopy() {
    System.out.println("== Shallow copy");
    ShallowPlaylist original = new ShallowPlaylist("Road Trip");
    original.add(new Song("Yellow", 10));

    ShallowPlaylist copy = original.clone();
    copy.setName("Gym");
    copy.add(new Song("Clocks", 3));
    copy.getSongs().getFirst().setPlays(99);

    System.out.println(original.getName());                    // Road Trip
    System.out.println(original.getSongs());                   // both songs, Yellow with 99 plays
    System.out.println(copy.getSongs() == original.getSongs());   // true
  }

  static void deepCopy() {
    System.out.println("== Deep copy");
    Playlist original = new Playlist("Road Trip");
    original.add(new Song("Yellow", 10));

    Playlist copy = original.clone();
    copy.add(new Song("Clocks", 3));
    copy.getSongs().getFirst().setPlays(99);

    System.out.println(original.getSongs());                   // [Song[title=Yellow, plays=10]]
    System.out.println(copy.getSongs());                       // Yellow 99, Clocks 3
    System.out.println(copy.getSongs() == original.getSongs());   // false

    Playlist byConstructor = new Playlist(original);
    byConstructor.getSongs().getFirst().setPlays(42);
    System.out.println(original.getSongs().getFirst().getPlays());   // 10
  }

  static void missingCloneable() {
    System.out.println("== Missing Cloneable");
    try {
      new Podcast("Lokesh").clone();
    } catch (CloneNotSupportedException e) {
      System.out.println(e);
    }
  }

  static void copyConstructorAndInheritance() {
    System.out.println("== Copy constructor and inheritance");
    Song live = new LiveSong("Yellow", 10, "Wembley");

    Song byConstructor = new Song(live);
    System.out.println(byConstructor.getClass().getSimpleName());   // Song
    System.out.println(byConstructor);

    Song byClone = live.clone();
    System.out.println(byClone.getClass().getSimpleName());         // LiveSong
    System.out.println(byClone);
  }

  static void arrays() {
    System.out.println("== Arrays");
    int[] plays = {10, 3, 7};
    int[] playsCopy = plays.clone();
    playsCopy[0] = 99;
    System.out.println(Arrays.toString(plays));      // [10, 3, 7]

    Song[] songs = {new Song("Yellow", 10), new Song("Clocks", 3)};
    Song[] songsCopy = songs.clone();
    System.out.println(songsCopy == songs);          // false
    System.out.println(songsCopy[0] == songs[0]);    // true
    songsCopy[0].setPlays(99);
    System.out.println(songs[0].getPlays());         // 99

    Song[] deepSongs = Arrays.stream(songs).map(Song::clone).toArray(Song[]::new);
    System.out.println(deepSongs[0] == songs[0]);    // false

    int[][] grid = {{1, 2}, {3, 4}};
    int[][] gridCopy = grid.clone();
    gridCopy[0][0] = 99;
    System.out.println(grid[0][0]);                  // 99

    int[][] gridDeep = Arrays.stream(grid).map(int[]::clone).toArray(int[][]::new);
    gridDeep[1][1] = 99;
    System.out.println(grid[1][1]);                  // 4
  }

  @SuppressWarnings("unchecked")
  static void collections() {
    System.out.println("== Collections");
    ArrayList<Song> songs = new ArrayList<>(List.of(new Song("Yellow", 10), new Song("Clocks", 3)));

    ArrayList<Song> cloned = (ArrayList<Song>) songs.clone();
    List<Song> copied = new ArrayList<>(songs);
    List<Song> readOnly = List.copyOf(songs);
    System.out.println(cloned.get(0) == songs.get(0));     // true
    System.out.println(copied.get(0) == songs.get(0));     // true
    System.out.println(readOnly.get(0) == songs.get(0));   // true

    cloned.add(new Song("Fix You", 5));
    System.out.println(songs.size());                      // 2

    List<Song> deep = songs.stream().map(Song::new).toList();
    deep.get(0).setPlays(99);
    System.out.println(songs.get(0).getPlays());           // 10
  }

  static void serializationCopies() throws Exception {
    System.out.println("== Serialization, Commons Lang, Jackson");
    Playlist original = new Playlist("Road Trip");
    original.add(new Song("Yellow", 10));

    Playlist viaStreams = DeepCopyUtils.deepCopy(original);
    viaStreams.getSongs().getFirst().setPlays(99);
    System.out.println(original.getSongs().getFirst().getPlays());   // 10

    Playlist viaCommons = SerializationUtils.clone(original);
    System.out.println(viaCommons.getSongs().getFirst() == original.getSongs().getFirst());   // false
    System.out.println(viaCommons.getSongs());

    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(original);
    System.out.println(json);
    Playlist viaJson = mapper.readValue(json, Playlist.class);
    System.out.println(viaJson.getSongs().getFirst() == original.getSongs().getFirst());     // false
    System.out.println(viaJson.getSongs());
  }

  static void records() {
    System.out.println("== Records");
    List<String> artists = new ArrayList<>(List.of("Coldplay"));
    Track track = new Track("Yellow", artists);
    artists.add("Someone else");
    System.out.println(track.artists());              // [Coldplay]

    Track renamed = track.withTitle("Yellow (Live)");
    System.out.println(renamed);
    System.out.println(track);
    try {
      track.artists().add("X");
    } catch (UnsupportedOperationException e) {
      System.out.println(e);
    }
  }

  static void finalFields() {
    System.out.println("== Final fields");
    Album original = new Album("Parachutes", List.of(new Song("Yellow", 10)));
    Album copy = new Album(original);
    copy.getTracks().getFirst().setPlays(99);
    System.out.println(original.getTracks().getFirst().getPlays());   // 10
  }
}
