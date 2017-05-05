package dks.attendance;

/**
 * Created by DKS on 2/24/2017.
 */

public class model {
    String subject_name,faculty_name,student_data;

    public model(String subject_name,String faculty_name,String student_data) {
        this.subject_name = subject_name;
        this.faculty_name = faculty_name;
        this.student_data = student_data;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getFaculty_name() {
        return faculty_name;
    }

    public void setFaculty_name(String faculty_name) {
        this.faculty_name = faculty_name;
    }

    public String getStudent_data() {
        return student_data;
    }

    public void setStudent_data(String student_data) {
        this.student_data = student_data;
    }

}

