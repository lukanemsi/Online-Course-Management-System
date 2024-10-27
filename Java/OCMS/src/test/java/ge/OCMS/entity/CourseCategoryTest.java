package ge.OCMS.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourseCategoryTest {

    @Test
    void getCategoryId_whenSetCategoryId_thenReturnsCategoryId() {
        CourseCategory courseCategory = new CourseCategory();
        Long expectedCategoryId = 1L;

        courseCategory.setCategoryId(expectedCategoryId);
        Long actualCategoryId = courseCategory.getCategoryId();

        assertEquals(expectedCategoryId, actualCategoryId);
    }

    @Test
    void getCategoryName_whenSetCategoryName_thenReturnsCategoryName() {
        CourseCategory courseCategory = new CourseCategory();
        String expectedCategoryName = "Mathematics";

        courseCategory.setCategoryName(expectedCategoryName);
        String actualCategoryName = courseCategory.getCategoryName();

        assertEquals(expectedCategoryName, actualCategoryName);
    }

    @Test
    void equals_whenSameCategoryId_thenReturnsTrue() {
        CourseCategory courseCategory1 = new CourseCategory();
        courseCategory1.setCategoryId(1L);

        CourseCategory courseCategory2 = new CourseCategory();
        courseCategory2.setCategoryId(1L);

        boolean areEqual = courseCategory1.equals(courseCategory2);

        assertTrue(areEqual);
    }

    @Test
    void equals_whenDifferentCategoryId_thenReturnsFalse() {
        CourseCategory courseCategory1 = new CourseCategory();
        courseCategory1.setCategoryId(1L);

        CourseCategory courseCategory2 = new CourseCategory();
        courseCategory2.setCategoryId(2L);

        boolean areEqual = courseCategory1.equals(courseCategory2);

        assertFalse(areEqual);
    }

    @Test
    void hashCode_whenSameCategoryId_thenReturnsSameHashCode() {
        CourseCategory courseCategory1 = new CourseCategory();
        courseCategory1.setCategoryId(1L);

        CourseCategory courseCategory2 = new CourseCategory();
        courseCategory2.setCategoryId(1L);

        int hashCode1 = courseCategory1.hashCode();
        int hashCode2 = courseCategory2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_whenDifferentCategoryId_thenReturnsDifferentHashCode() {
        CourseCategory courseCategory1 = new CourseCategory();
        courseCategory1.setCategoryId(1L);

        CourseCategory courseCategory2 = new CourseCategory();
        courseCategory2.setCategoryId(2L);

        int hashCode1 = courseCategory1.hashCode();
        int hashCode2 = courseCategory2.hashCode();

        assertNotEquals(hashCode1, hashCode2);
    }
}
