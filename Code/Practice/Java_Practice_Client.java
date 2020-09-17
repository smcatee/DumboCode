import java.util.*;

public class Java_Practice_Client
{

    public class Java_Practice_ArrayTest_Client
    {
        public void main( String [] args )
        {
            int [] numbers = {5,4,3,2,1};
            
            Java_Practice arrDefault = new Java_Practice.arrayTest.arrayTest();
            Java_Practice arr1 = new Java_Practice.arrayTest(numbers);
            System.out.println("Created arrDefault with default constructor and arr1 object with secondary constructor.");
            System.out.println("arrDefault: " + arrDefault.toString()
            + "\narr1: " + arr1.toString());
        }
    }
    
}