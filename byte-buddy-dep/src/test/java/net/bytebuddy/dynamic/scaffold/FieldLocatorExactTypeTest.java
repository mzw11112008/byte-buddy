package net.bytebuddy.dynamic.scaffold;

import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.test.utility.ObjectPropertyAssertion;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class FieldLocatorExactTypeTest {

    private static final String FOO = "foo", QUX = "qux";

    @Test
    public void testExactTypeFound() throws Exception {
        FieldLocator.Resolution resolution = new FieldLocator.ForExactType(new TypeDescription.ForLoadedType(Foo.class)).locate(FOO);
        assertThat(resolution.isResolved(), is(true));
        assertThat(resolution.getFieldDescription(), is((FieldDescription) new FieldDescription.ForLoadedField(Foo.class.getDeclaredField(FOO))));
    }

    @Test
    public void testExactTypeFoundWithType() throws Exception {
        FieldLocator.Resolution resolution = new FieldLocator.ForExactType(new TypeDescription.ForLoadedType(Foo.class)).locate(FOO, new TypeDescription.ForLoadedType(Void.class));
        assertThat(resolution.isResolved(), is(true));
        assertThat(resolution.getFieldDescription(), is((FieldDescription) new FieldDescription.ForLoadedField(Foo.class.getDeclaredField(FOO))));
    }

    @Test
    public void testExactTypeNotFoundInherited() throws Exception {
        assertThat(new FieldLocator.ForExactType(new TypeDescription.ForLoadedType(Bar.class)).locate(FOO).isResolved(), is(false));
    }

    @Test
    public void testExactTypeNotFoundNotExistent() throws Exception {
        assertThat(new FieldLocator.ForExactType(new TypeDescription.ForLoadedType(Foo.class)).locate(QUX).isResolved(), is(false));
    }

    @Test
    public void testExactTypeNotFoundInvisible() throws Exception {
        assertThat(new FieldLocator.ForExactType(new TypeDescription.ForLoadedType(Foo.class), new TypeDescription.ForLoadedType(Object.class)).locate(FOO).isResolved(), is(false));
    }

    @Test
    public void testExactTypeNotFoundWrongType() throws Exception {
        assertThat(new FieldLocator.ForExactType(new TypeDescription.ForLoadedType(Foo.class)).locate(FOO, new TypeDescription.ForLoadedType(Object.class)).isResolved(), is(false));
    }

    @Test
    public void testObjectProperties() throws Exception {
        ObjectPropertyAssertion.of(FieldLocator.ForExactType.class).apply();
    }

    @SuppressWarnings("unused")
    private static class Foo {

        private Void foo;

        protected Void bar;
    }

    @SuppressWarnings("unused")
    private static class Bar extends Foo {

        protected Void bar;
    }
}