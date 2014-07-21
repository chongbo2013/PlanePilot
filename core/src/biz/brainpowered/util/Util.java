package biz.brainpowered.util;

/**
 * Created by sebastian on 2014/07/18.
 */
import java.util.ArrayList;
import java.util.Random;

public class Util {

    public static Integer getRandomNumberBetween(int from, int to) {
        return from + (int)(Math.random() * ((to - from) + 1));
    }

    public static Integer getRandomNumberBetween(float from, float to) {
        return Math.round(from + (int)(Math.random() * ((to - from) + 1)));
    }

    public static <T> void cleanNulls( final ArrayList<T> lst )
    {
        int pFrom = 0;
        int pTo = 0;
        final int len = lst.size();
        //copy all not-null elements towards list head
        while ( pFrom < len )
        {
            if ( lst.get( pFrom ) != null )
                lst.set( pTo++, lst.get( pFrom ) );
            ++pFrom;
        }
        //there are some elements left in the tail - clean them
        lst.subList( pTo, len ).clear();
    }
}
