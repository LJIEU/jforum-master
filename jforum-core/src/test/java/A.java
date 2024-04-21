import java.util.List;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/04/02 12:54
 */
public class A {
    private Long id;
    private Long pid;
    private String name;
    private List<A> children;

    public A(Long id, Long pid, String name, List<A> children) {
        this.id = id;
        this.pid = pid;
        this.name = name;
        this.children = children;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<A> getChildren() {
        return children;
    }

    public void setChildren(List<A> children) {
        this.children = children;
    }
}
