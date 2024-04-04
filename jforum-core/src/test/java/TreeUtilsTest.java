import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.liu.core.converter.TreeConverter;
import com.liu.core.utils.TreeUtils;

import java.util.ArrayList;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/02 13:15
 */
public class TreeUtilsTest {
    public static void main(String[] args) {
        ArrayList<A> list = new ArrayList<>();
        for (long i = 1; i < 15; i++) {
            A a;
            // 第一层
            if (i < 3)
                a = new A(i, 0L, RandomUtil.randomString(4), null);
            else if (i < 10) {
                // 第二层
                a = new A(i, RandomUtil.randomLong(1, 3), RandomUtil.randomString(4), null);
            } else {
                // 第三层
                a = new A(i, RandomUtil.randomLong(3, 10), RandomUtil.randomString(4), null);
            }
            list.add(a);
        }
//        list.add(new A(1L, 0L, RandomUtil.randomString(4), null));
//        list.add(new A(2L, 1L, RandomUtil.randomString(4), null));
//        list.add(new A(3L, 2L, RandomUtil.randomString(4), null));
//        list.add(new A(4L, 3L, RandomUtil.randomString(4), null));
        list.stream().filter(v -> v.getPid() == 0).forEach(System.out::println);
        System.out.println("============================================");
        TreeConverter<A> converter = new AConverter();
        System.out.println(JSONUtil.toJsonStr(TreeUtils.convertTree(list, converter)));
    }
}
