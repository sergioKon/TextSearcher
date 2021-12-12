import org.junit.Assert;
import org.junit.Test;

public class SearcherTest {

    @Test
   public void  worldPositionTest() {
       String text = "\"When we were taking coffee in the drawing-room that night after dinner, I told Arthur and Mary my experience, and of the precious " +
               "treasure which we had under our roof, suppressing only the name of my client. Lucy Parr, who had brought in the coffee, had, I am sure, " +
               "left the room; but I cannot swear that the door was closed. Mary and Arthur were much interested and wished to see the famous coronet, " +
               "but I thought it better not to disturb it." ;
       String key= "Arthur";
       String[] words= text.split(" ");

       Assert.assertEquals(key,words[14]);
       Assert.assertEquals(key,words[63]);

       text= "Oh, but he had only picked it up to look at it. Oh, do, do take my word for it that he is innocent. Let the matter drop and say no more. " +
               "It is so dreadful to think of our dear Arthur in prison!";
        words= text.split(" ");
        Assert.assertEquals(key,words[41]);
    }
}
