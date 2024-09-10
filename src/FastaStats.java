import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

import javax.sound.midi.Sequence;
public class FastaStats {
  public static void main(String[] args) throws Exception{
    try(BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
      String sequence = "";
      String line = br.readLine();
      int count = 0;
      HashMap<String, Stats> sequences = new HashMap<>();

      while (line != null) {
        if (line.charAt(0) == '>' && count != 0) {
          sequence = sequence.replaceAll("\n", "");
          Stats stats = calculateStats(sequence);
          sequences.put(sequence, stats);
          sequence = "";
          line = br.readLine();
          continue;
        }
        if (count != 0) {
          sequence = sequence + line;
        }
        line = br.readLine();
        count++;
      }
      sequence = sequence.replaceAll("\n", "");
      Stats stats = calculateStats(sequence);
      sequences.put(sequence, stats);
      //need to generate total stats
      String result = generateTotalStats(sequences);
      System.out.println(result);
    } 
  }

  public static Stats calculateStats(String sequence) {
    int length = sequence.length();
    int countA = 0;
    int countC = 0;
    int countT = 0;
    int countG = 0;

    for (char c : sequence.toCharArray()) {
        switch (c) {
            case 'A':
                countA++;
                break;
            case 'C':
                countC++;
                break;
            case 'T':
                countT++;
                break;
            case 'G':
                countG++;
                break;
        }
    }

    Stats stats = new Stats();
    stats.setLength(length);
    stats.setCountA(countA);
    stats.setCountC(countC);
    stats.setCountT(countT);
    stats.setCountG(countG);

    return stats;
  }

  public static String generateTotalStats(HashMap<String, Stats> sequences) {
    int minLen = Integer.MAX_VALUE;
    int maxLen = Integer.MIN_VALUE;
    int totLen = 0;
    int countA = 0;
    int countC = 0;
    int countG = 0;
    int countT = 0;

    for (Stats stats : sequences.values()) {
        int length = stats.getLength();
        if (length < minLen) {
            minLen = length;
        }
        if (length > maxLen) {
            maxLen = length;
        }
        totLen += length;
        countA += stats.getCountA();
        countC += stats.getCountC();
        countG += stats.getCountG();
        countT += stats.getCountT();
    }

    int numRecords = sequences.size();
    double meanLen = (double) totLen / numRecords;

    String result = "{\n\"min_len\" : " + minLen + ",\n" + "\"max_len\" : " + maxLen + ",\n" + 
    "\"tot_len\" : " + totLen + ",\n" + "\"num_records\" : " + numRecords + ",\n" + 
    "\"mean_len\" : " + meanLen + ",\n" +  "\"count_a\" : " + countA + ",\n" +  "\"count_c\" : " + countC + ",\n" +
    "\"count_g\" : " + countG + ",\n" +  "\"count_t\" : " + countT + "\n}";

    return result;
  }
}
