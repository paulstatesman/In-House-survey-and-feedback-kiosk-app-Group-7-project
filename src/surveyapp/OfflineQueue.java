package surveyapp;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class OfflineQueue {
    private static final String FILE_PATH = "responses.txt";

    // Add response to offline queue
    public static void addResponse(String userName, Map<String, List<String>> answers){
        Map<String, Map<String, List<String>>> offlineMap = loadQueue();
        offlineMap.put(userName, answers);
        saveQueue(offlineMap);
    }

    // Load queued responses from file
    public static Map<String, Map<String, List<String>>> loadQueue(){
        Map<String, Map<String, List<String>>> map = new LinkedHashMap<>();
        File file = new File(FILE_PATH);
        if(!file.exists()) return map;

        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;
            String currentUser = null;
            while((line = br.readLine()) != null){
                line = line.trim();
                if(line.startsWith("USER:")){
                    currentUser = line.substring(5).trim();
                    map.put(currentUser, new LinkedHashMap<>());
                } else if(currentUser != null && line.contains("->")){
                    String[] parts = line.split("->");
                    String question = parts[0].trim();
                    String[] ansArray = parts[1].split(",");
                    List<String> ansList = new ArrayList<>();
                    for(String a : ansArray) if(!a.isEmpty()) ansList.add(a.trim());
                    map.get(currentUser).put(question, ansList);
                }
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        return map;
    }

    // Save queue to file
    private static void saveQueue(Map<String, Map<String, List<String>>> map){
        try(PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH))){
            for(Map.Entry<String, Map<String,List<String>>> userEntry : map.entrySet()){
                pw.println("USER:" + userEntry.getKey());
                for(Map.Entry<String,List<String>> ansEntry : userEntry.getValue().entrySet()){
                    pw.println(ansEntry.getKey() + " -> " + String.join(",", ansEntry.getValue()));
                }
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    // Sync offline responses to the database
    public static void syncResponses(){
        Map<String, Map<String, List<String>>> offlineMap = loadQueue();
        if(offlineMap.isEmpty()) return;

        try(Connection conn = DBConnection.getConnection()){
            for(Map.Entry<String, Map<String, List<String>>> userEntry : offlineMap.entrySet()){
                String userName = userEntry.getKey();

                for(Map.Entry<String, List<String>> ansEntry : userEntry.getValue().entrySet()){
                    String questionText = ansEntry.getKey();
                    List<String> answers = ansEntry.getValue();

                    // Find survey_id and question_id
                    int surveyId = 0;
                    int questionId = 0;

                    String query = "SELECT s.id as survey_id, q.id as question_id " +
                            "FROM surveys s JOIN questions q ON s.id = q.survey_id " +
                            "WHERE q.text=?";
                    try(PreparedStatement pst = conn.prepareStatement(query)){
                        pst.setString(1, questionText);
                        try(ResultSet rs = pst.executeQuery()){
                            if(rs.next()){
                                surveyId = rs.getInt("survey_id");
                                questionId = rs.getInt("question_id");
                            }
                        }
                    }

                    if(surveyId != 0 && questionId != 0){
                        String insert = "INSERT INTO responses(user_name,survey_id,question_id,answer) VALUES (?,?,?,?)";
                        try(PreparedStatement pst = conn.prepareStatement(insert)){
                            pst.setString(1,userName);
                            pst.setInt(2,surveyId);
                            pst.setInt(3,questionId);
                            pst.setString(4,String.join(",",answers));
                            pst.executeUpdate();
                        }
                    }
                }
            }

            // Clear offline file after sync
            saveQueue(new LinkedHashMap<>());
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
