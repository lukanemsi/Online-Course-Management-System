package ge.OCMS.wrapper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoleEnumTest {

    @Test
    void getValue_whenCalledOnStudent_thenReturnsStudentValue() {
        RoleEnum role = RoleEnum.STUDENT;
        assertEquals("STUDENT", role.getValue());
    }

    @Test
    void getValue_whenCalledOnInstructor_thenReturnsInstructorValue() {
        RoleEnum role = RoleEnum.INSTRUCTOR;
        assertEquals("INSTRUCTOR", role.getValue());
    }

    @Test
    void toString_whenCalledOnStudent_thenReturnsStudentAsString() {
        RoleEnum role = RoleEnum.STUDENT;
        assertEquals("STUDENT", role.toString());
    }

    @Test
    void toString_whenCalledOnInstructor_thenReturnsInstructorAsString() {
        RoleEnum role = RoleEnum.INSTRUCTOR;
        assertEquals("INSTRUCTOR", role.toString());
    }
}
