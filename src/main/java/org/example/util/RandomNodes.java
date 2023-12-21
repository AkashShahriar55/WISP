package org.example.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import org.example.model.Graph;
import org.example.model.Vertex;

public class RandomNodes {

	public static void main(String[] args) throws IOException {
		String filename = "./data/subset100000.nt";
		
		Graph graph = new Graph(filename);
		
		HashMap<Integer, Vertex> nodes = graph.getNodes();
		
		int[][] nodeSF = new int[100][];
		
		for(int i=0; i<100; i++) {
			Vertex start = null;
			Vertex end = null;
			int s = 0;
			int e = 0;
			while(start == null) {
				s = (int)(Math.random() * 100000);
				start = nodes.get(s);
			}
			while(end == null) {
				e = (int)(Math.random() * 100000);
				end = nodes.get(e);
			}
			nodeSF[i] = new int[]{s, e};
		}
		
		BufferedWriter writer = null;
        try {
            //create a temporary file
            File logFile = new File("./randomNodes.txt");

            // This will output the full path where the file will be written to...
            System.out.println(logFile.getCanonicalPath());

            writer = new BufferedWriter(new FileWriter(logFile));
            for(int i=0; i<nodeSF.length; i++) {
        		writer.write(nodeSF[i][0]+" "+nodeSF[i][1]+"\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {
            }
        }
	}

}
