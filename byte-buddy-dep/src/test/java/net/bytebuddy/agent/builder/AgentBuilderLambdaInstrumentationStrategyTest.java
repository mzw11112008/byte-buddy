package net.bytebuddy.agent.builder;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.test.utility.ObjectPropertyAssertion;
import org.junit.Test;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.ProtectionDomain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

public class AgentBuilderLambdaInstrumentationStrategyTest {

    private AccessControlContext accessControlContext = AccessController.getContext();

    @Test
    public void testEnabled() throws Exception {
        assertThat(AgentBuilder.LambdaInstrumentationStrategy.of(true).isEnabled(), is(true));
        assertThat(AgentBuilder.LambdaInstrumentationStrategy.of(false).isEnabled(), is(false));
    }

    @Test
    public void testEnabledStrategyNeverThrowsException() throws Exception {
        ClassFileTransformer initialClassFileTransformer = mock(ClassFileTransformer.class);
        assertThat(LambdaFactory.register(initialClassFileTransformer,
                mock(AgentBuilder.Default.LambdaInstrumentationStrategy.LambdaInstanceFactory.class),
                new AgentBuilder.LambdaInstrumentationStrategy.LambdaInjector(accessControlContext)), is(true));
        try {
            ByteBuddy byteBuddy = mock(ByteBuddy.class);
            Instrumentation instrumentation = mock(Instrumentation.class);
            ClassFileTransformer classFileTransformer = mock(ClassFileTransformer.class);
            try {
                AgentBuilder.Default.LambdaInstrumentationStrategy.ENABLED.apply(byteBuddy, instrumentation, classFileTransformer, accessControlContext);
            } finally {
                assertThat(LambdaFactory.release(classFileTransformer), is(false));
            }
        } finally {
            assertThat(LambdaFactory.release(initialClassFileTransformer), is(true));
        }
    }

    @Test
    public void testDisabledStrategyIsNoOp() throws Exception {
        ByteBuddy byteBuddy = mock(ByteBuddy.class);
        Instrumentation instrumentation = mock(Instrumentation.class);
        ClassFileTransformer classFileTransformer = mock(ClassFileTransformer.class);
        AgentBuilder.Default.LambdaInstrumentationStrategy.DISABLED.apply(byteBuddy, instrumentation, classFileTransformer, accessControlContext);
        verifyZeroInteractions(byteBuddy);
        verifyZeroInteractions(instrumentation);
        verifyZeroInteractions(classFileTransformer);
    }

    @Test
    public void testObjectProperties() throws Exception {
        ObjectPropertyAssertion.of(AgentBuilder.Default.LambdaInstrumentationStrategy.class).apply();
        ObjectPropertyAssertion.of(AgentBuilder.Default.LambdaInstrumentationStrategy.LambdaInjector.class).create(new ObjectPropertyAssertion.Creator<AccessControlContext>() {
            @Override
            public AccessControlContext create() {
                return new AccessControlContext(new ProtectionDomain[]{mock(ProtectionDomain.class)});
            }
        }).apply();
        ObjectPropertyAssertion.of(AgentBuilder.Default.LambdaInstrumentationStrategy.MetaFactoryRedirection.class).apply();
        ObjectPropertyAssertion.of(AgentBuilder.Default.LambdaInstrumentationStrategy.AlternativeMetaFactoryRedirection.class).apply();
        ObjectPropertyAssertion.of(AgentBuilder.Default.LambdaInstrumentationStrategy.LambdaInstanceFactory.class).apply();
        ObjectPropertyAssertion.of(AgentBuilder.Default.LambdaInstrumentationStrategy.LambdaInstanceFactory.BridgeMethodImplementation.class).apply();
        ObjectPropertyAssertion.of(AgentBuilder.Default.LambdaInstrumentationStrategy.LambdaInstanceFactory.BridgeMethodImplementation.Appender.class).apply();
        ObjectPropertyAssertion.of(AgentBuilder.Default.LambdaInstrumentationStrategy.LambdaInstanceFactory.ConstructorImplementation.class).apply();
        ObjectPropertyAssertion.of(AgentBuilder.Default.LambdaInstrumentationStrategy.LambdaInstanceFactory.ConstructorImplementation.Appender.class).apply();
        ObjectPropertyAssertion.of(AgentBuilder.Default.LambdaInstrumentationStrategy.LambdaInstanceFactory.FactoryImplementation.class).apply();
        ObjectPropertyAssertion.of(AgentBuilder.Default.LambdaInstrumentationStrategy.LambdaInstanceFactory.FactoryImplementation.Appender.class).apply();
        ObjectPropertyAssertion.of(AgentBuilder.Default.LambdaInstrumentationStrategy.LambdaInstanceFactory.FactoryImplementation.Appender.class).apply();
        ObjectPropertyAssertion.of(AgentBuilder.Default.LambdaInstrumentationStrategy.LambdaInstanceFactory.LambdaMethodImplementation.Appender.class).apply();
        ObjectPropertyAssertion.of(AgentBuilder.Default.LambdaInstrumentationStrategy.LambdaInstanceFactory.SerializationImplementation.class).apply();
    }
}
