import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/02 12:54
 */
@Data
@AllArgsConstructor
public class A {
    private Long id;
    private Long pid;
    private String name;
    private List<A> children;
}
