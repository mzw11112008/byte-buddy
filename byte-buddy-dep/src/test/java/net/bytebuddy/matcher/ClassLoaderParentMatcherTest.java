package net.bytebuddy.matcher;

import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessControlContext;
import java.security.AccessController;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ClassLoaderParentMatcherTest extends AbstractElementMatcherTest<ClassLoaderParentMatcher<?>> {

    private AccessControlContext accessControlContext = AccessController.getContext();

    private ClassLoader parent, child, noChild;

    @SuppressWarnings("unchecked")
    public ClassLoaderParentMatcherTest() {
        super((Class<ClassLoaderParentMatcher<?>>) (Object) ClassLoaderParentMatcher.class, "isParentOf");
    }

    @Before
    public void setUp() throws Exception {
        parent = new URLClassLoader(new URL[0], null);
        noChild = new URLClassLoader(new URL[0], null);
        child = new URLClassLoader(new URL[0], parent);
    }

    @Test
    public void testMatch() throws Exception {
        assertThat(new ClassLoaderParentMatcher<ClassLoader>(child, accessControlContext).matches(parent), is(true));
    }

    @Test
    public void testMatchBootstrap() throws Exception {
        assertThat(new ClassLoaderParentMatcher<ClassLoader>(child, accessControlContext).matches(null), is(true));
    }

    @Test
    public void testNoMatch() throws Exception {
        assertThat(new ClassLoaderParentMatcher<ClassLoader>(noChild, accessControlContext).matches(parent), is(false));
    }
}