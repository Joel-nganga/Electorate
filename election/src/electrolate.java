import java.io.*;
import java.net.*;
import java.util.*;

class Electorate {
    private static final int PORT = 12345;
    private static final String GROUP_ADDRESS = "224.0.0.1";
    private static final int NUM_ELECTORATES = 5;

    public static void main(String[] args) {
        try {
            InetAddress group = InetAddress.getByName(GROUP_ADDRESS);
            MulticastSocket socket = new MulticastSocket(PORT);
            socket.joinGroup(group);

            // Generate random vote
            char vote = (Math.random() < 0.5) ? 'A' : 'B';
            System.out.println("I am an electorate. My vote is: " + vote);

            // Send vote to other electorates
            byte[] voteData = String.valueOf(vote).getBytes();
            DatagramPacket packet = new DatagramPacket(voteData, voteData.length, group, PORT);
            socket.send(packet);

            // Receive votes from other electorates
            List<Character> votes = new ArrayList<>();
            while (votes.size() < NUM_ELECTORATES - 1) {
                byte[] buffer = new byte[256];
                packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                char receivedVote = (char) buffer[0];
                votes.add(receivedVote);
            }

            // Determine winner
            int countA = 0, countB = 0;
            for (char v : votes) {
                if (v == 'A') {
                    countA++;
                } else if (v == 'B') {
                    countB++;
                }
            }
            char winner = (countA > countB) ? 'A' : 'B';
            System.out.println("Winner is candidate: " + winner);

            socket.leaveGroup(group);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
