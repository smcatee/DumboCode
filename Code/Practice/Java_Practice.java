/*
array vs array list
design pattern
classes and objects
object creation
serialization and de-serialization
exception
exception handling
collections
*/


import java.util.*;
import java.io.*;
public class Java_Practice
{

    public class arrayTest
    {
        private final int defaultArraySize = 3;
        private int[] arr;
        private ArrayList<Integer> arrL;
        
        public arrayTest()
        {
            this.arr = new int [defaultArraySize];
            for (int i=0; i < defaultArraySize; i++)
            {
                this.arr[i] = i;
            }
            setIntArray(arr);
        }
        
        public arrayTest(final int[] newArray) {
            setIntArray(newArray);
        }

        public String toString() {
            String returnArr = "";
            String returnArrL = "";

            for (int i = 0; i < this.arr.length; i++) {
                returnArr += this.arr[i] + " ";
                returnArrL += this.arrL.get(i) + " ";
            }
            return returnArr + "\n" + returnArrL;
        }

        public void setIntArray(final int[] newArray)
        {
            // instantiate array and arrayList with same length as parameter
            this.arr = new int [newArray.length];
            this.arrL = new ArrayList<Integer>(newArray.length);
            for (int i = 0; i < newArray.length; i++)
            {
                this.arr[i] = newArray[i];
                this.arrL.set(i, newArray[i]);
            }
        }
        
    }

}   