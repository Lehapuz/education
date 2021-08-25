import javax.persistence.*;

@Entity
@Table(name = "Linkedpurchaselist")
public class Linkedpurchaselist {

    @EmbeddedId
    private Linkedpurchaselistkey linkedpurchaselistkey;

    //@ManyToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "student_id", insertable = false, updatable = false)
    //private Student student;

    //@ManyToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "course_id", insertable = false, updatable = false)
    //private Course course;

    @JoinColumn(name = "student_id", insertable = false, updatable = false)
    private int studentId;

    @JoinColumn(name = "course_id", insertable = false, updatable = false)
    private int courseId;

    public Linkedpurchaselistkey getLinkedpurchaselistkey() {
        return linkedpurchaselistkey;
    }

    public void setLinkedpurchaselistkey(Linkedpurchaselistkey linkedpurchaselistkey) {
        this.linkedpurchaselistkey = linkedpurchaselistkey;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
