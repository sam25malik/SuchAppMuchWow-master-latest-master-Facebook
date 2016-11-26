package such.app.much.wow;

import android.graphics.Bitmap;

import com.parse.ParseFile;

import java.util.ArrayList;
import java.util.Date;


public class Photo {
    public  String image;
    public  String text;
    public  String user;
    public  Long time;
    public  ParseFile parseFile;
    public Bitmap imageBitmap;
    public  Integer commentsNum;
    public ArrayList<String> comments;
    public  Integer likes;

    public Photo() {
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public ParseFile getParseFile() {
        return parseFile;
    }

    public void setParseFile(ParseFile parseFile) {
        this.parseFile = parseFile;
    }

    public Integer getCommentsNum() {
        return commentsNum;
    }

    public void setCommentsNum(Integer commentsNum) {
        this.commentsNum = commentsNum;
    }

    public ArrayList<String> getComments() {
        return comments;
    }

    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public static String toHumanReadable(Long time){
        Date postTime = new Date(time*1000);
        Date currTime = new Date();
        long diff = currTime.getTime() - postTime.getTime();
        long diffSeconds = (diff / 1000) % 60;
        long diffMinutes = (diff / (60 * 1000)) % 60;
        long diffHours = (diff / (60 * 60 * 1000)) % 24;
        int diffInDays = (int) ((currTime.getTime() - postTime.getTime()) / (1000 * 60 * 60 * 24));
        String assignedTime = "";
        if (diffInDays > 1) {
            assignedTime = Long.toString(diffInDays) + " days ago";
        } else if (diffInDays == 1) {
            assignedTime = (Long.toString(diffInDays) + " day and " +
                    Long.toString(diffHours) + " hours ago");
        }else if (diffHours > 1) {
            assignedTime = Long.toString(diffHours) + " hours ago";
        } else if (diffHours == 1) {
            assignedTime = (Long.toString(diffHours) + " hour and " +
                    Long.toString(diffMinutes) + " minutes ago");
        } else if (diffMinutes > 1) {
            assignedTime = Long.toString(diffMinutes) + " minutes ago";
        } else if (diffMinutes == 1) {
            assignedTime = Long.toString(diffMinutes) + " minute ago";
        }
        else{
            assignedTime = "seconds ago";
        }
        return assignedTime;
    }


    public static String toSmallHumanReadable(Long time){
        Date postTime = new Date(time*1000);
        Date currTime = new Date();
        long diff = currTime.getTime() - postTime.getTime();
        long diffSeconds = (diff / 1000) % 60;
        long diffMinutes = (diff / (60 * 1000)) % 60;
        long diffHours = (diff / (60 * 60 * 1000)) % 24;
        int diffInDays = (int) ((currTime.getTime() - postTime.getTime()) / (1000 * 60 * 60 * 24));
        String assignedTime = "";
        if (diffInDays > 1) {
            assignedTime = Long.toString(diffInDays) + " d";
        } else if (diffInDays == 1) {
            assignedTime = (Long.toString(diffInDays) + " d " +
                    Long.toString(diffHours) + " h");
        }else if (diffHours > 1) {
            assignedTime = Long.toString(diffHours) + " h";
        } else if (diffHours == 1) {
            assignedTime = (Long.toString(diffHours) + " h " +
                    Long.toString(diffMinutes) + " m");
        } else if (diffMinutes > 1) {
            assignedTime = Long.toString(diffMinutes) + " m";
        } else if (diffMinutes == 1) {
            assignedTime = Long.toString(diffMinutes) + " m";
        }
        else{
            assignedTime = "secs";
        }
        return assignedTime;
    }
}
