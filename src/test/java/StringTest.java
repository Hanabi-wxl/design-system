import org.apache.commons.collections.ListUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class StringTest {

    @Test
    public void t() {

        List<Integer> ids = new ArrayList<>();
        List<Integer> firstTeacherTeamIds = new ArrayList<>();

        ids.add(1);
        ids.add(2);
        ids.add(3);
        ids.add(4);

        firstTeacherTeamIds.add(1);
        firstTeacherTeamIds.add(6);
        firstTeacherTeamIds.add(2);

        for (int i = 0; i < ids.size(); i++) {

            for (int j = 0; j < firstTeacherTeamIds.size(); j++) {
                if (ids.get(i).equals(firstTeacherTeamIds.get(j))){
                    ids.remove(i);
                    i--;
                    break;
                }
            }
        }

        System.out.println(ids);

//        String[] str = new String[100];
//        str[str.length-1]="1";
//        for (int i = 0; i < str.length; i++) {
//            System.out.println(str[i]);
//        }

//        List<Integer> list1 = new ArrayList<>();
//        List<Integer> list2 = new ArrayList<>();
//
//        list1.add(1);
//        list1.add(2);
//        list1.add(3);
//        list1.add(4);
//        list1.add(5);
//
//        list2.add(1);
//        list2.add(5);
//
//        System.out.println(ListUtils.subtract(list1,list2));

    }
}
