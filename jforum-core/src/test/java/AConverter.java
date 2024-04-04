import com.liu.core.converter.TreeConverter;

import java.util.List;

/**
 * Description: 转换器
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/02 12:51
 */
public class AConverter implements TreeConverter<A> {
    @Override
    public List<A> getChildren(A data) {
        return data.getChildren();
    }

    @Override
    public void setChildren(A data, List<A> children) {
        data.setChildren(children);
    }

    @Override
    public Long getPid(A data) {
        return data.getPid();
    }

    @Override
    public Long getId(A data) {
        return data.getId();
    }
}
